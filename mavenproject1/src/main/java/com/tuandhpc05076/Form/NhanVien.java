/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tuandhpc05076.Form;

import com.tuandhpc05076.MaHoa.MaHoa;
import com.tuandhpc05076.Object.O_DangNhap;
import com.tuandhpc05076.Object.O_NhanVien;
import com.tuandhpc05076.swing0.Form;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DELL E5470
 */
public class NhanVien extends javax.swing.JPanel {

    public static ArrayList<O_NhanVien> list = new ArrayList<>();
    DefaultTableModel tblmodel;
    String userName = "sa";
    String password = "123";
    String url = "jdbc:sqlserver://localhost:1433; databaseName= EduSys;encrypt=false";
    int i = -1;
    O_DangNhap tk = new O_DangNhap();
    ArrayList<O_DangNhap> listDangNhap = tk.getAlllist();

    public NhanVien() {
        initComponents();
        jTabbedPane1.setBackground(NhanVien.this.getBackground());
        jPanel1.setBackground(NhanVien.this.getBackground());
        jPanel2.setBackground(NhanVien.this.getBackground());
        TieuDe();
        loadDataToArray();
        Duyet();

    }

    public void TieuDe() {
        tblmodel = new DefaultTableModel();
        String[] tbl = new String[]{"Mã NV", "Vai trò", "Họ và tên"};
        tblmodel.setColumnIdentifiers(tbl);
        tblUser.setModel(tblmodel);
    }

    public void loadDataToArray() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            java.sql.Connection con = DriverManager.getConnection(url, userName, password);
            java.sql.Statement st = con.createStatement();
            String sql = "SELECT * FROM NhanVien";
            ResultSet rs = st.executeQuery(sql);
            list.clear();
            while (rs.next()) {

                String MaNv = rs.getString(1);
                String MatKhau = rs.getString(2);
                String HoTen = rs.getString(3);
                boolean VaiTro = rs.getBoolean(4);
                list.add(new O_NhanVien(MaNv, MatKhau, HoTen, VaiTro));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Duyet() {
        tblmodel.setRowCount(0);
        for (O_NhanVien nv : list) {
            String ct = "";
            if (nv.isVaiTro()) {
                ct = "Trưởng phòng";
            } else {
                ct = "Nhân viên";
            }
            Object[] tbl = new Object[]{nv.getMaNV(), ct, nv.getHoTen()};
            tblmodel.addRow(tbl);
        }
    }

    public void btnNew() {
        txtMaNV.setText("");
        txtMaNV.setEditable(true);
        txtMatKhau.setText("");
        txtXacNhanMK.setText("");
        cboVaiTro.setSelectedItem(null);
        txtHoVaTen.setText("");
    }

    public void ShowThongTin() {
        O_NhanVien nv = list.get(i);
        txtMaNV.setText(nv.getMaNV());
        txtMatKhau.setText(nv.getMatKhau());
        if (nv.isVaiTro() == true) {
            cboVaiTro.setSelectedItem("Trưởng phòng");
        } else {
            cboVaiTro.setSelectedItem("Nhân viên");
        }
        tblUser.setRowSelectionInterval(i, i);
    }
    String mk = ".{3,}";
    String hoTen = "^[a-zA-Z ]+$";

    public boolean checkForm() {
        if (txtMaNV.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Chưa chập Mã NV");
            return false;
        }
        boolean kiemMa = false;
        for (O_NhanVien nv : list) {
            if (nv.getMaNV().equals(txtMaNV.getText())) {
                kiemMa = true;
            }
        }
        if (kiemMa) {
            JOptionPane.showMessageDialog(this, "Mã của bạn đã tồn tại trong danh sách không thê thêm vào");
            return false;
        }
        if (new String(txtMatKhau.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mật khẩu");
            return false;
        }
        if (!new String(txtMatKhau.getPassword()).matches(mk)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu ít nhất 3 kí tự");
            return false;
        }
        if (!new String(txtMatKhau.getPassword()).equals(new String(txtXacNhanMK.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Nhập lại mật khẩu không giống mật khẩu");
            return false;
        }
        if (cboVaiTro.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn vai trò");
            return false;
        }
        if (txtHoVaTen.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Họ tên của bạn đã để trống");
            return false;
        }
//        if(!txtHoVaTen.getText().matches(hoTen)){
//            JOptionPane.showMessageDialog(this,"Họ tên của bạn khôngn đúng định dạng");
//            return false;
//        }
        return true;
    }

    public void btnSave() {
        if (checkForm() == false) {
            return;
        }
        boolean kiem = false;
        for (O_DangNhap dn : listDangNhap) {
            if (dn.isVaiTro() == false) {
                kiem = true;
            }
        }
        if (kiem == true) {
            JOptionPane.showMessageDialog(this, "Bạn là nhân viên không có quyền thêm");
            return;
        }
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            java.sql.Connection con = DriverManager.getConnection(url, userName, password);
            String sqla = "insert into dbo.NhanVien values(?,?,?,?)";
            PreparedStatement st = con.prepareStatement(sqla);
            st.setString(1, txtMaNV.getText());
            MaHoa mh = new MaHoa();
            String pass = mh.toSHA(new String(txtMatKhau.getPassword()));
            st.setString(2, pass);
            st.setString(3, txtHoVaTen.getText());
            if (cboVaiTro.getSelectedItem().equals("Trưởng phòng")) {
                st.setBoolean(4, true);
            } else {
                st.setBoolean(4, false);
            }

            st.executeUpdate();
            con.close();
            loadDataToArray();
            Duyet();
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công");
            btnNew();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnDelete() {
        boolean kiem = false;
        for (O_DangNhap dn : listDangNhap) {
            if (dn.isVaiTro() == false) {
                kiem = true;
            }
        }
        if (kiem == true) {
            JOptionPane.showMessageDialog(this, "Bạn là nhân viên không có quyền xóa");
            return;
        }
        int chon = tblUser.getSelectedRow();
        String name = (String) tblmodel.getValueAt(chon, 0);
        boolean kiemXoaChinhMinh = false;
        for (O_DangNhap dn : listDangNhap) {
            if (dn.getMaNV().equals(name)) {
                kiemXoaChinhMinh = true;
            }
        }
        if (kiemXoaChinhMinh) {
            JOptionPane.showMessageDialog(this, "Không được xóa chính mình");
            return;
        }
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            java.sql.Connection con = DriverManager.getConnection(url, userName, password);
            String sqla = "DELETE FROM dbo.NhanVien WHERE MaNV=? ";
            PreparedStatement st = con.prepareStatement(sqla);
            st.setString(1, txtMaNV.getText());

            st.executeUpdate();
            con.close();
            loadDataToArray();
            Duyet();
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công");
            btnNew();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nhân viên này đang quản lý không thể xóa");
            e.printStackTrace();
        }
    }

    public void btnUpdate() {
        boolean kiem = false;
        for (O_DangNhap dn : listDangNhap) {
            if (dn.isVaiTro() == false) {
                kiem = true;
            }
        }
        if (kiem == true) {
            JOptionPane.showMessageDialog(this, "Bạn là nhân viên không có quyền cập nhật");
            return;
        }
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            java.sql.Connection con = DriverManager.getConnection(url, userName, password);
            String sqla = "UPDATE dbo.NhanVien SET MatKhau =? , HoTen=?, VaiTro =? WHERE MaNV =? ";
            PreparedStatement st = con.prepareStatement(sqla);
            st.setString(4, txtMaNV.getText());
            st.setString(1, new String(txtMatKhau.getPassword()));
            st.setString(2, txtHoVaTen.getText());
            if (cboVaiTro.getSelectedItem().equals("Trưởng phòng")) {
                st.setBoolean(3, true);
            } else {
                st.setBoolean(3, false);
            }
            st.executeUpdate();
            con.close();
            loadDataToArray();
            Duyet();
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công");
            btnNew();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnTim() {
        boolean kiem = false;
        if (txtTimKiem.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mã");
            return;
        }
        for (O_NhanVien nv : list) {
            if (nv.getMaNV().trim().equals(txtTimKiem.getText())) {
                txtMaNV.setText(nv.getMaNV());
                txtMaNV.setEditable(false);
                txtMatKhau.setText(nv.getMatKhau());
                txtXacNhanMK.setText(nv.getMatKhau());
                if (nv.isVaiTro() == true) {
                    cboVaiTro.setSelectedItem("Trưởng phòng");
                } else {
                    cboVaiTro.setSelectedItem("Nhân viên");
                }
                txtHoVaTen.setText(nv.getHoTen());
                jTabbedPane1.setSelectedIndex(0);
                JOptionPane.showMessageDialog(this, "Đã tìm thấy");
                kiem = true;
            }
        }
        if (kiem == false) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtMaNV = new com.tuandhpc05076.Swing.TextField();
        txtHoVaTen = new com.tuandhpc05076.Swing.TextField();
        btnMoi = new com.tuandhpc05076.Swing.Button();
        btnThem = new com.tuandhpc05076.Swing.Button();
        btnSua = new com.tuandhpc05076.Swing.Button();
        btnXoa = new com.tuandhpc05076.Swing.Button();
        btnToi = new com.tuandhpc05076.Swing.Button();
        btnCuoi = new com.tuandhpc05076.Swing.Button();
        btnLui = new com.tuandhpc05076.Swing.Button();
        btnDau = new com.tuandhpc05076.Swing.Button();
        cboVaiTro = new com.tuandhpc05076.Swing.Combobox();
        txtMatKhau = new com.tuandhpc05076.Swing.PasswordField();
        txtXacNhanMK = new com.tuandhpc05076.Swing.PasswordField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        txtTimKiem = new com.tuandhpc05076.Swing.TextField1();
        btnTimKiem = new com.tuandhpc05076.Swing.Button();

        setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txtMaNV.setBackground(new java.awt.Color(255, 255, 255));
        txtMaNV.setLabelText("Mã NV");

        txtHoVaTen.setBackground(new java.awt.Color(255, 255, 255));
        txtHoVaTen.setLabelText("Họ và tên");

        btnMoi.setBackground(new java.awt.Color(153, 153, 255));
        btnMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tuandhpc05076/icon1/edit.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMoiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMoiMouseExited(evt);
            }
        });
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(153, 153, 255));
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tuandhpc05076/icon1/plus.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnThemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnThemMouseExited(evt);
            }
        });
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(153, 153, 255));
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tuandhpc05076/icon1/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSuaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSuaMouseExited(evt);
            }
        });
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(153, 153, 255));
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tuandhpc05076/icon1/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnXoaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnXoaMouseExited(evt);
            }
        });
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnToi.setBackground(new java.awt.Color(153, 153, 255));
        btnToi.setForeground(new java.awt.Color(255, 255, 255));
        btnToi.setText(">>");
        btnToi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnToiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnToiMouseExited(evt);
            }
        });
        btnToi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToiActionPerformed(evt);
            }
        });

        btnCuoi.setBackground(new java.awt.Color(153, 153, 255));
        btnCuoi.setForeground(new java.awt.Color(255, 255, 255));
        btnCuoi.setText(">|");
        btnCuoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCuoiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCuoiMouseExited(evt);
            }
        });
        btnCuoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCuoiActionPerformed(evt);
            }
        });

        btnLui.setBackground(new java.awt.Color(153, 153, 255));
        btnLui.setForeground(new java.awt.Color(255, 255, 255));
        btnLui.setText("<<");
        btnLui.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLuiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLuiMouseExited(evt);
            }
        });
        btnLui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuiActionPerformed(evt);
            }
        });

        btnDau.setBackground(new java.awt.Color(153, 153, 255));
        btnDau.setForeground(new java.awt.Color(255, 255, 255));
        btnDau.setText("|<");
        btnDau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDauMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDauMouseExited(evt);
            }
        });
        btnDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDauActionPerformed(evt);
            }
        });

        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Trưởng phòng", "Nhân viên" }));
        cboVaiTro.setSelectedIndex(-1);
        cboVaiTro.setLabeText("Vai trò");

        txtMatKhau.setBackground(new java.awt.Color(255, 255, 255));
        txtMatKhau.setLabelText("Mật khẩu");
        txtMatKhau.setShowAndHide(true);

        txtXacNhanMK.setBackground(new java.awt.Color(255, 255, 255));
        txtXacNhanMK.setLabelText("Xác nhận mật khẩu");
        txtXacNhanMK.setShowAndHide(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
                .addComponent(btnDau, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLui, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnToi, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHoVaTen, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                    .addComponent(cboVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtXacNhanMK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtXacNhanMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(cboVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtHoVaTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCuoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnToi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông tin", jPanel1);

        tblUser.setModel(new javax.swing.table.DefaultTableModel(
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
        tblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUser);

        txtTimKiem.setHint("Nhập mã cần tìm kiếm");

        btnTimKiem.setBackground(new java.awt.Color(153, 153, 255));
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tuandhpc05076/icon1/search.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTimKiemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTimKiemMouseExited(evt);
            }
        });
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh sách", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textField4ActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        btnNew();        // TODO add your handling code here:
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDauActionPerformed
        // TODO add your handling code here:

        if (list.size() > 0) {
            i = 0;
            ShowThongTin();
        }
    }//GEN-LAST:event_btnDauActionPerformed

    private void btnCuoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuoiActionPerformed
        // TODO add your handling code here:
        if (list.size() > 0) {
            i = list.size() - 1;
            ShowThongTin();
        }
    }//GEN-LAST:event_btnCuoiActionPerformed

    private void btnLuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuiActionPerformed
        if (list.size() > 0) {
            i--;
            if (i < 0) {
                i = 0;
            }
            ShowThongTin();
        }

// TODO add your handling code here:

    }//GEN-LAST:event_btnLuiActionPerformed

    private void btnToiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToiActionPerformed
        // TODO add your handling code here:
        if (list.size() > 0) {
            i++;
            if (i == list.size()) {
                i = list.size() - 1;
            }
            ShowThongTin();
        }
    }//GEN-LAST:event_btnToiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        btnSave();
    }//GEN-LAST:event_btnThemActionPerformed

    private void tblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserMouseClicked
        // TODO add your handling code here:
        int chon = tblUser.getSelectedRow();
        String name = (String) tblmodel.getValueAt(chon, 0);
        for (O_NhanVien nv : list) {
            if (nv.getMaNV().trim().equals(name)) {
                txtMaNV.setText(nv.getMaNV());
                txtMaNV.setEditable(false);
                txtMatKhau.setText(nv.getMatKhau());
                txtXacNhanMK.setText(nv.getMatKhau());
                if (nv.isVaiTro() == true) {
                    cboVaiTro.setSelectedItem("Trưởng phòng");
                } else {
                    cboVaiTro.setSelectedItem("Nhân viên");
                }
                txtHoVaTen.setText(nv.getHoTen());
            }
        }
        jTabbedPane1.setSelectedIndex(0);

    }//GEN-LAST:event_tblUserMouseClicked

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        btnDelete();        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        btnUpdate();        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        btnTim();        // TODO add your handling code here:
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnTimKiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemMouseExited
        // TODO add your handling code here:

        btnTimKiem.setBackground(new Color(153, 153, 255));
    }//GEN-LAST:event_btnTimKiemMouseExited

    private void btnTimKiemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemMouseEntered
        // TODO add your handling code here:
        btnTimKiem.setBackground(Color.pink);
    }//GEN-LAST:event_btnTimKiemMouseEntered

    private void btnMoiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMoiMouseEntered
        // TODO add your handling code here:
        btnMoi.setBackground(Color.pink);
    }//GEN-LAST:event_btnMoiMouseEntered

    private void btnMoiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMoiMouseExited
        // TODO add your handling code here:
        btnMoi.setBackground(new Color(153, 153, 255));
    }//GEN-LAST:event_btnMoiMouseExited

    private void btnThemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThemMouseExited
        btnThem.setBackground(new Color(153, 153, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnThemMouseExited

    private void btnThemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThemMouseEntered
        btnThem.setBackground(Color.pink);        // TODO add your handling code here:
    }//GEN-LAST:event_btnThemMouseEntered

    private void btnSuaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuaMouseEntered
        // TODO add your handling code here:
        btnSua.setBackground(Color.pink);
    }//GEN-LAST:event_btnSuaMouseEntered

    private void btnSuaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuaMouseExited
        btnSua.setBackground(new Color(153, 153, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaMouseExited

    private void btnXoaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXoaMouseEntered
        btnXoa.setBackground(Color.pink);         // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaMouseEntered

    private void btnXoaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXoaMouseExited
        // TODO add your handling code here:
        btnXoa.setBackground(new Color(153, 153, 255));
    }//GEN-LAST:event_btnXoaMouseExited

    private void btnDauMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDauMouseExited
        btnDau.setBackground(new Color(153, 153, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnDauMouseExited

    private void btnDauMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDauMouseEntered
        btnDau.setBackground(Color.pink);         // TODO add your handling code here:
    }//GEN-LAST:event_btnDauMouseEntered

    private void btnLuiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLuiMouseEntered
        // TODO add your handling code here:
        btnLui.setBackground(Color.pink);
    }//GEN-LAST:event_btnLuiMouseEntered

    private void btnLuiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLuiMouseExited
        btnLui.setBackground(new Color(153, 153, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnLuiMouseExited

    private void btnToiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToiMouseEntered
        btnToi.setBackground(Color.pink);          // TODO add your handling code here:
    }//GEN-LAST:event_btnToiMouseEntered

    private void btnToiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToiMouseExited
        btnToi.setBackground(new Color(153, 153, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnToiMouseExited

    private void btnCuoiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCuoiMouseEntered
        btnCuoi.setBackground(Color.pink);          // TODO add your handling code here:
    }//GEN-LAST:event_btnCuoiMouseEntered

    private void btnCuoiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCuoiMouseExited
        btnCuoi.setBackground(new Color(153, 153, 255));        // TODO add your handling code here:
    }//GEN-LAST:event_btnCuoiMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.tuandhpc05076.Swing.Button btnCuoi;
    private com.tuandhpc05076.Swing.Button btnDau;
    private com.tuandhpc05076.Swing.Button btnLui;
    private com.tuandhpc05076.Swing.Button btnMoi;
    private com.tuandhpc05076.Swing.Button btnSua;
    private com.tuandhpc05076.Swing.Button btnThem;
    private com.tuandhpc05076.Swing.Button btnTimKiem;
    private com.tuandhpc05076.Swing.Button btnToi;
    private com.tuandhpc05076.Swing.Button btnXoa;
    private com.tuandhpc05076.Swing.Combobox cboVaiTro;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblUser;
    private com.tuandhpc05076.Swing.TextField txtHoVaTen;
    private com.tuandhpc05076.Swing.TextField txtMaNV;
    private com.tuandhpc05076.Swing.PasswordField txtMatKhau;
    private com.tuandhpc05076.Swing.TextField1 txtTimKiem;
    private com.tuandhpc05076.Swing.PasswordField txtXacNhanMK;
    // End of variables declaration//GEN-END:variables

}
