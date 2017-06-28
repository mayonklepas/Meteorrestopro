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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;

/**
 * FXML Controller class
 *
 * @author user
 */
public class SettingController implements Initializable {

    @FXML
    private Pane pkembali;
    @FXML
    private Label luser;
    @FXML
    private TextField thost;
    @FXML
    private TextField tuser;
    @FXML
    private TextField tpassword;
    @FXML
    private Button bsimpankoneksi;
    @FXML
    private TextField tperusahaan;
    @FXML
    private TextField talamat;
    @FXML
    private TextField temail;
    @FXML
    private TextField ttelp;
    @FXML
    private TextField tnohp;
    @FXML
    private Button bsimpaninformasi;
    @FXML
    private TextArea tkelompokmeja;
    @FXML
    private TextArea tkelompokmakanan, tslot;
    @FXML
    private Button bsimpanutil;
    FIlehelper fh = new FIlehelper();
    Operationhelper oh = new Operationhelper();
    Sessionhelper sh = new Sessionhelper();
    Connectionhelper ch=new Connectionhelper();
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    @FXML
    private ColorPicker cptema;
    @FXML
    private ColorPicker cphoverbutton;
    @FXML
    private ColorPicker cppressedbutton;
    @FXML
    private Button bsimpantema;
    @FXML
    private ComboBox<String> cpenjualan;
    @FXML
    private ComboBox<String> cpembelian;
    @FXML
    private Button bsimpanakun;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadinfo();
        loadkoneksi();
        loadutil();
        loadtema();
        simpaninfo();
        simpankoneksi();
        simpankelompok();
        kembali();
        loadakunumum();
        luser.setText(sh.getUsername());
        bsimpaninformasi.setId("bc");
        bsimpankoneksi.setId("bc");
        bsimpanutil.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        bsimpantema.setId("bc");
        simpantema();
        simpanakunumum();
        //pkembali.setId("pane");
    }

    private void loadkoneksi() {
        String[] koneksi = fh.getkoneksi().split(";");
        thost.setText(koneksi[0]);
        tuser.setText(koneksi[1]);
        tpassword.setText(koneksi[2]);
    }
    
    private void loadakunumum(){
        try {
            String[] akun = fh.getakunumum().split(";");
            cpenjualan.getEditor().setText(akun[0]);
            cpembelian.getEditor().setText(akun[1]);
            ObservableList<String> ols=FXCollections.observableArrayList();
            PreparedStatement pre=ch.connect().prepareStatement("SELECT kode_perkiraan,nama_perkiraan FROM perkiraan");
            ResultSet res=pre.executeQuery();
            while (res.next()) {                
                ols.add(res.getString("kode_perkiraan")+":"+res.getString("nama_perkiraan"));
            }
            cpenjualan.setItems(ols);
            cpembelian.setItems(ols);
            res.close();
            pre.close();
            ch.close();
        } catch (SQLException ex) {
            Logger.getLogger(SettingController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ch.close();
        }
        
    }

    private void loadinfo() {
        String[] info = fh.getinfo().split(";");
        tperusahaan.setText(info[0]);
        talamat.setText(info[1]);
        temail.setText(info[2]);
        ttelp.setText(info[3]);
        tnohp.setText(info[4]);

    }

    private void loadutil() {
        tkelompokmeja.setText(fh.getkategorimeja());
        tkelompokmakanan.setText(fh.getkategorimenu());
        tslot.setText(fh.getslot());
    }

    private void loadtema() {
        String[] warnasekarang = fh.loadtema().split(";");
        cptema.setValue(Color.web(warnasekarang[0]));
        cphoverbutton.setValue(Color.web(warnasekarang[1]));
        cppressedbutton.setValue(Color.web(warnasekarang[2]));
    }

    private void simpankoneksi() {
        bsimpankoneksi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder sb = new StringBuilder();
                sb.append(thost.getText() + ";\n");
                sb.append(tuser.getText() + ";\n");
                sb.append(tpassword.getText());
                fh.setall("koneksi", sb.toString());
                oh.sukses("Berhasil Disimpan");
            }
        });
    }

    private void simpaninfo() {
        bsimpaninformasi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder sb = new StringBuilder();
                sb.append(tperusahaan.getText() + ";\n");
                sb.append(talamat.getText() + ";\n");
                sb.append(temail.getText() + ";\n");
                sb.append(ttelp.getText() + ";\n");
                sb.append(tnohp.getText());
                fh.setall("info", sb.toString());
                oh.sukses("Berhasil Disimpan");
            }
        });
    }

    private void simpankelompok() {
        bsimpanutil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fh.setall("kategorimeja", tkelompokmeja.getText());
                fh.setall("kategorimenu", tkelompokmakanan.getText());
                fh.setall("slot", tslot.getText());
                oh.sukses("Berhasil Disimpan");
            }
        });
    }
    
    
    private void simpanakunumum(){
        bsimpanakun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fh.setakunumum(cpenjualan.getEditor().getText()+";"+
                        cpembelian.getEditor().getText());
                oh.sukses("Pengaturan Berhasil Disimpan");
            }
        });
    }

    private void kembali() {
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(SettingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void simpantema() {
        bsimpantema.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                String warnatema = "#" + Integer.toHexString(cptema.getValue().hashCode()).substring(0, 6);
                String warnahoverbutton = "#" + Integer.toHexString(cphoverbutton.getValue().hashCode()).substring(0, 6);
                String warnapressbutton = "#" + Integer.toHexString(cppressedbutton.getValue().hashCode()).substring(0, 6);
                String[] warnasekarang = fh.loadtema().split(";");
                if (warnatema.equals(warnahoverbutton) || warnatema.equals(warnapressbutton)
                        || warnahoverbutton.equals(warnapressbutton)) {
                    oh.gagal("Warna antara 3 pilihan tidak boleh sama");
                } else {
                    fh.settema(warnasekarang[0], warnatema, warnasekarang[1], warnahoverbutton, warnasekarang[2], warnapressbutton);
                    fh.setconfigtema(warnatema, warnahoverbutton, warnapressbutton);
                    oh.info("Tema Berhasil Diterapkan");
                    try {
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(SettingController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }

            }
        });

    }

}
