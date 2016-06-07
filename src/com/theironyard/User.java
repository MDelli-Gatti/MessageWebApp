package com.theironyard;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by michaeldelli-gatti on 6/6/16.
 */
public class User {
    String name;
    String password;
    ArrayList<Message> messages = new ArrayList<>();



    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
