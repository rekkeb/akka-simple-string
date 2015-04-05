package main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.message.Message;
import akka.spring.AppConfiguration;
import akka.spring.SpringExtension;
import akka.util.Timeout;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        // create a spring context and scan the classes
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfiguration.class);

        // get hold of the actor system
        ActorSystem system = ctx.getBean(ActorSystem.class);
        // use the Spring Extension to create props for a named actor bean
        ActorRef stringer = system.actorOf(
                SpringExtension.SpringExtProvider.get(system).props("stringer"), "Stringer");


        stringer.tell(Message.SPLIT, ActorRef.noSender());

        //Initialize logger
        final LoggingAdapter log = Logging.getLogger(system, Main.class);

        FiniteDuration duration = FiniteDuration.create(5, TimeUnit.SECONDS);
        Future<Object> result = ask(stringer, Message.IS_DONE,
                Timeout.durationToTimeout(duration));

        try {
            while(!(Boolean)Await.result(result, duration)){
                result = ask(stringer, Message.IS_DONE,
                        Timeout.durationToTimeout(duration));
            }
        } catch (Exception e) {
            log.error("Failed getting result: " + e.getMessage());
        } finally {
            log.info("Finished: " + result.value());

            system.shutdown();
            system.awaitTermination();
        }

    }

}
