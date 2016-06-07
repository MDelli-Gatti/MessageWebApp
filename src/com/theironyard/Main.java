package com.theironyard;

import spark.ModelAndView;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashMap;
import spark.template.mustache.MustacheTemplateEngine;


public class Main {

    static User user;
    //static ArrayList<User> userlist = new ArrayList<>();
    static ArrayList<Message> messages = new ArrayList<>();
    static HashMap<User, ArrayList<Message>> userList = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get("/",
                (request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("messages", messages);
                        m.put("username", user.name);
                        //m.put("password", user.password);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post("/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");
                    user = new User(username, password);
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post("/create-message",
                (request, response) -> {
                    String message = request.queryParams("message");
                    Message m = new Message(message);
                    messages.add(m);
                    //userList.put(user, messages);
                    response.redirect("/");
                    return "";
                }
        );
    }
}
