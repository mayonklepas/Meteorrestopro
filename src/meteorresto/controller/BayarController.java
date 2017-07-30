/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
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
public class BayarController implements Initializable {

    @FXML
    private Label ltotal;
    @FXML
    private Label lkembalian;
    @FXML
    private TextField ljumlahuang;
    @FXML
    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bc;
    Sessionhelper sh = new Sessionhelper();
    Operationhelper oh = new Operationhelper();
    Connectionhelper ch = new Connectionhelper();
    double total_belanja = 0, total_kembali = 0;
    NumberFormat nf = NumberFormat.getInstance();
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    ArrayList<listmenugettersetter> listmenupilihan = new ArrayList<>();
    @FXML
    private FlowPane fpakunuang;
    ObservableList<Button> btlist = FXCollections.observableArrayList();
    ObservableList<Entitykeuangan> datakeuangan = FXCollections.observableArrayList();
    @FXML
    private Button bcl;
    FIlehelper fh = new FIlehelper();
    public static String kode_transaksi,kategorimeja,namameja;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadtotaluang();
        kalkulasi();
        clearfield();
        kalkulasibybutton();
        bcl.setId("bc");
        b1.setId("bc");
        b2.setId("bc");
        b3.setId("bc");
        b4.setId("bc");
        b5.setId("bc");
        b6.setId("bc");
        b7.setId("bc");
        b8.setId("bc");
        b9.setId("bc");
        b0.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        loadakunkeuangan();

    }

    private EventHandler actionbayar(int y) {
        EventHandler evt = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (oh.konfirmasibayar(datakeuangan.get(y).nama_akun_keuangan) == true) {
                    try {
                        String[] info = fh.getinfo().split(";");
                        HashMap hash = new HashMap(11);
                        hash.put("kode_meja", sh.getKode_meja());
                        hash.put("slot", sh.getSlot());
                        hash.put("kode_kasir", sh.getKode_user());
                        hash.put("nama_kasir", sh.getUsername());
                        hash.put("kategori_meja", kategorimeja);
                        hash.put("nama_meja", namameja);
                        hash.put("perusahaan", info[0]);
                        hash.put("alamat", info[1]);
                        hash.put("nohp", info[3]);
                        hash.put("kode_transaksi", kode_transaksi);
                        double jumlahuang=0;
                        if(ljumlahuang.getText().equals("0")||ljumlahuang.getText().equals("")){
                            jumlahuang=total_belanja;
                        }else{
                            jumlahuang=Double.parseDouble(ljumlahuang.getText());
                        }
                        hash.put("uang",jumlahuang);
                        JasperReport jr = (JasperReport) JRLoader.loadObject(new File("laporan/Struckkasir2.jasper"));
                        JasperPrint jp = JasperFillManager.fillReport(jr, hash, ch.connect());
                        JasperPrintManager.printReport(jp, false);
                        ch.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        oh.error(e);
                    }
                    rawclearpembayaran(datakeuangan.get(y).kode_akun_keuangan);
                    Node node = (Node) event.getSource();
                    Stage st = (Stage) node.getScene().getWindow();
                    st.close();
                }
            }
        };

        return evt;

    }

    private void loadakunkeuangan() {
        try {
            datakeuangan.clear();
            btlist.clear();
            fpakunuang.getChildren().clear();
            String sql = "SELECT kode_akun_keuangan,nama_akun_keuangan FROM akun_keuangan ORDER BY kode_akun_keuangan ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode = res.getString("kode_akun_keuangan");
                String nama = res.getString("nama_akun_keuangan");
                datakeuangan.add(new Entitykeuangan(kode, nama));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < datakeuangan.size(); i++) {
                final int y = i;

                Button bt = new Button(datakeuangan.get(i).nama_akun_keuangan);
                bt.setPrefWidth(80);
                bt.setPrefHeight(50);
                bt.setWrapText(true);
                bt.setAlignment(Pos.CENTER);
                bt.setContentDisplay(ContentDisplay.TOP);
                btlist.add(bt);
                btlist.get(i).setPadding(new Insets(3));
                btlist.get(i).setOnAction(actionbayar(y));
                btlist.get(i).setId("button-meja");
            }
            fpakunuang.getChildren().addAll(btlist);
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void loadtotaluang() {
        total_belanja = sh.getTotal_bayar();
        ltotal.setText("Rp " + nf.format(total_belanja));
    }

    private void rawkalkulasi() {
        try {
            if (ljumlahuang.getText().equals("")) {
                total_kembali = 0;
                lkembalian.setText(nf.format(total_kembali));
            } else {
                total_kembali = Double.parseDouble(oh.digitinputreplacer(ljumlahuang.getText())) - total_belanja;
                lkembalian.setText(nf.format(total_kembali));
            }
        } catch (Exception e) {
            oh.error(e);
        }
    }

    private void kurangistock() {
        try {
            String sql = "SELECT m.kode_master_resep,t.jumlah FROM transaksi t "
                    + "INNER JOIN menu m ON t.kode_menu=m.kode WHERE status=0 AND kode_meja=? AND slot=?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, sh.getKode_meja());
            pre.setString(2, sh.getSlot());
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String kode_resep = res.getString("kode_master_resep");
                int jumlah_pesan = res.getInt("jumlah");
                listmenupilihan.add(new listmenugettersetter(kode_resep, jumlah_pesan));
            }
            pre.close();
            res.close();
            ch.close();
            for (int i = 0; i < listmenupilihan.size(); i++) {
                String sqlselectbahan = "SELECT kode_bahan,jumlah_pakai FROM resep_detail WHERE kode_master=?";
                PreparedStatement preselectbahan = ch.connect().prepareStatement(sqlselectbahan);
                preselectbahan.setString(1, listmenupilihan.get(i).getKode_resep());
                ResultSet resselectbahan = preselectbahan.executeQuery();
                while (resselectbahan.next()) {
                    String sqlupdastock = "UPDATE bahan SET jumlah=jumlah - ? WHERE kode=? ";
                    PreparedStatement preupdatestock = ch.connect().prepareStatement(sqlupdastock);
                    preupdatestock.setDouble(1, resselectbahan.getDouble("jumlah_pakai") * listmenupilihan.get(i).jumlah_pesanan);
                    preupdatestock.setString(2, resselectbahan.getString("kode_bahan"));
                    preupdatestock.executeUpdate();
                    preupdatestock.close();
                    ch.close();
                }

                preselectbahan.close();
                resselectbahan.close();
                ch.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BayarController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ch.close();
        }
    }

    private void rawclearpembayaran(String kode_akun) {
        kurangistock();
        try {
            String sql = "UPDATE transaksi SET status=1,kode_akun_keuangan=? WHERE kode_meja=? AND slot=?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setString(1, kode_akun);
            pre.setString(2, sh.getKode_meja());
            pre.setString(3, sh.getSlot());
            pre.executeUpdate();
            String sqlcount = "SELECT COUNT(kode_meja) total_data FROM transaksi WHERE "
                    + "kode_meja=? AND status=0";
            PreparedStatement precount = ch.connect().prepareStatement(sqlcount);
            precount.setString(1, sh.getKode_meja());
            ResultSet res = precount.executeQuery();
            int total_data = 0;
            while (res.next()) {
                total_data = res.getInt("total_data");
            }
            pre.close();
            res.close();
            ch.close();
            if (total_data == 0) {
                String sqlupdatemeja = "UPDATE meja SET status=0 WHERE kode=?";
                PreparedStatement preupdatemeja = ch.connect().prepareStatement(sqlupdatemeja);
                preupdatemeja.setString(1, sh.getKode_meja());
                preupdatemeja.executeUpdate();
                pre.close();
                ch.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BayarController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void kalkulasi() {
        ljumlahuang.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                rawkalkulasi();
            }
        });

        /*ljumlahuang.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    rawclearpembayaran();
                    Node node = (Node) event.getSource();
                    Stage st = (Stage) node.getScene().getWindow();
                    st.close();
                }
            }
        });*/
    }

    private void kalkulasibybutton() {
        final EventHandler<ActionEvent> bthanler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (e.getSource() == b0) {
                    ljumlahuang.appendText("0");
                    rawkalkulasi();
                } else if (e.getSource() == b1) {
                    ljumlahuang.appendText("1");
                    rawkalkulasi();
                } else if (e.getSource() == b2) {
                    ljumlahuang.appendText("2");
                    rawkalkulasi();
                } else if (e.getSource() == b3) {
                    ljumlahuang.appendText("3");
                    rawkalkulasi();
                } else if (e.getSource() == b4) {
                    ljumlahuang.appendText("4");
                    rawkalkulasi();
                } else if (e.getSource() == b5) {
                    ljumlahuang.appendText("5");
                    rawkalkulasi();
                } else if (e.getSource() == b6) {
                    ljumlahuang.appendText("6");
                    rawkalkulasi();
                } else if (e.getSource() == b7) {
                    ljumlahuang.appendText("7");
                    rawkalkulasi();
                } else if (e.getSource() == b8) {
                    ljumlahuang.appendText("8");
                    rawkalkulasi();
                } else if (e.getSource() == b9) {
                    ljumlahuang.appendText("9");
                    rawkalkulasi();
                } else if (e.getSource() == bc) {
                    ljumlahuang.setText(ljumlahuang.getText().substring(0, ljumlahuang.getText().length() - 1));
                    rawkalkulasi();
                }
            }
        };

        b0.setOnAction(bthanler);
        b1.setOnAction(bthanler);
        b2.setOnAction(bthanler);
        b3.setOnAction(bthanler);
        b4.setOnAction(bthanler);
        b5.setOnAction(bthanler);
        b6.setOnAction(bthanler);
        b7.setOnAction(bthanler);
        b8.setOnAction(bthanler);
        b9.setOnAction(bthanler);
        bc.setOnAction(bthanler);

    }

    private void clearfield() {
        bcl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ljumlahuang.clear();
            }
        });
    }

    public class listmenugettersetter {

        String kode_resep;
        int jumlah_pesanan;

        public listmenugettersetter(String kode_resep, int jumlah_pesanan) {
            this.kode_resep = kode_resep;
            this.jumlah_pesanan = jumlah_pesanan;
        }

        public String getKode_resep() {
            return kode_resep;
        }

        public void setKode_resep(String kode_resep) {
            this.kode_resep = kode_resep;
        }

        public int getJumlah_pesanan() {
            return jumlah_pesanan;
        }

        public void setJumlah_pesanan(int jumlah_pesanan) {
            this.jumlah_pesanan = jumlah_pesanan;
        }

    }

    public class Entitykeuangan {

        String kode_akun_keuangan, nama_akun_keuangan;

        public Entitykeuangan(String kode_akun_keuangan, String nama_akun_keuangan) {
            this.kode_akun_keuangan = kode_akun_keuangan;
            this.nama_akun_keuangan = nama_akun_keuangan;
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
