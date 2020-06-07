package LoadData;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class LoadFileTXT {
	private  String name;
	private int id;
	private int year;
	private String address;
	
	public void readData() {
		try (Scanner input=new Scanner(new File("src/LoadData/Stu_data.txt"))){
			while(input.hasNextLine()) {
				name="";
				String line;
				line=input.nextLine();
			try(Scanner data=new Scanner(line)){
				while (!data.hasNextInt()) {
					name+=data.next()+" ";
				}
				name=name.trim();
				//get id
				
				if(data.hasNextInt()) {
					id=data.nextInt();
				}
				//get year
				if (data.hasNextInt()) {
					year=data.nextInt();
				}
				//get address
				if (data.hasNextLine()) {
					address=data.nextLine();
				}
			}
			//check 
//			System.out.println(name+"\t"+id+"\t"+year+"\t"+address+"\t");
			saveData();//call method savedata into database
			
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//CREATE CONNECTTION TO DATABASE
	private Connection connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/students","root","123456789");
		} catch (SQLException |ClassNotFoundException e	) {
			System.out.println(e);
			return null;
		}
	}
	private void saveData() {
		try (Connection connect=connect();
				PreparedStatement pstat=connect.prepareStatement("INSERT INTO students VALUES(?,?,?,?)")){
			pstat.setString(1, name);
			pstat.setInt(2, id);
			pstat.setInt(3, year);
			pstat.setString(4, address);
			
			pstat.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);		
			}
	}
	}

class FDemo{
	public static void main(String[] args) {
		LoadFileTXT st=new LoadFileTXT();
		try {
			st.readData();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
