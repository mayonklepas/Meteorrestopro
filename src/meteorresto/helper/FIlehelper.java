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
    File dirimage = new File("image");
    File style = new File("style.css");
    File configstyle = new File("configstyle");
    File reg=new File("reg");
    File akunumum=new File("akunumum");

    public FIlehelper() {
        if (!koneksi.exists() || !kategorimeja.exists() || !kategorimenu.exists()
                || !dirimage.exists() || !slot.exists() || !info.exists() || !configstyle.exists() || !reg.exists() || 
                !akunumum.exists()) {
            try {
                koneksi.createNewFile();
                kategorimeja.createNewFile();
                kategorimenu.createNewFile();
                slot.createNewFile();
                info.createNewFile();
                dirimage.mkdir();
                configstyle.createNewFile();
                reg.createNewFile();
                akunumum.createNewFile();
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
    
    public String getreg() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(reg));
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
    
    public String getakunumum() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(akunumum));
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

    public String loadtema() {
        StringBuilder data = new StringBuilder();
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(configstyle));
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

    public void settema(String temacari, String temareplace, String bhovercari, String bhoverreplace,
            String bpresscari, String bpressreplace) {
        StringBuilder data = new StringBuilder();
        String line = "";
        try {
            BufferedReader brtema = new BufferedReader(new FileReader(style));
            while ((line = brtema.readLine()) != null) {
                data.append(line);
            }
            brtema.close();
            BufferedWriter bwtema = new BufferedWriter(new FileWriter(style));
            bwtema.write(data.toString().replaceAll(temacari, temareplace));
            bwtema.flush();
            bwtema.close();

            data.setLength(0);

            BufferedReader brbhover = new BufferedReader(new FileReader(style));
            while ((line = brbhover.readLine()) != null) {
                data.append(line);
            }
            brbhover.close();
            BufferedWriter bwbhover = new BufferedWriter(new FileWriter(style));
            bwbhover.write(data.toString().replaceAll(bhovercari, bhoverreplace));
            bwbhover.flush();
            bwbhover.close();

            data.setLength(0);

            BufferedReader brbpress = new BufferedReader(new FileReader(style));
            while ((line = brbpress.readLine()) != null) {
                data.append(line);
            }
            brbhover.close();
            BufferedWriter bwbpress = new BufferedWriter(new FileWriter(style));
            bwbpress.write(data.toString().replaceAll(bpresscari, bpressreplace));
            bwbpress.flush();
            bwbpress.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void setconfigtema(String tema,String bhover,String bpress){
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(configstyle));
            bw.write(tema+";"+bhover+";"+bpress);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setreg(String reqid){
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(reg));
            bw.write(reqid);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setakunumum(String akunstr){
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(akunumum));
            bw.write(akunstr);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FIlehelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
