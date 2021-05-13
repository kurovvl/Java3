package GU.Lesson02.Models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel extends DBHandler {
    private final static String tableName = "users";
    private int userID;
    private String login;
    private String password;
    private String nick;
    private boolean userInitialized = false;


    public boolean isUserInitialized() {
        return userInitialized;
    }

    public int getUserID() {
        return userID;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNick() {
        return nick;
    }

    //---

    private final static String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
            "        UserID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
            "        UserLogin TEXT DEFAULT NULL,\n" +
            "        UserPass TEXT DEFAULT NULL,\n" +
            "        UserName TEXT DEFAULT NULL\n" +
            "        )";


    //---


    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password != codePass(oldPassword)) return false;
        boolean res = changeParameter("UserPass", codePass(newPassword));
        if (res) this.password = password;
        return res;
    }

    public boolean changeName(String name) {
        boolean res = changeParameter("UserName", name);
        if (res) this.nick = name;
        return res;
    }

//    private UserModel(int userID, String login, String password, String name) {
//        this.userID = userID;
//        this.login = login;
//        this.password = password;
//        this.nick = name;
//    }

    public UserModel() {
        super(createTableSQL);

    }

    // Ух, я думал в яве есть что-то готовое для генерации мд5, а тут такое  - спасибо стэкОверфлоу
    private static String codePass(String password) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = bytesToHex(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    private boolean changeParameter(String par, String newValue) {
        //System.out.println(par);
        if (!userInitialized) return false;

        // а что, разве нет поддержки update top (1) ?? как так? у меня это в привычке уже писать так

        String sqlQuery = String.format("update " + tableName + " set %1$s = '%2$s' where UserID = %3$d", par, newValue, this.userID);
        //System.out.println(sqlQuery);
        return super.executeQuery(sqlQuery);
    }

    public boolean createUser(String login, String password, String name) {
        if (userInitialized) return false;
        String sqlQuery = String.format("insert into " + tableName + " (UserLogin,UserPass,UserName) values ('%1$s','%2$s','%3$s')", login, codePass(password), name);
        //System.out.println(sqlQuery);
        int y = super.insertQuery(sqlQuery);
        //System.out.println(y);
        return y > 0;
    }

    public UserModel getUser(String login, String password) {
        String sqlQuery = String.format("select * from " + tableName + " where UserLogin = '%1$s' and UserPass = '%2$s'", login, codePass(password));
        ResultSet rs = super.selectQuery(sqlQuery);
        // а тут я хотел сделать через Field, но все время получалось из простой задачи вырастал монстр =(
        try {
            if (rs.next()) {
                this.userID = rs.getInt("UserID");
                this.login = rs.getString("UserLogin");
                this.password = rs.getString("UserPass");
                this.nick = rs.getString("UserName");
                this.userInitialized = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("ID = %4$d, Login = %1$s, Password = %2$s, Name = %3$s ", this.login, this.password, this.nick, this.userID);
    }

}
