
public class Node {
	
	private Patient patient;
	private Node next;
	private Node previous;
	
	public Node(Patient patient, Node previous, Node next) {
		
		this.patient = patient;
		this.previous = previous;
		this.next = next;
	}
	
	public Patient getPatient(){
		return patient;
	}
	
	
	public Node getNext(){
		return next;
	}
	
	public Node getPrevious(){
		return previous;
	}
	
	public void setNext(Node node){
		this.next = node;
	}
	
	public void setPrevious(Node node){
		this.previous = node;
	}
	
	
}
