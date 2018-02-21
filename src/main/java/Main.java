import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static spark.Spark.*;

public class Main {

    private static ArrayList<Question> list = new ArrayList<Question>();

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        try {
            list = FileHelper.readQuestionListToJSONFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        get("/question", (req, res) -> {
            String attributeId = req.attribute("id");

            if(attributeId != null){
                return new Gson().toJson(getQuestionById(attributeId));
            } else {
                return new Gson().toJson(list.get(new Random().nextInt(list.size())));
            }
        });

        get("/question/all", (req, res) -> new Gson().toJson(list));

        post("/question", (req, res) -> {
            Question question = new Gson().fromJson(req.body(), Question.class);

            if(question.validate().equals("valid")) {
                list.add(question);
                FileHelper.saveQuestionListToJSONFile(list);
                return new Gson().toJson(list);
            } else {
                return new Gson().toJson(question.validate());
            }
        });

        attachCors();
    }

    private static Question getQuestionById(String id){
        return list.stream()
                .filter(item -> item.id.equals(id))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                })
                .get();
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