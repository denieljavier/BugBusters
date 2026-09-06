/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bugbusters.Class;
import bugbusters.database.BBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 *
 * @author Deniel
 */
public class solutions implements functions{
    // ATTRIBUTE(S)
    private int solutionID; //* AUTONUMBERED *//
    private String solutionName;
    private int solutionVol;
    
    // >>>> CONNECT TO MS.ACCESS <<<< // 
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    // DEFAULT CONSTRUCTOR
    public solutions(){
        con = BBConnection.MSAccessConnection();
    }
    
    // CONSTRUCTOR W/ PARAMETER
    public solutions(int solutionID, String solutionName, int solutionVol, int qntyUsed){
        this.solutionID = solutionID;
        this.solutionName = solutionName;
        this.solutionVol = solutionVol;
        con = BBConnection.MSAccessConnection();
    }
    
    // ENCAPSULATION
    public void setSolutionID(int solutionID){
        this.solutionID = solutionID;
    }
    public int getSolutionID(){
        return solutionID;
    }
    public void setSolutionName(String solutionName){
        this.solutionName = solutionName;
    }
    public String getSolutionName(){
        return solutionName;
    }
    public void setSolutionVol(int solutionVol){
        this.solutionVol = solutionVol;
    }
    public int getSolutionVol(){
        return solutionVol;
    }
    
    // >>>> DATABASE METHODS <<<<< //
    @Override
    public void SaveInfo(){
        try{
            st = con.prepareStatement("INSERT INTO Solutions (solutionID, solutionName, solutionVol)" +
                    "VALUES ('"+solutionID+"','"+solutionName+"','"+solutionVol+"')");
            st.execute();
            System.out.println("Solution's Info Saved!");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void UpdateInfo(){
        try{
            st = con.prepareStatement("UPDATE Solutions SET " +
                                      "solutionID='" + solutionID + "', " +
                                      "solutionName='" + solutionName + "', " +
                                      "solutionVol='" + solutionVol + "'");
            st.executeUpdate();
            System.out.println("Solution's Info Updated!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void DeleteInfo(){
        try{
            st = con.prepareStatement("DELETE FROM Solutions WHERE solutionID='"+solutionID+"'");
            st.executeUpdate();
            System.out.println("Solution's Info DELETED!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void SearchInfo(){
        try{
            st = con.prepareStatement("SELECT * FROM Solutions WHERE solutionID='"+solutionID+"'");
            rs = st.executeQuery();
            
            if(rs.next()){
                solutionName = rs.getString("solutionName");
                solutionVol = rs.getInt("solutionVol");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public ArrayList<String> View(){
        ArrayList<String> list = new ArrayList<>();
        try{
            st = con.prepareStatement("SELECT * FROM Solutions ORDER BY solutionID DESC");
            rs = st.executeQuery();
            
            while(rs.next()){
                String profile = rs.getInt("solutionID") + " | " +
                                 rs.getString("solutionName") + " | " +
                                 rs.getInt("solutionVol");
                list.add(profile);
                        
                        }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    
    // RECORDS
        public LinkedList<solutions> records(){
            LinkedList<solutions> list = new LinkedList<>();
            try{
                st = con.prepareStatement("SELECT * FROM Solutions");
                rs = st.executeQuery();
                
                while(rs.next()){
                    solutions sol1 = new solutions();
                    sol1.solutionID = rs.getInt("solutionID");
                    sol1.solutionName = rs.getString("solutionName");
                    sol1.solutionVol = rs.getInt("solutionVol");
                    list.add(sol1);
                }
                return list;
            }catch(SQLException e){
                System.out.println("NO RECORDS FOUND!");
                return null;
            }
        }
}
