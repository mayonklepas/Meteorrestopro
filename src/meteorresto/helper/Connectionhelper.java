/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Connectionhelper {

    FIlehelper fh = new FIlehelper();
    String[] stkon = fh.getkoneksi().split(";");
    String hostcheck=stkon[0];
    String host = stkon[0]+"dbrestopro";
    String user = stkon[1];
    String password = stkon[2];
    Connection con;

    public Connectionhelper() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection connect() {
        try {
            con = DriverManager.getConnection(host, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return con;
    }
    
    public int checkconnection(){
        int countdb=0;
        try {
            con = DriverManager.getConnection(hostcheck, user, password);
            PreparedStatement pre=con.prepareStatement("SELECT COUNT(datname) AS jumlah FROM pg_catalog.pg_database WHERE lower(datname) = lower('dbrestopro');");
            ResultSet res=pre.executeQuery();
            res.next();
            countdb=res.getInt("jumlah");
        } catch (SQLException ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return countdb;
    }
    
    public void createdb(){
        try {
            con = DriverManager.getConnection(hostcheck, user, password);
            PreparedStatement pre=con.prepareStatement("CREATE DATABASE dbrestopro;");
            pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public void close() {
        try {
            if (con != null) {
                con.close();
            }
            super.finalize();
        } catch (SQLException ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean statuskeneksi(){
        boolean status=false;
        try {
            status = con.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(Connectionhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

}
