import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static spark.Spark.*;

public class Main {

    private static ArrayList<Message> list = new ArrayList<Message>();

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        try {
            list = FileHelper.readQuestionListToJSONFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        get("", (req, res) -> "Welcome to Social Hacker Academy's Message Bank API\n" +
                "There are the existing endpoints:\n" +
                "* GET /messages 'Get a Random Message'\n"+
                "* GET /question/all 'Get all question'\n"+
                "* POST /question 'Send a new question to be saved'\n");

        get("/messages", (req, res) -> {
                return new Gson().toJson(list);
        });

        post("/messages", (req, res) -> {
            Message message = new Gson().fromJson(req.body(), Message.class);

            if(message.validate().equals("valid")) {
                list.add(message);
                FileHelper.saveQuestionListToJSONFile(list);
                return new Gson().toJson(list);
            } else {
                return new Gson().toJson(message.validate());
            }
        });

        attachCors();
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    private static void attachCors(){
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }
}