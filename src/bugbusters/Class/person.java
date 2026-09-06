    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bugbusters.Class;


/**
 *
 * @author Deniel
 */
public abstract class person {
   // ABSTRACT ATTRIBUTE(S)
   String firstName;
   String lastName;
   String contactNum;
   
   public person(){
       
   }
   public person(String firstName, String lastName, String contactNum){
       this.lastName = lastName;
       this.firstName = firstName;
       this.contactNum = contactNum;
   }
   
   // ENCAPSULATION
   public void setfirstName(String firstName){
       this.firstName = firstName;
   }
   public String getfirstName(){
       return firstName;
   }
   
   public void setLastName(String lastName){
       this.lastName = lastName;
   }
   public String getlastName(){
       return lastName;
   }
   
   public void setContactNum(String contactNum){
       this.contactNum = contactNum;
   }
   public String getContactNum(){
       return contactNum;
   }
}
