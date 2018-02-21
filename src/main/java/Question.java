import java.util.UUID;

public class Question {

    public String id = UUID.randomUUID().toString();
    public String subject;
    public String lesson;
    public int pts;
    public String objective;
    public String question;
    public String answer;
    public int score;

    public String validate(){
        if(pts < 0 || pts > 4){
            return "pts must be greater than 0 or less than 4";
        }

        if(question.length() > 0){
            return "Question must have a length greater than 0";
        }

        if(answer.length() > 0){
            return "Answer must have a length greater than 0";
        }

        return "valid";
    }

}
