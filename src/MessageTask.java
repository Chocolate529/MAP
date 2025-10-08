import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTask extends Task{
    private Message msg;

    public MessageTask(String taskID, String descriere, Message msg) {
        super(taskID, descriere);
        this.msg = msg;
    }

    public void execute() {
        System.out.println("Mesaj:" + msg.getBody() + " " +
                            "Data:" + msg.getDate());
    }

    public Message getMsg() {
        return msg;
    }
}
