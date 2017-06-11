/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author user
 */
public class MeteorResto extends Application {

    Connectionhelper ch = new Connectionhelper();
    FIlehelper fh = new FIlehelper();

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
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Progressdialogstart.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Myresto POS");
                    String css = new File("style.css").getAbsolutePath();
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
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    stage.close();
                    Stage st2 = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Login.fxml"));
                    Scene sc = new Scene(root);
                    st2.setScene(sc);
                    String css = new File("style.css").getAbsolutePath();
                    st2.getScene().getStylesheets().clear();
                    st2.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                    st2.show();
                } catch (IOException ex) {
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
                System.out.println("Database telah dibuat");
                String pathdata = new File("dbrestopro.backup").getAbsolutePath();
                String cmd = "pg_restore -U " + fh.getkoneksi().split(";")[1] + " --dbname=dbrestopro --create --verbose " + pathdata;
                Runtime.getRuntime().exec(cmd);
            }
            return null;
        }

    }

}
