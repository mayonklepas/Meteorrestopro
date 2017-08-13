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
import java.text.NumberFormat;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
public class ResepController implements Initializable {

    @FXML
    private AnchorPane header;
    @FXML
    private Pane pkembali;
    @FXML
    private TextField tkode;
    @FXML
    private TextField tnama;
    @FXML
    private TableView<Entitybahan> tablebahan;
    @FXML
    private TableColumn<Entitybahan, String> resepbahan;
    @FXML
    private TableColumn<Entitybahan, String> resepjumlah;
    @FXML
    private Button bsimpan;
    @FXML
    private Button bclear;
    @FXML
    private AnchorPane footer;
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
    private Pane detail;
    @FXML
    private TextField tcari;
    Connectionhelper ch = new Connectionhelper();
    Operationhelper oh = new Operationhelper();
    Sessionhelper sh = new Sessionhelper();
    ObservableList tabledata = FXCollections.observableArrayList();
    ObservableList<Entitybahan> tabledatabahan = FXCollections.observableArrayList();
    ObservableList olscombobahan = FXCollections.observableArrayList();
    ObservableList<Entitybahanlist> tabledatabahanlist = FXCollections.observableArrayList();
    String ids, idsresep;
    @FXML
    private Button btambah;
    NumberFormat nf = NumberFormat.getInstance();

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
        combobahan();
        tambahbahan();
        ontablecellchange();
        onkodetype();
        selectresep();
        hapusresep();
        luser.setText(sh.getUsername());
        bsimpan.setId("bc");
        bclear.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        detail.setId("tema");
        tlimit.setId("tema");
        btambah.setId("bc");
    }

    private void kembali() {
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String sql = "SELECT COUNT(kode_resep) jumlahdata FROM resep_master WHERE kode_resep=?";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, tkode.getText());
                    ResultSet res = pre.executeQuery();
                    int jumlahdata = 0;
                    while (res.next()) {
                        jumlahdata = res.getInt("jumlahdata");
                    }
                    if (tnama.getText().equals("")) {
                        table.getItems().clear();
                        tabledata.clear();
                        tabledatabahan.clear();
                        olscombobahan.clear();
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } else {
                        if (jumlahdata == 0) {
                            oh.gagal("Anda belum menyimpan resep");
                        } else {
                            table.getItems().clear();
                            tabledata.clear();
                            tabledatabahan.clear();
                            olscombobahan.clear();
                            FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                            Parent root = fxl.load();
                            sh.getSt().getScene().setRoot(root);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                }
            }
        });
    }

    private void loaddata() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
            String sql = "SELECT kode_resep,nama_resep,status FROM resep_master ORDER BY nama_resep DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode = res.getString("kode_resep");
                String snama = res.getString("nama_resep");
                String sstatus = res.getString("status");
                tabledata.add(new Entity(skode, snama, sstatus));
            }
            pre.close();
            res.close();
            ch.close();
            kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
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
                rawloadbahan(ids);
            }
        });
    }

    private void rawsimpan() {
        if (ids == null || ids.equals("")) {
            try {
                String sql = "INSERT INTO resep_master(kode_resep,nama_resep) VALUES(?,?)";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, tkode.getText());
                pre.setString(2, tnama.getText());
                pre.executeUpdate();
                pre.close();
                ch.close();
                oh.sukses("Data Resep Berhasil Disimpan");
                loaddata();
                loadtotaldata();
            } catch (SQLException ex) {
                Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                oh.error(ex);
            } finally {
                ch.close();
            }

        } else {
            if (oh.konfirmasi("ubah") == true) {
                try {
                    String sql = "UPDATE resep_master SET kode_resep=?,nama_resep=? WHERE kode_resep=?";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, tkode.getText());
                    pre.setString(2, tnama.getText());
                    pre.setString(3, ids);
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    oh.sukses("Data Resep Berhasil Perbaharui");
                    loaddata();
                    loadtotaldata();
                } catch (SQLException ex) {
                    Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
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
                if (tkode.getText().equals("") || tnama.getText().equals("")) {
                    oh.gagal("Maaf, kode dan nama tidak boleh kosong");
                } else {
                    rawsimpan();
                }
            }
        });
    }

    private void rawhapus() {
        if (oh.konfirmasi("hapus") == true) {
            try {
                String sql = "DELETE FROM resep_master WHERE kode_resep=?;"
                        + "DELETE FROM resep_detail WHERE kode_master=?";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, ids);
                pre.setString(2, ids);
                pre.executeUpdate();
                pre.close();
                ch.close();
                oh.sukses("Data Berhasil Dihapus");
                loaddata();
                loadtotaldata();
            } catch (SQLException ex) {
                Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
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
        tkode.clear();
        tnama.clear();
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
            String sql = "SELECT kode_resep,nama_resep,status FROM resep_master "
                    + "WHERE kode_resep ILIKE ? OR "
                    + "nama_resep ILIKE ? OR "
                    + "status::character varying ILIKE ? "
                    + "ORDER BY nama DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            for (int i = 0; i < 3; i++) {
                pre.setString(i + 1, "%" + tcari.getText() + "%");
            }
            pre.setInt(4, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode = res.getString("kode_resep");
                String snama = res.getString("nama_resep");
                String sstatus = res.getString("status");
                tabledata.add(new Entity(skode, snama, sstatus));
            }
            pre.close();
            res.close();
            ch.close();
            kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            table.setItems(tabledata);

        } catch (SQLException ex) {
            Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void combobahan() {
        olscombobahan.clear();
        try {
            String sql = "SELECT kode,nama FROM bahan ORDER BY nama DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode = res.getString("kode");
                String snama = res.getString("nama");
                olscombobahan.add(snama + "-" + skode);
            }
            pre.close();
            res.close();
            ch.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ch.close();
        }
    }

    private void tambahbahan() {
        btambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                try {
                    if (tkode.getText().equals("")) {
                        oh.gagal("Kode tidak boleh kosong");
                    } else {
                        String sql = "INSERT INTO resep_detail(kode_master,jumlah_pakai) VALUES(?,0)";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, tkode.getText());
                        pre.executeUpdate();
                        ch.close();
                        rawloadbahan(tkode.getText());
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    ch.close();
                }
            }
        });
    }

    private void tambahbahanlist() {
        btambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tabledatabahanlist.add(new Entitybahanlist("0", "Klik Disini", "0", "0"));
            }
        });
    }

    private void selectresep() {
        tablebahan.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int i = newValue.intValue();
                if (i < 0) {
                    System.out.println("out");
                } else {
                    idsresep = tabledatabahan.get(i).getId();
                }

            }
        });
    }

    private void hapusresep() {
        tablebahan.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    if (oh.konfirmasi("hapus") == true) {
                        try {
                            String sql = "DELETE FROM resep_detail WHERE id=?";
                            PreparedStatement pre = ch.connect().prepareStatement(sql);
                            pre.setInt(1, Integer.parseInt(idsresep));
                            pre.executeUpdate();
                            pre.close();
                            ch.close();
                            oh.sukses("Data Berhasil Dihapus");
                            rawloadbahan(tkode.getText());
                        } catch (SQLException ex) {
                            Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                            oh.error(ex);
                        } finally {
                            ch.close();
                        }

                    }
                }
            }
        });
    }

    private void ontablecellchange() {
        resepbahan.setCellFactory(ComboBoxTableCell.forTableColumn(olscombobahan));
        resepjumlah.setCellFactory(TextFieldTableCell.forTableColumn());
        resepbahan.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Entitybahan, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Entitybahan, String> event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                try {
                    if (tkode.getText().equals("")) {
                        oh.gagal("Kode tidak boleh kosong");
                    } else {
                        String sql = "UPDATE resep_detail SET kode_master=?,kode_bahan=? WHERE id=?";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, tkode.getText());
                        pre.setString(2, event.getNewValue().split("-")[1]);
                        pre.setInt(3, Integer.parseInt(event.getRowValue().id));
                        pre.executeUpdate();
                        ch.close();
                        rawloadbahan(tkode.getText());
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    ch.close();
                }
            }
        });
        resepjumlah.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Entitybahan, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Entitybahan, String> event) {
                try {
                    if (tkode.getText().equals("")) {
                        oh.gagal("Kode tidak boleh kosong");
                    } else {
                        String sql = "UPDATE resep_detail SET kode_master=?,jumlah_pakai=? WHERE id=?";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, tkode.getText());
                        pre.setDouble(2, Double.parseDouble(oh.digitinputreplacer(event.getNewValue())));
                        pre.setInt(3, Integer.parseInt(event.getRowValue().id));
                        pre.executeUpdate();
                        ch.close();
                        rawloadbahan(tkode.getText());
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    ch.close();
                }
            }
        });

    }

    private void rawloadbahan(String kode_resep) {
        tablebahan.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablebahan.getItems().clear();
        try {
            String sql = "SELECT rd.id,b.nama,rd.jumlah_pakai FROM resep_detail rd "
                    + "LEFT JOIN bahan b ON b.kode=rd.kode_bahan  WHERE kode_master = ? ORDER BY rd.id";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, kode_resep);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String sid = res.getString("id");
                String snama_bahan = res.getString("nama");
                String sjumlahpakai = nf.format(res.getDouble("jumlah_pakai"));
                tabledatabahan.add(new Entitybahan(sid, snama_bahan, sjumlahpakai));
            }
            pre.close();
            res.close();
            ch.close();
            resepbahan.setCellValueFactory(new PropertyValueFactory<>("nama_bahan"));
            resepjumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
            tablebahan.setItems(tabledatabahan);
        } catch (SQLException ex) {
            Logger.getLogger(ResepController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void onkodetype() {
        tkode.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                rawloadbahan(tkode.getText());
            }
        });

        tkode.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (event.getCode() == KeyCode.ENTER) {
                    rawloadbahan(tkode.getText());
                }
            }
        });
    }

    public class Entity {

        String kode, nama, status;

        public Entity(String kode, String nama, String status) {
            this.kode = kode;
            this.nama = nama;
            this.status = status;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class Entitybahan {

        String id, nama_bahan, jumlah;

        public Entitybahan(String id, String nama_bahan, String jumlah) {
            this.id = id;
            this.nama_bahan = nama_bahan;
            this.jumlah = jumlah;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama_bahan() {
            return nama_bahan;
        }

        public void setNama_bahan(String nama_bahan) {
            this.nama_bahan = nama_bahan;
        }

        public String getJumlah() {
            return jumlah;
        }

        public void setJumlah(String jumlah) {
            this.jumlah = jumlah;
        }

    }

    public class Entitybahancombo {

        String kode, nama_bahan;

        public Entitybahancombo(String kode, String nama_bahan) {
            this.kode = kode;
            this.nama_bahan = nama_bahan;
        }

        public String getKode() {
            return kode;
        }

        public void setKode(String kode) {
            this.kode = kode;
        }

        public String getNama_bahan() {
            return nama_bahan;
        }

        public void setNama_bahan(String nama_bahan) {
            this.nama_bahan = nama_bahan;
        }

    }

    public class Entitybahanlist {

        String id, nama_bahan, jumlah, status;

        public Entitybahanlist(String id, String nama_bahan, String jumlah, String status) {
            this.id = id;
            this.nama_bahan = nama_bahan;
            this.jumlah = jumlah;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama_bahan() {
            return nama_bahan;
        }

        public void setNama_bahan(String nama_bahan) {
            this.nama_bahan = nama_bahan;
        }

        public String getJumlah() {
            return jumlah;
        }

        public void setJumlah(String jumlah) {
            this.jumlah = jumlah;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
