import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleLinkedList {
	
	private Node head;
	private int size;
	
	public DoubleLinkedList(){
		this.head = null;
		this.size = 0;
	}
	
	public Node getNode(int position) {
		
		Node node = this.head;
		
		if (position != 1){
			for (int i = 1; i<position; i++){
				node = node.getNext();
			}
		}
		
		return node;
	}
	
	public int size(){
		return this.size;
	}
	
	public void add(Patient patient){
		if(this.head == null) {
			this.head = new Node(patient, null, null);
		} else {
			// find the last node in the structure
			Node last = getNode(this.size);
			
			// create a new node
			Node n = new Node(patient, null, null);
			
			// update the pointer
			last.setNext(n);
			n.setPrevious(last);
		}
		
		// increment the size 
		size = size + 1;
	}
	
	/* adding patients to queue list according to their priority: highest in the head , lowest in the tail
	within priority the queue is FIFO type */
	public void addSortedByPriority(Patient patient){
		
		if (this.head==null){
			this.head = new Node(patient, null, null);
		}
		
		else {
			
			Node newNode = new Node(patient, null, null);
			
			// if head priority is less that of new -> become new head
			if (newNode.getPatient().getPriority() > head.getPatient().getPriority()){
				Node next = head;
				head = newNode;
				head.setNext(next);
				next.setPrevious(head);
								
			}
			else if (this.size() == 1){
				head.setNext(newNode);
				newNode.setPrevious(head);
			}
			
			/*
			 *  if head has higher priority than the new node, looping through nodes until the next 
			node has lower priority */
			else{
				
				Node last = null;
				
				for (int i=1; i <= this.size(); i++){
					if (this.getNode(i).getPatient().getPriority() >= newNode.getPatient().getPriority()){
						last = this.getNode(i);
					}
				}
				// if node is tail adding after it as new tail
				if (last.getNext() == null){
					last.setNext(newNode);
					newNode.setPrevious(last);
				}
				
				//adding in the middle of list
				else {
					Node next = last.getNext();
					// put new node after the last with same or higher priority and update pointers for 3 nodes
					last.setNext(newNode);
					newNode.setPrevious(last);
					newNode.setNext(next);
					next.setPrevious(newNode);
				}
				
			}
			
		}
		
		// incrementing size of list
		size += 1;
	}
	
	public void delete(Patient patient){
		// first check if head node is our patient, as mostly first patient will be deleted
		if(head.getPatient()== patient){
			head = head.getNext();
			head.setPrevious(null);
		}
		// then check last Node
		else if(this.getNode(this.size()).getPatient() == patient){
			this.getNode(this.size()).getPrevious().setNext(null);
		}
		// or remove from the middle	
		else{
			for (int i=1; i < this.size(); i++){
				if (this.getNode(i).getPatient() == patient){
					this.getNode(i).getPrevious().setNext(this.getNode(i).getNext());
					this.getNode(i).getNext().setPrevious(this.getNode(i).getPrevious());
				}
				
			}
		}
		size -=1;
	}
	
	public void delete(int position){
		if (this.size()==1){
			head = null;				
		}
		else{
			
			if(position == 1){
				head = head.getNext();
				head.setPrevious(null);
			}
			
			else if (position == this.size()){
				this.getNode(position).getPrevious().setNext(null);
			}
			
			else{
				this.getNode(position).getNext().setPrevious(this.getNode(position).getPrevious());
				this.getNode(position).getPrevious().setNext(this.getNode(position).getNext());
				
			}
		}
		
		this.size = size - 1;
	}
	// make DLL empty
	public void empty(){
		while (this.size() > 0){
			this.delete(1);
		}
	}
	
	// create DLL from SQL Result Set
	public void loadfromRS(ResultSet rs, String queueType){
		if (rs == null){
			return;
		}
		else{
			try {
				while (rs.next()){
					String fname = rs.getString("firstName");
					String lname = rs.getString("lastName");
					String dob = rs.getString("dob");
					String condition = rs.getString("condition");
					String vital = rs.getString("vitalSigns");
					String treatment = rs.getString("treatment");
					int dis = rs.getInt("discharged");
					int prior = rs.getInt("priority");
					String intID = rs.getString("intID");
					Patient p = new Patient(fname, lname, dob, condition, vital, treatment, dis, 
							prior, intID);
					
					switch (queueType){
					
					case "fifo":
						this.add(p);
						break;
						
					case "priority":
						this.addSortedByPriority(p);
						break;
					default:
						System.out.println("Keywords for queue type should be: 'fifo' or 'priority'");
						break;
					
					}
				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
