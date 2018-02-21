import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHelper {

    public static void saveQuestionListToJSONFile(ArrayList<Question> json) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("questions.txt");
        out.println(new Gson().toJson(json));
    }

    public static ArrayList<Question> readQuestionListToJSONFile() throws IOException {
        String file = readFile("questions.txt");

        if(file != null){
            return new Gson().fromJson(readFile("questions.txt"), new TypeToken<ArrayList<Question>>(){}.getType());
        } else {
            return new ArrayList<>();
        }
    }

    public static String readFile(String path) throws IOException {
        if(Paths.get(path).toFile().exists()) {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded);
        } else {
            return null;
        }
    }
}
