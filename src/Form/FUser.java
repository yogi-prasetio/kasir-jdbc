/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Yogi Prasetio
 */
public class FUser extends javax.swing.JFrame {
    /**
     * Creates new form FUser
     */
    Statement st;
    ResultSet rs;
    koneksi koneksi;
    
    //Variable IReport
    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;
    
    public FUser() {
        koneksi = new koneksi();
        initComponents();
        load_data();
        IDOtomatis();
        level();
    }

    private void load_data() {
        Object header[]={"ID USER","LEVEL","NAMA USER","JENIS KELAMIN","NO HANDPHONE","USERNAME","PASSWORD"};
        DefaultTableModel data= new DefaultTableModel(null,header);
        TUser.setModel(data);
        String sql="SELECT tbl_user.ID_USER, tbl_level.LEVEL,tbl_user.NAMA_USER,tbl_user.JK, tbl_user.NOPE, tbl_user.USERNAME,"
        + "tbl_user.PASSWORD FROM tbl_user INNER JOIN tbl_level ON tbl_user.ID_LEVEL=tbl_level.ID_LEVEL;";
        try {
            st=koneksi.con.createStatement();
            rs=st.executeQuery(sql);
            while(rs.next()) {
                String k1=rs.getString(1);
                String k2=rs.getString(2);
                String k3=rs.getString(3);
                String k4=rs.getString(4);
                String k5=rs.getString(5);
                String k6=rs.getString(6);
                String k7=rs.getString(7);
                String k[]={k1,k2,k3,k4,k5,k6,k7};
                data.addRow(k);
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Pembuatan ID User Otomatis
    private void IDOtomatis() {
        try {
            st=koneksi.con.createStatement();
            String sql="SELECT * FROM tbl_user order by ID_USER desc";
            rs=st.executeQuery(sql);
            if(rs.next()) {
                String ID=rs.getString("ID_USER").substring(4);
                String NO = "" + (Integer.parseInt(ID) + 1);
                String Nol="";
                if(NO.length()==1) {
                    Nol="000";
                }
                else if(NO.length()==2) {
                    Nol="00";
                }
                IIdUser.setText("USER" + Nol + NO);
                IIdUser.setEnabled(false);
            }
            else {
                IIdUser.setText("USER0001");
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //load Data Level Daroi Tabel Level ke ComboBox Level
    public void level() {
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_level";
            rs = st.executeQuery(query);
            while (rs.next()) {
                CLevel.addItem(rs.getString("LEVEL"));
            }
            rs.close();
        }
        catch (Exception e) {
        }
    }
        
    //Input Data ke Tabel tbl_user
    public void input_data() {
        try {
            //Mengambil Data Jenis Kelamin
            String jk="";
            if(JkL.isSelected())
                {jk=JkL.getText();}
            else
                {jk=JkP.getText();}
            //Mengambil ComboBox
            st=koneksi.con.createStatement();
            String id="";
            String query = "SELECT ID_LEVEL FROM tbl_level WHERE LEVEL='"+CLevel.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            if(rs.next()) {
                id=rs.getString("ID_LEVEL");
            }
            //Query Input ke Tabel USer
            String sql="INSERT INTO tbl_user values('"+IIdUser.getText()
            +"','"+id
            +"','"+INama.getText()
            +"','"+jk
            +"','"+INope.getText()
            +"','"+IUsername.getText()
            +"','"+IPassword.getText()
            +"')";
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Data User Berhasil Dimasukan");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Cek Form Input
    private void Cek_Form(){        
        if (CLevel.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (INama.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (Gjk.isSelected(null)){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (INope.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (IUsername.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (IPassword.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else {
            int ans = JOptionPane.showConfirmDialog(null, "Anda yakin akan menyimpan?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ans == JOptionPane.YES_OPTION){
                input_data();        
                IDOtomatis();
                Empty();
            }                
            load_data();
        }
    }
    
    //Kosongkan Form
    private void Empty(){
        CLevel.setSelectedIndex(0);
        INama.setText("");
        Gjk.clearSelection();
        JkP.setSelected(false);
        JkL.setSelected(false);
        INope.setText("");
        IUsername.setText("");
        IPassword.setText("");
    }
    
    //Update
    private void Edit() {
        try {
        //Mengambil Data Jenis Kelamin
            String jk="";
            if(JkL.isSelected())
                {jk=JkL.getText();}
            else
                {jk=JkP.getText();}
            //Mengambil ComboBox
            st=koneksi.con.createStatement();
            String id="";
            String query = "SELECT ID_LEVEL FROM tbl_level WHERE LEVEL='"+CLevel.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            if(rs.next()) {
                id=rs.getString("ID_LEVEL");
            }
            //Query Input ke Tabel USer
            String sql_edit="UPDATE tbl_user SET id_level='"+id
                    +"',nama_user='"+INama.getText()
                    +"',jk='"+jk
                    +"',nope='"+INope.getText()
                    +"',username='"+IUsername.getText()
                    +"',password='"+IPassword.getText()
                    +"' WHERE id_user='"+IIdUser.getText()+"'";
            st.executeUpdate(sql_edit);
            JOptionPane.showMessageDialog(null, "Data berhasil Di Update");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Delete
    private void Hapus() {
        try {
            st = koneksi.con.createStatement();
            String sql_delete="Delete FROM tbl_user WHERE id_user='"+IIdUser.getText()+"'";
            st.executeUpdate(sql_delete);
            JOptionPane.showMessageDialog(null, "Data berhasil Di Hapus");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        Gjk = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        TUser = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        IIdUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        CLevel = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        INama = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        JkL = new javax.swing.JRadioButton();
        JkP = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        INope = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        IUsername = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        IPassword = new javax.swing.JPasswordField();
        BSimpan = new javax.swing.JButton();
        BEdit = new javax.swing.JButton();
        BHapus = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        BKembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TUser);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel1.setText("FORM INPUT DATA");

        jLabel2.setText("ID USER");

        jLabel3.setText("LEVEL");

        CLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Pilih Level --" }));
        CLevel.setToolTipText("");

        jLabel4.setText("NAMA USER");

        jLabel5.setText("JENIS KELAMIN");

        Gjk.add(JkL);
        JkL.setText("L");

        Gjk.add(JkP);
        JkP.setText("P");

        jLabel6.setText("NO HP");

        jLabel7.setText("USERNAME");

        jLabel8.setText("PASSWORD");

        BSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Save.png"))); // NOI18N
        BSimpan.setText("SIMPAN");
        BSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSimpanActionPerformed(evt);
            }
        });

        BEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Update.png"))); // NOI18N
        BEdit.setText("EDIT");
        BEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEditActionPerformed(evt);
            }
        });

        BHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        BHapus.setText("HAPUS");
        BHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BHapusActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel9.setText("DATA USER APLIKASI KASIR");

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel10.setText("DATA USER");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel11.setText("Copyright @ Kelompok 7 20th");

        BKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        BKembali.setText("  KEMBALI");
        BKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2))
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(INama, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(JkP)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(IIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(33, 33, 33)))
                                    .addComponent(CLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(IPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(IUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(INope, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JkL)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(BSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BHapus))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(jLabel1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(481, 481, 481))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BKembali)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23))))))
            .addGroup(layout.createSequentialGroup()
                .addGap(399, 399, 399)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(IIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(19, 19, 19)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(INama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JkP)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(JkL)))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(INope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(IUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(IPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BSimpan)
                            .addComponent(BEdit)
                            .addComponent(BHapus)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BKembali)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSimpanActionPerformed
        // TODO add your handling code here:
        Cek_Form();
    }//GEN-LAST:event_BSimpanActionPerformed

    private void TUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TUserMouseClicked
        // TODO add your handling code here:
        int bar=TUser.getSelectedRow();
        String a = TUser.getValueAt(bar, 0).toString();
        String b = TUser.getValueAt(bar, 1).toString();
        String c = TUser.getValueAt(bar, 2).toString();
        String d = TUser.getValueAt(bar, 3).toString();
        String e = TUser.getValueAt(bar, 4).toString();
        String f = TUser.getValueAt(bar, 5).toString();
        String g = TUser.getValueAt(bar, 6).toString();
        
        IIdUser.setText(a);
        CLevel.setSelectedItem(b);
        INama.setText(c);
        INope.setText(e);
        IUsername.setText(f);
        IPassword.setText(g);
        if("L".equals(d)) {
            JkL.setSelected(true);
        }
        else {
            JkP.setSelected(true);
        }
        //set disable button
        BSimpan.setEnabled(false);
        //Aktifkane button Edit dan Hapus
        BEdit.setEnabled(true);
        BHapus.setEnabled(true);
    }//GEN-LAST:event_TUserMouseClicked

    private void BEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEditActionPerformed
        // TODO add your handling code here:
        int ans = JOptionPane.showConfirmDialog(null, "Anda yakin akan mengubah data?",
                "Edit Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.YES_OPTION){
            Edit();
            BSimpan.setEnabled(true);
            IDOtomatis();
            Empty();
        }                
        load_data();        
    }//GEN-LAST:event_BEditActionPerformed

    private void BHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BHapusActionPerformed
        // TODO add your handling code here:
        int ans = JOptionPane.showConfirmDialog(null, "Anda yakin akan menghapus data?",
                "Hapus Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.YES_OPTION){
            Hapus();
            BSimpan.setEnabled(true);
            IDOtomatis();
            Empty();
        }                
        load_data();        
    }//GEN-LAST:event_BHapusActionPerformed

    private void BKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BKembaliActionPerformed
        // TODO add your handling code here:
        new FMenu().show();
        this.dispose();
    }//GEN-LAST:event_BKembaliActionPerformed

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
            java.util.logging.Logger.getLogger(FUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BEdit;
    private javax.swing.JButton BHapus;
    private javax.swing.JButton BKembali;
    private javax.swing.JButton BSimpan;
    private javax.swing.JComboBox CLevel;
    private javax.swing.ButtonGroup Gjk;
    private javax.swing.JTextField IIdUser;
    private javax.swing.JTextField INama;
    private javax.swing.JTextField INope;
    private javax.swing.JPasswordField IPassword;
    private javax.swing.JTextField IUsername;
    private javax.swing.JRadioButton JkL;
    private javax.swing.JRadioButton JkP;
    private javax.swing.JTable TUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
