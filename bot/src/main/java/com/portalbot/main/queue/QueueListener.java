package com.portalbot.main.queue;

import com.portalbot.main.MySQLRequester;
import com.portalbot.main.PortalRequester;
import com.portalbot.main.exceptions.BadLoggingException;

import java.util.ArrayList;
import java.util.List;

public class QueueListener {
    private MySQLRequester mySQLRequester;
    private QueueParser parser;
    private PortalRequester requester;

    public QueueListener() throws BadLoggingException {
        requester = new PortalRequester("1111111111");
        parser  = new QueueParser();
        mySQLRequester  = new MySQLRequester();
    }

    public List<QueueTask> listen() {
        List<QueueTask> result = new ArrayList<>();
        for (String portalNumber : parser.parseQueue(requester.getQueue())) {
            if (!mySQLRequester.getQueueTaskList().contains(portalNumber)) {
                QueueTask task = (parser.parseTask(requester.getTaskBody(portalNumber)));
                result.add(task);
                mySQLRequester.insertTask(task);
            }
        }
        return result;
    }
}
