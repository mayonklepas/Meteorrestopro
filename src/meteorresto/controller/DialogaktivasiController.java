/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;

/**
 * FXML Controller class
 *
 * @author Minami
 */
public class DialogaktivasiController implements Initializable {

    @FXML
    private AnchorPane header;
    @FXML
    private TextField tid;
    @FXML
    private TextField tasn;
    FIlehelper fh = new FIlehelper();
    Operationhelper oh = new Operationhelper();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        header.setId("tema");
        tid.setText(oh.gethddid());
        setreg();
    }

    private void setreg() {
        tasn.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String srcencode = oh.gethddid() + "bk201!@#";
                    String base64res = Base64.getEncoder().encodeToString(srcencode.getBytes());
                    if (base64res.equals(tasn.getText())) {
                        fh.setreg(tasn.getText());
                        oh.info("Registrasi Berhasil,Silahkan Buka Kembali");
                        System.exit(0);
                    }else{
                        oh.gagal("Registrasi Gagal, Key Tidak Cocok");
                    }

                }

            }

        });

    }

}
