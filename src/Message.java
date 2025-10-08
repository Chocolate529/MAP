import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String id;
    private String subject;
    private String body;
    private String from;
    private String to;
    private LocalDateTime date;

    public Message(String id, String subject, String body, String from, String to, LocalDateTime date) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date +
                '}';
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
