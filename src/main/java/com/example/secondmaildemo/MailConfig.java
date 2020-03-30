package com.example.secondmaildemo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailConfig {

    @Value("${Spring.mail.host}")
    private String host;
    @Value("${Spring.mail.port}")
    private int port;
    @Value("${Spring.mail.username}")
    private String username;
    @Value("${Spring.mail.password}")
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
