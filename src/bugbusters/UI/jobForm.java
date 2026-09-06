/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bugbusters.UI;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import bugbusters.database.BBConnection;
import java.awt.Font;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



/**
 *
 * @author Deniel
 */
public class jobForm extends javax.swing.JFrame {
    Connection con = BBConnection.MSAccessConnection();
    PreparedStatement st;
    ResultSet rs;
    
    /**
     * Creates new form jobForm
     */
    private final ButtonGroup jobStatusGroup = new ButtonGroup();
    public jobForm() {
        initComponents();
        loadJobFormToTable();
        
        // JOB STATUS BUTTONS
         jobStatusGroup.add(rbtnDone);
         jobStatusGroup.add(rbtnPending);
         jobStatusGroup.add(rbtnCancel);
         rbtnPending.setSelected(true);
         
         setLocationRelativeTo(null);   // this is to center the UI whenever it is running

         con = BBConnection.MSAccessConnection();
         
         // THIS IS TO SHOW THE JOB ID IN THE TEXTFIELD WHENEVER WE SELECT SOMTHING IN CLIENT COMBOBOX
         cboClient.addActionListener(e -> {
             int idx = cboClient.getSelectedIndex();
             if(idx >= 0 && idx < jobIDList.size()){
                 int jobID = jobIDList.get(idx);
                 textJobID.setText(String.valueOf(jobID));
             }else{
                 textJobID.setText("");
             }
         });
         cboTechnician.addActionListener(e -> {
             int idx1 = cboTechnician.getSelectedIndex();
             if(idx1 >= 0 && idx1 < techIDList.size()){
                 String techID = techIDList.get(idx1);
                 textTechID.setText(techID);
             }else{
                 textTechID.setText("");
             }
         });
          cboClient.addActionListener(e -> {
             int idx2 = cboClient.getSelectedIndex();
             if(idx2 >= 0 && idx2 < clientIDList.size()){
                 int clientID = clientIDList.get(idx2);
                 textClientID.setText(Integer.toString(clientID));
             }else{
                 textTechID.setText("");
             }
         });
          
// >>>>>>>>>>>>>>>>> CLIENT'S INFORMATION IN COMBO BOXES SETTING ON TEXT FIELDS <<<<<<<<<<<<<<<<<<<<<

         // SETTING TEXT WHENEVER USER SELECT CLIENT IN COMBO BOX
         
         // LAST NAME
         cboClient.addActionListener(e -> {
             int idx3 = cboClient.getSelectedIndex();
             if(idx3 >= 0 && idx3 < clientLastName.size()){
                 String ClientLastName = clientLastName.get(idx3);
                 textLastName.setText(ClientLastName);
             }else{
                 textLastName.setText("");
             }
         });
         
         // FIRST NAME
         cboClient.addActionListener(e -> {
             int idx4 = cboClient.getSelectedIndex();
             if(idx4 >= 0 && idx4 < clientFirstName.size()){
                 String ClientFirstName = clientFirstName.get(idx4);
                 textFirstName.setText(ClientFirstName);
             }else{
                 textFirstName.setText("");
             }
         });
         
         // CONTACT NUMBER
         cboClient.addActionListener(e -> {
             int idx5 = cboClient.getSelectedIndex();
             if(idx5 >= 0 && idx5 < contactNum.size()){
                 String contactNumber = contactNum.get(idx5);
                 textContactNum.setText(contactNumber);
             }else{
                 textContactNum.setText("");
             }
         });
         
         // CITY ADDRESS
         cboClient.addActionListener(e -> {
             int idx6 = cboClient.getSelectedIndex();
             if(idx6 >= 0 && idx6 < cityAdd.size()){
                 String cityAddress = cityAdd.get(idx6);
                 textCityAdd.setText(cityAddress);
             }else{
                 textCityAdd.setText("");
             }
         });
         
         // HOME ADDRESS
         cboClient.addActionListener(e -> {
             int idx7 = cboClient.getSelectedIndex();
             if(idx7 >= 0 && idx7 < homeAdd.size()){
                 String homeAddress = homeAdd.get(idx7);
                 textHomeAdd.setText(homeAddress);
             }else{
                 textHomeAdd.setText("");
             }
         });
         
         // CLIENT CONCERNS
         cboClient.addActionListener(e -> {
             int idx8 = cboClient.getSelectedIndex();
             if(idx8 >= 0 && idx8 < clientConcerns.size()){
                 String clientConcern = clientConcerns.get(idx8);
                 textClientConcerns.setText(clientConcern);
             }else{
                 textClientConcerns.setText("");
             }
         });
         
         // THESE LOAD THE DATAS ONTO THE TABLE
         loadSolutionToTable();
         loadClientFormToTable();
         loadJobDoneToTable();
         loadJobPendingToTable();
         
         // LOAD COMBOBOXES
         loadClientsToCombo();
         loadTechToCombo();
         loadSolutionToCombo();
         clearFields();
        
    }
  
    
    
    // COMBO BOX SELECTION (TECHNICIAN)
    private List<String> techIDList = new ArrayList<>();
    private void loadTechToCombo(){
        cboTechnician.removeAllItems();
        String sql = "SELECT TechnicianDetails.techID, TechnicianDetails.firstName, TechnicianDetails.lastName " +
                "FROM TechnicianDetails ORDER BY TechnicianDetails.lastName";
        
        try(Connection con = BBConnection.MSAccessConnection();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                String techID = rs.getString("techID");
                String name = rs.getString("lastName") + ", " + rs.getString("firstName");
                cboTechnician.addItem(name);
                techIDList.add(techID);
        }
            
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    }
    
    // COMBO BOX SELECTION (CLIENT)
    private List<Integer> jobIDList = new ArrayList<>();
    private List<Integer> clientIDList = new ArrayList<>();
    private List<String> clientLastName = new ArrayList<>();
    private List<String> clientFirstName = new ArrayList<>();
    private List<String> contactNum = new ArrayList<>();
    private List<String> cityAdd = new ArrayList<>();
    private List<String> homeAdd = new ArrayList<>();
    private List<String> clientConcerns = new ArrayList<>();
    private void loadClientsToCombo(){
        cboClient.removeAllItems();
        jobIDList.clear();
        
        String sql = "SELECT DISTINCT " +
                "Job.jobID, Job.clientID, Client.firstName, Client.lastName, Client.contactNum, " +
                "Client.cityAdd, Client.homeAdd, Report.clientConcerns " +
                "FROM Job INNER JOIN Client ON Job.clientID = Client.clientID " +
                "INNER JOIN Report ON Job.jobID = Report.jobID " +
                "LEFT JOIN TechAssign ON Job.jobID = TechAssign.jobID " +
                "WHERE TechAssign.status IS NULL OR TechAssign.status = 'PENDING' " +
                "ORDER BY Client.lastName, Client.firstName";
 
        try(Connection con = BBConnection.MSAccessConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()){
            
            clientIDList.clear();
            jobIDList.clear();
            while(rs.next()){
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                String contactNum1 = rs.getString("contactNum");
                String cityAdd1 = rs.getString("cityAdd");
                String homeAdd1 = rs.getString("homeAdd");
                String clientConcerns1 = rs.getString("clientConcerns");
                String name = rs.getString("lastName") + ", " + rs.getString("firstName");
                cboClient.addItem(name);
                jobIDList.add(rs.getInt("jobID"));
                clientIDList.add(rs.getInt("clientID"));
                clientLastName.add(lastName);
                clientFirstName.add(firstName);
                contactNum.add(contactNum1);
                cityAdd.add(cityAdd1);
                homeAdd.add(homeAdd1);
                clientConcerns.add(clientConcerns1); 
            }
            System.out.println("Clients loaded: " + jobIDList.size());
            for (int i = 0; i < jobIDList.size(); i++) {
            System.out.println(i + " -> jobID " + jobIDList.get(i));
            }
                rs.close();
                pst.close();
                con.close();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        
    }
    // COMBO BOX SELECTION (SOLUTIONS)
    private final List<Integer>  solutionIDList = new ArrayList<>();
    public void loadSolutionToCombo(){
        
        String sql = "SELECT s.solutionID, s.solutionName " +
                "FROM Solutions AS s " +
                "WHERE s.solutionID NOT IN (" +
                " SELECT js.solutionID " +
                "FROM JobSolution AS js " +
                "INNER JOIN TechAssign AS ta ON js.jobID = ta.jobID " +
                "WHERE ta.status IS NULL OR ta.status <> 'PENDING'" +
                ") ORDER BY s.solutionName";
        
        cboSolution.removeAllItems();
        solutionIDList.clear();
        
        try(Connection con = BBConnection.MSAccessConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()){
            
            while(rs.next()){
                int id = rs.getInt("solutionID");
                String name = rs.getString("solutionName");
                cboSolution.addItem(name);
                solutionIDList.add(id);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        int index = cboSolution.getSelectedIndex();
            if (index < 0) {
                String brand = "No Solutions available";
                cboSolution.addItem(brand);
                return;
        } else if (index > 0){
            cboSolution.removeAllItems();
        } 
            }
    
    public void fillFieldsFromSelectedRow(){
        int row1 = tblJob.getSelectedRow();
        int row2 = tblClientReports.getSelectedRow();
        int row3 = tblSolution.getSelectedRow();
        int row4 = tblJobPending.getSelectedRow();
        
        if(row4 == -1) return;
        // GETTING VALUES FROM THE TABLE (CLIENT)
        String bookingDate = tblClientReports.getValueAt(row2, 0).toString();
        String lastName = tblClientReports.getValueAt(row2, 1).toString();
        String firstName = tblClientReports.getValueAt(row2, 2).toString();
        String contactNumber = tblClientReports.getValueAt(row2, 3).toString();
        String cityAdd = tblClientReports.getValueAt(row2, 4).toString();
        String homeAdd = tblClientReports.getValueAt(row2, 5).toString();
        String clientConcerns = tblClientReports.getValueAt(row2, 6).toString();
        String clientID = tblClientReports.getValueAt(row2, 7).toString();
        String jobID = tblClientReports.getValueAt(row2, 8).toString();
        
        // GETTING VALUES FROM >>>>>>>>>>>>>> TECHNICIAN (JOB/QUERIES REPORTS) <<<<<<<<<<<<<<
        String techID = tblJob.getValueAt(row1, 2).toString();
        
        // SETTING TEXT FIELDS (CLIENT)
        textLastName.setText(lastName);
        textFirstName.setText(firstName);
        textContactNum.setText(contactNumber);
        textCityAdd.setText(cityAdd);
        textHomeAdd.setText(homeAdd);
        
        // GETTING THE VALUES
        String jobID2 = tblJobPending.getValueAt(row4, 0).toString();
        String clientID2 = tblJobPending.getValueAt(row4, 1).toString();
        String techID2 = tblJobPending.getValueAt(row4, 2).toString();
        String solUsed = tblJobPending.getValueAt(row4, 3).toString();
        String stat = tblJobPending.getValueAt(row4, 4).toString();
        String serviceFee = tblJobPending.getValueAt(row4, 5).toString();
        
        //SETTInG THE VALUES
        cboSolution.setActionCommand(solUsed);
        cboTechnician.setActionCommand(techID2);
        textTechID.setText(techID2);
        textServiceFee.setText(serviceFee);
        
        
        
        selectComboByID(cboTechnician, techID);
        selectComboByID(cboClient, clientID);

    }
   
        private void selectComboByID(JComboBox<String> combo, String id){
            for (int i = 0; i < combo.getItemCount(); i++) {
                String item = combo.getItemAt(i);
                if(item.contains("ID: " + id) || item.startsWith(id + " ")){
                    combo.setSelectedItem(i);
                    break;
                }
            }
        }
    
        private boolean isFormValid(){
        if(textQuantity.getText().trim().isEmpty()) return false;
        if(textVolume.getText().trim().isEmpty()) return false;
        if(cboClient.getSelectedItem().toString().isEmpty()) return false;
        if(cboTechnician.getSelectedItem().toString().isEmpty()) return false;
        if(cboSolution.getSelectedItem().toString().isEmpty()) return false;
        if(jobStatusGroup.getSelection() == null) return false;
        return true;
}
        
        private void clearFields(){
            textJobID.setText("");
            textTechID.setText("");
            textLastName.setText("");
            textFirstName.setText("");
            textCityAdd.setText("");
            textHomeAdd.setText("");
            textContactNum.setText("");
            textClientConcerns.setText("");
            textVolume.setText("");
            textQuantity.setText("");
            cboClient.setSelectedIndex(-1);
            cboSolution.setSelectedIndex(-1);
            cboTechnician.setSelectedIndex(-1);
            jobStatusGroup.clearSelection();
            textServiceFee.setText("");
            textClientID.setText("");
        }
        
        private void loadSolutionToTable(){
            try{
                DefaultTableModel model = (DefaultTableModel) tblSolution.getModel();
                model.setRowCount(0);
                
                String sql = "SELECT jobID, jobDate, solutionName, solutionVol, qntyUsed, TotalMl " +
                        "FROM qryJobSolutions";
                Connection con = BBConnection.MSAccessConnection();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                
                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getInt("jobID"),
                        rs.getDate("jobDate"),
                        rs.getString("solutionName"),
                        rs.getInt("solutionVol"),
                        rs.getInt("qntyUsed"),
                        rs.getInt("totalMl")
                    });
                    }
                rs.close();
                pst.close();
                
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }

        }
        private void loadJobPendingToTable(){
            try {
                Connection con = BBConnection.MSAccessConnection();
                DefaultTableModel model = (DefaultTableModel) tblJobPending.getModel();
                model.setRowCount(0);
                
                String sql = "SELECT Job.jobID, Client.clientID, " +
                        "TechAssign.techID, TechAssign.status, " + 
                        "Solutions.solutionName, Job.serviceFee, " +
                        "JobSolution.qntyUsed, Solutions.solutionVol, "+
                        "Client.lastName AS cLname, Client.firstName AS cFname, Client.homeAdd, Client.cityAdd, Client.contactNum, " +
                        "Report.clientConcerns, Solutions.solutionID, " +
                        "TechnicianDetails.lastName, TechnicianDetails.firstName " +
                        "FROM (((Job " +
                        "LEFT JOIN TechAssign ON Job.jobID = TechAssign.jobID) " +
                        "LEFT JOIN JobSolution ON Job.jobID = JobSolution.jobID) " +
                        "LEFT JOIN Solutions ON JobSolution.solutionID = Solutions.solutionID) " +
                        "LEFT JOIN Report ON Job.jobID = Report.jobID " +
                        "LEFT JOIN Client ON Job.clientID = Client.clientID " +
                        "LEFT JOIN TechnicianDetails ON TechAssign.techID = TechnicianDetails.techID " +
                        "WHERE TechAssign.status = 'PENDING'";
                
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()){
                    BigDecimal fee = rs.getBigDecimal("serviceFee");
                    String StrFee = String.format("%.2f", fee);
                    model.addRow(new Object[]{
                        rs.getInt("jobID"),
                        rs.getInt("clientID"),
                        rs.getString("techID"),
                        rs.getString("solutionName"),
                        rs.getString("status"),
                        StrFee,
                        rs.getInt("qntyUsed"),
                        rs.getInt("solutionVol"),
                        rs.getString("cLname"),
                        rs.getString("cFname"),
                        rs.getString("homeAdd"),
                        rs.getString("cityAdd"),
                        rs.getString("contactNum"),
                        rs.getString("clientConcerns"),
                        rs.getInt("solutionID"),
                        rs.getString("lastName"),
                        rs.getString("firstName")
                    });
                }
                con.close();
            } catch (Exception e){
                e.printStackTrace();
                }
        }
        private void loadJobDoneToTable(){
            try {
                Connection con = BBConnection.MSAccessConnection();
                DefaultTableModel model = (DefaultTableModel) tblJobDone.getModel();
                model.setRowCount(0);
                
                String sql = "SELECT Job.jobID, Job.clientID, " +
                        "TechAssign.techID, TechAssign.status, " + 
                        "Solutions.solutionName, Job.serviceFee " +
                        "FROM (((Job " +
                        "LEFT JOIN TechAssign ON Job.jobID = TechAssign.jobID) " +
                        "LEFT JOIN JobSolution On Job.jobID = JobSolution.jobID) " +
                        "LEFT JOIN Solutions ON JobSolution.solutionID = Solutions.solutionID) " +
                        "WHERE TechAssign.status = 'DONE'";
                
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()){
                    BigDecimal fee = rs.getBigDecimal("serviceFee");
                    String StrFee = String.format("%.2f", fee);
                    model.addRow(new Object[]{
                        rs.getInt("jobID"),
                        rs.getInt("clientID"),
                        rs.getString("techID"),
                        rs.getString("solutionName"),
                        rs.getString("status"),
                        StrFee
                    });
                }
                con.close();
            } catch (Exception e){
                e.printStackTrace();
                }
        }
        private void loadJobFormToTable(){
            try {
                Connection con = BBConnection.MSAccessConnection();
                DefaultTableModel model = (DefaultTableModel) tblJob.getModel();
                model.setRowCount(0);
                
                String sql = "SELECT Job.jobID, Job.clientID, " +
                        "TechAssign.techID, TechAssign.status, " + 
                        "Solutions.solutionName, Job.serviceFee, Solutions.solutionID " +
                        "FROM (((Job " +
                        "LEFT JOIN TechAssign ON Job.jobID = TechAssign.jobID) " +
                        "LEFT JOIN JobSolution On Job.jobID = JobSolution.jobID) " +
                        "LEFT JOIN Solutions ON JobSolution.solutionID = Solutions.solutionID) " +
                        "WHERE TechAssign.status IS NULL";
                
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()){
                    BigDecimal fee = rs.getBigDecimal("serviceFee");
                    String StrFee = String.format("%.2f", fee);
                    model.addRow(new Object[]{
                        rs.getInt("jobID"),
                        rs.getInt("clientID"),
                        rs.getString("techID"),
                        rs.getString("solutionName"),
                        rs.getString("status"),
                        StrFee,
                        rs.getInt("solutionID")
                    });
                }
                con.close();
            } catch (Exception e){
                e.printStackTrace();
                }
        }
        
        private void loadClientFormToTable(){
            try{
                Connection con = BBConnection.MSAccessConnection();
                DefaultTableModel model = (DefaultTableModel) tblClientReports.getModel();
                model.setRowCount(0);
                
                String sql = "SELECT Job.jobDate, " +
                "Client.firstName, Client.lastName, Client.contactNum, " +
                "Client.cityAdd, Client.homeAdd, " +
                "Report.clientConcerns, " +
                "Client.clientID, " +
                "Job.jobID " +
                "FROM  (Client INNER JOIN Job ON Client.clientID = Job.clientID) " +
                "INNER JOIN Report ON Job.jobID = Report.jobID";
                
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
            
            int rowCount = 0;
            while (rs.next()){
                rowCount++;
                model.addRow(new Object[]{
                    rs.getDate("jobDate"),
                    rs.getString("lastName"),
                    rs.getString("firstName"),
                    rs.getString("contactNum"),
                    rs.getString("cityAdd"),
                    rs.getString("homeAdd"),
                    rs.getString("clientConcerns"), 
                    rs.getInt("clientID"), // >>>> 7 Hidden
                    rs.getInt("jobID") // >>>>>>>> 8 Hidden
                });
            }
             System.out.println("Rows from query: " + rowCount);
            con.close();
            

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        }
        
        private String getSelectedJobStatus(){
            if(rbtnDone.isSelected()){
                return "DONE";
            } else if (rbtnPending.isSelected()){
                return "PENDING";
            } else if (rbtnCancel.isSelected()){
                return "CANCELLED";
            }
            return "PENDING";
        }
        
        private String buildReceiptText(List<Object[]> rows){
            StringBuilder sb = new StringBuilder();
            
            // HEADER
            sb.append("****************************************\n");
            sb.append("    SOLUTION USAGE REPORT\n");
            sb.append("****************************************\n");
            sb.append(new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a").format(new java.util.Date())).append("\n\n");
            
            // DETAIL LINES
            sb.append(String.format("%-12s %-20s %8s %8s %8s\n", "Job Date", "Solution", "Volume", "QntyUsed", "Total ml"));
            sb.append("----------------------------------------\n");
            
            for(Object[] row : rows){
                java.sql.Date d = (java.sql.Date) row[0];
                String solutionName = (String) row[1];
                int vol = (Integer) row[2];
                int qty = (Integer) row[3];
                int total = (Integer) row[4];
                
                sb.append(String.format("%-12s %-20s %8d %8d %8d\n", new java.text.SimpleDateFormat("yyyy-MM-dd").format(d),
                solutionName,vol, qty, total));
            }
            
            // YEAR TOTALS
            sb.append("========================================\n");
            sb.append("TOTAL ML PER YEAR:\n");
            sb.append("========================================\n");
            
            Map<Integer, Integer> totalsPerYear = new LinkedHashMap<>();
            for(Object[] r: rows){
                java.sql.Date d = (java.sql.Date) r[0];
                int totalMl = Integer.parseInt(r[4].toString());
                
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.setTime(d);
                int year = c.get(java.util.Calendar.YEAR);
                
                totalsPerYear.merge(year, totalMl, Integer::sum);
            }
            for(Map.Entry<Integer, Integer> e : totalsPerYear.entrySet()){
                sb.append(String.format("Year %d: %, d mL\n", e.getKey(), e.getValue()));
                
            }
            
            sb.append("========================================\n");
            return sb.toString();
        }
        
        private void showReceiptDialog(String receipt){
            JTextArea textArea = new JTextArea(receipt);
            textArea.setEditable(false);
            textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
            textArea.setLineWrap(false);
            
            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new java.awt.Dimension(500, 350));
            
            int option = JOptionPane.showConfirmDialog(this, scroll, "SOLUTION USAGE REPORT", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
            if(option == JOptionPane.OK_OPTION){
                try{
                    textArea.print();   
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnReturn1 = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cboTechnician = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        textHomeAdd = new javax.swing.JTextField();
        textCityAdd = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        textLastName = new javax.swing.JTextField();
        textFirstName = new javax.swing.JTextField();
        textClientConcerns = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboSolution = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        textQuantity = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        textVolume = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblJob = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientReports = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSolution = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblJobDone = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblJobPending = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnPrintReport = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAddSolution = new javax.swing.JButton();
        btnAddQueries = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        rbtnPending = new javax.swing.JRadioButton();
        rbtnCancel = new javax.swing.JRadioButton();
        rbtnDone = new javax.swing.JRadioButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cboClient = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        textJobID = new javax.swing.JTextField();
        textTechID = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        textContactNum = new javax.swing.JTextField();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jLabel4 = new javax.swing.JLabel();
        textServiceFee = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        textClientID = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();

        jLabel16.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        jLabel16.setText("JOB ID#:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1470, 850));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel1.setText("JOB QUERIES / REPORTS");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, -1, -1));

        btnReturn1.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        btnReturn1.setText("RETURN");
        btnReturn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturn1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnReturn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 99, 34));

        btnRefresh.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        btnRefresh.setText("REFRESH");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel1.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 110, 34));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1620, 90));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(705, 139, -1, -1));

        cboTechnician.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        cboTechnician.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboTechnicianMouseClicked(evt);
            }
        });
        cboTechnician.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTechnicianActionPerformed(evt);
            }
        });
        getContentPane().add(cboTechnician, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, 205, 34));

        jLabel6.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel6.setText("City Address:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 530, -1, -1));

        textHomeAdd.setEditable(false);
        getContentPane().add(textHomeAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 290, 35));

        textCityAdd.setEditable(false);
        getContentPane().add(textCityAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 550, 240, 35));

        jLabel7.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel7.setText("CONTACT NO#:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 590, -1, -1));

        jLabel8.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel8.setText("First Name");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 470, -1, -1));

        textLastName.setEditable(false);
        getContentPane().add(textLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 490, 190, 35));

        textFirstName.setEditable(false);
        getContentPane().add(textFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 490, 240, 35));

        textClientConcerns.setEditable(false);
        getContentPane().add(textClientConcerns, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 670, 540, 67));

        jLabel9.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel9.setText("CONCERNS:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 650, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel11.setText("CHEMICAL SOLUTIONS");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, -1, -1));

        cboSolution.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        getContentPane().add(cboSolution, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, 205, 34));

        jLabel12.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel12.setText("Select a Solution:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));
        getContentPane().add(textQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 127, 34));

        jLabel13.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel13.setText("Quantity:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 330, -1, -1));
        getContentPane().add(textVolume, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 350, 127, 34));

        jLabel14.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel14.setText("Volume:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 330, -1, -1));

        jLabel15.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel15.setText("mL");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 360, -1, -1));

        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel2.setText("JOB QUERIES");
        jPanel9.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 38));

        jPanel5.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 750, 60));

        tblJob.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "JobID", "Client ID", "Tech ID", "Solution Used", "Status", "Service Fee", "solutionID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblJob.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblJob);
        if (tblJob.getColumnModel().getColumnCount() > 0) {
            tblJob.getColumnModel().getColumn(0).setResizable(false);
            tblJob.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblJob.getColumnModel().getColumn(1).setResizable(false);
            tblJob.getColumnModel().getColumn(2).setResizable(false);
            tblJob.getColumnModel().getColumn(3).setResizable(false);
            tblJob.getColumnModel().getColumn(4).setResizable(false);
            tblJob.getColumnModel().getColumn(5).setResizable(false);
            tblJob.getColumnModel().getColumn(6).setMinWidth(0);
            tblJob.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblJob.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        jPanel5.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, 740, 510));

        jTabbedPane1.addTab("JOB QUERIES", jPanel5);

        tblClientReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date Booked", "Last Name", "First Name", "Contact Number", "City Add", "Home Add", "Concerns", "Client ID", "Job ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientReports.setFocusable(false);
        tblClientReports.setRowSelectionAllowed(false);
        tblClientReports.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblClientReports);
        if (tblClientReports.getColumnModel().getColumnCount() > 0) {
            tblClientReports.getColumnModel().getColumn(0).setResizable(false);
            tblClientReports.getColumnModel().getColumn(1).setResizable(false);
            tblClientReports.getColumnModel().getColumn(2).setResizable(false);
            tblClientReports.getColumnModel().getColumn(3).setMinWidth(0);
            tblClientReports.getColumnModel().getColumn(3).setPreferredWidth(0);
            tblClientReports.getColumnModel().getColumn(3).setMaxWidth(0);
            tblClientReports.getColumnModel().getColumn(4).setMinWidth(0);
            tblClientReports.getColumnModel().getColumn(4).setPreferredWidth(0);
            tblClientReports.getColumnModel().getColumn(4).setMaxWidth(0);
            tblClientReports.getColumnModel().getColumn(5).setMinWidth(0);
            tblClientReports.getColumnModel().getColumn(5).setPreferredWidth(0);
            tblClientReports.getColumnModel().getColumn(5).setMaxWidth(0);
            tblClientReports.getColumnModel().getColumn(6).setResizable(false);
            tblClientReports.getColumnModel().getColumn(7).setMinWidth(0);
            tblClientReports.getColumnModel().getColumn(7).setPreferredWidth(0);
            tblClientReports.getColumnModel().getColumn(7).setMaxWidth(0);
            tblClientReports.getColumnModel().getColumn(8).setMinWidth(0);
            tblClientReports.getColumnModel().getColumn(8).setPreferredWidth(0);
            tblClientReports.getColumnModel().getColumn(8).setMaxWidth(0);
        }

        jPanel8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel17.setText("RECENT REPORTS");
        jPanel8.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 340, 50));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("CLIENT REPORTS", jPanel4);

        tblSolution.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Job ID", "Job Date", "Brand", "Volume", "Quantity", "Total ML"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSolution.setFocusable(false);
        tblSolution.setRowSelectionAllowed(false);
        tblSolution.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblSolution);
        if (tblSolution.getColumnModel().getColumnCount() > 0) {
            tblSolution.getColumnModel().getColumn(0).setResizable(false);
            tblSolution.getColumnModel().getColumn(1).setResizable(false);
            tblSolution.getColumnModel().getColumn(2).setResizable(false);
            tblSolution.getColumnModel().getColumn(3).setResizable(false);
            tblSolution.getColumnModel().getColumn(4).setResizable(false);
            tblSolution.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel18.setText("SOLUTION USAGE");
        jPanel10.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 340, 50));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SOLUTIONS USED", jPanel7);

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblJobDone.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "JobID", "Client ID", "Tech ID", "Solution Used", "Status", "Service Fee"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblJobDone.setEnabled(false);
        tblJobDone.setFocusable(false);
        tblJobDone.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(tblJobDone);
        if (tblJobDone.getColumnModel().getColumnCount() > 0) {
            tblJobDone.getColumnModel().getColumn(0).setResizable(false);
            tblJobDone.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblJobDone.getColumnModel().getColumn(2).setResizable(false);
            tblJobDone.getColumnModel().getColumn(3).setResizable(false);
            tblJobDone.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel12.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 730, 470));

        jTabbedPane2.addTab("DONE", jPanel12);

        tblJobPending.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "JobID", "Client ID", "Tech ID", "Solution Used", "Status", "Service Fee", "qnty", "Volume", "lastName", "firstName", "homeAdd", "cityAdd", "contactNum", "concerns", "solutionID", "techLastName", "techFirstName"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblJobPending.getTableHeader().setReorderingAllowed(false);
        tblJobPending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblJobPendingMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblJobPending);
        if (tblJobPending.getColumnModel().getColumnCount() > 0) {
            tblJobPending.getColumnModel().getColumn(0).setResizable(false);
            tblJobPending.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(1).setResizable(false);
            tblJobPending.getColumnModel().getColumn(2).setResizable(false);
            tblJobPending.getColumnModel().getColumn(3).setResizable(false);
            tblJobPending.getColumnModel().getColumn(4).setResizable(false);
            tblJobPending.getColumnModel().getColumn(5).setResizable(false);
            tblJobPending.getColumnModel().getColumn(6).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(6).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(7).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(7).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(7).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(8).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(8).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(8).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(9).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(9).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(9).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(10).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(10).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(10).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(11).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(11).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(11).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(12).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(12).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(12).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(13).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(13).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(13).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(14).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(14).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(14).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(15).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(15).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(15).setMaxWidth(0);
            tblJobPending.getColumnModel().getColumn(16).setMinWidth(0);
            tblJobPending.getColumnModel().getColumn(16).setPreferredWidth(0);
            tblJobPending.getColumnModel().getColumn(16).setMaxWidth(0);
        }

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("PENDING", jPanel14);

        jLabel29.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N

        jPanel13.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel30.setText("JOB HISTORY");
        jPanel13.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, 38));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane2)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("JOB HISTORY", jPanel11);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 110, 770, 650));

        btnUpdate.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 300, 100, 40));

        btnPrintReport.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        btnPrintReport.setText("PRINT REPORT");
        btnPrintReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintReportActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrintReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 210, 160, 50));

        btnDelete.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 100, 40));

        btnAddSolution.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        btnAddSolution.setText("ADD SOLUTIONS");
        btnAddSolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSolutionActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddSolution, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 130, 160, 70));

        btnAddQueries.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        btnAddQueries.setText("ADD QUERIES");
        btnAddQueries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddQueriesActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddQueries, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 300, 160, 90));

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        rbtnPending.setFont(new java.awt.Font("Schadow BT", 1, 12)); // NOI18N
        rbtnPending.setText("PENDING");

        rbtnCancel.setFont(new java.awt.Font("Schadow BT", 1, 12)); // NOI18N
        rbtnCancel.setText("CANCELLED");

        rbtnDone.setFont(new java.awt.Font("Schadow BT", 1, 12)); // NOI18N
        rbtnDone.setText("DONE");

        jLabel20.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel20.setText("JOB STATUS:");

        jLayeredPane1.setLayer(rbtnPending, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(rbtnCancel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(rbtnDone, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel20, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(rbtnDone)
                    .addComponent(rbtnCancel)
                    .addComponent(rbtnPending))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnDone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtnPending, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtnCancel)
                .addGap(22, 22, 22))
        );

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 120, -1, 150));

        jLabel21.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel21.setText("Select a Client:");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, -1, -1));

        cboClient.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        cboClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClientActionPerformed(evt);
            }
        });
        getContentPane().add(cboClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, 205, 34));

        jLabel22.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        jLabel22.setText("JOB ID#:");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        textJobID.setEditable(false);
        textJobID.setFont(new java.awt.Font("Schadow BT", 0, 14)); // NOI18N
        textJobID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textJobIDActionPerformed(evt);
            }
        });
        getContentPane().add(textJobID, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 111, 44));

        textTechID.setEditable(false);
        textTechID.setFont(new java.awt.Font("Schadow BT", 0, 14)); // NOI18N
        textTechID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textTechIDMouseClicked(evt);
            }
        });
        textTechID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textTechIDActionPerformed(evt);
            }
        });
        getContentPane().add(textTechID, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 111, 44));

        jLabel23.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        jLabel23.setText("TECH ID#:");
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 100, -1));

        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel10.setText("CLIENT'S PROFILE");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jLabel10)
                .addContainerGap(174, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 550, 50));

        jLabel24.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel24.setText("ID#:");
        getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 40, -1));

        jLabel25.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel25.setText("Home Address:");
        getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, -1, -1));

        textContactNum.setEditable(false);
        getContentPane().add(textContactNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 610, 206, 35));

        jLayeredPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLayeredPane2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel4.setText("SERVICE FEE");
        jLayeredPane2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, -1, -1));
        jLayeredPane2.add(textServiceFee, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 100, 42));

        jLabel5.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        jLabel5.setText(".00");
        jLayeredPane2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 30, 20));

        jLabel27.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        jLabel27.setText("PHP");
        jLayeredPane2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 40, 20));

        getContentPane().add(jLayeredPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 400, 280, 190));

        jLabel26.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel26.setText("Select a Technician:");
        getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, -1, -1));

        textClientID.setEditable(false);
        getContentPane().add(textClientID, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 490, 70, 30));

        jLabel28.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        jLabel28.setText("Last Name: ");
        getContentPane().add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 470, 150, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadJobFormToTable();
        loadTechToCombo();
        loadClientFormToTable();
        loadClientsToCombo();
        loadSolutionToTable();
        
        clearFields();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int row = tblJobPending.getSelectedRow();
        
        if (row == -1){
            JOptionPane.showMessageDialog(this, "Select a record to first!", "SYSTEM", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String techID = textTechID.getText();
        String stat = getSelectedJobStatus();
        String fee = textServiceFee.getText().trim();
        BigDecimal serviceFee = new BigDecimal(fee);
        int solutionID = (int) tblJobPending.getValueAt(row, 14);
        int qntyUsed = Integer.parseInt(textQuantity.getText());
        int jobID = (int)tblJobPending.getValueAt(row, 0);
        int solutionVol = (int) tblJobPending.getValueAt(row, 7);
        Object brand = cboSolution.getSelectedItem();
        String solutionBrand = brand.toString();
        
        //TECHSOLUTION CARRY
        try(Connection con = BBConnection.MSAccessConnection()){
//                PreparedStatement pst = con.prepareStatement(
//                        "UPDATE TechSolutionCarry SET solutionID = ? " +
//                                "WHERE techID=?")){
//                pst.setInt(1, solutionID);
//                pst.setString(2, techID);
//                pst.executeUpdate(); 
                
            
            //QUANTITY
            String sql1 = "UPDATE JobSolution SET qntyUsed = ? WHERE solutionID = ?";
            PreparedStatement pstJobSol = con.prepareStatement(sql1);
            if (stat.equalsIgnoreCase("CANCELLED")){
                PreparedStatement pstNull2 = con.prepareStatement(
                "UPDATE JobSolution SET qntyUsed = NULL WHERE solutionID = ?");
                pstNull2.setInt(1, solutionID);
                pstNull2.executeUpdate();
            } else {
                pstJobSol.setInt(1, qntyUsed);
                pstJobSol.setInt(2, solutionID);
                pstJobSol.executeUpdate(); 
            }
            
            // VOLUME
            String sql3 = "UPDATE Solutions SET solutionVol = ? WHERE solutionID = ?";
            PreparedStatement pst2 = con.prepareStatement(sql3);
            if (stat.equalsIgnoreCase("CANCELLED")){
                PreparedStatement pstNUll1 = con.prepareStatement(
                        "UPDATE Solutions SET solutionVol = NULL WHERE solutionID = ?");
            pstNUll1.setInt(1, solutionID);
            pstNUll1.executeUpdate();
            }else {
            pst2.setInt(1, solutionVol);
            pst2.setInt(2, solutionID);
            pst2.executeUpdate();
            }
            
            // SOLUTIONS
            String sql4 = "UPDATE Solutions SET solutionName = ? WHERE solutionID = ?";
            PreparedStatement pst3 = con.prepareStatement(sql4);
            pst3.setString(1, solutionBrand);
            pst3.setInt(2, solutionID);
            pst3.executeUpdate();
            
            String fee1 = textServiceFee.getText();
            BigDecimal serviceFeeNULL = BigDecimal.ZERO;
            try{
            // SERVICE FEE
            String sql5 = "UPDATE Job SET serviceFee = ? WHERE jobID = ?";
            PreparedStatement pst4 = con.prepareStatement(sql5);
            if (stat.equalsIgnoreCase("CANCELLED")){
            PreparedStatement pstNull4 = con.prepareStatement(
            "UPDATE Job SET serviceFee = NULL WHERE jobID = ?");
            if(fee1 == null){
            pstNull4.setBigDecimal(1, serviceFeeNULL);
            pstNull4.executeUpdate();
            }
            } else {
            pst4.setBigDecimal(1, serviceFee);
            pst4.setInt(2, jobID);
            pst4.executeUpdate(); 
            }
            
            }catch(NumberFormatException ex){
            System.out.println("(INVALID NUMBER) Service fee must be a number/Integer.");
            System.out.println(ex.getMessage());
        }
            
             // STATUS
            String sql2 = "UPDATE TechAssign SET techID = ?, status = ? WHERE jobID = ?";
            PreparedStatement pst1 = con.prepareStatement(sql2);
            if (stat.equalsIgnoreCase("CANCELLED")){
            PreparedStatement pstNull5 = con.prepareStatement(
            "UPDATE TechAssign SET status = NULL WHERE jobID = ?");
            pstNull5.setInt(1, jobID);
            pstNull5.executeUpdate(); 
            } else {
            pst1.setString(1, techID);
            pst1.setString(2, stat);
            pst1.setInt(3, jobID);
            pst1.executeUpdate(); 
            }
            
            con.close();
            loadClientsToCombo();
            loadJobPendingToTable();
            loadSolutionToTable();
            loadJobDoneToTable();
            loadJobFormToTable();
            loadSolutionToCombo();
            clearFields();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
        
        
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnPrintReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintReportActionPerformed
        List<Object[]> rows = new ArrayList<>();
        
        String sql = "SELECT jobDate, solutionName, solutionVol, qntyUsed, totalMl " +
                "FROM qryJobSolutions ORDER BY jobDate";
        
        try(Connection con = BBConnection.MSAccessConnection();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            while (rs.next()){
                rows.add(new Object[]{
                    rs.getDate("jobDate"),
                    rs.getString("solutionName"),
                    rs.getInt("solutionVol"),
                    rs.getInt("qntyUsed"),
                    rs.getInt("totalMl")
                });
            }
        } catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading report: " + e.getMessage(), "SOLUTION USAGE REPORT" ,JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (rows.isEmpty()){
            JOptionPane.showMessageDialog(this,
                "No reports to print.",
                "SOLUTION USAGE REPORT",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // BUILDING THE RECEIPT
        String solutionReceipt = buildReceiptText(rows);
        showReceiptDialog(solutionReceipt);
    }//GEN-LAST:event_btnPrintReportActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tblJob.getSelectedRow();
        
        if(row == -1){
            JOptionPane.showMessageDialog(this, "Please Select a record to delete!", "TECHNICIAN RECORD", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?","Confirm Delete", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION){
            return;
        }
        
        try{
            Connection con = BBConnection.MSAccessConnection();
            con.setAutoCommit(false);
              
             int jobID = (int) tblJob.getValueAt(row, 0);
             
             // DELETE JOB
             String fee1 = textServiceFee.getText();
             BigDecimal serviceFeeNULL = BigDecimal.ZERO;
             String sql1 = "UPDATE Job SET serviceFee = ? WHERE jobID = ?";
             
             PreparedStatement pst1 = con.prepareStatement(sql1);
             pst1.setBigDecimal(1, serviceFeeNULL);
             pst1.setInt(2, jobID);
             pst1.executeUpdate();
             
             
             //DELETE JOBSOLUTION
             String sql2 = "DELETE FROM JobSolution WHERE jobID = ?";
             PreparedStatement pst2 = con.prepareStatement(sql2);
             pst2.setInt(1, jobID);
             pst2.executeUpdate();
             
             //DELETE SOLUTION 
             int sol = (int) tblJob.getValueAt(row, 6);
             String sql3 = "UPDATE Solutions SET solutionVol = NULL WHERE solutionID = ?";
             PreparedStatement pst3 = con.prepareStatement(sql3);
             pst3.setInt(1, sol);
             pst3.executeUpdate();
             
             // DELETE TECH ASSIGN
             String sql4 = "DELETE FROM TechAssign WHERE jobID = ?";
             PreparedStatement pst4 = con.prepareStatement(sql4);
             pst4.setInt(1, jobID);
             pst4.executeUpdate();
             
             //DELETE TECHSOLUTIONCARRY
             String techID = (String) tblJob.getValueAt(row, 2);
             String sql5 = "DELETE FROM TechSolutionCarry WHERE techID = ?";
             PreparedStatement pst5 = con.prepareStatement(sql5);
             pst5.setString(1, techID);
             pst5.executeUpdate();
             
             con.commit();
             loadJobFormToTable();
             loadJobDoneToTable();
             loadJobPendingToTable();
             loadSolutionToCombo();
             loadSolutionToTable();
             
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddSolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSolutionActionPerformed
        Solution s = new Solution();
        s.setLocationRelativeTo(this);
        s.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAddSolutionActionPerformed

    private void btnAddQueriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddQueriesActionPerformed
        Connection con = BBConnection.MSAccessConnection();
        
        if(!isFormValid()){
            JOptionPane.showMessageDialog(this, "You are REQUIRED to fill all the necessary fields for Information Purposes", "BOOKING RECORDS", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try{
        con.setAutoCommit(false);
        // GETTING THE DATAS FROM THE USER
        String jobID = textJobID.getText();
        String techID = textTechID.getText();
        String qnty = textQuantity.getText();
        String status = getSelectedJobStatus();
        String SolVol = textVolume.getText();
       
        
        // SENDING DATAS INTO ACCESS
        // TECH ASSIGN
        String sqlTechAss = "INSERT INTO TechAssign (jobID, techID, status) VALUES (?, ?, ?)";
        PreparedStatement pstTechAss = con.prepareStatement(sqlTechAss);
        pstTechAss.setInt(1, Integer.parseInt(jobID));
        pstTechAss.setString(2, techID);
        pstTechAss.setString(3, status);
        pstTechAss.executeUpdate();
        pstTechAss.close();
        
        // JOB
        try{
        BigDecimal serviceFee = new BigDecimal(textServiceFee.getText()).setScale(2, RoundingMode.HALF_UP);
        
        String sqlJob = "UPDATE Job SET serviceFee = ? WHERE jobID = ?";
        PreparedStatement pstJob = con.prepareStatement(sqlJob);
        pstJob.setBigDecimal(1, serviceFee);
        pstJob.setInt(2, Integer.parseInt(jobID));
        pstJob.executeUpdate();
        pstJob.close();
        
         }catch(NumberFormatException ex){
            System.out.println("(INVALID NUMBER) Service fee must be a number/Integer.");
            System.out.println(ex.getMessage());
        }
         
        // JOB SOLUTION
        int solIndex = cboSolution.getSelectedIndex();
        if(solIndex < 0 || solIndex >= solutionIDList.size()){
            return;
        }
        int solutionID = solutionIDList.get(solIndex);
        
        String sqlJobSol = "INSERT INTO JobSolution (jobID, solutionID, qntyUsed) VALUES (?, ?, ?)";
        PreparedStatement pstJobSol = con.prepareStatement(sqlJobSol);
        pstJobSol.setInt(1, Integer.parseInt(jobID));
        pstJobSol.setInt(2, solutionID);
        pstJobSol.setInt(3, Integer.parseInt(qnty));
        pstJobSol.executeUpdate();
        pstJobSol.close();
        
        // CHECK 
        String sqlCheck = "SELECT COUNT(*) AS cnt FROM TechSolutionCarry " +
                "WHERE techID = ? AND solutionID = ?";
        PreparedStatement pstCheck = con.prepareStatement(sqlCheck);
        pstCheck.setString(1, techID);
        pstCheck.setInt(2, solutionID);
        ResultSet rs = pstCheck.executeQuery();
        rs.next();
        int cnt = rs.getInt("cnt");
        rs.close();
        pstCheck.close();
        
        if(cnt == 0){
        // TECH-SOLUTION_CARRY
        String sqlTechSolCar = "INSERT INTO TechSolutionCarry (techID, solutionID) VALUES (?, ?)";
        PreparedStatement pstTechSolCar = con.prepareStatement(sqlTechSolCar);
        pstTechSolCar.setString(1, techID);
        pstTechSolCar.setInt(2, solutionID);
        pstTechSolCar.executeUpdate();
        pstTechSolCar.close();
        }
        
        // SOLUTIONS
        String sqlSolution = "UPDATE Solutions SET solutionVol=? WHERE solutionID =?";
        PreparedStatement pstSol = con.prepareStatement(sqlSolution);
        pstSol.setInt(1, Integer.parseInt(SolVol));
        pstSol.setInt(2, solutionID);
        pstSol.executeUpdate();
        pstSol.close();
        
        con.commit();
        loadJobFormToTable();
        loadJobDoneToTable();
        loadJobPendingToTable();
        loadSolutionToTable();
        loadSolutionToCombo();
        JOptionPane.showMessageDialog(this, "Proceed to JOB HISTORY (PENDINGS) to see the following queries", "BOOKING RECORDS", JOptionPane.INFORMATION_MESSAGE);
        
        clearFields();
        
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_btnAddQueriesActionPerformed

    private void btnReturn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturn1ActionPerformed
        adminDashboard ad = new adminDashboard();
        ad.setLocationRelativeTo(this);
        ad.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnReturn1ActionPerformed

    private void textJobIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textJobIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textJobIDActionPerformed

    private void textTechIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textTechIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textTechIDActionPerformed

    private void cboClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboClientActionPerformed

    private void cboTechnicianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTechnicianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTechnicianActionPerformed

    private void cboTechnicianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTechnicianMouseClicked
        int row = cboTechnician.getSelectedIndex();
        if(row == -1) return;
        
        try(Connection con = BBConnection.MSAccessConnection()){
            String sql = "SELECT TechnicianDetails.techID FROM TechnicianDetails";
            PreparedStatement pst = con.prepareStatement(sql);

            
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                String techID = rs.getString("techID");
                textTechID.setText(String.valueOf(techID));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_cboTechnicianMouseClicked

    private void textTechIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textTechIDMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_textTechIDMouseClicked

    private void tblJobPendingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJobPendingMouseClicked
        tblJobPending.addMouseListener( new java.awt.event.MouseAdapter(){
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e){
            int row = tblJobPending.getSelectedRow();
            if(row == -1) return;
            
            // GETTING THE VALUES FROM THE TABLE
            String jobID = tblJobPending.getValueAt(row, 0).toString();
            String clientID = tblJobPending.getValueAt(row, 1).toString();
            String techID = tblJobPending.getValueAt(row, 2).toString();
            String solName = tblJobPending.getValueAt(row, 3).toString().trim();
            String status = tblJobPending.getValueAt(row, 4).toString().trim();
            String serviceFee = tblJobPending.getValueAt(row, 5).toString();
            String quantity = tblJobPending.getValueAt(row, 6).toString();
            String solVol = tblJobPending.getValueAt(row, 7).toString();
            String lastName = tblJobPending.getValueAt(row, 8).toString();
            String firstName = tblJobPending.getValueAt(row, 9).toString();
            String homeAdd = tblJobPending.getValueAt(row, 10).toString();
            String cityAdd = tblJobPending.getValueAt(row, 11).toString();
            String contactNum = tblJobPending.getValueAt(row, 12).toString();
            String clientConcerns = tblJobPending.getValueAt(row, 13).toString();
            String solutionID = tblJobPending.getValueAt(row, 14).toString();
            String techFirstName = tblJobPending.getValueAt(row, 15).toString();
            String techLastName = tblJobPending.getValueAt(row, 16).toString();
            
            
            // SETTING THE TEXT FIELDS
            textJobID.setText(jobID);
            textTechID.setText(techID);
            textServiceFee.setText(serviceFee);
            textClientID.setText(clientID);
            textQuantity.setText(quantity);
            textFirstName.setText(firstName);
            textLastName.setText(lastName);
            textVolume.setText(solVol);
            textHomeAdd.setText(homeAdd);
            textCityAdd.setText(cityAdd);
            textContactNum.setText(contactNum);
            textClientConcerns.setText(clientConcerns);
            
            
            
            // SETTING COMBO BOXES

            cboTechnician.setSelectedItem(techID);
            cboSolution.setSelectedItem(solName);
            
            String clientID2 = tblJobPending.getValueAt(row, 1).toString().trim();
            
            for(int i =0; i < cboClient.getItemCount(); i++){
                String item = cboClient.getItemAt(i).toString();
                
                if(item.startsWith(clientID2 + " ")){
                    cboClient.setSelectedIndex(i);
                    break;
                }
            }
            
            
            
            // STATUS RADIO BUTTONS
            if ("DONE".equalsIgnoreCase(status)){
                rbtnDone.setSelected(true);
            } else if ("PENDING".equalsIgnoreCase(status)){
                rbtnPending.setSelected(true);
            } else if ("CANCELLED".equalsIgnoreCase(status)){
                rbtnCancel.setSelected(true);
            }
        }
    });
    }//GEN-LAST:event_tblJobPendingMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(jobForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jobForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jobForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jobForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jobForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddQueries;
    private javax.swing.JButton btnAddSolution;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPrintReport;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReturn1;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.JComboBox<String> cboClient;
    private javax.swing.JComboBox<String> cboSolution;
    private javax.swing.JComboBox<String> cboTechnician;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JRadioButton rbtnCancel;
    private javax.swing.JRadioButton rbtnDone;
    private javax.swing.JRadioButton rbtnPending;
    private javax.swing.JTable tblClientReports;
    private javax.swing.JTable tblJob;
    private javax.swing.JTable tblJobDone;
    private javax.swing.JTable tblJobPending;
    private javax.swing.JTable tblSolution;
    private javax.swing.JTextField textCityAdd;
    private javax.swing.JTextField textClientConcerns;
    private javax.swing.JTextField textClientID;
    private javax.swing.JTextField textContactNum;
    private javax.swing.JTextField textFirstName;
    private javax.swing.JTextField textHomeAdd;
    private javax.swing.JTextField textJobID;
    private javax.swing.JTextField textLastName;
    private javax.swing.JTextField textQuantity;
    private javax.swing.JTextField textServiceFee;
    private javax.swing.JTextField textTechID;
    private javax.swing.JTextField textVolume;
    // End of variables declaration//GEN-END:variables
}
