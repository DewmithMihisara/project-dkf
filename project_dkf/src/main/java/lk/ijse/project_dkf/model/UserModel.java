package lk.ijse.project_dkf.model;

import lk.ijse.project_dkf.dto.Buyer;
import lk.ijse.project_dkf.dto.User;
import lk.ijse.project_dkf.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public static boolean addUser(User user) throws SQLException {
        String sql ="INSERT INTO User (UserName, Password, UserEmail, UserContact, UserAddress, Position) VALUES(?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(
                sql,
                user.getUserName(),
                user.getPassword(),
                user.getUserEmail(),
                user.getContact(),
                user.getAddress(),
                user.getPosition()
        );
    }

    public static String getOwnerMail() throws SQLException {
        String sql = "SELECT UserEmail FROM User WHERE Position='owner'";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}