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
import lk.ijse.project_dkf.db.DBConnection;
import lk.ijse.project_dkf.dto.Buyer;
import lk.ijse.project_dkf.dto.OrderRatio;
import lk.ijse.project_dkf.dto.Output;
import lk.ijse.project_dkf.dto.Pack;
import lk.ijse.project_dkf.dto.tm.OrderRatioTM;
import lk.ijse.project_dkf.dto.tm.OutputTM;
import lk.ijse.project_dkf.dto.tm.PackingTM;
import lk.ijse.project_dkf.model.*;
import lk.ijse.project_dkf.notification.PopUps;
import lk.ijse.project_dkf.util.AlertTypes;
import lk.ijse.project_dkf.util.Navigation;
import lk.ijse.project_dkf.util.Rout;
import lk.ijse.project_dkf.validation.inputsValidation;
import lombok.Getter;
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

@Getter
public class OutputFormController implements Initializable {
    @FXML
    private Label timeLbl;
    @FXML
    private AnchorPane root;
    @FXML
    private TableColumn<?, ?> clrCol;
    @FXML
    private TableColumn<?, ?> dateCol;
    @FXML
    private TableColumn<?, ?> timeCol;
    @FXML
    private TableColumn<?, ?> sizeCol;
    @FXML
    private TableColumn<?, ?> qtyCol;
    @FXML
    private TableView<OutputTM> outTbl;
    @FXML
    private ComboBox<String> clrCmbBx;
    @FXML
    private Text dateTxt;
    @FXML
    private ComboBox<String> orderIdCmbBox;
    @FXML
    private TextField qtyTxt;
    @FXML
    private ComboBox<String> sizeCmbBx;
    boolean clId,size,qty;

    {
        clId=false;
        size=false;
        qty=false;
    }
    @FXML
    void addBtnOnAction(ActionEvent event) {
        clId= inputsValidation.isNullCmb(clrCmbBx);
        size=inputsValidation.isNullCmb(sizeCmbBx);
        qty=inputsValidation.isNumberOrNull(qtyTxt);

        if (clId && size && qty){
            Output output = new Output(
                    orderIdCmbBox.getSelectionModel().getSelectedItem(),
                    Date.valueOf(dateTxt.getText()),
                    Time.valueOf(timeLbl.getText()),
                    clrCmbBx.getSelectionModel().getSelectedItem(),
                    sizeCmbBx.getSelectionModel().getSelectedItem(),
                    Integer.parseInt(qtyTxt.getText())
            );
            try {
                boolean affectedRows = OutputModel.add(output);
                if (affectedRows) {
                    PopUps.popUps(AlertTypes.CONFORMATION, "Output Add", "Output is add properly.");
                }
            } catch (SQLException e) {
                PopUps.popUps(AlertTypes.WARNING, "SQL Warning", "Database error when add output.");
            }finally {
                loadValues(orderIdCmbBox.getSelectionModel().getSelectedItem());
                clrCmbBx.setValue(null);
                sizeCmbBx.setValue(null);
                qtyTxt.setText("");
            }
        }

    }
    @FXML
    void reportOnAction(ActionEvent event) throws JRException, SQLException {
        if (inputsValidation.isNullCmb(orderIdCmbBox)){
            Thread printThread = new Thread(() -> {
                try {
                    InputStream rpt = ShipingFormController.class.getResourceAsStream("/reports/DailyOut.jrxml");
                    JasperReport compile =  JasperCompileManager.compileReport(rpt);
                    Map<String,Object> data = new HashMap<>();
                    data.put("orderId",orderIdCmbBox.getSelectionModel().getSelectedItem());
                    data.put("date",Date.valueOf(dateTxt.getText()));
                    JasperPrint report = JasperFillManager.fillReport(compile,data, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(report,false);
                } catch (JRException | SQLException e){
                    e.printStackTrace();
                }
            });
            printThread.start();
        }else {
            ShakeTextAnimation.ShakeText(orderIdCmbBox);
            PopUps.popUps(AlertTypes.ERROR, "REPORTS", "Report cant print.");
        }

    }
    @FXML
    void deleteBtnOnAction(ActionEvent event) {
        OutputTM output=outTbl.getSelectionModel().getSelectedItem();
        try {
            boolean delete = OutputModel.delete(output, orderIdCmbBox.getSelectionModel().getSelectedItem());
            if (delete) {
                PopUps.popUps(AlertTypes.CONFORMATION, "Output Add", "Output is delete properly.");
            }

        } catch (SQLException e) {
            PopUps.popUps(AlertTypes.WARNING, "SQL Warning", "Database error when delete output.");
        }finally {
            loadValues(orderIdCmbBox.getSelectionModel().getSelectedItem());
        }
    }
    @FXML
    void orderIdOnAction(ActionEvent event) {
        loadValues(orderIdCmbBox.getSelectionModel().getSelectedItem());
        loadClotheId();
        clrCmbBx.setDisable(false);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOrderDate();
        setCellValueFactory();
        loadOrderIds();
        loadSize();
        setTime();
    }
    private void loadSize() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> clr = new ArrayList<>();
        clr.add("S");
        clr.add("M");
        clr.add("L");
        clr.add("XL");
        clr.add("XXl");

        obList.addAll(clr);
        sizeCmbBx.setItems(obList);
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
    private void loadOrderIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> ids = null;
        try {
            ids = IdModel.loadOrderIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (String id : ids) {
            obList.add(id);
        }
        orderIdCmbBox.setItems(obList);
    }
    private void setOrderDate() {
        dateTxt.setText(String.valueOf(LocalDate.now()));
    }
    private void loadValues(String id) {
        ObservableList<OutputTM> outputTMS = FXCollections.observableArrayList();
        try {
            List<Output> all = OutputModel.getAll(id);
            for (Output output: all){
                outputTMS.add(new OutputTM(
                        output.getDate(),
                        output.getTime(),
                        output.getClId(),
                        output.getSize(),
                        output.getOut()
                ));
            }
            outTbl.setItems(outputTMS);
        } catch (SQLException e) {
            PopUps.popUps(AlertTypes.WARNING, "SQL Warning", "Database error when load values.");        }
    }
    private void setCellValueFactory() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        clrCol.setCellValueFactory(new PropertyValueFactory<>("clId"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("out"));
    }
    void setTime(){
        SetTime.setTime(timeLbl);
    }
}
