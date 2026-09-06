/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bugbusters.Class;

import bugbusters.database.BBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

    /**
 *
 * @author Deniel
 */
public class job implements functions{
    // ATTRIBUTE(S)
    private int jobID;          //* AUTONUMBERED *//
    private int clientID;      //* AUTONUMBERED *//
    private Date jobDate;
    private BigDecimal serviceFee;
    
    
    // >>>> CONNECT TO MS.ACCESS <<<< // 
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    // DEFAULT CONSTRUCTOR
    public job(){
        con = BBConnection.MSAccessConnection();
    }
    
    // CONSTRUCTOR W/ PARAMETER(S)
    public job(int jobID, int clientID,  Date jobDate, BigDecimal serviceFee){
        this.jobID = jobID;
        this.clientID = clientID;
        this.jobDate = jobDate;
        this.serviceFee = serviceFee;
        con = BBConnection.MSAccessConnection();
    }
    
    
    // ENCAPSULATION (GET/SET)
    public void setJobID(int jobID){
        this.jobID = jobID;
    }
    public int getJobID(){
        return jobID;
    }
    public void setClientID(int clientID){
        this.clientID = clientID;
    }
    public int getClientID(){
        return clientID;
    }
    public void setJobDate(Date jobDate){
        this.jobDate = jobDate;
    }
    public Date getJobDate(){
        return jobDate;
    }
    public void setServiceFee(BigDecimal serviceFee){
        this.serviceFee = serviceFee;
    }
    public BigDecimal getServiceFee(){
        return serviceFee;
    }
    
    // >>>> DATABASE METHODS <<<< //
    @Override
    public void SaveInfo(){
        try{
            st = con.prepareStatement("INSERT INTO Job (jobID, clientID, jobDate, serviceFee) " + 
                    "VALUES ('"+jobID+"','"+clientID+"','"+jobDate+"','"+serviceFee+"')");
            st.execute();
            System.out.println("Job's info saved!");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
  
     // UPDATE INFO
    @Override
    public void UpdateInfo(){
        try{
            st = con.prepareStatement("UPDATE Job SET " +
                                      "jobID='" + jobID + "', " +
                                      "clientID='" + clientID + "', " +
                                      "jobDate='" + jobDate + "', " +
                                      "serviceFee='" + serviceFee + "'");
            st.executeUpdate();
            System.out.println("Job's Info Updated!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // DELETE INFO
    @Override
    public void DeleteInfo(){
        try{
            st = con.prepareStatement("DELETE FROM Job WHERE jobID='"+jobID+"'");
            st.executeUpdate();
            System.out.println("Job's Info DELETED!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // SEARCH INFO
    @Override
    public void SearchInfo(){
        try{
            st = con.prepareStatement("SELECT * FROM Job WHERE jobID='"+jobID+"'");
            rs = st.executeQuery();
            
            if(rs.next()){
                jobID = rs.getInt("jobID");
                clientID = rs.getInt("clientID");
                jobDate = rs.getDate("jobDate");
                serviceFee = rs.getBigDecimal(4);
                
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // VIEW INFO
    public ArrayList<String> View(){
        ArrayList<String> list = new ArrayList<>();
        try{
            st = con.prepareStatement("SELECT * FROM Job ORDER BY jobID DESC");
            rs = st.executeQuery();
            
            while(rs.next()){
                String profile = rs.getInt("jobID") + " | " +
                              rs.getInt("clientID") + " | " +
                              rs.getDate("jobDate") + " | " +
                              rs.getBigDecimal("serviceFee");
                list.add(profile);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    
    // RECORDS
        public LinkedList<job> records(){
            LinkedList<job> list = new LinkedList<>();
            try{
                st = con.prepareStatement("SELECT * FROM Job");
                rs = st.executeQuery();
                
                while(rs.next()){
                    job job = new job();
                    job.jobID = rs.getInt("jobID");
                    job.clientID = rs.getInt("clientID");
                    job.jobDate = rs.getDate("jobDate");
                    job.serviceFee = rs.getBigDecimal("serviceFee");
                    list.add(job);
                }
                return list;
            }catch(SQLException e){
                System.out.println("NO RECORDS FOUND!");
                return null;
            }
        }
    
}
