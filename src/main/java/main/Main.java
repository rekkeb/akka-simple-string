package main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Stringer;
import akka.message.Message;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        final ActorSystem actorSystem = ActorSystem.create("helloakka");
        final ActorRef stringer = actorSystem.actorOf(Props.create(Stringer.class, "This string is going to be split and reversed"), "stringer");

        stringer.tell(Message.SPLIT, ActorRef.noSender());

        //actorSystem.shutdown();

    }

}
