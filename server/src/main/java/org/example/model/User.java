package org.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String account;
    private String password;

    public User(int id, String name, String account, String password) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(String account, String password, String name) {
        this.account = account;
        this.password = password;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
