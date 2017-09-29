/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author user
 */
public class MeteorResto extends Application {

    Connectionhelper ch = new Connectionhelper();
    FIlehelper fh = new FIlehelper();
    Operationhelper oh = new Operationhelper();
    StringBuilder data = new StringBuilder();

    @Override
    public void start(Stage stage) throws Exception {
        loadstart ls = new loadstart();
        Thread th = new Thread(ls);
        th.setDaemon(true);
        th.start();
        ls.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.getIcons().add(new Image(getClass().getResource("/meteorresto/icon/favico.png").toString()));
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Progressdialogstart.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                    stage.getScene().getStylesheets().clear();
                    stage.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(MeteorResto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ls.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates
                    stage.close();
                    String srcencode = "";
                    Stage st2 = new Stage();
                    try {
                       srcencode=oh.gethddid() + "bk201!@#"; 
                    } catch (Exception e) {
                        e.printStackTrace();
                        oh.error(e);
                    }                    
                    String base64res = Base64.getEncoder().encodeToString(srcencode.getBytes());
                    if (fh.getreg().equals(base64res)) {
                        st2.getIcons().add(new Image(getClass().getResource("/meteorresto/icon/icon.png").toString()));
                        st2.resizableProperty().setValue(Boolean.FALSE);
                        st2.setTitle("Restoku Login");
                        Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Login.fxml"));
                        Scene sc = new Scene(root);
                        st2.setScene(sc);
                        String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                        st2.getScene().getStylesheets().clear();
                        st2.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                        st2.show();
                    } else {
                        st2.getIcons().add(new Image(getClass().getResource("/meteorresto/icon/favico.png").toString()));
                        st2.resizableProperty().setValue(Boolean.FALSE);
                        st2.setTitle("Restoku Aktivasi");
                        Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Dialogaktivasi.fxml"));
                        Scene sc = new Scene(root);
                        st2.setScene(sc);
                        String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                        st2.getScene().getStylesheets().clear();
                        st2.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                        st2.show();
                    }

                } catch (Exception ex) {
                    Logger.getLogger(MeteorResto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        launch(args);
    }

    public class loadstart extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            if (ch.checkconnection() == 0) {
                ch.createdb();
                System.out.println("Database Telah Dibuat");
                //String pathdata = new File("dbrestopro.backup").getAbsolutePath();
                //System.out.println(pathdata);
                //String cmd = "pg_restore -U " + fh.getkoneksi().split(";")[1] + " --dbname=dbrestopro --create --verbose " + "\"" + pathdata + "\"";
                //Runtime.getRuntime().exec(cmd);
                try {
                    File datasql = new File("dbrestopro.txt");
                    String line = "";
                    BufferedReader br = new BufferedReader(new FileReader(datasql));
                    while ((line = br.readLine()) != null) {
                        data.append(line);
                    }
                    PreparedStatement pre = ch.connect().prepareStatement(data.toString());
                    pre.executeUpdate();
                    ch.connect().close();
                    System.out.println("Table Telah Dibuat");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Thread.sleep(3 * 1000);
            return null;
        }

    }

}
