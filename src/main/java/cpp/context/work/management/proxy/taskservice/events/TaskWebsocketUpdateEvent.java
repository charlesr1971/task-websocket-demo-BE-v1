package cpp.context.work.management.proxy.taskservice.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * The CDI Event Classifier
 *
 * @author betrand.ugorji1
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({
 ElementType.TYPE,
 ElementType.FIELD,
 ElementType.METHOD,
 ElementType.PARAMETER})
public @interface TaskWebsocketUpdateEvent {

}
