package lk.ijse.project_dkf.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lk.ijse.project_dkf.dto.Buyer;
import lk.ijse.project_dkf.dto.OrderRatio;
import lk.ijse.project_dkf.dto.Output;
import lk.ijse.project_dkf.dto.tm.OrderRatioTM;
import lk.ijse.project_dkf.model.*;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OutputFormController implements Initializable {
    @FXML
    private TableColumn<?, ?> clrCol;

    @FXML
    private TableColumn<?, ?> dateCol;
    @FXML
    private TableColumn<?, ?> oIdCol;
    @FXML
    private TableColumn<?, ?> sizeCol;
    @FXML
    private TableColumn<?, ?> qtyCol;
    @FXML
    private TableView<Output> outTbl;
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

    @FXML
    void addBtnOnAction(ActionEvent event) {
        Output output=new Output(
                orderIdCmbBox.getSelectionModel().getSelectedItem(),
                Date.valueOf(dateTxt.getText()),
                clrCmbBx.getSelectionModel().getSelectedItem(),
                sizeCmbBx.getSelectionModel().getSelectedItem(),
                Integer.parseInt(qtyTxt.getText())
        );
        try {
            boolean affectedRows= OutputModel.add(output);
            if (affectedRows ) {
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Add!")
                        .show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Something is wrong")
                    .show();
        }
    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {
        Output output = outTbl.getSelectionModel().getSelectedItem();
        try {
            boolean delete=OutputModel.delete(output);
            if (delete){
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Deleted !")
                        .show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Something is wrong")
                    .show();
        }
    }
    @FXML
    void orderIdOnAction(ActionEvent event) {
        loadClr();
        clrCmbBx.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOrderDate();
        setCellValueFactory();
        loadValues();
        loadOrderIds();
        loadSize();
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

    private void loadClr() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> ids = null;
        try {
            ids = IdModel.loadClr(orderIdCmbBox.getSelectionModel().getSelectedItem());
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
    private void loadValues() {
        ObservableList<Output> outObj = FXCollections.observableArrayList();
        try {
            List<Output> all = OutputModel.getAll(orderIdCmbBox.getSelectionModel().getSelectedItem());
            for (Output output: all){
                outObj.add(new Output(
                        output.getOId(),
                        output.getDate(),
                        output.getClr(),
                        output.getSize(),
                        output.getOut()
                ));
            }
            outTbl.setItems(outObj);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Something is wrong")
                    .show();
        }
    }

    private void setCellValueFactory() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        oIdCol.setCellValueFactory(new PropertyValueFactory<>("oId"));
        clrCol.setCellValueFactory(new PropertyValueFactory<>("clr"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("out"));
    }
}
