package net.veteran.veteransmp.commands.tpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TpaManager {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, TpaRequest> requests = new HashMap<>();

    public void addRequest(String from, String to, Runnable timeoutTask) {
        Runnable wrappedTask = () -> {
            requests.remove(from + ":" + to);
            if (timeoutTask!=null)
                timeoutTask.run();
        };
        ScheduledFuture<?> task = scheduler.schedule(wrappedTask,60, TimeUnit.SECONDS);
        TpaRequest request = getRequest(from, to);
        if (request != null ) request.cancelTask();
        requests.put(from + ":" + to, new TpaRequest(from, to, task));
    }

    public ArrayList<TpaRequest> getRequestsFromPlayer(String from) {
        ArrayList<TpaRequest> list = new ArrayList<TpaRequest>();
        requests.forEach((item, value) -> {
            if(item.startsWith(from + ":"))
                list.add(value);
        });
        return list;
    }

    public ArrayList<TpaRequest> getRequestsToPlayer(String to) {
        ArrayList<TpaRequest> list = new ArrayList<TpaRequest>();
        requests.forEach((item, value) -> {
            if(item.endsWith(":" + to))
                list.add(value);
        });
        return list;
    }

    public TpaRequest getRequest(String from, String to) {
        return requests.get(from + ":" + to);
    }

    public void acceptRequest(String from, String to) {
        TpaRequest request = requests.get(from + ":" + to);
        if(request != null) {
            request.accept();
            request.cancelTask();
        }
    }

    public void removeRequest(String from, String to) {
        TpaRequest request = requests.get(from + ":" + to);
        if(request != null) {
            requests.remove(from + ":" + to);
            request.cancelTask();
        }
    }
}

