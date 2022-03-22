package cpp.context.work.management.proxy.taskservice.websocket;

import cpp.context.work.management.proxy.taskservice.events.TaskWebsocketUpdateEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author bu
 */
@Singleton
@Startup
@ServerEndpoint(value = "/update")
public class TaskWebsocketEndpoint {

    private static final Set<Session> sessions
            = Collections.synchronizedSet(new HashSet<>());

    /*
    * Deal with On Open Events
     */
    @OnOpen
    public void onOpen(final Session session) {
        sessions.add(session);
        System.out.println("Opened Session: " + session);
    }

    /*
    * Deal with On Close Events
     */
    @OnClose
    public void onClose(Session session) {
        try {
            sessions.remove(session);
        System.out.println("Closed Session: " + session);
        } catch (Exception e) {
        }
    }

    /*
    * Recieve On Message Events
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Got UI Message: " + message);
    }

    /*
    * Update browser
     */
    public void update(@Observes @TaskWebsocketUpdateEvent String payload) {
        System.out.println("Got Server Update: " + payload);
        final String text = payload;
        sessions.stream().forEach((session) -> {
            try {
                if (session.isOpen()) {
                    Logger.getLogger(TaskWebsocketEndpoint.class.getName()).log(Level.INFO, null, payload);
                    session.getBasicRemote().sendText(text);
                    Logger.getLogger(TaskWebsocketEndpoint.class.getName()).log(Level.INFO, null, "Updated Clients: " + payload);
                    System.out.println("Updated Clients: " + payload);
                }
            } catch (IOException ex) {
                Logger.getLogger(TaskWebsocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
