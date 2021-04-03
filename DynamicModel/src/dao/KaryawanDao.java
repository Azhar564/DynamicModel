/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Data;

/**
 *
 * @author azhar
 */
public class KaryawanDao {
    private static int numColumn, numTable;
    private final Connection koneksi;
    private PreparedStatement preSmt;
    private ResultSet rs;
    
    public KaryawanDao(){
        koneksi = Koneksi.getConnection();
        try{
            String sql = "SELECT * FROM karyawan";
                preSmt = koneksi.prepareStatement(sql);
                rs = preSmt.executeQuery();
            numTable = 0;
            numColumn = rs.getMetaData().getColumnCount();
            while(rs.next()){
                numTable++;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    public Data getData(){
        Object[][] get;
        Data data = new Data();
        
        try {
                String sql = "SELECT * FROM karyawan";
                preSmt = koneksi.prepareStatement(sql);
                rs = preSmt.executeQuery();
                
                get = new Object[getRowCount(rs)][numColumn];
                
                int i = 0;
                while (rs.next()) {
                    
                    for (int j = 0; j < 4; j++) {
                        get[i][j] = rs.getObject(j+1);
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
    
    public Object[] getDataById(Object id){
        Object[][] data = getData().getData();
        Object[] column = new Object[numColumn];
        
        for (int i = 0; i < numTable; i++) {
            if (id == data[i][0]) {
                for (int j = 0; j < numColumn; j++) {
                    column[j] = data[i][j]; 
                }
            }
        }
        
        return column;
    }
    
    public void saveData(Data data, String page){
        
    }
    
    //count the number of rows and return as int
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
        KaryawanDao dao = new KaryawanDao();
        System.out.println(dao.getDataById(2)[0]);
    }
}
