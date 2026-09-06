/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bugbusters.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Deniel
 */
public class BBConnection {

    private static final String DB_PATH = "C:\\Users\\Deniel\\Downloads\\BugBusters.011726\\BugBusters\\src//BugBusters.accdb"; 
    
    // UCanAccess URL format
    private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;

    public static Connection MSAccessConnection() {
        Connection con = null;
        
        try {           
            // Establish the connection
            con = DriverManager.getConnection(DB_URL);

            //JOptionPane.showMessageDialog(null, "Connected to MS Access Database!");
            System.out.println( "Connected to MS Access Database!");
            
        } catch (SQLException ex) {
            // Handle connection errors
            //JOptionPane.showMessageDialog(null, "Database Connection Failed:\n" + ex.getMessage());
            System.out.println( "Database Connection Failed:\n" + ex.getMessage());
            
        } 
        return con;
    }
    
    public static void main(String[] args) {
        Connection testCon = MSAccessConnection();
        if (testCon != null) {
            try {
                testCon.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
