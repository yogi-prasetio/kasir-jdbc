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
public class FSupplier extends javax.swing.JFrame {
    /**
     * Creates new form FSupplier
     */
    Statement st;
    ResultSet rs;
    koneksi koneksi;
    
    //Variable IReport
    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;
    
    public FSupplier() {
        koneksi = new koneksi();
        initComponents();
        load_data();
        IDOtomatis();
        level();
    }

    private void load_data() {
        Object header[]={"ID SUPPLIER","KODE PEMBELIAN","NAMA SUPPLIER","NO HANDPHONE","ALAMAT"};
        DefaultTableModel data= new DefaultTableModel(null,header);
        TSupplier.setModel(data);
        String sql="SELECT * FROM tbl_supplier;";
        try {
            st=koneksi.con.createStatement();
            rs=st.executeQuery(sql);
            while(rs.next()) {
                String k1=rs.getString(1);
                String k2=rs.getString(2);
                String k3=rs.getString(3);
                String k4=rs.getString(4);
                String k5=rs.getString(5);
                String k[]={k1,k2,k3,k4,k5};
                data.addRow(k);
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Pembuatan ID Supplier Otomatis
    private void IDOtomatis() {
        try {
            st=koneksi.con.createStatement();
            String sql="SELECT * FROM tbl_supplier order by id_supplier desc";
            rs=st.executeQuery(sql);
            if(rs.next()) {
                String ID=rs.getString("id_supplier").substring(1);
                String NO = "" + (Integer.parseInt(ID) + 1);
                String Nol="";
                if(NO.length()==1) {
                    Nol="000";
                }
                else if(NO.length()==2) {
                    Nol="00";
                }
                IId_Supplier.setText("S" + Nol + NO);
                IId_Supplier.setEnabled(false);
            }
            else {
                IId_Supplier.setText("S0001");
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
            String query = "SELECT * FROM tbl_pembelian";
            rs = st.executeQuery(query);
            while (rs.next()) {
                CKd_Beli.addItem(rs.getString("kd_pembelian"));
            }
            rs.close();
        }
        catch (Exception e) {
        }
    }
        
    //Input Data ke Tabel tbl_supplier
    public void input_data() {
        try {
            //Query Input ke Tabel Supplier
            String sql="INSERT INTO tbl_supplier values('"+IId_Supplier.getText()
            +"','"+CKd_Beli.getSelectedItem()
            +"','"+INama.getText()
            +"','"+INope.getText()
            +"','"+IAlamat.getText()
            +"')";
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Data Barang Berhasil Dimasukan");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Cek Form Input
    private void Cek_Form(){        
        if (CKd_Beli.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (INama.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (INope.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (IAlamat.getText().equals("")){
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
        CKd_Beli.setSelectedIndex(0);
        INama.setText("");
        INope.setText("");
        IAlamat.setText("");
        IDOtomatis();
        BSimpan.setEnabled(true);
    }
    
    //Update
    private void Edit() {
        try {
            //Query Input ke Tabel Supplier
            String sql_edit="UPDATE tbl_supplier SET id_supplier='"+IId_Supplier.getText()
                    +"',kd_pembelian='"+CKd_Beli.getSelectedItem()
                    +"',nama_supplier='"+INama.getText()
                    +"',nope_supplier='"+INope.getText()
                    +"',alamat='"+IAlamat.getText()
                    +"' WHERE id_supplier='"+IId_Supplier.getText()+"'";
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
            String sql_delete="Delete FROM tbl_supplier WHERE id_supplier='"+IId_Supplier.getText()+"'";
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

        jScrollPane1 = new javax.swing.JScrollPane();
        TSupplier = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        IId_Supplier = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        CKd_Beli = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        INama = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        INope = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        BSimpan = new javax.swing.JButton();
        BEdit = new javax.swing.JButton();
        BHapus = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        IAlamat = new javax.swing.JTextArea();
        BKembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TSupplier.setModel(new javax.swing.table.DefaultTableModel(
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
        TSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TSupplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TSupplier);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel1.setText("FORM INPUT DATA");

        jLabel2.setText("ID SUPPLIER");

        jLabel3.setText("KD PEMBELIAN");

        CKd_Beli.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- PILIH --" }));
        CKd_Beli.setToolTipText("");

        jLabel4.setText("NAMA SUPPLIER");

        jLabel6.setText("NO HP");

        jLabel7.setText("ALAMAT");

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
        jLabel9.setText("DATA SUPPLIER APLIKASI KASIR");

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel10.setText("DATA SUPPLIER");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel11.setText("Copyright @ Kelompok 7 20th");

        IAlamat.setColumns(20);
        IAlamat.setRows(5);
        jScrollPane2.setViewportView(IAlamat);

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(INama, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(IId_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CKd_Beli, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(INope, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BSimpan)
                        .addGap(18, 18, 18)
                        .addComponent(BEdit)
                        .addGap(18, 18, 18)
                        .addComponent(BHapus)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BKembali)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(284, 284, 284)
                                    .addComponent(jLabel10))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30))
            .addGroup(layout.createSequentialGroup()
                .addGap(386, 386, 386)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(IId_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CKd_Beli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(19, 19, 19)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(INama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(INope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BSimpan)
                            .addComponent(BEdit)
                            .addComponent(BHapus))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BKembali)
                .addGap(7, 7, 7)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSimpanActionPerformed
        // TODO add your handling code here:
        Cek_Form();
    }//GEN-LAST:event_BSimpanActionPerformed

    private void TSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TSupplierMouseClicked
        // TODO add your handling code here:
        int bar=TSupplier.getSelectedRow();
        String a = TSupplier.getValueAt(bar, 0).toString();
        String b = TSupplier.getValueAt(bar, 1).toString();
        String c = TSupplier.getValueAt(bar, 2).toString();
        String d = TSupplier.getValueAt(bar, 3).toString();
        String e = TSupplier.getValueAt(bar, 4).toString();
        
        IId_Supplier.setText(a);
        CKd_Beli.setSelectedItem(b);
        INama.setText(c);
        INope.setText(d);
        IAlamat.setText(e);
        //set disable button
        BSimpan.setEnabled(false);
        //Aktifkane button Edit dan Hapus
        BEdit.setEnabled(true);
        BHapus.setEnabled(true);
    }//GEN-LAST:event_TSupplierMouseClicked

    private void BEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEditActionPerformed
        // TODO add your handling code here:
        int ans = JOptionPane.showConfirmDialog(null, "Anda yakin akan mengubah data?",
                "Edit Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.YES_OPTION){
            Edit();
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
            java.util.logging.Logger.getLogger(FSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FSupplier().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BEdit;
    private javax.swing.JButton BHapus;
    private javax.swing.JButton BKembali;
    private javax.swing.JButton BSimpan;
    private javax.swing.JComboBox CKd_Beli;
    private javax.swing.JTextArea IAlamat;
    private javax.swing.JTextField IId_Supplier;
    private javax.swing.JTextField INama;
    private javax.swing.JTextField INope;
    private javax.swing.JTable TSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
