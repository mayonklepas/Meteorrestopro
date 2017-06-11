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
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import meteorresto.helper.Connectionhelper;
import meteorresto.helper.FIlehelper;
import meteorresto.helper.Operationhelper;
import meteorresto.helper.Sessionhelper;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author user
 */
public class LaporanController implements Initializable {

    @FXML
    private Pane pkembali;
    @FXML
    private Label luser;
    @FXML
    private SwingNode sn;
    @FXML
    private ComboBox<String> ckategori;
    @FXML
    private DatePicker ddari;
    @FXML
    private Button bcetak, bclear;
    Sessionhelper sh = new Sessionhelper();
    FIlehelper fh = new FIlehelper();
    Connectionhelper ch = new Connectionhelper();
    Operationhelper oh = new Operationhelper();
    @FXML
    private DatePicker dke;
    @FXML
    private AnchorPane header;
    @FXML
    private AnchorPane footer;
    @FXML
    private TextField tfilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        kembali();
        loadkategori();
        printlaporan();
        luser.setText(sh.getUsername());
        bersikandata();
        bcetak.setId("bc");
        bclear.setId("bc");
        header.setId("tema");
        footer.setId("tema");
        onchangecombo();
        ddari.setDisable(true);
        dke.setDisable(true);
        tfilter.setDisable(true);
    }

    private void bersikandata() {
        bclear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (oh.konfirmasi("Hapus") == true) {
                    try {
                        String sql = "DELETE FROM transaksi;"
                                + "ALTER TABLE transaksi DROP id;"
                                + "ALTER TABLE transaksi ADD id serial;"
                                + "UPDATE meja SET status=0;";
                        PreparedStatement pre = ch.connect().prepareStatement(sql);
                        pre.executeUpdate();
                        oh.sukses("Data Transaksi Telah Dihapus");
                        pre.close();
                        ch.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LaporanController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        ch.close();
                    }
                } else {

                }
            }
        });
    }

    private void kembali() {
        pkembali.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXMLLoader fxl = new FXMLLoader(getClass().getResource("/meteorresto/view/Main.fxml"));
                    Parent root = fxl.load();
                    sh.getSt().getScene().setRoot(root);
                } catch (IOException ex) {
                    Logger.getLogger(LaporanController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadkategori() {
        ObservableList<String> olscombo = FXCollections.observableArrayList();
        olscombo.add("Laporan Harian");
        olscombo.add("Laporan Harian Permenu");
        olscombo.add("Laporan Periodik Permenu");
        olscombo.add("Laporan Periodik Pembelian");
        olscombo.add("Laporan Periodik Catatan Pengeluaran dan Pendapatan");
        olscombo.add("Laporan Periodik Rekap Usaha");
        olscombo.add("Daftar Menu");
        olscombo.add("Laporan Stock Bahan");
        ckategori.setItems(olscombo);
    }

    private void onchangecombo() {
        ckategori.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() == 0) {
                    ddari.setDisable(false);
                    ddari.setValue(LocalDate.now());
                    dke.setDisable(true);
                    tfilter.setDisable(true);
                } else if (newValue.intValue() == 1) {
                    ddari.setDisable(false);
                    ddari.setValue(LocalDate.now());
                    dke.setDisable(true);
                    tfilter.setDisable(false);
                    tfilter.requestFocus();
                } else if (newValue.intValue() == 2) {
                    ddari.setDisable(false);
                    ddari.setValue(LocalDate.now());
                    dke.setDisable(false);
                    dke.setValue(LocalDate.now());
                    tfilter.setDisable(true);
                }else if (newValue.intValue() == 3) {
                    ddari.setDisable(false);
                    ddari.setValue(LocalDate.now());
                    dke.setDisable(false);
                    dke.setValue(LocalDate.now());
                    tfilter.setDisable(true);
                }else if (newValue.intValue() == 4) {
                    ddari.setDisable(false);
                    ddari.setValue(LocalDate.now());
                    dke.setDisable(false);
                    dke.setValue(LocalDate.now());
                    tfilter.setDisable(true);
                }else if (newValue.intValue() == 5) {
                    ddari.setDisable(false);
                    ddari.setValue(LocalDate.now());
                    dke.setDisable(false);
                    dke.setValue(LocalDate.now());
                    tfilter.setDisable(true);
                } else {
                    ddari.setDisable(true);
                    dke.setDisable(true);
                    tfilter.setDisable(true);
                }
            }
        });
    }

    private void printlaporan() {
        bcetak.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st2 = new Stage();
                st2.initStyle(StageStyle.UNDECORATED);
                st2.initModality(Modality.APPLICATION_MODAL);
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/meteorresto/view/Progressdialogcustom.fxml"));
                    Scene sc = new Scene(root);
                    st2.setScene(sc);
                    String css = new File("style.css").getAbsolutePath();
                    st2.getScene().getStylesheets().clear();
                    st2.getScene().getStylesheets().add("file:///" + css.replace("\\", "/"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] info = fh.getinfo().split(";");
                if (ckategori.getEditor().getText().equals("")) {
                    oh.gagal("Maaf anda belum memilih kategori laporan");
                } else {
                    int i = ckategori.getSelectionModel().getSelectedIndex();
                    if (i == 0) {
                        try {
                            Instant dari = Instant.from(ddari.getValue().atStartOfDay(ZoneId.of("GMT")));
                            HashMap hm = new HashMap(3);
                            hm.put("header", info[0]);
                            hm.put("tanggal", new Date().from(dari));
                            hm.put("SUBREPORT_DIR", new File("laporan").getPath() + "/");
                            String path = "laporan/Laporanpenjualanharianmaster.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    } else if (i == 1) {
                        if (tfilter.getText().equals("")) {
                            oh.gagal("Maaf anda belum mengisi kata kunci");
                        } else {
                            try {
                                Instant dari = Instant.from(ddari.getValue().atStartOfDay(ZoneId.of("GMT")));
                                HashMap hm = new HashMap(3);
                                hm.put("header", info[0]);
                                hm.put("tanggal", new Date().from(dari));
                                hm.put("key", "%" + tfilter.getText() + "%");
                                String path = "laporan/Laporanharianpermenu.jasper";
                                loadrepot lr = new loadrepot(path, hm);
                                Thread th = new Thread(lr);
                                th.setDaemon(true);
                                th.start();
                                ch.connect().close();
                                lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) {
                                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                        st2.showAndWait();
                                    }
                                });
                                lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) {
                                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                        st2.close();
                                    }
                                });
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                oh.error(ex);
                            }
                        }
                    }else if (i == 2) {
                        try {
                            Instant dari = Instant.from(ddari.getValue().atStartOfDay(ZoneId.of("GMT")));
                            Instant ke = Instant.from(dke.getValue().atStartOfDay(ZoneId.of("GMT")));
                            HashMap hm = new HashMap(4);
                            hm.put("header", info[0]);
                            hm.put("tanggal_dari", new Date().from(dari));
                            hm.put("tanggal_sampai", new Date().from(ke));
                            hm.put("SUBREPORT_DIR", new File("laporan").getPath() + "/");
                            String path = "laporan/Laporanpenjualanpermenumaster.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    } else if (i == 3) {
                        try {
                            Instant dari = Instant.from(ddari.getValue().atStartOfDay(ZoneId.of("GMT")));
                            Instant ke = Instant.from(dke.getValue().atStartOfDay(ZoneId.of("GMT")));
                            HashMap hm = new HashMap(3);
                            hm.put("header", info[0]);
                            hm.put("tanggal_dari", new Date().from(dari));
                            hm.put("tanggal_sampai", new Date().from(ke));
                            String path = "laporan/Laporanpembelian.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    }else if (i == 4) {
                        try {
                            Instant dari = Instant.from(ddari.getValue().atStartOfDay(ZoneId.of("GMT")));
                            Instant ke = Instant.from(dke.getValue().atStartOfDay(ZoneId.of("GMT")));
                            HashMap hm = new HashMap(4);
                            hm.put("header", info[0]);
                            hm.put("tanggal_dari", new Date().from(dari));
                            hm.put("tanggal_sampai", new Date().from(ke));
                            hm.put("SUBREPORT_DIR", new File("laporan").getPath() + "/");
                            String path = "laporan/Laporancatatanmaster.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    }else if (i == 5) {
                        try {
                            Instant dari = Instant.from(ddari.getValue().atStartOfDay(ZoneId.of("GMT")));
                            Instant ke = Instant.from(dke.getValue().atStartOfDay(ZoneId.of("GMT")));
                            HashMap hm = new HashMap(3);
                            hm.put("header", info[0]);
                            hm.put("tanggal_dari", new Date().from(dari));
                            hm.put("tanggal_sampai", new Date().from(ke));
                            String path = "laporan/Laporanrekap.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    } else if (i == 6) {
                        try {

                            HashMap hm = new HashMap(2);
                            hm.put("header", info[0]);
                            hm.put("SUBREPORT_DIR", new File("laporan").getPath() + "/");
                            String path = "laporan/Laporanmenumaster.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    }else if (i == 7) {
                        try {

                            HashMap hm = new HashMap(1);
                            hm.put("header", info[0]);
                            String path = "laporan/Laporanstockbahan.jasper";
                            loadrepot lr = new loadrepot(path, hm);
                            Thread th = new Thread(lr);
                            th.setDaemon(true);
                            th.start();
                            ch.connect().close();
                            lr.setOnRunning(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.showAndWait();
                                }
                            });
                            lr.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                    st2.close();
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            oh.error(ex);
                        }
                    }
                }

            }

        });
    }

    public class loadrepot extends Task<Void> {

        String path;
        HashMap hm;

        public loadrepot(String path, HashMap hm) {
            this.path = path;
            this.hm = hm;
        }

        @Override
        protected Void call() throws Exception {
            try {
                JasperReport jr = (JasperReport) JRLoader.loadObject(new File(path));
                JasperPrint jp = JasperFillManager.fillReport(jr, hm, ch.connect());
                sn.setContent(new net.sf.jasperreports.swing.JRViewer(jp));
                ch.connect().close();
            } catch (Exception e) {
                e.printStackTrace();
                oh.error(e);
            }

            return null;
        }

    }

}
