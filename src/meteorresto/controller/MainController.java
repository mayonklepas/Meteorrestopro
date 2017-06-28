/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteorresto.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
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
        lnamacafe.setText(fh.getinfo().split(";")[0]);
        luser.setText(sh.getUsername());
        mainpane.setId("tema");
        pmeja.setId("pane");
        pmenu.setId("pane");
        plaporan.setId("pane");
        ppenjualan.setId("pane");
        ppembelian.setId("pane");
        psetting.setId("pane");
        pakun.setId("pane");
        pbahan.setId("pane");
        presep.setId("pane");
        pcatatan.setId("pane");
        pperkiraan.setId("pane");
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

    private void loadpenjulanharichart() {
        XYChart.Series linechart = new XYChart.Series();
        linechart.setName("Penjualan Harian");
        try {
            String sql = "SELECT EXTRACT(DAY FROM tanggal) as hari,SUM(harga_masing*jumlah) AS total FROM transaksi "
                    + "WHERE EXTRACT(MONTH FROM tanggal)= EXTRACT(MONTH FROM now()) GROUP BY hari ORDER BY hari ASC";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                linechart.getData().add(new XYChart.Data(res.getString("hari"), res.getDouble("total")));
            }
            pre.close();
            res.close();
            ch.close();
            gpenjualanperhari.getData().setAll(linechart);
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
                area.getData().add(new XYChart.Data(res.getString("bulan"), res.getDouble("total")));
            }
            gpenjulanperbulan.getData().setAll(area);
            pre.close();
            res.close();
            ch.close();
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
            String sql = "SELECT kode_menu,SUM(jumlah) AS total FROM transaksi INNER JOIN menu ON "
                    + "transaksi.kode_menu=menu.kode GROUP BY kode_menu ORDER BY kode_menu LIMIT 10";
            PreparedStatement pre = ch.connect().prepareStatement(sql);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                bar.getData().add(new XYChart.Data(res.getString("kode_menu"), res.getDouble("total")));
            }
            gtopselling.getData().setAll(bar);
            pre.close();
            res.close();
            ch.close();
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
                if (sh.getTipe().equals("admin")) {
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

}
