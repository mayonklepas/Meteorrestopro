/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.helper;

import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Sessionhelper {
    
    public static  String username,tipe,kode_user,kode_meja,slot;
    public static int status_meja,tax,status_tax;
    public static double total_bayar;
    public static Stage st;

    public Sessionhelper() {
    }

    public Sessionhelper(String username, String tipe, String kode_user,
            int status_meja,String kode_meja ,double total_bayar ,String slot,Stage st, int tax) {
        this.username = username;
        this.tipe = tipe;
        this.kode_user = kode_user;
        this.status_meja=status_meja;
        this.kode_meja=kode_meja;
        this.total_bayar=total_bayar;
        this.slot=slot;
        this.st = st;
        this.tax=tax;
        this.status_tax=status_tax;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getKode_user() {
        return kode_user;
    }

    public void setKode_user(String kode_user) {
        this.kode_user = kode_user;
    }

    public Stage getSt() {
        return st;
    }

    public void setSt(Stage st) {
        this.st = st;
    }

    public int getStatus_meja() {
        return status_meja;
    }

    public void setStatus_meja(int status_meja) {
        this.status_meja = status_meja;
    }

    public String getKode_meja() {
        return kode_meja;
    }

    public void setKode_meja(String kode_meja) {
        this.kode_meja = kode_meja;
    }

    public double getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(double total_bayar) {
        this.total_bayar = total_bayar;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getStatus_tax() {
        return status_tax;
    }

    public void setStatus_tax(int status_tax) {
        this.status_tax = status_tax;
    }
    
    
    
    
    
    
}
