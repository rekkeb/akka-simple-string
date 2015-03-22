package akka.tests;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Reverser;
import akka.message.Message;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


@RunWith(JUnit4.class)
public class TestsReverser {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        //system.shutdown();
        //system.awaitTermination(Duration.create("10 seconds"));

        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testReverseDataFuture() throws Exception {
        new JavaTestKit(system) {{
            final Props props = Props.create(Reverser.class);
            final TestActorRef<Reverser> reverser = TestActorRef.create(system, props);

            final Future<Object> future = akka.pattern.Patterns.ask(reverser, "abcde", 3000);

            Assert.assertTrue(future.isCompleted());
            Assert.assertEquals(Message.DONE, Await.result(future, Duration.Zero()));

        }};
    }

    @Test
    public void testReverseData() throws Exception {
        new JavaTestKit(system) {{
            final Props props = Props.create(Reverser.class);
            final ActorRef reverser = system.actorOf(props);

            reverser.tell("abcde", getRef());

            expectMsgEquals(Duration.create(1000, "millis"), Message.DONE);

        }};
    }

}