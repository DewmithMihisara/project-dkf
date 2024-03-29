package lk.ijse.project_dkf.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class PackingTM {
    private Date date;
    private Time time;
    private String clId;
    private String size;
    private int qty;
}
