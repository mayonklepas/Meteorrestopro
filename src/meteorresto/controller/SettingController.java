/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadinfo();
        loadkoneksi();
        loadutil();
        simpaninfo();
        simpankoneksi();
        simpankelompok();
        kembali();
        luser.setText(sh.getUsername());
        bsimpaninformasi.setId("bc");
        bsimpankoneksi.setId("bc");
        bsimpanutil.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        //pkembali.setId("pane");
    }

    private void loadkoneksi() {
        String[] koneksi = fh.getkoneksi().split(";");
        thost.setText(koneksi[0]);
        tuser.setText(koneksi[1]);
        tpassword.setText(koneksi[2]);
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
    
    private void loadtema(){
        
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
    
    private void simpantema(){
        
    }

}
