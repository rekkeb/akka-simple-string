package main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.message.Message;
import akka.spring.AppConfiguration;
import akka.spring.SpringExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

        /*final ActorSystem actorSystem = ActorSystem.create("helloakka");
        final ActorRef stringer = actorSystem.actorOf(Props.create(Stringer.class, "This string is going to be split and reversed"), "stringer");*/

        stringer.tell(Message.SPLIT, ActorRef.noSender());

    }

}
