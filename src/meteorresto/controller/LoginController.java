/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.File;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;

/**
 * FXML Controller class
 *
 * @author user
 */
public class LoginController implements Initializable {

    public static Stage windowstage;

    private TextField tusername;
    @FXML
    private PasswordField tpassword;
    @FXML
    private Button blogin;
    Connectionhelper ch = new Connectionhelper();
    Operationhelper oh = new Operationhelper();
    Sessionhelper sh = new Sessionhelper();
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    @FXML
    private ComboBox<String> cuser;
    ObservableList<String> olscombo=FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadcombo();
        login();
        blogin.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        
    }

    @FXML
    private void keypress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                String sql = "SELECT COUNT(id) AS total,tipe,id,nama FROM akun WHERE "
                        + "username=? AND password=? GROUP BY tipe,id,nama LIMIT 1";
                PreparedStatement pre = ch.connect().prepareStatement(sql);
                pre.setString(1, cuser.getEditor().getText());
                pre.setString(2, tpassword.getText());
                ResultSet res = pre.executeQuery();
                int jumlah = 0;
                String tipe = "";
                String id = "";
                String nama = "";
                while (res.next()) {
                    jumlah = res.getInt("total");
                    tipe = res.getString("tipe");
                    id = res.getString("id");
                    nama = res.getString("nama");
                }
                pre.close();
                res.close();
                ch.close();
                if (jumlah == 0) {
                    oh.gagal("Maaf Username / Password Salah");
                } else {
                    Stage stage = new Stage();
                    sh.setSt(stage);
                    sh.setKode_user(id);
                    sh.setUsername(nama);
                    sh.setTipe(tipe);
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    //stage.setFullScreen(true);
                    String css = new File("style.css").getAbsolutePath();
                    stage.getScene().getStylesheets().clear();
                    stage.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                    stage.setMaximized(true);
                    stage.setTitle("Myresto POS App");
                    stage.show();
                    Node node = (Node) event.getSource();
                    Stage st = (Stage) node.getScene().getWindow();
                    st.close();

                }

            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ch.close();
            }
        }
    }

    
    private void loadcombo(){
        try {
            PreparedStatement pre=ch.connect().prepareStatement("SELECT username FROM akun");
            ResultSet res=pre.executeQuery();
            while (res.next()) {                
                olscombo.add(res.getString("username"));
            }
            cuser.setItems(olscombo);
            ch.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ch.close();
        }
    }
    
    private void login() {
        blogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String sql = "SELECT COUNT(id) AS total,tipe,id,nama FROM akun WHERE "
                            + "username=? AND password=? GROUP BY tipe,id,nama LIMIT 1";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, cuser.getEditor().getText());
                    pre.setString(2, tpassword.getText());
                    ResultSet res = pre.executeQuery();
                    int jumlah = 0;
                    String tipe = "";
                    String id = "";
                    String nama = "";
                    while (res.next()) {
                        jumlah = res.getInt("total");
                        tipe = res.getString("tipe");
                        id = res.getString("id");
                        nama = res.getString("nama");
                    }
                    pre.close();
                    res.close();
                    ch.close();
                    if (jumlah == 0) {
                        oh.gagal("Maaf Username / Password Salah");
                    } else {
                        Stage stage = new Stage();
                        sh.setSt(stage);
                        sh.setKode_user(id);
                        sh.setUsername(nama);
                        sh.setTipe(tipe);
                        Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Main.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                        stage.getScene().getStylesheets().clear();
                        stage.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                        stage.setMaximized(true);
                        stage.setTitle("Teman Resto POS App");
                        stage.show();
                        Node node = (Node) event.getSource();
                        Stage st = (Stage) node.getScene().getWindow();
                        st.close();

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

}
