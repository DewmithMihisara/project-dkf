package lk.ijse.project_dkf.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.project_dkf.animation.SetTime;
import lk.ijse.project_dkf.animation.ShakeTextAnimation;
import lk.ijse.project_dkf.animation.defueltText;
import lk.ijse.project_dkf.db.DBConnection;
import lk.ijse.project_dkf.dto.Pack;
import lk.ijse.project_dkf.dto.Stock;
import lk.ijse.project_dkf.dto.tm.PackingTM;
import lk.ijse.project_dkf.model.*;
import lk.ijse.project_dkf.notification.PopUps;
import lk.ijse.project_dkf.util.AlertTypes;
import lk.ijse.project_dkf.validation.inputsValidation;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;


public class PackingFormController implements Initializable {
    @FXML
    private Label timeLbl;
    @FXML
    private TableColumn<?, ?> timeColm;
    @FXML
    private ComboBox<String> clrCmbBx;
    @FXML
    private TableColumn<?, ?> clrColm;
    @FXML
    private TableColumn<?, ?> dateColm;
    @FXML
    private Text dateTxt;
    @FXML
    private ComboBox<String> orderIdCmbBox;
    @FXML
    private AnchorPane pane;
    @FXML
    private TableColumn<?, ?> qtyColm;
    @FXML
    private TextField qtyTxt;
    @FXML
    private ComboBox<String> sizeCmbBx;
    @FXML
    private TableColumn<?, ?> sizeColm;
    @FXML
    private TableView<PackingTM> tblPacking;
    boolean clId,size,qty;

    {
        clId=false;
        size=false;
        qty=false;
    }
    @FXML
    void pkBtnOnAction(ActionEvent event) throws JRException, SQLException {
        Thread printThread = new Thread(() -> {
            try {
                if (inputsValidation.isNullCmb(orderIdCmbBox)){
                    InputStream rpt = ShipingFormController.class.getResourceAsStream("/reports/packing.jrxml");
                    JasperReport compile =  JasperCompileManager.compileReport(rpt);
                    Map<String,Object> data = new HashMap<>();
                    data.put("orderId",orderIdCmbBox.getSelectionModel().getSelectedItem());
                    JasperPrint report = JasperFillManager.fillReport(compile,data, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(report,false);
                }
            } catch (JRException | SQLException e) {
                e.printStackTrace();
            }
        });
        printThread.start();
    }
    @FXML
    void addBtnOnAction(ActionEvent event) throws IOException {

        qty=inputsValidation.isNumberOrNull(qtyTxt);
        size= inputsValidation.isNullCmb(sizeCmbBx);
        clId= inputsValidation.isNullCmb(clrCmbBx);

        if (clId && size && qty){
            Pack pack=new Pack(
                    orderIdCmbBox.getSelectionModel().getSelectedItem(),
                    Date.valueOf(dateTxt.getText()),
                    Time.valueOf(timeLbl.getText()),
                    clrCmbBx.getSelectionModel().getSelectedItem(),
                    sizeCmbBx.getSelectionModel().getSelectedItem(),
                    Integer.parseInt(qtyTxt.getText())
            );
            Stock stock =new Stock(
                    clrCmbBx.getSelectionModel().getSelectedItem(),
                    orderIdCmbBox.getSelectionModel().getSelectedItem(),
                    sizeCmbBx.getSelectionModel().getSelectedItem(),
                    Integer.parseInt(qtyTxt.getText())
            );
            try {
                boolean affectedRows= StockPlaceModel.add(pack,stock);
                if (affectedRows){
                    PopUps.popUps(AlertTypes.CONFORMATION, "Pack add", "Packing details is add properly.");
                }
            } catch (SQLException e) {
                PopUps.popUps(AlertTypes.WARNING, "SQL Warning", "Database error when add packing.");
            }finally {
                loadValues(orderIdCmbBox.getSelectionModel().getSelectedItem());
                clrCmbBx.setValue(null);
                sizeCmbBx.setValue(null);
                qtyTxt.setText("");
            }
        }
    }
    @FXML
    void deleteBtnOnAction(ActionEvent event) {
        PackingTM packingTM = tblPacking.getSelectionModel().getSelectedItem();
        try {
            boolean delete=PackingModel.delete(packingTM, orderIdCmbBox.getSelectionModel().getSelectedItem());
            if (delete){
                PopUps.popUps(AlertTypes.CONFORMATION, "Pack Delete", "Packing details is delete properly.");
            }

        } catch (SQLException e) {
            PopUps.popUps(AlertTypes.WARNING, "SQL Warning", "Database error when delete packing.");
        }finally {
            loadValues(orderIdCmbBox.getSelectionModel().getSelectedItem());
        }
    }
    @FXML
    void idOnAction(ActionEvent event) {
        String id= String.valueOf(orderIdCmbBox.getSelectionModel().getSelectedItem());
        loadValues(id);
        loadClotheId();
        clrCmbBx.setDisable(false);

    }
    private void loadClotheId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> ids = null;
        try {
            ids = IdModel.loadClothId(orderIdCmbBox.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (String id : ids) {
            obList.add(id);
        }
        clrCmbBx.setItems(obList);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadOrderIds();
        loadSize();
        setDate();
        setTime();
    }
    private void setDate() {
        dateTxt.setText(String.valueOf(LocalDate.now()));
    }
    private void loadSize() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String>clr=new ArrayList<>();
        clr.add("S");
        clr.add("M");
        clr.add("L");
        clr.add("XL");
        clr.add("XXl");
        obList.addAll(clr);
        sizeCmbBx.setItems(obList);
    }
    private void loadOrderIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> ids = null;
        try {
            ids = IdModel.loadOrderIds();
        } catch (SQLException e) {}

        for (String id : ids) {
            obList.add(id);
        }
        orderIdCmbBox.setItems(obList);
    }
    private void loadValues(String id) {
        ObservableList<PackingTM> packingTMS = FXCollections.observableArrayList();
        try {
            List<Pack> all = PackingModel.getAll(id);
            for (Pack pack: all){
                packingTMS.add(new PackingTM(
                        pack.getDate(),
                        pack.getTime(),
                        pack.getClId(),
                        pack.getSize(),
                        pack.getPackQty()
                ));
            }
            tblPacking.setItems(packingTMS);
        } catch (SQLException e) {
            PopUps.popUps(AlertTypes.WARNING, "SQL Warning", "Database error when load values.");
        }
    }
    private void setCellValueFactory() {
        dateColm.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColm.setCellValueFactory(new PropertyValueFactory<>("time"));
        clrColm.setCellValueFactory(new PropertyValueFactory<>("clId"));
        sizeColm.setCellValueFactory(new PropertyValueFactory<>("size"));
        qtyColm.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }
    void setTime(){
        SetTime.setTime(timeLbl);
    }
}
