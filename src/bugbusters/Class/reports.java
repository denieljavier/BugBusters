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
public class reports implements functions {
    // ATTRIBUITE(S)
    private int reportID;   //* AUTONUMBERED *//
    private int jobID;
    private String clientConcerns;
    
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    // DEFAULT CONSTRUCTOR
    public reports(){
        con = BBConnection.MSAccessConnection();
    }
    
    // CONSTRUCTOR W/ PARAMETER(S)
    public reports(int reportID, int jobID, String clientConcerns){
        this.reportID = reportID;
        this.jobID = jobID;
        this.clientConcerns = clientConcerns;
        con = BBConnection.MSAccessConnection();
    }
    
    // METHOD(S)
    public void displayInfo(){
    }
    
    // ENCAPSULATIONNS (GET/SET)
    public void setReportID(int reportID){
        this.reportID = reportID;
    }
    public int getReportID(){
        return reportID;
    }
    public void setJobID(int jobID){
        this.jobID = jobID;
    }
    public int getJobID(){
        return jobID;
    }
    public void setClientConcerns(String clientConcerns){
        this.clientConcerns = clientConcerns;
    }
    public String getClientConcerns(){
        return clientConcerns;
    }
    
    // >>>> DATABASE METHODS <<< //
    // CREATE / SAVE RECORDS (Report is AutoNumbered)
    @Override
    public void SaveInfo(){
        try{
            st = con.prepareStatement(
                "INSERT INTO Report (jobID, clientConcerns)" +
                "VALUES ('"+jobID+"','"+clientConcerns+"')");
            st.execute();
            System.out.println("Report Saved!");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    //UPDATE
    @Override
    public void UpdateInfo(){
        try{
            st = con.prepareStatement(
                 "UPDATE Report SET " + 
                 "jobID='"+jobID+"', " + 
                 "clientConcerns='"+clientConcerns+"', " +
                 "WHERE reportID='"+reportID+"', ");
            st.executeUpdate();
            System.out.println("Report Updated!");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    //DELETE
    @Override
    public void DeleteInfo(){
        try{
            st = con.prepareStatement(
                 "DELETE FROM Report WHERE reportID='"+reportID+"'");
            st.executeUpdate();
            System.out.println("Report Deleted!");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    //SEARCH
    @Override
    public void SearchInfo(){
        try{
            st = con.prepareStatement(
                 "SELECT * FROM Report WHERE reportID='"+reportID+"'");
            rs = st.executeQuery();
            
            if(rs.next()){
                jobID = rs.getInt("jobID");
                clientConcerns = rs.getString("clientConcerns");
            }
        } catch(Exception e){
            System.out.print(e.getMessage());
        }
    }
    // VIEW
    public ArrayList<String> View(){
        ArrayList<String> list = new ArrayList<>();
        try{
            st = con.prepareStatement(
            "SELECT * FROM Report ORDER BY reportID DESC");
            rs = st.executeQuery();
            
            while(rs.next()){
                String row =
                        rs.getInt("reportID") + " | " +
                        rs.getInt("jobID") + " | " +
                        rs.getString("clientConcerns");
                list.add(row);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    
    //RECORDS
    public LinkedList<reports> records(){
        LinkedList<reports> list = new LinkedList<>();
        try{
            st = con.prepareStatement("SELECT * FROM Report");
            rs = st.executeQuery();
            
            while(rs.next()){
                reports rep = new reports();
                rep.reportID = rs.getInt("reportID");
                rep.jobID = rs.getInt("getInt");
                rep.clientConcerns = rs.getString("clientConcerns");
                list.add(rep);
            }
            return list;
        }catch(SQLException e){
            System.out.println("No Records");
            return null;
        }
    }
}
