/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import model.Data;



/**
 *
 * @author azhar
 */
public class UniversalCrud {
    //init
    public int numColumn, numRow;
    public String[] nameColumn;

    //setting
    public String nameTable = "karyawan";    
    private final Connection koneksi;
    private PreparedStatement preSmt;
    private ResultSet rs;
    
    public UniversalCrud(String table){
        koneksi = Koneksi.getConnection();
        nameTable = table;
    }
    
    public ResultSet init(String query){
        ResultSet rse;
        try{
            String sql = query;
            preSmt = koneksi.prepareStatement(sql);
            rse = preSmt.executeQuery();
            
            numColumn = rse.getMetaData().getColumnCount();
            nameColumn = getColumnName(rse);
            numRow = getRowCount(rse);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            rse = null;
        }
        return rse;
    }
    
    private int getRowCount(ResultSet rs){
        int size = 0;
        try {
            rs.last();
            size = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException ex) {
            System.out.println("gagal row count : " + ex.getMessage());
        }
        return size;
    }
    
    private String[] getColumnName(ResultSet rs){
        
        String[] name = new String[numColumn];
        
        try {
            
            for (int i = 0; i < numColumn; i++) {
                name[i] = rs.getMetaData().getColumnName(i+1);
            }
        } catch (SQLException ex) {
            System.out.println("gagal get kolom : " + ex.getMessage());
        }
        return name;
        
    }
    
    public Data getAllData(String table){
        Object[][] get;
        Data data = new Data();
        
        try {
                String sql = "SELECT * FROM "+table;
                rs = init(sql);
                
                get = new Object[numRow][numColumn];
                
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
    
    public Object[] getDataById(Object id, String table){
        Object[][] data = getAllData(table).getData();
        Object[] column = new Object[numColumn];
        
        for (int i = 0; i < numRow; i++) {
            if (id.equals(data[i][0])) {
                System.arraycopy(data[i], 0, column, 0, numColumn);
            }
        }
        
        return column;
    }
    
    private String removeFirstandLast(String str)
    {
        str = str.substring(1, str.length() - 1);
        return str;
    }
    
    public boolean saveData(Object[] data, save set){
        String sql = null;
        if (set.equals(save.add)) {
                sql = "Insert into "+nameTable+" (" + removeFirstandLast(Arrays.toString(nameColumn)) + ") values"+" (";
                for (int i = 0; i < data.length-1; i++) {
                    sql += "'"+data[i]+"', ";
                }
                sql += "'"+data[data.length - 1] + "') ";
            }   
            else if (set.equals(save.edit)){
                getAllData(nameTable);
                sql = "Update "+nameTable+" set ";
                String[] columnName = getColumnName(rs);
                for (int i = 1; i < data.length -1; i++) {
                    sql += columnName[i]+"='"+data[i]+"', ";
                }
                sql += columnName[columnName.length-1]+"='"+data[data.length-1]+"' where "
                        +columnName[0]+"='"+ data[0]+"'";
        }
        try{
            System.out.println(sql);
            preSmt = koneksi.prepareStatement(sql);
            preSmt.executeUpdate();
            
            return true;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        
    }
    
    public boolean DeleteDataById(Object id){
        try{
            String sql = "Delete from "+nameTable+" where "+nameColumn[0]+"='"+id+"'";
            preSmt = koneksi.prepareStatement(sql);
            preSmt.executeUpdate();
            return true;
        }
        catch(SQLException e){
            System.out.println("gagal delete "+ e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
//        UniversalCrud dao = new UniversalCrud("karyawan");
//        Object[] get = dao.getDataById("K001", dao.nameTable);
//        get[2] = "11462333";
//        System.out.println(dao.DeleteDataById("K001"));

    }
}
