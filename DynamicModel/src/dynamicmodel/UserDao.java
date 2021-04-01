/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicmodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import connection.Koneksi;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import model.Data;
/**
 *
 * @author azhar
 */
public class UserDao {

    private final Connection koneksi;
    private PreparedStatement preSmt;
    private ResultSet rs;
    
    public UserDao(){
        koneksi = Koneksi.getConnection();
    }
    
    public Data getData(){
        Object[][] get /*=new Object[4][4]*/;
        Data data = new Data();
        try {
                String sql = "SELECT * FROM user";
                preSmt = koneksi.prepareStatement(sql);
                rs = preSmt.executeQuery();
                get = new Object[getRowCount(rs)][4];
                int i = 0;
                while (rs.next()) {
                    
                    for (int j = 0; j < 4; j++) {
                        get[i][j] = rs.getObject(j+1);
                        System.out.println(rs.getObject(j+1));
                    }
                    data.setData(get);
                    
                    i++;
                }
            } 
        
        catch (SQLException e ) {
            System.out.println("gagal get data user : "+e);
        }
        
        return data;
    }
    
    private int getRowCount(ResultSet resultSet) {
    if (resultSet == null) {
        return 0;
    }

    try {
        if (resultSet.last()) {
            return resultSet.getRow();
        }
    } catch (SQLException exp) {
        exp.printStackTrace();
    } finally {
        try {
            resultSet.beforeFirst();
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    return 0;
}
    
    public static void main(String[] args) {
        UserDao dao = new UserDao();
        System.out.println(dao.getData().getData()[0][3]);
    }
}
