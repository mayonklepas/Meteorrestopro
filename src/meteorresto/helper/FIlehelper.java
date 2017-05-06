/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FIlehelper {

    File koneksi = new File("koneksi");
    File kategorimeja = new File("kategorimeja");
    File kategorimenu = new File("kategorimenu");
    File info = new File("info");
    File slot = new File("slot");
    File dirimage=new File("image");

    public FIlehelper() {
        if (!koneksi.exists() || !kategorimeja.exists() || !kategorimenu.exists()
                ||!dirimage.exists()||!slot.exists() || !info.exists()) {
            try {
                koneksi.createNewFile();
                kategorimeja.createNewFile();
                kategorimenu.createNewFile();
                slot.createNewFile();
                info.createNewFile();
                dirimage.mkdir();
            } catch (IOException ex) {
                Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getkoneksi() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(koneksi));
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data.toString();

    }

    public String getkategorimeja() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(kategorimeja));
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data.toString();

    }

    public String getkategorimenu() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(kategorimenu));
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data.toString();

    }

    public String getinfo() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(info));
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data.toString();

    }
    
    public String getslot() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(slot));
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data.toString();

    }

    public void setall(String file, String data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
            bw.write(data);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
