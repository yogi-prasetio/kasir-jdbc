/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
/**
 *
 * @author Yogi Prasetio
 */
public class koneksi {
    Connection con;
    public koneksi() {
       String id, pass, driver, url;
       id = "root";
       pass = "";
       driver = "com.mysql.jdbc.Driver";
       url = "jdbc:mysql://localhost/db_kasir";
       try {
           Class.forName(driver).newInstance();
           con=DriverManager.getConnection(url, id, pass);           
       }
       catch (Exception e) {
           System.out.println(""+e.getLocalizedMessage());
                   
       }
   }
   public static void main(String[] args){
       koneksi k = new koneksi();
   }
}
