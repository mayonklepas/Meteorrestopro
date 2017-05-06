/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.helper;

import java.sql.Connection;
import java.sql.DriverManager;
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
    String host = stkon[0];
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
