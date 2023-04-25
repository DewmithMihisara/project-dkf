package lk.ijse.project_dkf.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.project_dkf.dto.LogHistory;
import lk.ijse.project_dkf.dto.User;
import lk.ijse.project_dkf.model.LogInModel;
import lk.ijse.project_dkf.notification.PopUps;
import lk.ijse.project_dkf.util.AlertTypes;
import lk.ijse.project_dkf.util.Navigation;
import lk.ijse.project_dkf.util.Rout;
import lk.ijse.project_dkf.validation.inputsValidation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogInFormController {
    @FXML
    private Button frgtPwBtn;
    @FXML
    private TextField pwTxt;
    @FXML
    private AnchorPane root;
    @FXML
    private Button sgnMainBtn;
    @FXML
    private Button signUpBtn;
    @FXML
    private TextField usrTxt;

    public static User user;
    public static LogHistory logHistory;
    boolean uName,pw;
    @FXML
    void frgtPwBtnOnActon(ActionEvent event) {
    }

    @FXML
    void sgnMainBtnOnAction(ActionEvent event) throws IOException {
        pw= inputsValidation.isNullTxt(pwTxt);
        uName=inputsValidation.isNullTxt(usrTxt);

        if (pw && uName){
            try {
                user = LogInModel.isCorrect(usrTxt.getText());
                if (user.getPassword().equals(pwTxt.getText())) {
                    Navigation.navigation(Rout.MAIN_DASHBOARD, root);
                    logHistory=new LogHistory();
                    logHistory.setUsrName(usrTxt.getText());
                    logHistory.setLogIn(LocalDateTime.now());
                } else {
                    PopUps.popUps(AlertTypes.ERROR, "Password is Wrong", "Your password is wrong. Try again");
                    pwTxt.clear();
                }
            } catch (Exception e) {
                PopUps.popUps(AlertTypes.ERROR, "User-name is Wrong", "Your user-name is wrong. Try again");
                usrTxt.clear();
                pwTxt.clear();
            }
        }
    }

    @FXML
    void signUpBtnOnAction(ActionEvent event) throws IOException {
        Navigation.navigation(Rout.NEW_AC, root);
    }

    @FXML
    void clzBtnOnAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void pwOnAction(ActionEvent event) {
        sgnMainBtn.fire();
    }

    @FXML
    void usrOnAction(ActionEvent event) {
        pwTxt.requestFocus();
    }
}
