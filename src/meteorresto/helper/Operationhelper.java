/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.helper;

import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jfree.chart.plot.dial.DialBackground;

/**
 *
 * @author user
 */
public class Operationhelper {

    public Operationhelper() {
    }

    public void sukses(String kata) {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Informasi");
        al.setHeaderText("Operasi Berhasil");
        al.setContentText(kata);
        al.showAndWait();
    }

    public void gagal(String kata) {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Informasi");
        al.setHeaderText("Operasi Gagal");
        al.setContentText(kata);
        al.showAndWait();
    }
    
    public void info(String kata) {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Informasi");
        al.setHeaderText("Informasi");
        al.setContentText(kata);
        al.showAndWait();
    }

    public void error(Exception ex) {
        Alert al = new Alert(Alert.AlertType.ERROR);
        al.setTitle("Error");
        al.setHeaderText("Terjadi Kesalahan");
        al.setContentText(ex.getMessage());
        al.showAndWait();
    }

    public boolean konfirmasi(String kata) {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Konfirmasi");
        al.setHeaderText("Yakin ingin meng"+kata+" data ini ?");
        al.setContentText("Data yang sudah di"+kata+" tidak bisa dikembalikan");
        ButtonType ya = new ButtonType("Ya");
        ButtonType tidak = new ButtonType("Tidak");
        al.getButtonTypes().setAll(ya, tidak);
        Optional<ButtonType> op = al.showAndWait();
        boolean hasil;
        if (op.get()==ya){
            hasil=true;
        }else{
            hasil=false;
        }
        return hasil;
    }
    
    
    
}
