
import com.mysql.jdbc.Connection;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.SimpleDoc;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Bhaskar
 */
public class LogicManager {

    String getBirthday() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/iem", "root", "root");
            Statement stmt = con.createStatement();
            SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);
            String query = "SELECT * FROM contact ";
            System.out.println("strDtae :" + strDate);
            ResultSet rs = stmt.executeQuery(query);
            String bdaypeople = "";
            int x;

            while (rs.next()) {

                String bday = rs.getString("DOB");

                bday = bday.substring(5);

                if (bday.equals(strDate)) {

                    bdaypeople = bdaypeople + rs.getString("name") + ",";
                    System.out.println(rs.getString("name"));
                }
            }
            if (!bdaypeople.equals("")) {
                return ("Wish " + bdaypeople.substring(0, bdaypeople.length() - 1) + " a happy birthday!!");
            }
            stmt.close();
            con.close();

        } catch (Exception e) {
        }
        return ("");
    }

    int insert(String nm, String mo, String adr, String db, String em, JFrame obj) {
        if (nm.isEmpty() || mo.isEmpty() || adr.isEmpty() || db.isEmpty() || em.isEmpty()) {
            JOptionPane.showMessageDialog(obj, "All fields necessary!");
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/iem", "root", "root");

                Statement stmt = con.createStatement();
                String query = "Insert into contact values('" + nm + "','" + mo + "','" + adr + "','" + db + "','" + em + "');";
                int r = stmt.executeUpdate(query);
                System.out.println(r);

                JOptionPane.showMessageDialog(obj, "Record has been inserted");
                stmt.close();
                con.close();
                
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(obj, "Error in connectivity");
                return -1;
            }
        }
        return +1;
    }

    void delete(ShowAll myShowFrame) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/iem", "root", "root");
            Statement stmt = con.createStatement();
            int bp = JOptionPane.showConfirmDialog(myShowFrame, "Do you want to delete the record ?");
            if (bp == JOptionPane.YES_OPTION) {
                String num = myShowFrame.getTable().getModel().getValueAt(myShowFrame.getTable().getSelectedRow(), 1).toString();
                String k = myShowFrame.getTable().getModel().getValueAt(myShowFrame.getTable().getSelectedRow(), 0).toString();
                String query = "Delete from contact where number = '" + num + "' and name = '" + k + "';";
                stmt.executeUpdate(query);
                JOptionPane.showMessageDialog(myShowFrame, "Record has been deleted");
                myShowFrame.getaModel().removeRow(myShowFrame.getTable().getSelectedRow());
            }
            if (bp == JOptionPane.CANCEL_OPTION) {
                myShowFrame.dispose();
                myShowFrame.setVisible(true);
            }
            if (bp == JOptionPane.NO_OPTION) {
                myShowFrame.dispose();
                myShowFrame.setVisible(true);
            }
            stmt.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(myShowFrame, "Error in connectivity");
        }
    }

    void showAll(ShowAll myFrame) {
        try {
            try {
                int ml = myFrame.getaModel().getRowCount();
                for (int mh = 0; mh <= ml; ++mh) {
                    myFrame.getaModel().removeRow(0);
                }
            } catch (Exception e) {
            }
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/iem", "root", "root");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM contact ;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                myFrame.getaModel().addRow(new Object[]{rs.getString("name"), rs.getString("number"), rs.getString("address"), rs.getString("DOB"), rs.getString("email")});
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(myFrame, "Error in connectivity");
        }
    }
    void checkDigits(JTextField mobno1,Update obj){
        char ch = '9';
        if(mobno1.getText().length() >= 1)
            ch=mobno1.getText().charAt(mobno1.getText().length()-1);
        if (mobno1.getText().length() >= 11||!Character.isDigit(ch)) {
              JOptionPane.showMessageDialog(obj, "Enter 10 digit mobile number");
               mobno1.setText(mobno1.getText().substring(0, mobno1.getText().length() - 1));
        } 
    }
    void checkDigits(JTextField mobno1,ContactList obj){
        char ch = '9';
        if(mobno1.getText().length() >= 1)
            ch=mobno1.getText().charAt(mobno1.getText().length()-1);
        if (mobno1.getText().length() >= 11||!Character.isDigit(ch)) {
              JOptionPane.showMessageDialog(obj, "Enter 10 digit mobile number");
               mobno1.setText(mobno1.getText().substring(0, mobno1.getText().length() - 1));
        } 
    }
    
    void generateFile() throws Exception{
        try{
            FileOutputStream fos = new FileOutputStream("ContactsAppResults.txt");
            DataOutputStream bos = new DataOutputStream(fos);
            bos.write(("Contacts List\r\n").getBytes());
            bos.write(("Made By : Bhaskar Tejaswi\r\n").getBytes());
            bos.write("==================================================\r\n".getBytes());
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/iem", "root", "root");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM contact ;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                bos.write((("NUMBER : "+rs.getString("number")+"\r\n").getBytes()));
                bos.write((("NAME : "+rs.getString("name")+"\r\n").getBytes()));
                bos.write((("ADDRESS : "+rs.getString("address")+"\r\n").getBytes()));
                bos.write((("DOB : "+rs.getString("DOB")+"\r\n").getBytes()));
                bos.write((("EMAIL : "+rs.getString("email")+"\r\n").getBytes()));
                bos.write((("ADDRESS : "+rs.getString("address")+"\r\n").getBytes()));
                bos.write("==================================================\r\n".getBytes()); //to add a new line we need to give \r\n in windows
            }   bos.write("\r\n".getBytes());
             rs.close();
            stmt.close();
            con.close();}                  
         catch (Exception e) {
        }
    }
    int search(Update myUpdateFrame, String mo) {
        int i = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection con = (java.sql.Connection) DriverManager.getConnection("jdbc:mysql://localhost/iem", "root", "root");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM contact where number = '" + mo + "';";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                myUpdateFrame.getEnterMobNoLabel().setVisible(false);
                myUpdateFrame.getMobnoSearched().setVisible(false);
                myUpdateFrame.getSearchButton().setVisible(false);
                myUpdateFrame.getaName().setText(rs.getString("name"));
                myUpdateFrame.getMobno().setText(rs.getString("number"));
                myUpdateFrame.getAddress().setText(rs.getString("address"));
                String dob = rs.getString("DOB");
                myUpdateFrame.getDob().setText(dob);
                myUpdateFrame.getEmail().setText(rs.getString("email"));
     /*  displaying the date in combobox is left to be done           
                  int year = Calendar.getInstance().get(Calendar.YEAR);
                for (int j = 1960; j <= year; j++) {
                myUpdateFrame.getyyyy().addItem(j + "");
                System.out.println(j);
                }
              
                String num = rs.getString("number");
                String[] arr = num.split("-");
                
                int yyyy = Integer.parseInt(arr[2]);
                int mm   = Integer.parseInt(arr[1]);
                int dd = Integer.parseInt(arr[0]);
                System.out.println(dd+" "+mm+" "+yyyy);
                
                int y,m,d;
              
                  y = ((DefaultComboBoxModel)myUpdateFrame.getyyyy().getModel()).getIndexOf(arr[2]);  
                  m = ((DefaultComboBoxModel)myUpdateFrame.getyyyy().getModel()).getIndexOf(arr[1]);  
                  d = ((DefaultComboBoxModel)myUpdateFrame.getyyyy().getModel()).getIndexOf(arr[0]);  
                   
              myUpdateFrame.getyyyy().setSelectedIndex(y);
              myUpdateFrame.getmm().setSelectedIndex(m);
              myUpdateFrame.getdd().setSelectedIndex(d);
              
  
   
                */
                
                
                i++;
            }
            if (i == 0) {
                JOptionPane.showMessageDialog(myUpdateFrame, "No Result Found");
                return -1;
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            if (i != 0) {
                JOptionPane.showMessageDialog(myUpdateFrame, "Error in connectivity");
            }
        }
        return +1;
    }
}