package net.veteran.veteransmp.commands.tpa;

import java.util.concurrent.ScheduledFuture;

public class TpaRequest {
    private final String to;
    private final String from;
    private boolean accepted;
    private final ScheduledFuture<?> task;

    public TpaRequest(String from, String to, ScheduledFuture<?>  task) {
        this.to = to;
        this.from = from;
        this.accepted = false;
        this.task = task;
    }

    public String getTo() {
        return this.to;
    }

    public  String getFrom() {
        return this.from;
    }

    public void accept() {
        task.cancel(false);
        this.accepted = true;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    public void cancelTask() {
        if(!accepted)
            task.cancel(false);
    }
}