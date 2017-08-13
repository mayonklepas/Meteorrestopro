/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;

/**
 * FXML Controller class
 *
 * @author user
 */
public class PindahmejaController implements Initializable {

    @FXML
    private ComboBox<String> cslotasal;
    @FXML
    private FlowPane fpasal;
    @FXML
    private Button pindahkan;

    Sessionhelper sh = new Sessionhelper();
    Operationhelper oh = new Operationhelper();
    Connectionhelper ch = new Connectionhelper();
    FIlehelper fh = new FIlehelper();
    String kode_meja;
    int status_meja = 0;
    ObservableList<Button> btl = FXCollections.observableArrayList();
    ObservableList<Entitymeja> datameja = FXCollections.observableArrayList();
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    public static String kode_transaksi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cslotasal.getEditor().setDisable(true);
        loadcomboslot();
        loadmeja();
        pindahmeja();
        pindahkan.setId("bc");

        header.setId("tema");
        footer.setId("tema");
    }

    private void loadcomboslot() {
        ObservableList<String> olsslot = FXCollections.observableArrayList();
        String[] dataslot = fh.getslot().split(";");
        for (int i = 0; i < dataslot.length; i++) {
            olsslot.add(dataslot[i]);
        }
        cslotasal.setItems(olsslot);
    }

    private void loadmeja() {
        try {
            datameja.clear();
            btl.clear();
            fpasal.getChildren().clear();
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
                btl.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        loadmeja();
                        btl.get(y).setId("button-meja-pilih");
                        status_meja = 1;
                        kode_meja = datameja.get(y).getKode();
                        cslotasal.getEditor().setText("Slot 1");

                    }
                });
            }
            fpasal.getChildren().addAll(btl);
            ch.connect().close();
        } catch (Exception ex) {
            Logger.getLogger(TransaksiController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void pindahmeja() {
        pindahkan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (status_meja == 0 || kode_meja.equals("") || kode_meja == null) {
                    oh.gagal("Maaf anda harus memilih slot");
                } else {
                    try {
                        String sql = "UPDATE transaksi SET kode_meja=?,slot=? WHERE kode_meja=? AND slot=?;"
                                + "UPDATE transaksi SET kode_transaksi=? WHERE kode_meja=? AND slot=?";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, kode_meja);
                        pre.setString(2, cslotasal.getEditor().getText());
                        pre.setString(3, sh.getKode_meja());
                        pre.setString(4, sh.getSlot());
                        pre.setString(5, kode_transaksi);
                        pre.setString(6, kode_meja);
                        pre.setString(7, cslotasal.getEditor().getText());
                        pre.executeUpdate();
                        pre.close();
                        ch.close();
                        String sqlcount = "SELECT COUNT(kode_meja) total_data FROM transaksi WHERE "
                                + "kode_meja=?";
                        PreparedStatement precount = ch.connect().prepareStatement(sqlcount);
                        precount.setString(1, sh.getKode_meja());
                        ResultSet res = precount.executeQuery();
                        int total_data = 0;
                        while (res.next()) {
                            total_data = res.getInt("total_data");
                        }
                        precount.close();
                        res.close();
                        ch.close();
                        if (total_data == 0) {
                            String sqlupdatemeja = "UPDATE meja SET status=0 WHERE kode=?";
                            PreparedStatement preupdatemeja = ch.connect().prepareStatement(sqlupdatemeja);
                            preupdatemeja.setString(1, sh.getKode_meja());
                            preupdatemeja.executeUpdate();
                            preupdatemeja.close();
                            ch.close();
                        }
                        String sqlupdatemeja2 = "UPDATE meja SET status=1 WHERE kode=?";
                        PreparedStatement preupdatemeja2 = ch.connect().prepareStatement(sqlupdatemeja2);
                        preupdatemeja2.setString(1, kode_meja);
                        preupdatemeja2.executeUpdate();
                        preupdatemeja2.close();
                        ch.close();
                        oh.sukses("Meja berhasil di pindah");
                        Node n = (Node) event.getSource();
                        Stage st = (Stage) n.getScene().getWindow();
                        st.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(PindahmejaController.class.getName()).log(Level.SEVERE, null, ex);
                        oh.error(ex);
                    } finally {
                        ch.close();
                    }
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

}
