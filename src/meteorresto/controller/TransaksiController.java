/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * FXML Controller class
 *
 * @author user
 */
public class TransaksiController implements Initializable {

    @FXML
    FlowPane fpmeja;
    @FXML
    private Pane pkembali;
    @FXML
    private Label luser;
    @FXML
    private Label ljumlah;
    @FXML
    private ComboBox<String> ctipemeja;
    @FXML
    private ComboBox<String> ckategorimenu;
    @FXML
    private FlowPane fpmenu;
    @FXML
    private ComboBox<String> cslot;
    @FXML
    private Button bbayar, bclear, border, bclear2, bpindah, bcetakbil;
    @FXML
    private TableView<Entity> tableview;
    @FXML
    private TableColumn<Entity, String> menu;
    @FXML
    private TableColumn<Entity, String> qty;
    @FXML
    private TableColumn<Entity, String> harga;
    Sessionhelper sh = new Sessionhelper();
    Operationhelper oh = new Operationhelper();
    FIlehelper fh = new FIlehelper();
    Connectionhelper ch = new Connectionhelper();
    ObservableList<Button> btl = FXCollections.observableArrayList();
    ObservableList<Entitymeja> datameja = FXCollections.observableArrayList();
    ObservableList<Button> btlmenu = FXCollections.observableArrayList();
    ObservableList<Entitymenu> datamenu = FXCollections.observableArrayList();
    ObservableList<String> olsmeja = FXCollections.observableArrayList();
    ObservableList<String> olsmenu = FXCollections.observableArrayList();
    ObservableList<Entity> olstable = FXCollections.observableArrayList();
    ObservableList<String> olsslot = FXCollections.observableArrayList();
    NumberFormat nf = NumberFormat.getInstance();
    String ids, kategorimeja, namameja, statusmeja;
    int status_tax = 0;
    String kode_transaksi, diskon_transaksi;
    double total_belanja = 0.0, tax = 0.0;
    File imgfile;
    FileInputStream fis;
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    @FXML
    private AnchorPane detail1;
    @FXML
    private AnchorPane detail2;
    @FXML
    private AnchorPane detail3;
    @FXML
    private Label ljumlah1;
    @FXML
    private Button bbatal;
    @FXML
    private ScrollPane spmeja;
    @FXML
    private ScrollPane spmenu;
    int pajak = Integer.parseInt(fh.getinfo().split(";")[5]);
    @FXML
    private TextField tcari;
    @FXML
    private TableColumn<Entity, String> diskon;
    @FXML
    private ComboBox<?> cdiskon_transaksi;
    @FXML
    private TableColumn<Entity, String> tanggal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sh.setStatus_meja(0);
        kembali();
        gantikategorimeja();
        clearmeja();
        gantikategorimenu();
        carimenu();
        clearmenu();
        gantislot();
        select();
        hapus();
        bayar();
        pindahmeja();
        simpan();
        cetakbill();
        popupmenu();
        batalkanpesanan();
        gantidiskon();
        loaddiskontransaksi();
        ctipemeja.getEditor().setDisable(true);
        ckategorimenu.getEditor().setDisable(true);
        cslot.getEditor().setDisable(true);
        luser.setText(sh.getUsername());
        bbayar.setId("bc");
        border.setId("bc");
        bcetakbil.setId("bc");
        bpindah.setId("bc");
        bclear.setId("bc");
        bclear2.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        detail1.setId("tema");
        detail2.setId("tema");
        detail3.setId("tema");
        bbatal.setId("bc");
        //pkembali.setId("pane");
        spmenu.setFitToWidth(true);
        spmeja.setFitToWidth(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    loadcombomeja();
                    loadcombomenu();
                    loadcomboslot();
                    loadmenu();
                    loadmeja();
                } catch (InterruptedException ex) {
                    Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                }
            }
        });
        if (sh.getTipe().equals("waiters")) {
            bcetakbil.setDisable(true);
            bbayar.setDisable(true);
        }
    }

    private void loaddiskontransaksi() {
        ObservableList olstransdiskon = FXCollections.observableArrayList();
        String datatransdiskon = fh.getdiskon();
        for (int i = 0; i < datatransdiskon.split(";").length; i++) {
            olstransdiskon.add(datatransdiskon.split(";")[i]);
        }
        cdiskon_transaksi.setItems(olstransdiskon);
    }

    private void kembali() {
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    tableview.getItems().clear();
                    btl.clear();
                    btlmenu.clear();
                    datamenu.clear();
                    datameja.clear();
                    olsmeja.clear();
                    olsmenu.clear();
                    olsslot.clear();
                    fis.close();
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);

                } catch (IOException ex) {
                    Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                }
            }
        });
    }

    private void loadcombomeja() {
        try {
            String sql = "SELECT kode_kategori_meja,nama_kategori_meja FROM kategori_meja "
                    + "ORDER BY kode_kategori_meja DESC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                olsmeja.add(res.getString("kode_kategori_meja") + "-" + res.getString("nama_kategori_meja"));
            }
            pre.close();
            res.close();
            ch.close();
            ctipemeja.setItems(olsmeja);
        } catch (SQLException ex) {
            Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
        ctipemeja.setItems(olsmeja);
    }

    private void loadcombomenu() {

        try {
            String sql = "SELECT kode_kategori_menu,nama_kategori_menu FROM kategori_menu "
                    + "ORDER BY kode_kategori_menu DESC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                olsmenu.add(res.getString("kode_kategori_menu") + "-" + res.getString("nama_kategori_menu"));
            }
            pre.close();
            res.close();
            ch.close();
            ckategorimenu.setItems(olsmenu);
        } catch (SQLException ex) {
            Logger.getLogger(PembelianController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
        ckategorimenu.setItems(olsmenu);
    }

    private void loadcomboslot() {

        String[] dataslot = fh.getslot().split(";");
        for (int i = 0; i < dataslot.length; i++) {
            olsslot.add(dataslot[i]);
        }
        cslot.setItems(olsslot);

    }

    public void setterkodetransaksi(String kode_meja, String slot) {
        try {
            String sqlgetno = "SELECT kode_transaksi FROM transaksi WHERE kode_meja=? AND slot=? AND status=0 "
                    + "ORDER BY id ASC LIMIT 1";
            PreparedStatement pregetno = ch.connect().prepareStatement(sqlgetno);
            pregetno.setString(1, kode_meja);
            pregetno.setString(2, slot);
            ResultSet resno = pregetno.executeQuery();
            while (resno.next()) {
                kode_transaksi = String.valueOf(resno.getString("kode_transaksi"));
            }
            pregetno.close();
            resno.close();
            ch.close();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private EventHandler actionmeja(int y) {
        EventHandler evt = new EventHandler() {
            @Override
            public void handle(Event event) {
                loadmeja();
                btl.get(y).setId("button-meja-pilih");
                sh.setKode_meja(datameja.get(y).kode);
                kategorimeja = datameja.get(y).kategori;
                namameja = datameja.get(y).nama;
                statusmeja = datameja.get(y).status;
                sh.setStatus_meja(1);
                cslot.getEditor().setText("Slot 1");
                //setterkodetransaksi(datameja.get(y).kode, cslot.getEditor().getText());
                diskon_transaksi = "0";
                loaddatatransaksi();
                cdiskon_transaksi.getEditor().setText(diskon_transaksi);

            }
        };

        return evt;

    }

    private EventHandler actionmejadetail(int y) {
        EventHandler evt = new EventHandler() {
            @Override
            public void handle(Event event) {

                loadmejadetailraw(ctipemeja.getEditor().getText());
                btl.get(y).setId("button-meja-pilih");
                sh.setKode_meja(datameja.get(y).kode);
                kategorimeja = datameja.get(y).kategori;
                namameja = datameja.get(y).nama;
                statusmeja = datameja.get(y).status;
                sh.setStatus_meja(1);
                cslot.getEditor().setText("Slot 1");
                //setterkodetransaksi(datameja.get(y).kode, cslot.getEditor().getText());
                diskon_transaksi = "0";
                loaddatatransaksi();
                cdiskon_transaksi.getEditor().setText(diskon_transaksi);

            }
        };

        return evt;

    }

    private void loadmeja() {
        try {
            datameja.clear();
            btl.clear();
            fpmeja.getChildren().clear();
            String sql = "SELECT kode,nama,kategori,status FROM meja ORDER BY kode ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode = res.getString("kode");
                String nama = res.getString("nama");
                String kategori = res.getString("kategori");
                String status = res.getString("status");
                datameja.add(new Entitymeja(kode, nama, kategori, status));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < datameja.size(); i++) {
                final int y = i;
                Button bt = new Button(datameja.get(i).nama);
                int statusmeja = Integer.parseInt(datameja.get(i).status);
                bt.setPrefWidth(70);
                bt.setWrapText(true);
                bt.setAlignment(Pos.CENTER);
                bt.setContentDisplay(ContentDisplay.TOP);
                btl.add(bt);
                btl.get(i).setPadding(new Insets(3));
                if (statusmeja == 0) {
                    btl.get(i).setId("button-meja");
                } else {
                    btl.get(i).setId("button-meja-dead");
                }
                //btl.get(i).setId("button-meja");
                btl.get(i).setOnAction(actionmeja(y));
            }
            fpmeja.getChildren().addAll(btl);
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void loadmejadetailraw(String key) {
        try {
            datameja.clear();
            btl.clear();
            fpmeja.getChildren().clear();
            String sql = "SELECT kode,nama,kategori,status FROM meja WHERE kategori = ? ORDER BY kode ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            if (key == null) {
                pre.setString(1, "");
            } else {
                pre.setString(1, key.split("-")[0]);
            }
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode = res.getString("kode");
                String nama = res.getString("nama");
                String kategori = res.getString("kategori");
                String status = res.getString("status");
                datameja.add(new Entitymeja(kode, nama, kategori, status));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < datameja.size(); i++) {
                final int y = i;
                Button bt = new Button(datameja.get(i).nama);
                int statusmeja = Integer.parseInt(datameja.get(i).status);
                bt.setPrefWidth(70);
                bt.setWrapText(true);
                bt.setAlignment(Pos.CENTER);
                bt.setContentDisplay(ContentDisplay.TOP);
                btl.add(bt);
                btl.get(i).setPadding(new Insets(3));
                if (statusmeja == 0) {
                    btl.get(i).setId("button-meja");
                } else {
                    btl.get(i).setId("button-meja-dead");
                }
                btl.get(i).setOnAction(actionmejadetail(y));
            }
            fpmeja.getChildren().addAll(btl);
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void gantikategorimeja() {
        ctipemeja.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadmejadetailraw(newValue);
            }
        });
    }

    private void rawclear() {
        ctipemeja.getEditor().clear();
        sh.setKode_meja("");
        sh.setStatus_meja(0);
        cslot.getEditor().clear();
        ctipemeja.getSelectionModel().clearSelection();
        loadmeja();
        loaddatatransaksi();
        kategorimeja = "";
        namameja = "";
        cdiskon_transaksi.getEditor().clear();
    }

    private void rawclear2() {
        //ctipemeja.getEditor().clear();
        //sh.setKode_meja("");
        //sh.setStatus_meja(0);
        //cslot.getEditor().clear();
        //ctipemeja.getSelectionModel().clearSelection();
        //loadmeja();
        loaddatatransaksi();
        //kategorimeja = "";
        //namameja = "";
    }

    private void clearmeja() {
        bclear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //ctipemeja.getSelectionModel().clearSelection();
                //cslot.getSelectionModel().clearSelection();
                rawclear();
            }
        });
    }

    private EventHandler actionmenu(int y) {
        EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int status = sh.getStatus_meja();
                if (status == 0) {
                    oh.gagal("Maaf anda belum memilih meja");
                } else if (cslot.getEditor().getText().isEmpty()) {
                    oh.gagal("Maaf anda belum memilih slot");
                } else {
                    try {
                        String sqlcount = "SELECT COUNT(kode_menu) AS jumlah FROM transaksi WHERE "
                                + "kode_menu=? AND kode_meja=? AND status =0 AND slot=?";
                        PreparedStatement precount = ch.connect().prepareStatement(sqlcount);
                        precount.setString(1, datamenu.get(y).kode);
                        precount.setString(2, sh.getKode_meja());
                        precount.setString(3, cslot.getEditor().getText());
                        int countdata = 0;
                        ResultSet res = precount.executeQuery();
                        while (res.next()) {
                            countdata = res.getInt("jumlah");
                        }
                        precount.close();
                        res.close();
                        ch.close();
                        if (countdata > 0) {
                            String sqlupdate = "UPDATE transaksi SET jumlah=jumlah+1,status_cetak=status_cetak+1 WHERE "
                                    + "kode_menu=? AND kode_meja=? AND status=0 AND slot=?";
                            PreparedStatement preupdate = ch.connect().prepareStatement(sqlupdate);
                            preupdate.setString(1, datamenu.get(y).kode);
                            preupdate.setString(2, sh.getKode_meja());
                            preupdate.setString(3, cslot.getEditor().getText());
                            preupdate.executeUpdate();
                            preupdate.close();
                            ch.close();
                        } else {
                            String sqlinsert = "INSERT INTO transaksi(kode_menu,harga_masing,jumlah,"
                                    + "kode_meja,slot,kode_user,diskon,status_cetak) "
                                    + "VALUES (?,?,?,?,?,?,0,1);"
                                    + "UPDATE meja SET status=1 WHERE kode=?";
                            PreparedStatement pre = ch.connect().prepareStatement(sqlinsert);
                            pre.setString(1, datamenu.get(y).getKode());
                            pre.setDouble(2, Double.parseDouble(datamenu.get(y).getHarga()));
                            pre.setInt(3, 1);
                            pre.setString(4, sh.getKode_meja());
                            pre.setString(5, cslot.getEditor().getText());
                            pre.setString(6, sh.getKode_user());
                            pre.setString(7, sh.getKode_meja());
                            pre.executeUpdate();
                            pre.close();
                            ch.close();
                        }

                        PreparedStatement preconnect = ch.connect().prepareStatement("SELECT COUNT(*) as total FROM "
                                + "pg_stat_activity");
                        ResultSet resconnect = preconnect.executeQuery();
                        while (resconnect.next()) {
                            System.out.println(resconnect.getString("total"));
                        }
                        preconnect.close();
                        resconnect.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                        oh.error(ex);
                    } finally {
                        ch.close();
                    }
                    loaddatatransaksi();

                }

            }
        };

        return handler;

    }

    private void loadmenu() {
        try {
            datamenu.clear();
            btlmenu.clear();
            fpmenu.getChildren().clear();
            String sql = "SELECT kode,nama,kategori,harga,gambar FROM menu ORDER BY kode DESC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode = res.getString("kode");
                String nama = res.getString("nama");
                String harga = res.getString("harga");
                String kategori = res.getString("kategori");
                String gambar = res.getString("gambar");
                datamenu.add(new Entitymenu(kode, nama, harga, kategori, gambar));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < datamenu.size(); i++) {
                final int y = i;
                ImageView imv = new ImageView();
                //imgfile=new File("image/" + datamenu.get(i).gambar);
                fis = new FileInputStream(new File("image/" + datamenu.get(i).gambar));
                Image img = new Image(fis);
                imv.setImage(img);
                imv.setFitHeight(100);
                imv.setFitWidth(100);
                Button bt = new Button(datamenu.get(i).nama, imv);
                bt.setPrefWidth(130);
                bt.setPrefHeight(150);
                bt.setWrapText(true);
                bt.setAlignment(Pos.TOP_CENTER);
                bt.setContentDisplay(ContentDisplay.TOP);
                btlmenu.add(bt);
                btlmenu.get(i).setPadding(new Insets(3));
                btlmenu.get(i).setId("button-menu");
                btlmenu.get(i).setOnAction(actionmenu(i));

            }
            fpmenu.getChildren().addAll(btlmenu);
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void loadmenudetailraw(String key) {
        try {
            datamenu.clear();
            btlmenu.clear();
            fpmenu.getChildren().clear();
            String sql = "SELECT kode,nama,kategori,harga,gambar FROM menu WHERE kategori=? ORDER BY kode DESC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            if (key == null) {
                pre.setString(1, "");
            } else {
                pre.setString(1, key.split("-")[0]);
            }
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode = res.getString("kode");
                String nama = res.getString("nama");
                String harga = res.getString("harga");
                String kategori = res.getString("kategori");
                String gambar = res.getString("gambar");
                datamenu.add(new Entitymenu(kode, nama, harga, kategori, gambar));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < datamenu.size(); i++) {
                ImageView imv = new ImageView();
                Image img = new Image(new FileInputStream(new File("image/" + datamenu.get(i).gambar)));
                imv.setImage(img);
                imv.setFitHeight(100);
                imv.setFitWidth(100);
                Button bt = new Button(datamenu.get(i).nama, imv);
                bt.setPrefWidth(130);
                bt.setPrefHeight(150);
                bt.setWrapText(true);
                bt.setAlignment(Pos.CENTER);
                bt.setContentDisplay(ContentDisplay.TOP);
                btlmenu.add(bt);
                btlmenu.get(i).setPadding(new Insets(3));
                btlmenu.get(i).setId("button-menu");
                btlmenu.get(i).setOnAction(actionmenu(i));
            }
            fpmenu.getChildren().addAll(btlmenu);
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void gantikategorimenu() {
        ckategorimenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadmenudetailraw(newValue);
            }
        });
    }

    private void loadcarimenuraw(String key) {
        try {
            datamenu.clear();
            btlmenu.clear();
            fpmenu.getChildren().clear();
            String sql = "SELECT kode,nama,kategori,harga,gambar FROM menu WHERE nama ILIKE ? ORDER BY kode DESC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, key);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode = res.getString("kode");
                String nama = res.getString("nama");
                String harga = res.getString("harga");
                String kategori = res.getString("kategori");
                String gambar = res.getString("gambar");
                datamenu.add(new Entitymenu(kode, nama, harga, kategori, gambar));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < datamenu.size(); i++) {
                ImageView imv = new ImageView();
                Image img = new Image(new FileInputStream(new File("image/" + datamenu.get(i).gambar)));
                imv.setImage(img);
                imv.setFitHeight(100);
                imv.setFitWidth(100);
                Button bt = new Button(datamenu.get(i).nama, imv);
                bt.setPrefWidth(130);
                bt.setPrefHeight(150);
                bt.setWrapText(true);
                bt.setAlignment(Pos.CENTER);
                bt.setContentDisplay(ContentDisplay.TOP);
                btlmenu.add(bt);
                btlmenu.get(i).setPadding(new Insets(3));
                btlmenu.get(i).setId("button-menu");
                btlmenu.get(i).setOnAction(actionmenu(i));
            }
            fpmenu.getChildren().addAll(btlmenu);
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void carimenu() {
        tcari.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    loadcarimenuraw("%" + tcari.getText() + "%");
                }
            }
        });
    }

    private void clearmenu() {
        bclear2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ckategorimenu.getSelectionModel().clearSelection();
                tcari.clear();
                loadmenu();

            }
        });
    }

    private void loaddatatransaksi() {
        olstable.clear();
        tableview.getItems().clear();
        try {
            String sql = "SELECT t.id,t.tanggal::date,m.nama,t.jumlah,t.diskon,t.tax,t.statustax,t.kode_transaksi,t.diskon_transaksi,"
                    + "(t.harga_masing*t.jumlah-(diskon/100*(t.harga_masing*t.jumlah))) as harga FROM transaksi t INNER JOIN "
                    + "menu m on t.kode_menu=m.kode WHERE t.status=0 AND t.kode_meja=? AND t.slot=? ORDER BY id DESC ";

            /*String sql = "SELECT t.kode_menu,MAX(t.id) AS id,MAX(t.tanggal::date) AS tanggal,m.nama,"
                    + "SUM(t.jumlah) AS jumlah,MAX(t.diskon) AS diskon,MAX(t.tax) AS tax,MAX(t.statustax) AS statustax,"
                    + "MAX(t.kode_transaksi) AS kode_transaksi,MAX(t.diskon_transaksi) AS diskon_transaksi,"
                    + "(MAX(t.harga_masing)*SUM(t.jumlah)-(MAX(diskon)/100*(MAX(t.harga_masing)*SUM(t.jumlah))))"
                    + " as harga FROM transaksi t INNER JOIN "
                    + "menu m on t.kode_menu=m.kode WHERE t.status=0 AND t.kode_meja=? AND t.slot=?"
                    + "GROUP BY t.kode_menu,m.nama,jumlah ";*/
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, sh.getKode_meja());
            pre.setString(2, cslot.getEditor().getText());
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String id = res.getString("id");
                String nama = res.getString("nama");
                String jumlah = res.getString("jumlah");
                String harga = nf.format(res.getDouble("harga"));
                String diskonmenu = res.getString("diskon");
                String tanggal = "";
                try {
                    Date datefromdb = new SimpleDateFormat("yyyy-MM-dd").parse(res.getString("tanggal"));
                    tanggal = new SimpleDateFormat("dd/MM/yy").format(datefromdb);
                } catch (ParseException ex) {
                    Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                }
                tax = res.getDouble("tax");
                status_tax = res.getInt("statustax");
                kode_transaksi = res.getString("kode_transaksi");
                diskon_transaksi = res.getString("diskon_transaksi");
                olstable.add(new Entity(id, nama, jumlah, harga, diskonmenu, tanggal));
            }
            pre.close();
            res.close();
            ch.close();
            //id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
            menu.setCellValueFactory(new PropertyValueFactory<>("menu"));
            qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
            diskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));
            tableview.setItems(olstable);
            total_belanja = 0;
            for (int i = 0; i < olstable.size(); i++) {
                total_belanja = total_belanja + Double.parseDouble(harga.getCellData(i).replaceAll("[.,]", ""));
            }
            double truediskon = 0;
            if (diskon_transaksi == null || diskon_transaksi.equals("")) {
                truediskon = 0;
            } else {
                truediskon = Double.parseDouble(diskon_transaksi);
            }
            total_belanja = total_belanja - (truediskon / 100 * (total_belanja)) + (tax / 100 * (total_belanja - (truediskon / 100 * (total_belanja))));
            ljumlah.setText("Rp. " + nf.format(total_belanja));
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ch.close();
        }

    }

    private void gantislot() {
        cslot.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int statusmeja = sh.getStatus_meja();
                if (statusmeja == 0) {
                    oh.gagal("Mohon Pilih meja terlebih dulu");
                } else {
                    total_belanja = 0;
                    setterkodetransaksi(sh.getKode_meja(), newValue);
                    loaddatatransaksi();
                }
            }
        });
    }

    private void select() {
        tableview.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int i = newValue.intValue();
                if (i < 0) {
                    System.out.println("out");
                } else {
                    ids = olstable.get(i).id;
                    System.out.println(ids);
                }

            }
        });
    }

    private void rawhapus() {
        if (oh.konfirmasi("hapus") == true) {
            try {
                String sql = "DELETE FROM transaksi WHERE id=?";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setInt(1, Integer.parseInt(ids));
                pre.executeUpdate();
                oh.sukses("Data Berhasil Dihapus");
                loaddatatransaksi();
                pre.close();
                ch.close();
            } catch (SQLException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                oh.error(ex);
            } finally {
                ch.close();
            }

        }
    }

    private void hapus() {
        tableview.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    rawhapus();
                }
            }
        });
    }

    private void pindahmeja() {
        bpindah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (sh.getStatus_meja() == 0 || cslot.getEditor().getText().equals("") || statusmeja.equals("0")) {
                    oh.gagal("Maaf anda harus memilih meja dan slot yang terisi lebih dulu");
                } else if (cekpesananbelumdisimpan() > 0) {
                    oh.gagal("Maaf anda belum menyimpan data");
                } else {
                    try {
                        System.out.println(kode_transaksi);
                        PindahmejaController.kode_transaksi = kode_transaksi;
                        sh.setSlot(cslot.getEditor().getText());
                        Stage st = new Stage(StageStyle.UTILITY);
                        st.setTitle("Pindah Meja");
                        st.initModality(Modality.APPLICATION_MODAL);
                        Parent root = (AnchorPane) FXMLLoader.load(getClass().getResource("/meteorresto/view/Pindahmeja.fxml"));
                        Scene sc = new Scene(root);
                        st.setScene(sc);
                        String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                        st.getScene().getStylesheets().clear();
                        st.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                        st.showAndWait();
                        rawclear();
                    } catch (IOException ex) {
                        Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private int cekpesananbelumdisimpan() {
        int jumlah = 0;
        try {
            String sql = "SELECT COUNT(*) AS jumlah FROM transaksi WHERE kode_meja=? "
                    + "AND slot=? AND status = 0 AND kode_transaksi IS NULL ;";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, sh.getKode_meja());
            pre.setString(2, cslot.getEditor().getText());
            ResultSet res = pre.executeQuery();
            res.next();
            jumlah = res.getInt("jumlah");
            System.out.println(jumlah);
            res.close();
            pre.close();
            ch.close();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
        return jumlah;
    }

    private void simpan() {
        border.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (sh.getStatus_meja() == 0 || cslot.getEditor().getText().equals("")) {
                    oh.gagal("Maaf anda harus memilih meja dan slot lebih dulu");
                } else {
                    try {
                        double ddiskontrans = 0;
                        if (cdiskon_transaksi.getEditor().getText().equals("")) {
                            ddiskontrans = 0;
                        } else {
                            ddiskontrans = Double.parseDouble(cdiskon_transaksi.getEditor().getText());
                        }
                        if (kode_transaksi == null || kode_transaksi.equals("")) {
                            String sql = "UPDATE transaksi SET kode_transaksi=?"
                                    + "WHERE kode_meja=? AND slot=? AND status=0 AND kode_transaksi IS NULL ;"
                                    + "UPDATE transaksi SET tax=?,statustax=1,diskon_transaksi=? WHERE kode_transaksi=?";
                            PreparedStatement pre = ch.connect().prepareStatement(sql);
                            String kode_transaksil = setnotransaksi();
                            pre.setString(1, kode_transaksil);
                            pre.setString(2, sh.getKode_meja());
                            pre.setString(3, cslot.getEditor().getText());
                            pre.setInt(4, pajak);
                            pre.setDouble(5, ddiskontrans);
                            pre.setString(6, kode_transaksil);
                            pre.executeUpdate();
                            pre.close();
                        } else {
                            String sql = "UPDATE transaksi SET kode_transaksi=? WHERE kode_meja=? "
                                    + "AND slot=? AND status = 0 AND kode_transaksi IS NULL;"
                                    + "UPDATE transaksi SET tax=?,statustax=1,diskon_transaksi=? WHERE kode_transaksi = ? ";
                            PreparedStatement pre = ch.connect().prepareStatement(sql);
                            pre.setString(1, kode_transaksi);
                            pre.setString(2, sh.getKode_meja());
                            pre.setString(3, cslot.getEditor().getText());
                            pre.setInt(4, pajak);
                            pre.setDouble(5, ddiskontrans);
                            pre.setString(6, kode_transaksi);
                            pre.executeUpdate();
                            pre.close();
                        }

                        loaddatatransaksi();
                        if (oh.konfirmasi("Cetak") == true) {
                            try {
                                String[] info = fh.getinfo().split(";");
                                HashMap hash = new HashMap(10);
                                hash.put("kode_meja", sh.getKode_meja());
                                hash.put("slot", cslot.getEditor().getText());
                                hash.put("kode_kasir", sh.getKode_user());
                                hash.put("nama_kasir", sh.getUsername());
                                hash.put("kategori_meja", kategorimeja);
                                hash.put("nama_meja", namameja);
                                hash.put("perusahaan", info[0]);
                                hash.put("alamat", info[1]);
                                hash.put("nohp", info[3]);
                                hash.put("kode_transaksi", kode_transaksi);
                                JasperReport jr = (JasperReport) JRLoader.loadObject(new File("laporan/" + info[7]));
                                JasperPrint jp = JasperFillManager.fillReport(jr, hash, ch.connect());
                                JasperPrintManager.printReport(jp, false);
                                ch.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                oh.gagal("Tidak ada data yang bisa dicetak");
                            } finally {
                                ch.close();
                            }

                        }
                        String sqlupdatecetak = "UPDATE transaksi SET status_cetak = 0 "
                                + "WHERE kode_transaksi=?";
                        PreparedStatement preupdatestatuscetak = ch.connect().prepareStatement(sqlupdatecetak);
                        preupdatestatuscetak.setString(1, kode_transaksi);
                        preupdatestatuscetak.executeUpdate();
                        ch.close();
                        rawclear();

                    } catch (SQLException ex) {
                        Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                        oh.error(ex);
                    }
                }

            }
        });
    }

    private void cetakbillraw() {
        if (sh.getStatus_meja() == 0 || cslot.getEditor().getText().equals("") || statusmeja.equals("0")) {
            oh.gagal("Maaf anda harus memilih meja dan slot yang terisi lebih dulu");
        } else {
            try {
                if (cekpesananbelumdisimpan() > 0) {
                    oh.gagal("Anda belum menyimpan data");

                    /*ObservableList<String> olstransdiskon = FXCollections.observableArrayList();
                            String datatransdiskon = fh.getdiskon();
                            for (int i = 0; i < datatransdiskon.split(";").length; i++) {
                                olstransdiskon.add(datatransdiskon.split(";")[i]);
                            }
                            ChoiceDialog<String> cd = new ChoiceDialog<>("0", olstransdiskon);
                            cd.setTitle("Konfirmasi");
                            cd.setHeaderText("Pilih jumlah diskon yang dipakai");
                            cd.setContentText("Diskon : ");
                            Optional<String> res = cd.showAndWait();
                            if (res.isPresent()) {
                                String sql = "UPDATE transaksi SET kode_transaksi=?"
                                        + "WHERE kode_meja=? AND slot=? AND kode_transaksi IS NULL ;"
                                        + "UPDATE transaksi SET tax=?,statustax=1,diskon_transaksi=? WHERE kode_transaksi=?";
                                PreparedStatement pre = ch.connect().prepareStatement(sql);
                                String kode_transaksil = setnotransaksi();
                                pre.setString(1, kode_transaksil);
                                pre.setString(2, sh.getKode_meja());
                                pre.setString(3, cslot.getEditor().getText());
                                pre.setInt(4, pajak);
                                pre.setDouble(5, Double.parseDouble(res.get()));
                                pre.setString(6, kode_transaksil);
                                pre.executeUpdate();
                                pre.close();

                                String[] info = fh.getinfo().split(";");
                                HashMap hash = new HashMap(9);
                                hash.put("kode_meja", sh.getKode_meja());
                                hash.put("slot", cslot.getEditor().getText());
                                hash.put("kode_kasir", sh.getKode_user());
                                hash.put("nama_kasir", sh.getUsername());
                                hash.put("kategori_meja", kategorimeja);
                                hash.put("nama_meja", namameja);
                                hash.put("perusahaan", info[0]);
                                hash.put("alamat", info[1]);
                                hash.put("nohp", info[3]);
                                JasperReport jr = (JasperReport) JRLoader.loadObject(new File("laporan/Struckkasir.jasper"));
                                JasperPrint jp = JasperFillManager.fillReport(jr, hash, ch.connect());
                                JasperPrintManager.printReport(jp, false);
                                ch.close();
                            }String sql = "UPDATE transaksi SET kode_transaksi=?"
                                    + "WHERE kode_meja=? AND slot=? AND kode_transaksi IS NULL ;"
                                    + "UPDATE transaksi SET tax=?,statustax=1 WHERE kode_transaksi=?";
                            PreparedStatement pre = ch.connect().prepareStatement(sql);
                            String kode_transaksil = setnotransaksi();
                            pre.setString(1, kode_transaksil);
                            pre.setString(2, sh.getKode_meja());
                            pre.setString(3, cslot.getEditor().getText());
                            pre.setInt(4, pajak);
                            pre.setString(5, kode_transaksil);
                            pre.executeUpdate();
                            pre.close();
                            String[] info = fh.getinfo().split(";");
                            HashMap hash = new HashMap(9);
                            hash.put("kode_meja", sh.getKode_meja());
                            hash.put("slot", cslot.getEditor().getText());
                            hash.put("kode_kasir", sh.getKode_user());
                            hash.put("nama_kasir", sh.getUsername());
                            hash.put("kategori_meja", kategorimeja);
                            hash.put("nama_meja", namameja);
                            hash.put("perusahaan", info[0]);
                            hash.put("alamat", info[1]);
                            hash.put("nohp", info[3]);
                            JasperReport jr = (JasperReport) JRLoader.loadObject(new File("laporan/Struckkasir.jasper"));
                            JasperPrint jp = JasperFillManager.fillReport(jr, hash, ch.connect());
                            JasperPrintManager.printReport(jp, false);
                            ch.close();*/
                } else {
                    String[] info = fh.getinfo().split(";");
                    HashMap hash = new HashMap(10);
                    hash.put("kode_meja", sh.getKode_meja());
                    hash.put("slot", cslot.getEditor().getText());
                    hash.put("kode_kasir", sh.getKode_user());
                    hash.put("nama_kasir", sh.getUsername());
                    hash.put("kategori_meja", kategorimeja);
                    hash.put("nama_meja", namameja);
                    hash.put("perusahaan", info[0]);
                    hash.put("alamat", info[1]);
                    hash.put("nohp", info[3]);
                    hash.put("kode_transaksi", kode_transaksi);
                    JasperReport jr = (JasperReport) JRLoader.loadObject(new File("laporan/Struckkasir.jasper"));
                    JasperPrint jp = JasperFillManager.fillReport(jr, hash, ch.connect());
                    JasperPrintManager.printReport(jp, false);
                    ch.close();
                    String sqlupdatecetak = "UPDATE transaksi SET status_cetak = 0 "
                            + "WHERE kode_transaksi=?";
                    PreparedStatement preupdatestatuscetak = ch.connect().prepareStatement(sqlupdatecetak);
                    preupdatestatuscetak.setString(1, kode_transaksi);
                    preupdatestatuscetak.executeUpdate();
                    ch.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                oh.error(ex);
            } finally {
                ch.close();
            }

            rawclear2();

        }

    }

    public String setnotransaksi() {
        String setkode = "";
        try {
            String sqlgetno = "SELECT kode_transaksi FROM transaksi WHERE kode_transaksi "
                    + "IS NOT NULL ORDER BY kode_transaksi::integer DESC LIMIT 1";
            PreparedStatement pregetno = ch.connect().prepareStatement(sqlgetno);
            ResultSet resno = pregetno.executeQuery();
            String tglhariini = new SimpleDateFormat("yyMMdd").format(new Date());
            while (resno.next()) {
                setkode = resno.getString("kode_transaksi");
            }
            //System.out.println(setkode.substring(0, 6));
            if (setkode == null || setkode.equals("")) {
                setkode = tglhariini + "0001";
            } else if (setkode.substring(0, 6).equals(tglhariini)) {
                setkode = String.valueOf(Integer.parseInt(setkode) + 1);
            } else {
                setkode = tglhariini + "0001";
                /*if(setkode.length()==1){
                    setkode="000"+setkode;
                }else if(setkode.length()==2){
                    setkode="00"+setkode;
                }else if(setkode.length()==3){
                    setkode="0"+setkode;
                }*/

            }
            pregetno.close();
            resno.close();
            ch.close();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ch.close();
        }
        return setkode;
    }

    private void cetakbill() {
        bcetakbil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                cetakbillraw();
            }
        });
    }

    private void bayar() {
        bbayar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (sh.getStatus_meja() == 0 || cslot.getEditor().getText().equals("") || statusmeja.equals("0")) {
                    oh.gagal("Maaf anda harus memilih meja dan slot yang terisi lebih dulu");
                } else if (cekpesananbelumdisimpan() > 0) {
                    oh.gagal("Maaf anda belum menyimpan pesanan");
                } else {
                    try {
                        sh.setTotal_bayar(total_belanja);
                        sh.setSlot(cslot.getEditor().getText());
                        sh.setTax(tax);
                        BayarController.kode_transaksi = kode_transaksi;
                        BayarController.namameja = namameja;
                        BayarController.kategorimeja = kategorimeja;
                        Stage st = new Stage(StageStyle.UTILITY);
                        st.setTitle("Pembayaran");
                        st.getIcons().add(new Image(getClass().getResource("/meteorresto/icon/favico.png").toString()));
                        st.initModality(Modality.APPLICATION_MODAL);
                        Parent root = (AnchorPane) FXMLLoader.load(getClass().getResource("/meteorresto/view/Bayar.fxml"));
                        Scene sc = new Scene(root);
                        st.setScene(sc);
                        String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                        st.getScene().getStylesheets().clear();
                        st.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                        st.showAndWait();
                        rawclear();
                    } catch (IOException ex) {
                        Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void popupmenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem hapus = new MenuItem("Hapus");
        MenuItem kurangi = new MenuItem("Kurangi");
        MenuItem tambah = new MenuItem("Tambah");
        cm.getItems().addAll(hapus, kurangi, tambah);
        hapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rawhapus();
            }
        });

        kurangi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String sql = "DELETE FROM transaksi WHERE jumlah=1 AND id=?; "
                            + " UPDATE transaksi SET jumlah=jumlah - 1,status_cetak=status_cetak-1 WHERE id=?";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setInt(1, Integer.parseInt(ids));
                    pre.setInt(2, Integer.parseInt(ids));
                    pre.executeUpdate();
                    loaddatatransaksi();
                    pre.close();
                    ch.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } finally {
                    ch.close();
                }
            }
        });

        tambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String sql = "UPDATE transaksi SET jumlah=jumlah + 1, status_cetak=status_cetak+1 WHERE id=?";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setInt(1, Integer.parseInt(ids));
                    pre.executeUpdate();
                    loaddatatransaksi();
                    pre.close();
                    ch.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } finally {
                    ch.close();
                }
            }
        });

        tableview.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    cm.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

    private void batalkanpesanan() {
        bbatal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (sh.getStatus_meja() == 0 || cslot.getEditor().getText().equals("") || statusmeja.equals("0")) {
                    oh.gagal("Maaf anda harus memilih meja dan slot yag terisi lebih dulu");
                } else {
                    if (kode_transaksi == null || kode_transaksi.equals("")) {
                        if (oh.konfirmasi("hapus") == true) {
                            try {
                                String sql = "DELETE FROM transaksi WHERE kode_meja=? AND slot=? AND status=0; "
                                        + " UPDATE meja SET status=0 WHERE kode=?";
                                PreparedStatement pre = ch.connect().prepareStatement(sql);
                                pre.setString(1, sh.getKode_meja());
                                pre.setString(2, cslot.getEditor().getText());
                                pre.setString(3, sh.getKode_meja());
                                pre.executeUpdate();
                                loaddatatransaksi();
                                pre.close();
                                ch.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                                oh.error(ex);
                            } finally {
                                ch.close();
                            }
                            rawclear();
                        }
                    } else {
                        TextInputDialog tid = new TextInputDialog();
                        tid.setTitle("Konfirmasi");
                        tid.setHeaderText("Konfirmasi Password ADMIN");
                        tid.setContentText("Admin Password");
                        Optional<String> result = tid.showAndWait();
                        if (result.isPresent()) {
                            try {
                                String sql = "SELECT COUNT(id) AS total FROM akun WHERE "
                                        + " password=? AND tipe = ? LIMIT 1";
                                PreparedStatement pre = ch.connect().prepareStatement(sql);
                                pre.setString(1, result.get());
                                pre.setString(2, "admin");
                                ResultSet res = pre.executeQuery();
                                res.next();
                                int jumlah = res.getInt("total");
                                pre.close();
                                res.close();
                                ch.close();
                                if (jumlah == 0) {
                                    oh.gagal("Maaf Username / Password Salah");
                                } else {
                                    try {
                                        String sqlvoid = "DELETE FROM transaksi WHERE kode_meja=? AND slot=? AND kode_transaksi IS NULL;"
                                                + "UPDATE transaksi SET status = 2,kode_user2=? WHERE kode_transaksi=?;"
                                                + "UPDATE meja SET status=0 WHERE kode=?";
                                        PreparedStatement prevoid = ch.connect().prepareStatement(sqlvoid);
                                        prevoid.setString(1, sh.getKode_meja());
                                        prevoid.setString(2, cslot.getEditor().getText());
                                        prevoid.setString(3, sh.getKode_user());
                                        prevoid.setString(4, kode_transaksi);
                                        prevoid.setString(5, sh.getKode_meja());
                                        prevoid.executeUpdate();
                                        loaddatatransaksi();
                                        prevoid.close();
                                        ch.close();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                                        oh.error(ex);
                                    } finally {
                                        ch.close();
                                    }
                                    rawclear();
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                                oh.error(ex);
                            }
                        }
                    }

                }

            }
        });
    }

    private void gantidiskon() {
        ObservableList olsdiskon = FXCollections.observableArrayList();
        String datadiskon = fh.getdiskon();
        for (int i = 0; i < datadiskon.split(";").length; i++) {
            olsdiskon.add(datadiskon.split(";")[i]);
        }
        diskon.setCellFactory(ComboBoxTableCell.forTableColumn(olsdiskon));
        diskon.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Entity, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Entity, String> event) {
                String sql = "UPDATE transaksi SET diskon=? WHERE id=?";
                PreparedStatement pre;
                try {
                    pre = ch.connect().prepareStatement(sql);
                    pre.setDouble(1, Double.parseDouble(event.getNewValue()));
                    pre.setInt(2, Integer.parseInt(ids));
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    loaddatatransaksi();
                } catch (SQLException ex) {
                    Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    ch.close();
                }

            }
        });
    }

    public class Entitymeja {

        String kode, nama, kategori, status;

        public Entitymeja(String kode, String nama, String kategori, String status) {
            this.kode = kode;
            this.nama = nama;
            this.kategori = kategori;
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

        public String getKategori() {
            return kategori;
        }

        public void setKategori(String kategori) {
            this.kategori = kategori;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class Entitymenu {

        String kode, nama, harga, karegori, gambar;

        public Entitymenu(String kode, String nama, String harga, String karegori, String gambar) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
            this.karegori = karegori;
            this.gambar = gambar;
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

        public String getHarga() {
            return harga;
        }

        public void setHarga(String harga) {
            this.harga = harga;
        }

        public String getKaregori() {
            return karegori;
        }

        public void setKaregori(String karegori) {
            this.karegori = karegori;
        }

        public String getGambar() {
            return gambar;
        }

        public void setGambar(String gambar) {
            this.gambar = gambar;
        }

    }

    public class Entity {

        String id, menu, qty, harga, diskon, tanggal;

        public Entity(String id, String menu, String qty, String harga, String diskon, String tanggal) {
            this.id = id;
            this.menu = menu;
            this.qty = qty;
            this.harga = harga;
            this.diskon = diskon;
            this.tanggal = tanggal;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMenu() {
            return menu;
        }

        public void setMenu(String menu) {
            this.menu = menu;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getHarga() {
            return harga;
        }

        public void setHarga(String harga) {
            this.harga = harga;
        }

        public String getDiskon() {
            return diskon;
        }

        public void setDiskon(String diskon) {
            this.diskon = diskon;
        }

        public String getTanggal() {
            return tanggal;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

    }

}
