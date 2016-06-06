package com.theironyard;

import spark.ModelAndView;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashMap;
import spark.template.mustache.MustacheTemplateEngine;


public class Main {

    static User user;
    static ArrayList<User> userlist = new ArrayList<>();
    static ArrayList<String> messages = new ArrayList<>();
    //static HashMap<String, String> userList = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get("/",
                (request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("username", user.name);
                        m.put("password", user.password);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post("/index",
                (request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");
                    user = new User(username, password);
                    userlist.add(user);
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post("/messages",
                (request, response) -> {
                    String message = request.queryParams("message");
                    messages.add(message);
                    response.redirect("/");
                    return "";
                }
                );
    }
}
