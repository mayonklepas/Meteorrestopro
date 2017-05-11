/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author user
 */
public class MeteorResto extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Teman Resto POS");
        String css = new File("style.css").getAbsolutePath();
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        launch(args);
    }

}
