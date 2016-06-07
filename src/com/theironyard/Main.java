package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;

import java.util.HashMap;
import spark.template.mustache.MustacheTemplateEngine;


public class Main {

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get("/",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("name");

                    HashMap m = new HashMap();
                    if (name == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        User user = users.get(name);
                        m.put("password", user.password);
                        m.put("name", user.name);
                        m.put("messages", user.messages);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post("/create-user",
                (request, response) -> {
                    String name = request.queryParams("name");
                    String pass = request.queryParams("password");
                    if (name == null || pass == null){
                        throw new Exception("name or pass not set");
                    }

                    User user = users.get(name);
                    if (user == null){
                        user = new User(name, pass);
                        users.put(name, user);
                    }
                    else if (!pass.equals(user.password)){
                        throw new Exception("wrong password");
                    }

                    Session session = request.session();
                    session.attribute("name", name);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post("/create-message",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("name");
                    if (name == null){
                        throw new Exception("not logged in");
                    }

                    String message = request.queryParams("message");
                    if (message == null){
                         throw new Exception("invalid message");
                    }

                    User user = users.get(name);
                    if (user == null){
                        throw new Exception("User does not exist");
                    }

                    Message m = new Message(message);
                    user.messages.add(m);

                    response.redirect("/");
                    return "";

            }
        );
        Spark.post(
                "/delete-message",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("name");
                    if (name == null) {
                        throw new Exception("not logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));

                    User user = users.get(name);
                    if (id < 0 || id - 1 >= user.messages.size() ) {
                        throw new Exception("invalid id");
                    }
                    user.messages.remove(id - 1);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("name");
                    if (name == null) {
                        throw new Exception("not logged in");
                    }
                    int editId = Integer.valueOf(request.queryParams("edit-id"));
                    User user = users.get(name);
                    if (editId < 0 || editId - 1 >= user.messages.size() ) {
                        throw new Exception("invalid id");
                    }
                    user.messages.remove(editId - 1);
                    String message = request.queryParams("edit-message");
                    Message m = new Message(message);
                    user.messages.add(m);

                    response.redirect("/");
                    return "";
                }

        );
    }
}
