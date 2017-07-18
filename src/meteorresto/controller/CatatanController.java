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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
public class CatatanController implements Initializable {

    @FXML
    private AnchorPane header;
    @FXML
    private Pane pkembali;
    @FXML
    private DatePicker dtanggal;
    @FXML
    private TextField tnama;
    @FXML
    private TextField tjumlah;
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
    private TableColumn<Entity, String> tanggal;
    @FXML
    private TableColumn<Entity, String> kode_transaksi;
    @FXML
    private Pane detail;
    @FXML
    private TextField tcari;
    Sessionhelper sh = new Sessionhelper();
    Connectionhelper ch = new Connectionhelper();
    ObservableList<Entity> tabledata = FXCollections.observableArrayList();
    ObservableList olscombo = FXCollections.observableArrayList();
    ObservableList olscombouang = FXCollections.observableArrayList();
    Operationhelper oh = new Operationhelper();
    String ids;
    @FXML
    private ComboBox<String> ckategori;
    @FXML
    private TextArea tketerangan;
    @FXML
    private TableColumn<Entity, String> kategori;
    @FXML
    private TableColumn<Entity, String> nama;
    @FXML
    private TableColumn<Entity, String> jumlah;
    @FXML
    private TableColumn<Entity, String> keterangan;
    NumberFormat nf = NumberFormat.getInstance();
    @FXML
    private ComboBox<?> cakunuang;
    @FXML
    private TableColumn<Entity, String> akun_keuangan;

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
        loadcombokeuangan();
        rawclear();
        luser.setText(sh.getUsername());
        bsimpan.setId("bc");
        bclear.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        detail.setId("tema");
        tlimit.setId("tema");
    }

    private void kembali() {
        ckategori.getEditor().clear();
        olscombo.clear();
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    table.getItems().clear();
                    tabledata.clear();
                    olscombo.clear();
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadcombo() {
        ckategori.getEditor().clear();
        olscombo.clear();
        try {
            String sql = "SELECT kode_perkiraan,nama_perkiraan FROM perkiraan ORDER BY kode_perkiraan DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                olscombo.add(res.getString("kode_perkiraan") + "-" + res.getString("nama_perkiraan"));
            }
            pre.close();
            res.close();
            ch.close();
            ckategori.setItems(olscombo);
        } catch (SQLException ex) {
            Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void loadcombokeuangan() {
        cakunuang.getEditor().clear();
        olscombouang.clear();
        try {
            String sql = "SELECT kode_akun_keuangan,nama_akun_keuangan FROM akun_keuangan "
                    + "ORDER BY kode_akun_keuangan DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                olscombouang.add(res.getString("kode_akun_keuangan") + "-" + res.getString("nama_akun_keuangan"));
            }
            pre.close();
            res.close();
            ch.close();
            cakunuang.setItems(olscombouang);
        } catch (SQLException ex) {
            Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void loaddata() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
            String sql = "SELECT kode,catatan.kode_perkiraan,nama_perkiraan,tanggal,nama,jumlah,keterangan,"
                    + "akun_keuangan.nama_akun_keuangan,akun_keuangan.kode_akun_keuangan FROM catatan "
                    + "INNER JOIN perkiraan ON catatan.kode_perkiraan=perkiraan.kode_perkiraan "
                    + "INNER JOIN akun_keuangan ON catatan.kode_akun_keuangan=akun_keuangan.kode_akun_keuangan"
                    + " ORDER BY tanggal DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {

                String skode_transaksi = res.getString("kode");
                String skodeperkiraan = res.getString("kode_perkiraan");
                String snamaperkiraan = res.getString("nama_perkiraan");
                String skodeakunkeuangan = res.getString("kode_akun_keuangan");
                String snamaakunkeuangan = res.getString("nama_akun_keuangan");
                String stanggal = res.getString("tanggal");
                String snama = res.getString("nama");
                String sjumlah = nf.format(res.getDouble("jumlah"));
                String sketerangan = res.getString("keterangan");
                tabledata.add(new Entity(skode_transaksi, skodeperkiraan, snamaperkiraan,
                        skodeakunkeuangan, snamaakunkeuangan, stanggal, snama, sjumlah, sketerangan));
            }
            pre.close();
            res.close();
            ch.close();
            tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
            kode_transaksi.setCellValueFactory(new PropertyValueFactory<>("kode"));
            kategori.setCellValueFactory(new PropertyValueFactory<>("nama_perkiraan"));
            akun_keuangan.setCellValueFactory(new PropertyValueFactory<>("nama_akun_keuangan"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            jumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
            keterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
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
                if (i < 0) {

                } else {
                    if (tanggal.getCellData(i) == null) {

                    } else {
                        dtanggal.setValue(LocalDate.parse(tanggal.getCellData(i)));
                    }
                    ids = kode_transaksi.getCellData(i);
                    ckategori.getEditor().setText(tabledata.get(i).getKode_perkiraan()
                            + "-" + tabledata.get(i).getNama_perkiraan());
                    cakunuang.getEditor().setText(tabledata.get(i).getKode_akun_keuangan()
                            + "-" + tabledata.get(i).getNama_akun_keuangan());
                    tnama.setText(nama.getCellData(i));
                    tjumlah.setText(jumlah.getCellData(i));
                    tketerangan.setText(keterangan.getCellData(i));
                }
            }
        });
    }

    private void rawsimpan() {
        int indexcombo = ckategori.getSelectionModel().getSelectedIndex();
        if (ckategori.getEditor().getText().equals("") || cakunuang.getEditor().getText().equals("")) {
            oh.equals("Maaf anda belum memilih kategori atau akun keuangan");
        } else {
            if (ids == null || ids.equals("")) {
                try {
                    String sqlgetno = "SELECT kode FROM catatan ORDER BY kode DESC LIMIT 1";
                    PreparedStatement pregetno = ch.connect().prepareStatement(sqlgetno);
                    ResultSet resno = pregetno.executeQuery();
                    String kode_transaksi = "";
                    String tglhariini = new SimpleDateFormat("yyMMdd").format(new Date());
                    while (resno.next()) {
                        kode_transaksi = resno.getString("kode");
                    }
                    if (kode_transaksi == null || kode_transaksi.equals("")
                            || !kode_transaksi.substring(0, 6).equals(tglhariini)) {
                        System.out.println(tglhariini);
                        kode_transaksi = tglhariini + "1";
                    } else {
                        kode_transaksi = String.valueOf(Integer.parseInt(kode_transaksi) + 1);
                    }
                    pregetno.close();
                    resno.close();
                    ch.close();
                    String sql = "INSERT INTO catatan(kode,kode_perkiraan,kode_akun_keuangan,tanggal,nama,jumlah,keterangan) "
                            + "VALUES(?,?,?,?::date,?,?,?)";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, kode_transaksi);
                    pre.setString(2, ckategori.getEditor().getText().split("-")[0]);
                    pre.setString(3, cakunuang.getEditor().getText().split("-")[0]);
                    pre.setString(4, dtanggal.getValue().toString());
                    pre.setString(5, tnama.getText());
                    pre.setDouble(6, Double.parseDouble(tjumlah.getText().replaceAll("[.,]", "")));
                    pre.setString(7, tketerangan.getText());
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    oh.sukses("Data Catatan Berhasil Disimpan");
                    loaddata();
                    loadtotaldata();
                } catch (SQLException ex) {
                    Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } finally {
                    ch.close();
                }

            } else {
                if (oh.konfirmasi("ubah") == true) {
                    try {
                        String sql = "UPDATE catatan SET kode_perkiraan=?,kode_akun_keuangan=?,tanggal=?::date,"
                                + "nama=?,jumlah=?,keterangan=? WHERE kode=?";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, ckategori.getEditor().getText().split("-")[0]);
                        pre.setString(2, cakunuang.getEditor().getText().split("-")[0]);
                        pre.setString(3, dtanggal.getValue().toString());
                        pre.setString(4, tnama.getText());
                        pre.setDouble(5, Double.parseDouble(tjumlah.getText().replaceAll("[.,]", "")));
                        pre.setString(6, tketerangan.getText());
                        pre.setString(7, ids);
                        pre.executeUpdate();
                        pre.close();
                        ch.close();
                        oh.sukses("Data Catatan Berhasil Perbaharui");
                        loaddata();
                        loadtotaldata();
                    } catch (SQLException ex) {
                        Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
                        oh.error(ex);
                    } finally {
                        ch.close();
                    }
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
                String sql = "DELETE FROM catatan WHERE kode=?";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, ids);
                pre.executeUpdate();
                pre.close();
                ch.close();
                oh.sukses("Data Berhasil Dihapus");
                loaddata();
                loadtotaldata();
            } catch (SQLException ex) {
                Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
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
        ckategori.getEditor().clear();
        cakunuang.getEditor().clear();
        tnama.clear();
        tjumlah.clear();
        tketerangan.clear();
        dtanggal.setValue(LocalDate.now());
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
            String sql = "SELECT kode,catatan.kode_perkiraan,nama_perkiraan,tanggal,nama,jumlah,keterangan,"
                    + "akun_keuangan.nama_akun_keuangan,akun_keuangan.kode_akun_keuangan FROM catatan "
                    + "INNER JOIN perkiraan ON catatan.kode_perkiraan=perkiraan.kode_perkiraan "
                    + "INNER JOIN akun_keuangan ON catatan.kode_akun_keuangan=akun_keuangan.kode_akun_keuangan "
                    + "WHERE tanggal::character varying ILIKE ? OR "
                    + "kode ILIKE ? OR "
                    + "nama ILIKE ? OR "
                    + "jumlah::character varying ILIKE ? OR "
                    + "nama_perkiraan ILIKE ? OR "
                    + "nama_akun_keuangan ILIKE ? OR "
                    + "keterangan ILIKE ? ORDER BY tanggal DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            for (int i = 0; i < 7; i++) {
                pre.setString(i + 1, "%" + tcari.getText() + "%");
            }
            pre.setInt(8, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode_transaksi = res.getString("kode");
                String skodeperkiraan = res.getString("kode_perkiraan");
                String snamaperkiraan = res.getString("nama_perkiraan");
                String skodeakunkeuangan = res.getString("kode_akun_keuangan");
                String snamaakunkeuangan = res.getString("nama_akun_keuangan");
                String stanggal = res.getString("tanggal");
                String snama = res.getString("nama");
                String sjumlah = nf.format(res.getDouble("jumlah"));
                String sketerangan = res.getString("keterangan");
                tabledata.add(new Entity(skode_transaksi, skodeperkiraan, snamaperkiraan,
                        skodeakunkeuangan, snamaakunkeuangan, stanggal, snama, sjumlah, sketerangan));
            }
            pre.close();
            res.close();
            ch.close();
            tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
            kode_transaksi.setCellValueFactory(new PropertyValueFactory<>("kode"));
            kategori.setCellValueFactory(new PropertyValueFactory<>("nama_kategori"));
            akun_keuangan.setCellValueFactory(new PropertyValueFactory<>("nama_akun_keuangan"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            jumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
            keterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(CatatanController.class.getName()).log(Level.SEVERE, null, ex);
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

        String kode, kode_perkiraan, nama_perkiraan, kode_akun_keuangan, nama_akun_keuangan, tanggal, nama, jumlah, keterangan;

        public Entity(String kode, String kode_perkiraan, String nama_perkiraan, String kode_akun_keuangan, String nama_akun_keuangan, String tanggal, String nama, String jumlah, String keterangan) {
            this.kode = kode;
            this.kode_perkiraan = kode_perkiraan;
            this.nama_perkiraan = nama_perkiraan;
            this.kode_akun_keuangan = kode_akun_keuangan;
            this.nama_akun_keuangan = nama_akun_keuangan;
            this.tanggal = tanggal;
            this.nama = nama;
            this.jumlah = jumlah;
            this.keterangan = keterangan;
        }

        public String getKode() {
            return kode;
        }

        public void setKode(String kode) {
            this.kode = kode;
        }

        public String getKode_perkiraan() {
            return kode_perkiraan;
        }

        public void setKode_perkiraan(String kode_perkiraan) {
            this.kode_perkiraan = kode_perkiraan;
        }

        public String getNama_perkiraan() {
            return nama_perkiraan;
        }

        public void setNama_perkiraan(String nama_perkiraan) {
            this.nama_perkiraan = nama_perkiraan;
        }

        public String getKode_akun_keuangan() {
            return kode_akun_keuangan;
        }

        public void setKode_akun_keuangan(String kode_akun_keuangan) {
            this.kode_akun_keuangan = kode_akun_keuangan;
        }

        public String getNama_akun_keuangan() {
            return nama_akun_keuangan;
        }

        public void setNama_akun_keuangan(String nama_akun_keuangan) {
            this.nama_akun_keuangan = nama_akun_keuangan;
        }

        public String getTanggal() {
            return tanggal;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getJumlah() {
            return jumlah;
        }

        public void setJumlah(String jumlah) {
            this.jumlah = jumlah;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }

    }

}
