/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Minami
 */
public class TentangController implements Initializable {

    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        header.setId("tema");
        footer.setId("tema");

    }

    @FXML
    private void keypress(KeyEvent event) {
    }

}
