package bugbusters.Class;

import bugbusters.database.BBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Deniel
 */
public class client extends person implements functions{
    
    //ATTTRIBUTE(S)
    private int clientID;   //* AUTONUMBERED *//
    private String homeAdd;
    private String cityAdd;
    
    //>>>> CONNECT TO MS ACCESS <<<<//
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    //DFAULT CONSTRUCTOR
    public client(){
        con=BBConnection.MSAccessConnection();
    }
    
    //CONSTRUCTOR w/ PARAMETERS
    public client(int clientID, String firstName, String lastName, String contactNum, String homeAdd, String cityAdd){
        super (firstName,lastName,contactNum);
        this.clientID = clientID;
        this.homeAdd = homeAdd;
        this.cityAdd = cityAdd;
        con=BBConnection.MSAccessConnection();
    }
    
    // ENCAPSULATION (GET-SET)
    public void setClientID(int clientID){
        this.clientID = clientID;
    }
    public int getClientID(){
        return clientID;
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
    public void setHomeAdd(String homeAdd){
        this.homeAdd = homeAdd;
    }
    public String getHomeAdd(){
        return homeAdd;
    }
    public void setCityAdd(String cityAdd){
        this.cityAdd = cityAdd;
    }
    public String getCityAdd(){
        return cityAdd;
    }
    
    //>>> DATABASE METHODS <<<//
    // CLIENT ID is AutoNumber
    @Override
    public void SaveInfo(){
        try{
            // CLIENT TABLE
            st = con.prepareStatement("INSERT INTO Client (firstName, lastName, contactNum, homAdd, cityAdd)" + 
                    "VALUES ('"+firstName+"','"+lastName+"','"+contactNum+"','"+homeAdd+"','"+cityAdd+"')");
            st.execute();
            
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    // UPDATE INFO
    @Override
    public void UpdateInfo(){
        try{
        st = con.prepareStatement(
              "UPDATE Client SET " + 
              "firstName='" + firstName + "', "+
              "lastName='" + lastName + "', " +
              "contactNum='" +contactNum+ "', " +
              "cityAdd='" + homeAdd + "', " +
              "homeAdd='" + cityAdd + "', " + 
               "WHERE clientID='" +clientID+"'");
        st.executeUpdate();
        
        System.out.println("Client Booking Updated!");
    }catch(Exception e){
        System.out.println(e.getMessage());
        }
    }
    // DELETE INFO
    @Override
    public void DeleteInfo(){
        try{
            st = con.prepareStatement(
            "DELETE FROM Client WHERE clientID='"+clientID+"'");
            st.executeUpdate();
           
            System.out.println("Client Info Deleted");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    // SEARCH INFO
    @Override
    public void SearchInfo(){
        try{
            st = con.prepareStatement(
            "SELECT * Client WHERE clientID='"+clientID+"'");
            rs = st.executeQuery();
            
            if(rs.next()){
                firstName = rs.getString("firstName");
                lastName = rs.getString("lastName");
                contactNum = rs.getString("contactNum");
                cityAdd = rs.getString("homeAdd");
                homeAdd = rs.getString("cityAdd");
            }
           
            rs = st.executeQuery();
            
            if(rs.next()){
                
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
     // VIEW ClIENT
        public ArrayList<String> View(){
            ArrayList<String> list = new ArrayList<>();
            try{
                st = con.prepareStatement (
                        "SELECT * FROM Client ORDER BY clientID DESC");
                rs = st.executeQuery();
                
                while(rs.next()){
                    String row = rs.getInt("clientID") + " | " +
                                 rs.getString("firstName") + " | " +
                                 rs.getString("lastName") + " | " +
                                 rs.getString("contactNum") + " | " +
                                 rs.getString("homeAdd") + " | " +
                                 rs.getString("cityAdd");
                    list.add(row);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return list;
        }
        
        // RECORDS
        public LinkedList<client> records(){
            LinkedList<client> list = new LinkedList<>();
            try{
                st = con.prepareStatement("SELECT * FROM Client");
                rs = st.executeQuery();
                
                while(rs.next()){
                    client c = new client();
                    c.clientID = rs.getInt("clientID");
                    c.firstName = rs.getString("firstName");
                    c.lastName = rs.getString("lastName");
                    c.contactNum = rs.getString("contactNum");
                    c.cityAdd = rs.getString("homeAdd");
                    c.homeAdd = rs.getString("cityAdd");
                    list.add(c);
                }
                return list;
            } catch (SQLException e){
                System.out.println("No records found!");
                return null;
            }
        }
}
