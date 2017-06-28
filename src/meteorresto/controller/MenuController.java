/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MenuController implements Initializable {

    @FXML
    private Pane pkembali;
    @FXML
    private TextField tkode;
    @FXML
    private TextField tnama;
    @FXML
    private TextField tharga;
    @FXML
    private ComboBox<String> ckategori;
    @FXML
    private TextField tgambar;
    @FXML
    private Button bcari;
    @FXML
    private Button bsimpan;
    @FXML
    private Button bclear;
    @FXML
    private Label luser;
    @FXML
    private Label ljumlah;
    @FXML
    private TextField tlimit;
    @FXML
    private TableView<Entity> table;
    @FXML
    private TableColumn<Entity, String> kode;
    @FXML
    private TableColumn<Entity, String> nama;
    @FXML
    private TableColumn<Entity, String> harga;
    @FXML
    private TableColumn<Entity, String> kategori;
    @FXML
    private TextField tcari;
    Sessionhelper sh = new Sessionhelper();
    Connectionhelper ch = new Connectionhelper();
    ObservableList<Entity> tabledata = FXCollections.observableArrayList();
    ObservableList olscombo = FXCollections.observableArrayList();
    ObservableList olscomboresep = FXCollections.observableArrayList();
    Operationhelper oh = new Operationhelper();
    FIlehelper fh = new FIlehelper();
    String ids, gambar;
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    @FXML
    private Pane detail;
    @FXML
    private ImageView imgpre;
    FileInputStream fis;
    @FXML
    private ComboBox<String> cresep;
    @FXML
    private TableColumn<Entity, String> nama_resep;
    NumberFormat nf=NumberFormat.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        kembali();
        loaddata();
        select();
        simpan();
        hapus();
        clear();
        cari();
        loadtotaldata();
        loadcombo();
        pilihgambar();
        rawclear();
        loadresep();
        luser.setText(sh.getUsername());
        bsimpan.setId("bc");
        bclear.setId("bc");
        bcari.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        detail.setId("tema");
        tlimit.setId("tema");

        //pkembali.setId("pane");
    }

    private void loadresep() {
        try {
            String sql = "SELECT kode_resep,nama_resep FROM resep_master ORDER BY kode_resep";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                olscomboresep.add(res.getString("nama_resep") + "-" + res.getString("kode_resep"));
            }
            cresep.setItems(olscomboresep);
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void kembali() {
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    fis.close();
                    table.getItems().clear();
                    tabledata.clear();
                    ckategori.getItems().clear();
                    olscombo.clear();
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loaddata() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
             String sql = "SELECT m.kode,m.nama,m.kategori,m.harga,m.gambar,"
                    + "m.kode_master_resep,rm.nama_resep FROM menu m "
                    + "LEFT JOIN resep_master rm ON m.kode_master_resep=rm.kode_resep ORDER BY nama DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            pre.setInt(1, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode = res.getString("kode");
                String snama = res.getString("nama");
                String sharga = nf.format(res.getDouble("harga"));
                String skategori = res.getString("kategori");
                String sgambar = "image/" + res.getString("gambar");
                String skode_resep = res.getString("kode_master_resep");
                String snama_resep= res.getString("nama_resep");
                tabledata.add(new Entity(skode, snama, sharga, skategori, sgambar,skode_resep,snama_resep));
            }
            pre.close();
            res.close();
            ch.close();
            kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
            kategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
            nama_resep.setCellValueFactory(new PropertyValueFactory<>("nama_resep"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void loadcombo() {
        String[] data = fh.getkategorimenu().split(";");
        for (int i = 0; i < data.length; i++) {
            olscombo.add(data[i]);
        }
        ckategori.setItems(olscombo);
    }

    private void loadtotaldata() {
        ljumlah.setText(" | " + String.valueOf(tabledata.size()) + " / " + tlimit.getText() + " DATA");
    }

    private void select() {
        table.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    int i = newValue.intValue();
                    if (i < 0) {
                        System.out.println("out");
                    } else {
                        ids = kode.getCellData(i);
                        gambar = tabledata.get(i).lokasigambar;
                        tkode.setText(kode.getCellData(i));
                        tnama.setText(nama.getCellData(i));
                        ckategori.getEditor().setText(kategori.getCellData(i));
                        tharga.setText(harga.getCellData(i));
                        cresep.getEditor().setText(tabledata.get(i).nama_resep+"-"+tabledata.get(i).kode_resep);
                        fis.close();
                        if (gambar != null) {
                            fis = new FileInputStream(new File(gambar));
                            imgpre.setImage(new Image(fis));
                            fis.close();
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                }

            }
        });
    }

    private void rawsimpan() {
        String[] extgambar = tgambar.getText().split("\\.");
        if (ids == null || ids.equals("")) {
            try {
                if (tgambar.getText().equals("")) {
                    String sql = "INSERT INTO menu(kode,nama,kategori,harga,kode_master_resep) VALUES(?,?,?,?,?)";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, tkode.getText());
                    pre.setString(2, tnama.getText());
                    pre.setString(3, ckategori.getEditor().getText());
                    pre.setDouble(4, Double.parseDouble(tharga.getText().replaceAll("[,.]", "")));
                    pre.setString(5, cresep.getEditor().getText().split("-")[1]);
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    oh.sukses("Data Menu Berhasil Disimpan");
                    loaddata();
                    loadtotaldata();
                } else {
                    String sql = "INSERT INTO menu(kode,nama,kategori,harga,gambar,kode_master_resep) VALUES(?,?,?,?,?,?)";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, tkode.getText());
                    pre.setString(2, tnama.getText());
                    pre.setString(3, ckategori.getEditor().getText());
                    pre.setDouble(4, Double.parseDouble(tharga.getText().replaceAll("[,.]", "")));
                    pre.setString(5, tkode.getText() + "." + extgambar[1]);
                    pre.setString(6, cresep.getEditor().getText().split("-")[1]);
                    pre.executeUpdate();
                    pre.close();
                    ch.close();
                    //copygambar(tkode.getText() + "." + extgambar[1], new File(tgambar.getText()));
                    resizeimage(extgambar[1], tkode.getText() + "." + extgambar[1], tgambar.getText());
                    oh.sukses("Data Menu Berhasil Disimpan");
                    loaddata();
                    loadtotaldata();
                }

            } catch (SQLException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                oh.error(ex);
            } finally {
                ch.close();
            }

        } else {
            if (oh.konfirmasi("ubah") == true) {
                try {
                    if (tgambar.getText().equals("")) {
                        String sql = "UPDATE menu SET kode=?,nama=?,kategori=?,harga=?,kode_master_resep=? WHERE kode=?";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.setString(1, tkode.getText());
                        pre.setString(2, tnama.getText());
                        pre.setString(3, ckategori.getEditor().getText());
                        pre.setDouble(4, Double.parseDouble(tharga.getText().replaceAll("[,.]", "")));
                        pre.setString(5, cresep.getEditor().getText().split("-")[1]);
                        pre.setString(6, ids);
                        pre.executeUpdate();
                        pre.close();
                        ch.close();
                        oh.sukses("Data Menu Berhasil Perbaharui");
                        loaddata();
                        loadtotaldata();
                    } else {
                        File gb = new File(gambar);
                        if (gb.delete()) {
                            String sql = "UPDATE menu SET kode=?,nama=?,kategori=?,harga=?,gambar=?,kode_master_resep=? WHERE kode=?";
                            PreparedStatement pre = ch.connect().prepareStatement(sql);
                            pre.setString(1, tkode.getText());
                            pre.setString(2, tnama.getText());
                            pre.setString(3, ckategori.getEditor().getText());
                            pre.setDouble(4, Double.parseDouble(tharga.getText().replaceAll("[,.]", "")));
                            pre.setString(5, tkode.getText() + "." + extgambar[1]);
                            pre.setString(6, cresep.getEditor().getText().split("-")[1]);
                            pre.setString(7, ids);
                            pre.executeUpdate();
                            pre.close();
                            ch.close();
                            //copygambar(tkode.getText() + "." + extgambar[1], new File(tgambar.getText()));
                            resizeimage(extgambar[1], tkode.getText() + "." + extgambar[1], tgambar.getText());
                            oh.sukses("Data Menu Berhasil Perbaharui");
                            loaddata();
                            loadtotaldata();
                        } else {
                            String sql = "UPDATE menu SET kode=?,nama=?,kategori=?,harga=?,gambar=?,kode_master_resep=? WHERE kode=?";
                            PreparedStatement pre = ch.connect().prepareStatement(sql);
                            pre.setString(1, tkode.getText());
                            pre.setString(2, tnama.getText());
                            pre.setString(3, ckategori.getEditor().getText());
                            pre.setDouble(4, Double.parseDouble(tharga.getText().replaceAll("[,.]", "")));
                            pre.setString(5, tkode.getText() + "." + extgambar[1]);
                            pre.setString(6, cresep.getEditor().getText().split("-")[1]);
                            pre.setString(7, ids);
                            pre.executeUpdate();
                            pre.close();
                            ch.close();
                            resizeimage(extgambar[1], tkode.getText() + "." + extgambar[1], tgambar.getText());
                            oh.sukses("Data Menu Berhasil Perbaharui");
                            loaddata();
                            loadtotaldata();
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    oh.error(ex);
                } finally {
                    ch.close();
                }
            }

        }
    }

    private void simpan() {
        bsimpan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                rawsimpan();
            }
        });
    }

    private void rawhapus() {
        if (oh.konfirmasi("hapus") == true) {
            try {
                File gb = new File(gambar);
                if (gb.delete() == true) {
                    String sql = "DELETE FROM menu WHERE kode=?";
                    PreparedStatement pre = ch.connect().prepareStatement(sql);
                    pre.setString(1, ids);
                    pre.executeUpdate();
                    oh.sukses("Data Berhasil Dihapus");
                    pre.close();
                    ch.close();
                    loaddata();
                    loadtotaldata();
                    rawclear();
                } else {
                    oh.gagal("Gambar Tidak ditemukan");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                oh.error(ex);
            } finally {
                ch.close();
            }

        }
    }

    private void hapus() {
        table.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    rawhapus();
                }
            }
        });
    }

    private void rawclear() {
        try {
            ids = "";
            tkode.clear();
            tnama.clear();
            ckategori.getEditor().clear();
            tharga.clear();
            tgambar.clear();
            fis = new FileInputStream(new File("image/def.png"));
            imgpre.setImage(new Image(fis));
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        }
    }

    private void clear() {
        bclear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loaddata();
                loadtotaldata();
                rawclear();
            }
        });
    }

    private void rawcari() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getItems().clear();
        try {
            String sql = "SELECT m.kode,m.nama,m.kategori,m.harga,m.gambar,"
                    + "m.kode_master_resep,rm.nama_resep FROM menu m "
                    + "LEFT JOIN resep_master rm ON m.kode_master_resep=rm.kode_resep WHERE kode ILIKE ? OR "
                    + "nama ILIKE ? OR "
                    + "kategori ILIKE ? OR "
                    + "harga::character varying ILIKE ? ORDER BY nama DESC LIMIT ?";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            for (int i = 0; i < 4; i++) {
                pre.setString(i + 1, "%" + tcari.getText() + "%");
            }
            pre.setInt(5, Integer.parseInt(tlimit.getText()));
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode = res.getString("kode");
                String snama = res.getString("nama");
                String sharga = nf.format(res.getDouble("harga"));
                String skategori = res.getString("kategori");
                String sgambar = "image/" + res.getString("gambar");
                String skode_resep = res.getString("kode_master_resep");
                String snama_resep= res.getString("nama_resep");
                tabledata.add(new Entity(skode, snama, sharga, skategori, sgambar,skode_resep,snama_resep));
            }
            pre.close();
            res.close();
            ch.close();
            kode.setCellValueFactory(new PropertyValueFactory<>("kode"));
            nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
            harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
            kategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
            nama_resep.setCellValueFactory(new PropertyValueFactory<>("nama_resep"));
            table.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void cari() {
        tcari.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                rawcari();
                loadtotaldata();
            }
        });
    }

    private void copygambar(String nama, File sumber) {
        try {
            Files.copy(sumber.toPath(), new File("image/" + nama).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        }
    }

    private void resizeimage(String ext, String nama, String sumber) {
        try {
            BufferedImage sourceimage = ImageIO.read(new File(sumber));
            BufferedImage bi = new BufferedImage(250, 200, sourceimage.getType());
            Graphics2D g2d = bi.createGraphics();
            g2d.drawImage(sourceimage, 0, 0, 250, 200, null);
            g2d.dispose();
            ImageIO.write(bi, ext, new File("image/" + nama));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pilihgambar() {
        bcari.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Cari Gambar");
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image FIle", "*.jpg", "*.png", "*.bmp"));
                File selectedfile = fc.showOpenDialog(sh.getSt());
                if (selectedfile != null) {
                    try {
                        tgambar.setText(selectedfile.getAbsolutePath());
                        fis = new FileInputStream(new File(selectedfile.getPath()));
                        Image img = new Image(fis);
                        imgpre.setImage(img);
                        fis.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                        oh.error(ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                        oh.error(ex);
                    }

                }
            }
        });

    }

    public class Entity {

        String kode, nama, harga, kategori, lokasigambar, kode_resep, nama_resep;

        public Entity(String kode, String nama, String harga, String kategori,
                String lokasigambar, String kode_resep, String nama_resep) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
            this.kategori = kategori;
            this.lokasigambar = lokasigambar;
            this.kode_resep = kode_resep;
            this.nama_resep = nama_resep;
        }

        public String getKode() {
            return kode;
        }

        public void setKode(String kode) {
            this.kode = kode;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getHarga() {
            return harga;
        }

        public void setHarga(String harga) {
            this.harga = harga;
        }

        public String getKategori() {
            return kategori;
        }

        public void setKategori(String kategori) {
            this.kategori = kategori;
        }

        public String getLokasigambar() {
            return lokasigambar;
        }

        public void setLokasigambar(String lokasigambar) {
            this.lokasigambar = lokasigambar;
        }

        public String getKode_resep() {
            return kode_resep;
        }

        public void setKode_resep(String kode_resep) {
            this.kode_resep = kode_resep;
        }

        public String getNama_resep() {
            return nama_resep;
        }

        public void setNama_resep(String nama_resep) {
            this.nama_resep = nama_resep;
        }
        
        

    }

}
