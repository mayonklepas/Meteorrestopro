/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;

/**
 * FXML Controller class
 *
 * @author user
 */
public class AkunController implements Initializable {

    @FXML
    private Pane pkembali;
    @FXML
    private TextField tkode;
    @FXML
    private TextField tnama;
    @FXML
    private TextField tusername;
    @FXML
    private PasswordField tpassword;
    @FXML
    private PasswordField trepassword;
    @FXML
    private ComboBox<String> ctipe;
    @FXML
    private Button bsimpan;
    @FXML
    private Button bclear;
    @FXML
    private Label luser;
    @FXML
    private Label ljumlah;
    @FXML
    private TextField tlimit;
    @FXML
    private TableView<Entity> table;
    @FXML
    private TableColumn<Entity, String> kode;
    @FXML
    private TableColumn<Entity, String> nama;
    @FXML
    private TableColumn<Entity, String> username;
    @FXML
    private TableColumn<Entity, String> password;
    @FXML
    private TableColumn<Entity, String> tipe;
    @FXML
    private TextField tcari;
    Sessionhelper sh = new Sessionhelper();
    Connectionhelper ch = new Connectionhelper();
    ObservableList tabledata = FXCollections.observableArrayList();
    ObservableList olscombo = FXCollections.observableArrayList();
    Operationhelper oh = new Operationhelper();
    String ids;
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    @FXML
    private Pane detail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        kembali();
        loaddata();
        select();
        simpan();
        hapus();
        clear();
        cari();
        loadtotaldata();
        loadcombo();
        luser.setText(sh.getUsername());
        bsimpan.setId("bc");
        bclear.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        detail.setId("tema");
        tlimit.setId("tema");
        //pkembali.setId("pane");
    }

    private void kembali() {
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    table.getItems().clear();
                    ctipe.getItems().clear();
                    tabledata.clear();
                    olscombo.clear();
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(AkunController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadcombo() {
        olscombo.addAll("admin", "kasir");
        ctipe.setItems(olscombo);
    }

    private void loaddata() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
            String sql = "SELECT id,nama,username,password,tipe FROM akun ORDER BY nama DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String sid = res.getString("id");
                String snama = res.getString("nama");
                String susername = res.getString("username");
                String spassword = res.getString("password");
                String stipe = res.getString("tipe");
                tabledata.add(new Entity(sid, snama, susername, spassword, stipe));
            }
            pre.close();
            res.close();
            ch.close();
            kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            username.setCellValueFactory(new PropertyValueFactory<>("username"));
            password.setCellValueFactory(new PropertyValueFactory<>("password"));
            tipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(AkunController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void loadtotaldata() {
        ljumlah.setText(" | " + String.valueOf(tabledata.size()) + " / " + tlimit.getText() + " DATA");
    }

    private void select() {
        table.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int i = newValue.intValue();
                ids = kode.getCellData(i);
                tkode.setText(kode.getCellData(i));
                tnama.setText(nama.getCellData(i));
                tusername.setText(username.getCellData(i));
                tpassword.setText(password.getCellData(i));
                ctipe.getEditor().setText(tipe.getCellData(i));
            }
        });
    }

    private void rawsimpan() {
        if (ids == null || ids.equals("")) {
            try {
                String sql = "INSERT INTO akun(id,nama,username,password,tipe) VALUES(?,?,?,?,?)";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, tkode.getText());
                pre.setString(2, tnama.getText());
                pre.setString(3, tusername.getText());
                pre.setString(4, tpassword.getText());
                pre.setString(5, ctipe.getEditor().getText());
                pre.executeUpdate();
                pre.close();
                ch.close();
                oh.sukses("Data Akun Berhasil Disimpan");
                loaddata();
                loadtotaldata();
            } catch (SQLException ex) {
                Logger.getLogger(AkunController.class.getName()).log(Level.SEVERE, null, ex);
                oh.error(ex);
            } finally {
                ch.close();
            }

        } else {
            if (oh.konfirmasi("ubah") == true) {
                try {
                    String sql = "UPDATE akun SET id=?,nama=?,username=?,password=?,tipe=? WHERE id=?";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, tkode.getText());
                    pre.setString(2, tnama.getText());
                    pre.setString(3, tusername.getText());
                    pre.setString(4, tpassword.getText());
                    pre.setString(5, ctipe.getEditor().getText());
                    pre.setString(6, ids);
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    oh.sukses("Data Akun Berhasil Perbaharui");
                    loaddata();
                    loadtotaldata();
                } catch (SQLException ex) {
                    Logger.getLogger(AkunController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } finally {
                    ch.close();
                }
            }

        }
    }

    private void simpan() {
        bsimpan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                rawsimpan();
            }
        });
    }

    private void rawhapus() {
        if (oh.konfirmasi("hapus") == true) {
            try {
                String sql = "DELETE FROM akun WHERE id=?";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, ids);
                pre.executeUpdate();
                pre.close();
                ch.close();
                oh.sukses("Data Berhasil Dihapus");
                loaddata();
                loadtotaldata();
            } catch (SQLException ex) {
                Logger.getLogger(AkunController.class.getName()).log(Level.SEVERE, null, ex);
                oh.error(ex);
            } finally {
                ch.close();
            }

        }
    }

    private void hapus() {
        table.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    rawhapus();
                }
            }
        });
    }

    private void rawclear() {
        ids = "";
        tkode.clear();
        tnama.clear();
        tusername.clear();
        tpassword.clear();
        trepassword.clear();
        ctipe.getEditor().clear();
    }

    private void clear() {
        bclear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loaddata();
                loadtotaldata();
                rawclear();
            }
        });
    }

    private void rawcari() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
            String sql = "SELECT id,nama,username,password,tipe FROM akun "
                    + "WHERE id ILIKE ? OR "
                    + "nama ILIKE ? OR "
                    + "username ILIKE ? OR "
                    + "tipe ILIKE ? ORDER BY nama DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            for (int i = 0; i < 4; i++) {
                pre.setString(i + 1, "%" + tcari.getText() + "%");
            }
            pre.setInt(5, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String sid = res.getString("id");
                String snama = res.getString("nama");
                String susername = res.getString("username");
                String spassword = res.getString("password");
                String stipe = res.getString("tipe");
                tabledata.add(new Entity(sid, snama, susername, spassword, stipe));
            }
            pre.close();
            res.close();
            ch.close();
            kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            username.setCellValueFactory(new PropertyValueFactory<>("username"));
            password.setCellValueFactory(new PropertyValueFactory<>("password"));
            tipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
            table.setItems(tabledata);

        } catch (SQLException ex) {
            Logger.getLogger(AkunController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void cari() {
        tcari.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                rawcari();
                loadtotaldata();
            }
        });
    }

    public class Entity {

        String kode, nama, username, password, tipe;

        public Entity(String kode, String nama, String username, String password, String tipe) {
            this.kode = kode;
            this.nama = nama;
            this.username = username;
            this.password = password;
            this.tipe = tipe;
        }

        public String getKode() {
            return kode;
        }

        public void setKode(String kode) {
            this.kode = kode;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTipe() {
            return tipe;
        }

        public void setTipe(String tipe) {
            this.tipe = tipe;
        }

    }

}
