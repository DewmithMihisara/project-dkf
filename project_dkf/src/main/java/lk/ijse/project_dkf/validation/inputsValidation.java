package lk.ijse.project_dkf.validation;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lk.ijse.project_dkf.animation.ShakeTextAnimation;
import lk.ijse.project_dkf.animation.defueltText;

import java.sql.Date;

public class inputsValidation {
    public static boolean isNullCmb(ComboBox cmb) {
        if (cmb.getSelectionModel().getSelectedItem()==null) {
            ShakeTextAnimation.ShakeText(cmb);
        } else {
            defueltText.Defuelt(cmb);
            return true;
        }
        return false;
    }
    public static boolean isNumberOrNull(TextField num) {
        if (num.getText().matches("^$")) {
            ShakeTextAnimation.ShakeText(num);
        }
        if (num.getText().matches("^(\\d+)$")) {
            defueltText.Defuelt(num);
            return true;
        } else {
            ShakeTextAnimation.ShakeText(num);
        }
        return false;
    }
    public static boolean isNullTxt(TextField txt) {
        if (txt.getText().matches("^$")) {
            ShakeTextAnimation.ShakeText(txt);
        } else {
            defueltText.Defuelt(txt);
            return true;
        }
        return false;
    }

    public static boolean isNullDate(DatePicker dedlineDate) {
        if (dedlineDate.getValue()==null) {
            ShakeTextAnimation.ShakeText(dedlineDate);
        } else {
            defueltText.Defuelt(dedlineDate);
            return true;
        }
        return false;
    }
}
