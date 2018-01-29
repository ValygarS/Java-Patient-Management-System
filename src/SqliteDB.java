import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

public class SqliteDB implements iDB {
	
	Connection c = null;
	Statement stmt = null;
	
	
	private void connect(){
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:MedicalDB.db");
			//System.out.println("Connected to DB!");
			
		}catch(Exception e){
			System.out.println("Error connecting to DB: " + e.getMessage());
			
		}
	}
	

	public void saveToDB(Object patient) {
		
		try{
			connect();
			this.stmt = c.createStatement();
			ObjectMapper om = new ObjectMapper(patient);
			
			stmt.executeUpdate(om.create());
			c.close();
			
		}catch(Exception e){
			System.out.println("Error saving to DB: " + e.getMessage());
		}
		
	}

	public void update(Object patient) {
		try{
			connect();
			this.stmt = c.createStatement();
			ObjectMapper om = new ObjectMapper(patient);
			stmt.executeUpdate(om.update());
			c.close();
			
		}catch(Exception e){
			System.out.println("Error updating DB: " + e.getMessage());
		}
		
	}
	
	
	// another method load all discharged patients from DB directly to Jlist, can be used if queue is not important
	public void loadAlltoGUI(JList<Patient> jlist){
		try{
			connect();
			this.stmt = c.createStatement();
			
			String sql = "SELECT * FROM Patients WHERE discharged = 1";
			ResultSet rs = stmt.executeQuery(sql);
			
			DefaultListModel<Patient> model = (DefaultListModel<Patient>) jlist.getModel();
			
			// if no discharged patients yet
			if(rs==null){
				return;
			}
			
			else{
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
					model.addElement(p);
					
					
				}
			}
			
			c.close();
			
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	// load from DB to DoubleLinkedList
	public void load(Object list, String queueType){
		try{
			connect();
			this.stmt = c.createStatement();
			String sql = "";
			switch(queueType){
			// no priority yet
			case "fifo":
				sql = "SELECT * FROM Patients WHERE priority = 0 AND discharged = 0";
				break;
			// priority has been set
			case "priority":
				sql = "SELECT * FROM Patients WHERE priority > 0 AND discharged = 0";
				break;
			// loads all discharged patients from DB
			case "getAll":
				sql = "SELECT * FROM Patients WHERE discharged = 1";
				break;
			default:
				break;
			}
			
			ResultSet rs = stmt.executeQuery(sql);
			if (rs == null){
				// if result set is empty, means no patients in the queue, do nothing
				return;
			}
			else{
				
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
					
					switch(queueType){
					// no priority
					case "fifo":
						((DoubleLinkedList)list).add(p);
						break;
					// priority has been set
					case "priority":
						((DoubleLinkedList)list).addSortedByPriority(p);
						break;
					// no priority for discharged
					case "getAll":
						((DoubleLinkedList)list).add(p);
						break;
						
					default:
						break;
					}
					
				}
			
				
			}
			
			c.close();
			
		}catch(Exception e){
			System.out.println("Error loading from DB to list: " + e.getMessage());
		}
	}

}