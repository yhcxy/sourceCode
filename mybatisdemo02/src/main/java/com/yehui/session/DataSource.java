package com.yehui.session;

import lombok.Data;

@Data
public class DataSource {
    private String url;
    private String driver;
    private String username;
    private String password;

    public DataSource(String url,String driver,String username,String password){
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
    }

}
