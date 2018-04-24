/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Minami
 */
public class Transaksi_marketController implements Initializable {

    @FXML
    private AnchorPane header;
    @FXML
    private Pane pkembali;
    @FXML
    private AnchorPane footer;
    @FXML
    private TableView<Entity> table;
    @FXML
    private TableColumn<Entity, String> kode;
    @FXML
    private TableColumn<Entity, String> nama;
    @FXML
    private TableColumn<Entity, String> jumlah;
    @FXML
    private TableColumn<Entity, String> harga;
    @FXML
    private TableColumn<Entity, String> total;
    @FXML
    private Pane detail;
    @FXML
    private TextField tcari;
    @FXML
    private ComboBox<String> cdiskon;
    @FXML
    private Label ltotal;
    @FXML
    private Label lkembalian;
    @FXML
    private TextField tjumlahuang;
    @FXML
    private FlowPane fpakunuang;

    ObservableList<Entity> tabledata = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void loaddata() {

    }

    private void caridansimpan() {

    }

    private void select() {

    }

    private void hapus() {

    }

    public class Entity {

        String kode, nama, jumlah, harga, total;

        public Entity(String kode, String nama, String jumlah, String harga, String total) {
            this.kode = kode;
            this.nama = nama;
            this.jumlah = jumlah;
            this.harga = harga;
            this.total = total;
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

        public String getJumlah() {
            return jumlah;
        }

        public void setJumlah(String jumlah) {
            this.jumlah = jumlah;
        }

        public String getHarga() {
            return harga;
        }

        public void setHarga(String harga) {
            this.harga = harga;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

    }

}
