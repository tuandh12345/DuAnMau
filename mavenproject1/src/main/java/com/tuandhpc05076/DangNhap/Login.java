/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tuandhpc05076.DangNhap;

import static com.tuandhpc05076.Form.NhanVien.list;
import com.tuandhpc05076.MaHoa.MaHoa;
import com.tuandhpc05076.Object.O_DangNhap;
import com.tuandhpc05076.Object.O_NhanVien;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
//Người thiết kế Đặng Hoàng Tuấn

//Đang học tại lớp IT18201

//Đang học tại lớp IT18201 tôi muốn học lớp này

<<<<<<< HEAD
=======
/**
 *
 *tôi tên là Đặng Hoàng Tuấn
 */
>>>>>>> parent of 6cb45c6 (Cập nhật lại lớp)
public class Login extends PanelCustom {

    public static ArrayList<O_DangNhap> list1 = new ArrayList<>();
    private EventLogin event;
    O_DangNhap dn = new O_DangNhap();

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setAlpha(1);

    }

    public void setEventLogin(EventLogin event) {
        this.event = event;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDangNhap = new com.tuandhpc05076.Swing.Button();
        txtPass = new com.tuandhpc05076.DangNhap.Password();
        txtUserName = new com.tuandhpc05076.Swing.TextField1();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(85, 193, 217));

        btnDangNhap.setText("Đăng nhập");
        btnDangNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDangNhapMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDangNhapMouseExited(evt);
            }
        });
        btnDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangNhapActionPerformed(evt);
            }
        });

        txtPass.setHint("Nhập mật khẩu");

        txtUserName.setHint("Nhập mã");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đăng nhập");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(btnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    public boolean kiem() {
        if (txtUserName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập mã");
            return false;
        }
        if (new String(txtPass.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu của bạn đã để trống");
            return false;
        }
        return true;
    }
    private void btnDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangNhapActionPerformed
        if (kiem() == false) {
            return;
        }
        list1.clear();
        MaHoa ma = new MaHoa();
        String pass = ma.toSHA(new String(txtPass.getPassword()));
        dn.getThongtin(txtUserName, new JPasswordField(pass));
        dn.getAlllist();
        int chon = 1;
        System.out.println(list1.size());
        if (list1.size() > 0) {
            JOptionPane.showMessageDialog(this, "Login succesfully!");
            com.tuandhpc05076.Main.Main m = new com.tuandhpc05076.Main.Main();
            m.setVisible(true);
            txtUserName.setText("");
            txtPass.setText("");
            chon = 2;

        }

        if (chon == 1) {
            JOptionPane.showMessageDialog(this, "Login Falled!");

        }

    }//GEN-LAST:event_btnDangNhapActionPerformed

    private void btnDangNhapMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDangNhapMouseExited
        btnDangNhap.setBackground(new Color(255, 255, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnDangNhapMouseExited

    private void btnDangNhapMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDangNhapMouseEntered
        // TODO add your handling code here:
        btnDangNhap.setBackground(Color.pink);
    }//GEN-LAST:event_btnDangNhapMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.tuandhpc05076.Swing.Button btnDangNhap;
    private javax.swing.JLabel jLabel1;
    private com.tuandhpc05076.DangNhap.Password txtPass;
    private com.tuandhpc05076.Swing.TextField1 txtUserName;
    // End of variables declaration//GEN-END:variables
}
