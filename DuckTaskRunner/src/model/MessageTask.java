package model;

import java.time.LocalDateTime;
import utils.Constants;

public class MessageTask extends Task{

    private String message;
    private String from;
    private String to;
    private LocalDateTime date;

    public MessageTask(String taskId, String description, String message, String from, String to, LocalDateTime time) {
        super(taskId, description,0);
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = time;
    }


    @Override
    public void execute() {
        System.out.println(toString());

    }

    public String toString(){
            return super.toString()+" | message="+message+" | from="+from+" | to="+to+" | time="+date.format(Constants.DATE_TIME_FORMAT);
    }
}
