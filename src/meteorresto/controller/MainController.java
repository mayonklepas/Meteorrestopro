/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;

/**
 *
 * @author user
 */
public class MainController implements Initializable {

    @FXML
    private Pane pmeja;

    @FXML
    private Pane pmenu;

    @FXML
    private Pane plaporan;

    @FXML
    private Pane psetting;

    @FXML
    private Pane pakun;

    @FXML
    private Label luser;

    Sessionhelper sh = new Sessionhelper();
    Operationhelper oh = new Operationhelper();
    @FXML
    private AnchorPane mainpane;
    @FXML
    private Pane pbahan;
    @FXML
    private Pane presep;
    @FXML
    private Pane ppenjualan;
    @FXML
    private Pane ppembelian;
    @FXML
    private Pane pcatatan;
    @FXML
    private Pane pperkiraan;
    @FXML
    private LineChart<String, Number> gpenjualanperhari;
    NumberAxis gpenjualanharix = new NumberAxis();
    NumberAxis gpenjualanhariy = new NumberAxis();
    @FXML
    private BarChart<String, Number> gtopselling;
    NumberAxis gtopsellingx = new NumberAxis();
    NumberAxis gtopsellingy = new NumberAxis();
    @FXML
    private AreaChart<String, Number> gpenjulanperbulan;
    NumberAxis gpenjualanbulanx = new NumberAxis();
    NumberAxis gpenjualanbulany = new NumberAxis();
    @FXML
    private TableView<Entitybahan> tablebahan;
    @FXML
    private TableColumn<Entitybahan, String> kode_bahan;
    @FXML
    private TableColumn<Entitybahan, String> nama_bahan;
    @FXML
    private TableColumn<Entitybahan, String> jumlah_g;
    @FXML
    private TableColumn<Entitybahan, String> jumlah_kg;
    Connectionhelper ch = new Connectionhelper();
    FIlehelper fh = new FIlehelper();
    ObservableList<Entitybahan> tabledata = FXCollections.observableArrayList();
    NumberFormat nf = NumberFormat.getInstance();
    @FXML
    private Label lnamacafe;
    @FXML
    private Label tharian;
    @FXML
    private Label ttopselling;
    @FXML
    private Label tbulanan;
    @FXML
    private ImageView imglogo;
    @FXML
    private Pane puang;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        menu();
        meja();
        akun();
        transaksi();
        laporan();
        setting();
        bahan();
        pembelian();
        catatan();
        resep();
        perkiraan();
        about();
        akunuang();
        closeapp();
        lnamacafe.setText(fh.getinfo().split(";")[0]);
        luser.setText(sh.getUsername());
        mainpane.setId("tema");
        pmeja.setId("pane");
        pmenu.setId("pane");
        puang.setId("pane");
        plaporan.setId("pane");
        ppenjualan.setId("pane");
        ppembelian.setId("pane");
        psetting.setId("pane");
        pakun.setId("pane");
        pbahan.setId("pane");
        presep.setId("pane");
        pcatatan.setId("pane");
        pperkiraan.setId("pane");
        tharian.setText("Grafik Penjualan Harian Bulan " + new SimpleDateFormat("MMMM YYYY").format(new Date()) + " (1000)");
        tbulanan.setText("Grafik Penjualan Bulanan Tahun " + new SimpleDateFormat("YYYY").format(new Date()) + " (1000)");
        ttopselling.setText("Grafik 10 Top Selling Menu Bulan " + new SimpleDateFormat("MMMM YYYY").format(new Date()));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    loaddatabahan();
                    loadpenjulanharichart();
                    loadpenjulanbulanchart();
                    loadtopselling();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void about() {
        imglogo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Stage st = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Tentang.fxml"));
                    Scene sc = new Scene(root);
                    st.setScene(sc);
                    st.setTitle("Tentang Aplikasi");
                    String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                    st.getScene().getStylesheets().clear();
                    st.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                    st.showAndWait();
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadpenjulanharichart() {
        XYChart.Series linechart = new XYChart.Series();
        linechart.setName("Penjualan Harian");
        try {
            String sql = "SELECT EXTRACT(DAY FROM tanggal) as hari,SUM(harga_masing*jumlah) AS total FROM transaksi "
                    + "WHERE EXTRACT(MONTH FROM tanggal)= EXTRACT(MONTH FROM now()) GROUP BY hari ORDER BY hari ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                linechart.getData().add(new XYChart.Data(res.getString("hari"), res.getDouble("total") / 1000));
            }
            pre.close();
            res.close();
            ch.close();
            gpenjualanperhari.getData().setAll(linechart);
            for (XYChart.Series<String, Number> s : gpenjualanperhari.getData()) {
                for (XYChart.Data<String, Number> d : s.getData()) {
                    Tooltip t = new Tooltip();
                    t.setText("Tanggal : " + d.getXValue().toString() + "\n"
                            + "Jumlah : Rp. " + nf.format(d.getYValue().doubleValue() * 1000));
                    t.setFont(new Font(16));
                    Tooltip.install(d.getNode(), t);

                    //Adding class on hover
                    d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                    //Removing class on exit
                    d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void loadpenjulanbulanchart() {
        XYChart.Series area = new XYChart.Series();
        try {
            String sql = "SELECT EXTRACT(MONTH FROM tanggal) as bulan,SUM(harga_masing*jumlah) AS total FROM transaksi "
                    + "WHERE EXTRACT(YEAR FROM tanggal)= EXTRACT(YEAR FROM now()) GROUP BY bulan ORDER BY bulan ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                area.getData().add(new XYChart.Data(res.getString("bulan"), res.getDouble("total") / 1000));
            }
            pre.close();
            res.close();
            ch.close();
            gpenjulanperbulan.getData().setAll(area);
            for (XYChart.Series<String, Number> s : gpenjulanperbulan.getData()) {
                for (XYChart.Data<String, Number> d : s.getData()) {
                    Tooltip t = new Tooltip();
                    t.setText("Bulan : " + d.getXValue().toString() + "\n"
                            + "Jumlah : Rp. " + nf.format(d.getYValue().doubleValue() * 1000));
                    t.setFont(new Font(16));
                    Tooltip.install(d.getNode(), t);
                    //Adding class on hover
                    d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                    //Removing class on exit
                    d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void loadtopselling() {
        XYChart.Series bar = new XYChart.Series();
        try {
            String sql = "SELECT kode_menu,menu.nama,SUM(jumlah) AS total FROM transaksi INNER JOIN menu ON "
                    + "transaksi.kode_menu=menu.kode WHERE EXTRACT(MONTH FROM tanggal)= EXTRACT(MONTH FROM now()) "
                    + "GROUP BY kode_menu,menu.nama ORDER BY kode_menu LIMIT 10";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                bar.getData().add(new XYChart.Data(res.getString("nama"), res.getDouble("total")));
            }

            pre.close();
            res.close();
            ch.close();
            gtopselling.getData().setAll(bar);
            for (XYChart.Series<String, Number> s : gtopselling.getData()) {
                for (XYChart.Data<String, Number> d : s.getData()) {

                    Tooltip t = new Tooltip();
                    t.setText("Menu : " + d.getXValue().toString() + "\n"
                            + "Terjual : " + d.getYValue().intValue() + " Item");
                    t.setFont(new Font(16));
                    Tooltip.install(d.getNode(), t);

                    //Adding class on hover
                    d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                    //Removing class on exit
                    d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }

    }

    private void loaddatabahan() {
        tablebahan.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablebahan.getItems().clear();
        try {
            String sql = "SELECT kode,nama,jumlah,(jumlah / 1000) AS jumlahkg FROM bahan ORDER BY jumlah ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                String skode = res.getString("kode");
                String snama = res.getString("nama");
                String sjumlah = nf.format(res.getDouble("jumlah")) + " gram";
                String sjumlahkg = nf.format(res.getDouble("jumlahkg")) + " Kg";
                tabledata.add(new Entitybahan(skode, snama, sjumlah, sjumlahkg));
            }
            pre.close();
            res.close();
            ch.close();
            kode_bahan.setCellValueFactory(new PropertyValueFactory<>("kode_bahan"));
            nama_bahan.setCellValueFactory(new PropertyValueFactory<>("nama_bahan"));
            jumlah_g.setCellValueFactory(new PropertyValueFactory<>("jumlah_g"));
            jumlah_kg.setCellValueFactory(new PropertyValueFactory<>("jumlah_kg"));
            tablebahan.setItems(tabledata);
        } catch (SQLException ex) {
            Logger.getLogger(BahanController.class.getName()).log(Level.SEVERE, null, ex);
            oh.error(ex);
        } finally {
            ch.close();
        }
    }

    private void menu() {
        pmenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Menu.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });

    }

    private void meja() {
        pmeja.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Meja.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void akun() {
        pakun.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Akun.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void akunuang() {
        puang.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Akunuang.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void bahan() {
        pbahan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Bahan.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void transaksi() {
        ppenjualan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Transaksi.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void laporan() {
        plaporan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin") || sh.getTipe().equals("spv")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Laporan.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void setting() {
        psetting.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Setting.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void pembelian() {
        ppembelian.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Pembelian.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void catatan() {
        pcatatan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Catatan.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void resep() {
        presep.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Resep.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    private void perkiraan() {
        pperkiraan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (sh.getTipe().equals("admin")) {
                    try {
                        FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Perkiraan.fxml"));
                        Parent root = fxl.load();
                        sh.getSt().getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    oh.gagal("Maaf Akses Ditolak");
                }
            }
        });
    }

    public class Entitybahan {

        String kode_bahan, nama_bahan, jumlah_g, jumlah_kg;

        public Entitybahan(String kode_bahan, String nama_bahan, String jumlah_g, String jumlah_kg) {
            this.kode_bahan = kode_bahan;
            this.nama_bahan = nama_bahan;
            this.jumlah_g = jumlah_g;
            this.jumlah_kg = jumlah_kg;
        }

        public String getKode_bahan() {
            return kode_bahan;
        }

        public void setKode_bahan(String kode_bahan) {
            this.kode_bahan = kode_bahan;
        }

        public String getNama_bahan() {
            return nama_bahan;
        }

        public void setNama_bahan(String nama_bahan) {
            this.nama_bahan = nama_bahan;
        }

        public String getJumlah_g() {
            return jumlah_g;
        }

        public void setJumlah_g(String jumlah_g) {
            this.jumlah_g = jumlah_g;
        }

        public String getJumlah_kg() {
            return jumlah_kg;
        }

        public void setJumlah_kg(String jumlah_kg) {
            this.jumlah_kg = jumlah_kg;
        }

    }

    private void closeapp() {
        sh.getSt().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    Stage st = new Stage();
                    st.getIcons().add(new Image(getClass().getResource("/meteorresto/icon/icon.png").toString()));
                    st.resizableProperty().setValue(Boolean.FALSE);
                    st.setTitle("Restoku Login");
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Login.fxml"));
                    Scene sc = new Scene(root);
                    st.setScene(sc);
                    String css = new File("style.css").getAbsolutePath().replace(" ", "%20");
                    st.getScene().getStylesheets().clear();
                    st.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                    st.show();
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
