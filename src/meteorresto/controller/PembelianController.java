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
public class PembelianController implements Initializable {
    
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
    private TableColumn<Entity, String> kode_bahan;
    @FXML
    private TableColumn<Entity, String> nama_bahan;
    @FXML
    private TableColumn<Entity, String> jumlah_total;
    @FXML
    private TableColumn<Entity, String> harga_total;
    @FXML
    private Pane detail;
    @FXML
    private TextField tcari;
    Sessionhelper sh = new Sessionhelper();
    Connectionhelper ch = new Connectionhelper();
    ObservableList<Entity> tabledata = FXCollections.observableArrayList();
    ObservableList olscombo = FXCollections.observableArrayList();
    Operationhelper oh = new Operationhelper();
    String ids;
    @FXML
    private TextField tkode_bahan;
    @FXML
    private TextField tharga;
    @FXML
    private TableColumn<Entity, String> jumlahkg;
    NumberFormat nf = NumberFormat.getInstance();
    @FXML
    private ComboBox<String> cakunuang;
    @FXML
    private TableColumn<Entity, String> akun_keuangan;
    ObservableList olscombouang = FXCollections.observableArrayList();

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
        rawclear();
        onkodetype();
        loadcombokeuangan();
        luser.setText(sh.getUsername());
        bsimpan.setId("bc");
        bclear.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        detail.setId("tema");
        tlimit.setId("tema");
    }
    
    private void kembali() {
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
                    Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
            Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }
    
    private void loaddata() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
            String sql = "SELECT tanggal,pembelian.kode_akun_keuangan,akun_keuangan.nama_akun_keuangan,"
                    + "kode_transaksi,kode_bahan,nama,jumlah,(jumlah / 1000) AS jumlahkg,harga "
                    + "FROM pembelian INNER JOIN akun_keuangan "
                    + "ON pembelian.kode_akun_keuangan=akun_keuangan.kode_akun_keuangan "
                    + "WHERE kode_user = ? ORDER BY tanggal DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, sh.kode_user);
            pre.setInt(2, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String stanggal = res.getString("tanggal");
                String skode_transaksi = res.getString("kode_transaksi");
                String skode_bahan = res.getString("kode_bahan");
                String snama = res.getString("nama");
                String sjumlah = nf.format(res.getDouble("jumlah"));
                String sjumlahkg = nf.format(res.getDouble("jumlahkg"));
                String sharga = nf.format(res.getDouble("harga"));
                String skode_akun_keuangan = res.getString("kode_akun_keuangan");
                String snama_akun_keuangan = res.getString("nama_akun_keuangan");
                tabledata.add(new Entity(stanggal, skode_transaksi,
                        skode_bahan, snama, sjumlah, sjumlahkg, sharga, skode_akun_keuangan, snama_akun_keuangan));
            }
            pre.close();
            res.close();
            ch.close();
            tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
            kode_transaksi.setCellValueFactory(new PropertyValueFactory<>("kode_transaksi"));
            akun_keuangan.setCellValueFactory(new PropertyValueFactory<>("nama_akun_keuangan"));
            kode_bahan.setCellValueFactory(new PropertyValueFactory<>("kode_bahan"));
            nama_bahan.setCellValueFactory(new PropertyValueFactory<>("nama_bahan"));
            jumlah_total.setCellValueFactory(new PropertyValueFactory<>("jumlah_total"));
            jumlahkg.setCellValueFactory(new PropertyValueFactory<>("jumlahkg"));
            harga_total.setCellValueFactory(new PropertyValueFactory<>("harga_total"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
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
                try {
                    int i = newValue.intValue();
                    if (tanggal.getCellData(i) == null) {
                        
                    } else {
                        dtanggal.setValue(LocalDate.parse(tanggal.getCellData(i)));
                    }
                    ids = kode_transaksi.getCellData(i);
                    tkode_bahan.setText(kode_bahan.getCellData(i));
                    tnama.setText(nama_bahan.getCellData(i));
                    tjumlah.setText(jumlah_total.getCellData(i));
                    tharga.setText(harga_total.getCellData(i));
                    cakunuang.getEditor().setText(tabledata.get(i).kode_akun_keuangan + "-"
                            + tabledata.get(i).nama_akun_keuangan);
                } catch (Exception e) {
                    oh.error(e);
                }
                
            }
        });
    }
    
    private void onkodetype() {
        tkode_bahan.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    String sql = "SELECT kode,nama FROM bahan WHERE kode=? ";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, tkode_bahan.getText());
                    ResultSet res = pre.executeQuery();
                    while (res.next()) {
                        tnama.setText(res.getString("nama"));
                    }
                    pre.close();
                    res.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void rawsimpan() {
        if (cakunuang.getEditor().getText().equals("")) {
            oh.gagal("Anda belum memilih akun keuangan");
        } else {
            if (ids == null || ids.equals("")) {
                try {
                    String sqlgetno = "SELECT kode_transaksi FROM pembelian ORDER BY kode_transaksi DESC LIMIT 1";
                    PreparedStatement pregetno = ch.connect().prepareStatement(sqlgetno);
                    ResultSet resno = pregetno.executeQuery();
                    String kode_transaksi = "";
                    String tglhariini = new SimpleDateFormat("yyMMdd").format(new Date());
                    while (resno.next()) {
                        kode_transaksi = resno.getString("kode_transaksi");
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
                    String sql = "INSERT INTO pembelian(kode_transaksi,tanggal,kode_bahan,nama,jumlah,harga,"
                            + "kode_akun_keuangan,kode_user) "
                            + "VALUES(?,?::date,?,?,?,?,?,?)";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, kode_transaksi);
                    pre.setString(2, dtanggal.getValue().toString());
                    pre.setString(3, tkode_bahan.getText());
                    pre.setString(4, tnama.getText());
                    pre.setDouble(5, Double.parseDouble(oh.digitinputreplacer(tjumlah.getText())));
                    pre.setDouble(6, Double.parseDouble(oh.digitinputreplacer(tharga.getText())));
                    pre.setString(7, cakunuang.getEditor().getText().split("-")[0]);
                    pre.setString(8, sh.getKode_user());
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    oh.sukses("Data Pembelian Berhasil Disimpan");
                    loaddata();
                    loadtotaldata();
                    String sqlcount = "SELECT COUNT(kode) AS jumlah FROM bahan WHERE kode=? ";
                    PreparedStatement precount = ch.connect().prepareStatement(sqlcount);
                    precount.setString(1, tkode_bahan.getText());
                    ResultSet rescount = precount.executeQuery();
                    int jumlahdata = 0;
                    while (rescount.next()) {
                        jumlahdata = rescount.getInt("jumlah");
                    }
                    precount.close();
                    rescount.close();
                    if (jumlahdata == 1) {
                        String sqlupdatebahan = "UPDATE bahan SET jumlah=jumlah+? WHERE kode=? ";
                        PreparedStatement preupdatebahan = ch.connect().prepareStatement(sqlupdatebahan);
                        preupdatebahan.setDouble(1, Double.parseDouble(oh.digitinputreplacer(tjumlah.getText())));
                        preupdatebahan.setString(2, tkode_bahan.getText());
                        preupdatebahan.executeUpdate();
                        preupdatebahan.close();
                    } else {
                        String sqlinsertbahan = "INSERT INTO bahan(kode,nama,jumlah) VALUES(?,?,?)";
                        PreparedStatement preupdatebahan = ch.connect().prepareStatement(sqlinsertbahan);
                        preupdatebahan.setString(1, tkode_bahan.getText());
                        preupdatebahan.setString(2, tnama.getText());
                        preupdatebahan.setDouble(3, Double.parseDouble(oh.digitinputreplacer(tjumlah.getText())));
                        preupdatebahan.executeUpdate();
                        preupdatebahan.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } finally {
                    ch.close();
                }
                
            } else {
                if (oh.konfirmasi("ubah") == true) {
                    try {
                        String sql = "UPDATE pembelian SET tanggal=?::date,kode_bahan=?,"
                                + "nama=?,jumlah=?,harga=?,kode_akun_keuangan=? WHERE kode_transaksi=?";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, dtanggal.getValue().toString());
                        pre.setString(2, tkode_bahan.getText());
                        pre.setString(3, tnama.getText());
                        pre.setDouble(4, Double.parseDouble(oh.digitinputreplacer(tjumlah.getText())));
                        pre.setDouble(5, Double.parseDouble(oh.digitinputreplacer(tharga.getText())));
                        pre.setString(6, cakunuang.getEditor().getText().split("-")[0]);
                        pre.setString(7, ids);
                        pre.executeUpdate();
                        pre.close();
                        ch.close();
                        oh.sukses("Data Pembelian Berhasil Perbaharui");
                        loaddata();
                        loadtotaldata();
                    } catch (SQLException ex) {
                        Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
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
                String sql = "DELETE FROM pembelian WHERE kode_transaksi=?";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, ids);
                pre.executeUpdate();
                pre.close();
                ch.close();
                oh.sukses("Data Berhasil Dihapus");
                loaddata();
                loadtotaldata();
            } catch (SQLException ex) {
                Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
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
        tkode_bahan.clear();
        tnama.clear();
        tjumlah.clear();
        tharga.clear();
        dtanggal.setValue(LocalDate.now());
        cakunuang.getEditor().clear();
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
            String sql = "SELECT tanggal,kode_transaksi,pembelian.kode_akun_keuangan,"
                    + "akun_keuangan.nama_akun_keuangan,kode_bahan,nama,jumlah,(jumlah / 1000) AS jumlahkg,harga "
                    + "FROM pembelian INNER JOIN akun_keuangan ON pembelian.kode_akun_keuangan = "
                    + "akun_keuangan.kode_akun_keuangan WHERE (tanggal::character varying ILIKE ? OR "
                    + "kode_transaksi ILIKE ? OR "
                    + "nama ILIKE ? OR "
                    + "jumlah::character varying ILIKE ? OR "
                    + "harga::character varying ILIKE ? OR "
                    + "nama_akun_keuangan ILIKE ?) AND (kode_user=?) ORDER BY tanggal DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            for (int i = 0; i < 6; i++) {
                pre.setString(i + 1, "%" + tcari.getText() + "%");
            }
            pre.setString(7, sh.getKode_user());
            pre.setInt(8, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String stanggal = res.getString("tanggal");
                String skode_transaksi = res.getString("kode_transaksi");
                String skode_bahan = res.getString("kode_bahan");
                String snama = res.getString("nama");
                String sjumlah = nf.format(res.getDouble("jumlah"));
                String sjumlahkg = nf.format(res.getDouble("jumlahkg"));
                String sharga = nf.format(res.getDouble("harga"));
                String skode_akun_keuangan = res.getString("kode_akun_keuangan");
                String snama_akun_keuangan = res.getString("nama_akun_keuangan");
                tabledata.add(new Entity(stanggal, skode_transaksi,
                        skode_bahan, snama, sjumlah, sjumlahkg, sharga, skode_akun_keuangan, snama_akun_keuangan));
            }
            pre.close();
            res.close();
            ch.close();
            tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
            kode_transaksi.setCellValueFactory(new PropertyValueFactory<>("kode_transaksi"));
            akun_keuangan.setCellValueFactory(new PropertyValueFactory<>("nama_akun_keuangan"));
            kode_bahan.setCellValueFactory(new PropertyValueFactory<>("kode_bahan"));
            nama_bahan.setCellValueFactory(new PropertyValueFactory<>("nama_bahan"));
            jumlah_total.setCellValueFactory(new PropertyValueFactory<>("jumlah_total"));
            jumlahkg.setCellValueFactory(new PropertyValueFactory<>("jumlahkg"));
            harga_total.setCellValueFactory(new PropertyValueFactory<>("harga_total"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        String tanggal, kode_transaksi, kode_bahan, nama_bahan, jumlah_total, jumlahkg, harga_total,
                kode_akun_keuangan, nama_akun_keuangan;
        
        public Entity(String tanggal, String kode_transaksi, String kode_bahan, String nama_bahan, String jumlah_total, String jumlahkg, String harga_total, String kode_akun_keuangan, String nama_akun_keuangan) {
            this.tanggal = tanggal;
            this.kode_transaksi = kode_transaksi;
            this.kode_bahan = kode_bahan;
            this.nama_bahan = nama_bahan;
            this.jumlah_total = jumlah_total;
            this.jumlahkg = jumlahkg;
            this.harga_total = harga_total;
            this.kode_akun_keuangan = kode_akun_keuangan;
            this.nama_akun_keuangan = nama_akun_keuangan;
        }
        
        public String getTanggal() {
            return tanggal;
        }
        
        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }
        
        public String getKode_transaksi() {
            return kode_transaksi;
        }
        
        public void setKode_transaksi(String kode_transaksi) {
            this.kode_transaksi = kode_transaksi;
        }
        
        public String getKode_bahan() {
            return kode_bahan;
        }
        
        public void setKode_bahan(String kode_bahan) {
            this.kode_bahan = kode_bahan;
        }
        
        public String getNama_bahan() {
            return nama_bahan;
        }
        
        public void setNama_bahan(String nama_bahan) {
            this.nama_bahan = nama_bahan;
        }
        
        public String getJumlah_total() {
            return jumlah_total;
        }
        
        public void setJumlah_total(String jumlah_total) {
            this.jumlah_total = jumlah_total;
        }
        
        public String getJumlahkg() {
            return jumlahkg;
        }
        
        public void setJumlahkg(String jumlahkg) {
            this.jumlahkg = jumlahkg;
        }
        
        public String getHarga_total() {
            return harga_total;
        }
        
        public void setHarga_total(String harga_total) {
            this.harga_total = harga_total;
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
        
    }
    
}
