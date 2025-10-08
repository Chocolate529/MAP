import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        testTask();
    }

    private static void testTask(){
        MessageTask msg1 = new MessageTask("1", "descriere1", new Message("1","subiect1", "body1"
        ,"from1", "to1", LocalDateTime.now()));
        MessageTask msg2 = new MessageTask("2", "descriere2", new Message("2","subiect2", "body2"
                ,"from2", "to2", LocalDateTime.now()));
        MessageTask msg3 = new MessageTask("3", "descriere3", new Message("3","subiect3", "body3"
                ,"from3", "to3", LocalDateTime.now()));
        MessageTask msg4 = new MessageTask("4", "descriere4", new Message("4","subiect4", "body4"
                ,"from4", "to4", LocalDateTime.now()));
        MessageTask msg5 = new MessageTask("5", "descriere5", new Message("5","subiect5", "body5"
                ,"from5", "to5", LocalDateTime.now()));

        MessageTask[] messages = new MessageTask[]{msg1, msg2, msg3, msg4, msg5, msg5};
        for (MessageTask task : messages) {
            var msg = task.getMsg();
            System.out.println("id="+task.getTaskID()+"|"+
                    "description="+task.getDescriere()+"|"+
                    "message="+msg.getSubject()+":"+msg.getBody()+"|"+
                    "from="+msg.getFrom()+"|"+
                    "to="+msg.getTo()+"|"+
                    "date="+msg.getDate());
        }
    }}