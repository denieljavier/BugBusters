/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bugbusters.Class;
import java.sql.*;

/**
 *
 * @author Deniel
 */
public class certifications {
    // ATTRIBUTE(S)
    private int certID;
    private String techID;
    private String techCert;
    private String techSpec;
    private String certDescription;
    
    //>>>> CONNECT TO MS ACCESS <<<<<
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    // DFAULT CONSTRUCTOR
    public certifications(){
        
    }
    
    // CONSTRUCTOR W/ PARAMETER(S)
    public certifications(int cerID, String techID, String techCert, String techSpec, String certDescription){
        this.certID = certID;
        this.techID = techID;
        this.techCert = techCert;
        this.techSpec = techSpec;
        this.certDescription = certDescription;
    }
    
    // ENCAPSULATION (GET/SET)
    public void setCertID(int certID){
        this.certID = certID;
    }
    public int getCertID(){
        return certID;
    }
    public void setTechID(String techID){
        this.techID = techID;
    }
    public String getTechID(){
        return techID;
    }
    public void setTechCert(String techCert){
        this.techCert = techCert;
    }
    public String getTechCert(){
        return techCert;
    }
    public void setTechSpec(String techSpec){
        this.techSpec = techSpec;
    }
    public String getTechSpec(){
        return techSpec;
    }
    public void setCertDescription(String certDescription){
        this.certDescription = certDescription;
    }
    public String getCertDescription(){
        return certDescription;
    }
    
}
