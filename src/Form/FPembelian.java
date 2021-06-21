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
public class FPembelian extends javax.swing.JFrame {
    /**
     * Creates new form FPembelian
     */
    Statement st;
    ResultSet rs;
    koneksi koneksi;
    
    //Variable IReport
    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;
    
    public FPembelian() {
        koneksi = new koneksi();
        initComponents();
        load_data();
        IDOtomatis();
        barang();
        user();
    }

    private void load_data() {
        Object header[]={"KODE PEMBELIAN","NAMA BARANG","NAMA USER","TGL PEMBELIAN","JUMLAH BELI","TOTAL BELI"};
        DefaultTableModel data= new DefaultTableModel(null,header);
        TPembelian.setModel(data);
        String sql="SELECT kd_pembelian,tbl_barang.nama_barang,tbl_user.NAMA_USER, tgl_pembelian, jmlh_beli,total_beli "
                + "FROM tbl_pembelian INNER JOIN tbl_barang ON tbl_pembelian.kd_barang=tbl_barang.kd_barang "
                + "INNER JOIN tbl_user ON tbl_pembelian.id_user=tbl_user.ID_USER;";
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
                String k[]={k1,k2,k3,k4,k5,k6};
                data.addRow(k);
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Pembuatan ID Pembelian Otomatis
    private void IDOtomatis() {
        try {
            st=koneksi.con.createStatement();
            String sql="SELECT * FROM tbl_pembelian order by kd_pembelian desc";
            rs=st.executeQuery(sql);
            if(rs.next()) {
                String ID=rs.getString("kd_pembelian").substring(2);
                String NO = "" + (Integer.parseInt(ID) + 1);
                String Nol="";
                if(NO.length()==1) {
                    Nol="000";
                }
                else if(NO.length()==2) {
                    Nol="00";
                }
                IKd_Beli.setText("BL"+Nol + NO);
                IKd_Beli.setEnabled(false);
            }
            else {
                IKd_Beli.setText("BL0001");
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //load Data Barang
    public void barang() {
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_barang";
            rs = st.executeQuery(query);
            while (rs.next()) {
                CBarang.addItem(rs.getString("nama_barang"));
            }
            rs.close();
        }
        catch (Exception e) {
        }
    }
    //load Data User
    public void user() {
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_user";
            rs = st.executeQuery(query);
            while (rs.next()) {
                CUser.addItem(rs.getString("nama_user"));
            }
            rs.close();
        }
        catch (Exception e) {
        }
    }
        
    //Input Data ke Tabel tbl_Pembelian
    public void input_data() {
        try {
            //Mengambil ComboBox Barang
            st=koneksi.con.createStatement();
            String kd="";
            String query = "SELECT kd_barang FROM tbl_barang WHERE nama_barang='"+CBarang.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            if(rs.next()) {
                kd=rs.getString("kd_barang");
            }
            //Mengambil ComboBox User
            st=koneksi.con.createStatement();
            String id="";
            String q = "SELECT ID_USER FROM tbl_user WHERE NAMA_USER='"+CUser.getSelectedItem()+"'";
            rs = st.executeQuery(q);
            if(rs.next()) {
                id=rs.getString("ID_USER");
            }
            //Query Input ke Tabel USer
            String sql="INSERT INTO tbl_pembelian values('"+IKd_Beli.getText()
            +"','"+kd
            +"','"+id
            +"','"+ITanggal.getText()
            +"','"+IJumlah.getText()
            +"','"+ITotal.getText()
            +"')";
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Data Pembelian Berhasil Dimasukan");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Cek Form Input
    private void Cek_Form(){        
        if (CBarang.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (CUser.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (ITanggal.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (IJumlah.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data Harus Diisi!");
        } else if (ITotal.getText().equals("")){
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
        IDOtomatis();
        CBarang.setSelectedIndex(0);
        CUser.setSelectedIndex(0);     
        ITanggal.setText("");
        IJumlah.setText("");
        ITotal.setText("");
    }
    
    //Update
    private void Edit() {
        try {
        //Mengambil ComboBox Barang
            st=koneksi.con.createStatement();
            String kd="";
            String query = "SELECT kd_barang FROM tbl_barang WHERE nama_barang='"+CBarang.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            if(rs.next()) {
                kd=rs.getString("kd_barang");
            }
            //Mengambil ComboBox User
            st=koneksi.con.createStatement();
            String id="";
            String q = "SELECT ID_USER FROM tbl_user WHERE NAMA_USER='"+CUser.getSelectedItem()+"'";
            rs = st.executeQuery(q);
            if(rs.next()) {
                id=rs.getString("ID_USER");
            }
            //Query Input ke Tabel USer
            String sql_edit="UPDATE tbl_pembelian SET kd_pembelian='"+IKd_Beli.getText()
                    +"',kd_barang='"+kd
                    +"',id_user='"+id
                    +"',tgl_pembelian='"+ITanggal.getText()
                    +"',jmlh_beli='"+IJumlah.getText()
                    +"',total_beli='"+ITotal.getText()
                    +"' WHERE kd_pembelian='"+IKd_Beli.getText()+"'";
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
            String sql_delete="Delete FROM tbl_pembelian WHERE kd_pembelian='"+IKd_Beli.getText()+"'";
            st.executeUpdate(sql_delete);
            JOptionPane.showMessageDialog(null, "Data berhasil Di Hapus");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Get Harga
    private void Harga(){
        String harga;
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_barang WHERE nama_barang='"
                    +CBarang.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                harga=rs.getString("harga_beli");
            }
            rs.close();
        }
        catch (Exception e) {
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
        TPembelian = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        IKd_Beli = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        CBarang = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ITanggal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        IJumlah = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        BSimpan = new javax.swing.JButton();
        BEdit = new javax.swing.JButton();
        BHapus = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        ITotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        CUser = new javax.swing.JComboBox();
        BKembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TPembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        TPembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TPembelianMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TPembelian);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel1.setText("FORM INPUT DATA");

        jLabel2.setText("KODE PEMBELIAN");

        jLabel3.setText("NAMA BARANG");

        CBarang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- PILIH BARANG --" }));
        CBarang.setToolTipText("");

        jLabel4.setText("NAMA USER");

        jLabel6.setText("TGL PEMBELIAN");

        jLabel7.setText("JUMLAH BELI");

        IJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IJumlahActionPerformed(evt);
            }
        });
        IJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                IJumlahKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("TOTAL BELI");

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
        jLabel9.setText("DATA PEMBELIAN APLIKASI KASIR");

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel10.setText("DATA PEMBELIAN");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel11.setText("Copyright @ Kelompok 7 20th");

        ITotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ITotalActionPerformed(evt);
            }
        });

        jLabel5.setText("---------------------------------------------------------------------------------");

        CUser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- PILIH USER --" }));
        CUser.setToolTipText("");

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BSimpan)
                                .addGap(35, 35, 35)
                                .addComponent(BEdit)
                                .addGap(30, 30, 30)
                                .addComponent(BHapus))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(IKd_Beli, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(IJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ITanggal)
                                    .addComponent(ITotal)
                                    .addComponent(CBarang, 0, 159, Short.MAX_VALUE)
                                    .addComponent(CUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(284, 284, 284)
                                .addComponent(jLabel10))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(56, 56, 56)
                                    .addComponent(BKembali)))))
                    .addComponent(jLabel9))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(IKd_Beli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(19, 19, 19)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(CUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ITanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(IJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(ITotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BSimpan)
                            .addComponent(BEdit)
                            .addComponent(BHapus)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel11)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSimpanActionPerformed
        // TODO add your handling code here:
        Cek_Form();
    }//GEN-LAST:event_BSimpanActionPerformed

    private void TPembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TPembelianMouseClicked
        // TODO add your handling code here:
        int bar=TPembelian.getSelectedRow();
        String a = TPembelian.getValueAt(bar, 0).toString();
        String b = TPembelian.getValueAt(bar, 1).toString();
        String c = TPembelian.getValueAt(bar, 2).toString();
        String d = TPembelian.getValueAt(bar, 3).toString();
        String e = TPembelian.getValueAt(bar, 4).toString();
        String f = TPembelian.getValueAt(bar, 5).toString();
        
        IKd_Beli.setText(a);
        CBarang.setSelectedItem(b);
        CUser.setSelectedItem(c);
        ITanggal.setText(d);
        IJumlah.setText(e);
        ITotal.setText(f);
        //set disable button
        BSimpan.setEnabled(false);
        //Aktifkane button Edit dan Hapus
        BEdit.setEnabled(true);
        BHapus.setEnabled(true);
    }//GEN-LAST:event_TPembelianMouseClicked

    private void BEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEditActionPerformed
        // TODO add your handling code here:
        int ans = JOptionPane.showConfirmDialog(null, "Anda yakin akan mengubah data?",
                "Edit Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.YES_OPTION){
            Edit();
            Empty();
            BSimpan.setEnabled(true);                        
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
            BSimpan.setEnabled(true);            
        }                
        load_data();        
    }//GEN-LAST:event_BHapusActionPerformed

    private void BKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BKembaliActionPerformed
        // TODO add your handling code here:
        new FMenu().show();
        this.dispose();
    }//GEN-LAST:event_BKembaliActionPerformed

    private void IJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IJumlahKeyReleased
        // TODO add your handling code here:
        int hrg, jml, total, harga = 0;
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_barang WHERE nama_barang='"
                    +CBarang.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                harga=rs.getInt("harga_beli");
            }
            rs.close();
        }
        catch (Exception e) { }        
        hrg=Integer.valueOf(harga);
        jml=Integer.valueOf(IJumlah.getText());
        total=hrg*jml;
        ITotal.setText(""+total);    
    }//GEN-LAST:event_IJumlahKeyReleased

    private void ITotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ITotalActionPerformed
        // TODO add your handling code here:
        int hrg, jml, total, harga = 0;
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_barang WHERE nama_barang='"
                    +CBarang.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                harga=rs.getInt("harga_beli");
            }
            rs.close();
        }
        catch (Exception e) { }        
        hrg=Integer.valueOf(harga);
        jml=Integer.valueOf(IJumlah.getText());
        total=hrg*jml;
        ITotal.setText(""+total);    
    }//GEN-LAST:event_ITotalActionPerformed

    private void IJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IJumlahActionPerformed
        // TODO add your handling code here:
        int hrg, jml, total, harga = 0;
        try {
            st = koneksi.con.createStatement();
            String query = "SELECT * FROM tbl_barang WHERE nama_barang='"
                    +CBarang.getSelectedItem()+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                harga=rs.getInt("harga_beli");
            }
            rs.close();
        }
        catch (Exception e) { }        
        hrg=Integer.valueOf(harga);
        jml=Integer.valueOf(IJumlah.getText());
        total=hrg*jml;
        ITotal.setText(""+total);    
        
    }//GEN-LAST:event_IJumlahActionPerformed

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
            java.util.logging.Logger.getLogger(FPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FPembelian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BEdit;
    private javax.swing.JButton BHapus;
    private javax.swing.JButton BKembali;
    private javax.swing.JButton BSimpan;
    private javax.swing.JComboBox CBarang;
    private javax.swing.JComboBox CUser;
    private javax.swing.ButtonGroup Gjk;
    private javax.swing.JTextField IJumlah;
    private javax.swing.JTextField IKd_Beli;
    private javax.swing.JTextField ITanggal;
    private javax.swing.JTextField ITotal;
    private javax.swing.JTable TPembelian;
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
