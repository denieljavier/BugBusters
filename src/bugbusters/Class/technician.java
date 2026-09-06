package bugbusters.Class;

import bugbusters.database.BBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 *
 * @author Deniel
 */

public class technician extends person implements functions {
    //ATTRIBUTE(S)
    private String techID;
    private certifications certList;
    
    // >>>> CONNECT TO MS.ACCESS <<<< // 
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    
    //DFAULT CONSTRUCTOR
    public technician(){
        con = BBConnection.MSAccessConnection();    // >>>> THIS CONNECTS TO THE DATABASE.
        this.certList = new certifications();
    }
    //CONSTRUCTOR W/ PARAMETER(S)
    public technician(String techID, String firstName, String lastName, String contactNum) {
        super (firstName,lastName,contactNum);
        this.techID = techID;
        this.certList = new certifications();
        con = BBConnection.MSAccessConnection();    // >>>> THIS CONNECTS TO THE DATABASE.
        
    }
    // CERTIFICATIONS
    public void AddtechCert(String cert, String spec){
        certList.setTechCert(cert);
        certList.setTechSpec(spec);
    }
    
    // ENCAPSULATION (GET/SET)
    public void setTechID(String techID){
        this.techID = techID;
        certList.setTechID(techID);
    }
    public String getTechID(){
        return techID;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setContactNum(String contactNum){
        this.contactNum = contactNum;
    }
    public String getContactNum(){
        return contactNum;
    }
    
    // >>>> DATABASE METHODS <<<< //
    // TECHNICIAN.class techID is AutoNumber
    // SAVE INFO
    @Override
    public void SaveInfo(){
        try{
            st = con.prepareStatement("INSERT INTO TechnicianDetails (techID, firstName, lastName, contactNum) " +
                                      "VALUES ('"+techID+"','"+firstName+"','"+lastName+"','"+contactNum+"')");
            st.execute();
            System.out.println("Technician's Info Saved!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // UPDATE INFO
    @Override
    public void UpdateInfo(){
        try{
            st = con.prepareStatement("UPDATE TechnicianDetails SET " +
                                      "techID='" + techID + "', " +
                                      "firstName='" + firstName + "', " +
                                      "lastName='" + lastName + "', " +
                                      "contactNum='" + contactNum + "'");
            st.executeUpdate();
            System.out.println("Technician's Info Updated!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // DELETE INFO
    @Override
    public void DeleteInfo(){
        try{
            st = con.prepareStatement("DELETE FROM TechnicianDetails WHERE techID='"+techID+"'");
            st.executeUpdate();
            System.out.println("Technician's Info DELETED!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // SEARCH INFO
    @Override
    public void SearchInfo(){
        try{
            st = con.prepareStatement("SELECT * FROM TechnicianDetails WHERE techID='"+techID+"'");
            rs = st.executeQuery();
            
            if(rs.next()){
                firstName = rs.getString("firstName");
                lastName = rs.getString("lastName");
                contactNum = rs.getString("contactNum");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // VIEW INFO
    public ArrayList<String> View(){
        ArrayList<String> list = new ArrayList<>();
        try{
            st = con.prepareStatement("SELECT * FROM TechnicianDetails ORDER BY techID DESC");
            rs = st.executeQuery();
            
            while(rs.next()){
                String profile = rs.getString("techID") + " | " +
                              rs.getString("firstName") + " | " +
                              rs.getString("lastName") + " | " +
                              rs.getString("contactNum");
                list.add(profile);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    
    // RECORDS
        public LinkedList<technician> records(){
            LinkedList<technician> list = new LinkedList<>();
            try{
                st = con.prepareStatement("SELECT * FROM TechnicianDetails");
                rs = st.executeQuery();
                
                while(rs.next()){
                    technician tech = new technician();
                    tech.techID = rs.getString("techID");
                    tech.firstName = rs.getString("firstName");
                    tech.lastName = rs.getString("lastName");
                    tech.contactNum = rs.getString("contactNum");
                    list.add(tech);
                }
                return list;
            }catch(SQLException e){
                System.out.println("NO RECORDS FOUND!");
                return null;
            }
        }
    
    
}
