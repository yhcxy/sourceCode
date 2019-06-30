package com.yehui.util;


import com.yehui.session.DataSource;
import lombok.Data;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Data
public class MyBatisJDBCUtil {
    public static String url = null;
    public static String username = null;
    public static String password = null;
    public static String driver = null;


    public MyBatisJDBCUtil(DataSource dataSource) {
        url = dataSource.getUrl();
        username = dataSource.getUsername();
        password = dataSource.getPassword();
        driver = dataSource.getDriver();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加删除修改数据
     *
     * @return
     */
    public static boolean insertDeleteUpdateExecute(String sql, ArrayList<Object> paras) {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        boolean flag = false;
        int index = 1;
        try {
            pst = conn.prepareStatement(sql);
            if (paras != null && paras.size() > 0) {
                pst.clearParameters();
                for (int i = 0; i < paras.size(); i++) {
                    pst.setObject(index++, paras.get(i));
                }
            }
            int result = pst.executeUpdate();
            flag = result > 0 ? true : false;
        } catch (SQLException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            close(conn, pst);
        }
        return flag;
    }

    /**
     * 获取单个对象 可用于登录注册验证
     *
     * @param sql
     * @param paras
     * @param cls
     * @return
     */
    public  <T> T findBySingleObject(String sql, ArrayList<Object> paras, Class<T> cls) {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        T singleObject = null;
        int index = 1;

        try {
            pst = conn.prepareStatement(sql);
            if (paras != null && paras.size() > 0) {
                pst.clearParameters();
                for (int i = 0; i < paras.size(); i++) {
                    pst.setObject(index++, paras.get(i));
                }
            }
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                singleObject = cls.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1);
                    Object columnValue = rs.getObject(columnName);
                    Field field = cls.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(singleObject, columnValue);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close(conn, pst);

        }
        return singleObject;

    }

    /**
     * 列表查询
     */
    public  <T> List<T> queryListExecute(String sql, Object[] paras, Class<T> cls) {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        T singleObject = null;
        int index = 1;
        List<T> list = new ArrayList<T>();
        try {
            pst = conn.prepareStatement(sql);
            if (paras != null && paras.length > 0) {
                pst.clearParameters();
                for (int i = 0; i < paras.length; i++) {
                    pst.setObject(index++, paras[i]);
                }
            }
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                singleObject = cls.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1);
                    Object columdValue = rs.getObject(columnName);
                    Field field = cls.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(singleObject, columdValue);
                }
                list.add(singleObject);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close(conn, pst, rs);
        }

        return list;
    }


    /*
     * 释放资源
     */
    public static void close(Connection conn, PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn, PreparedStatement pst,
                             ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
