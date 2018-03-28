import java.util.UUID;

public class Message {

    public String id = UUID.randomUUID().toString();
    public long timeInMilliseconds = System.currentTimeMillis();
    public String name;
    public String message;

    public String validate() {
        if(name.isEmpty()){
           return "Name must not be empty";
        }

        if(message.isEmpty()){
            return "Message must not be empty";
        }

        return "valid";
    }
}
