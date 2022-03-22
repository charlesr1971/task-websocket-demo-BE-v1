package cpp.context.work.management.proxy.taskservice.events;

import java.io.Serializable;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 *
 * @author betrand.ugorji1
 */
public class EventTrigger implements Serializable {
    
 @Inject
 @TaskWebsocketUpdateEvent
 Event<String> taskEvent;

 public void fireTaskEvent(String message) {
  taskEvent.fire(message);
 }
}
