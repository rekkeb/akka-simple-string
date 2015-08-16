package akka.tests;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Reverser;
import akka.message.Message;
import akka.spring.AppConfiguration;
import akka.spring.SpringExtension;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TestsReverser {

    @Autowired
    private ActorSystem system;


    @Test
    public void testReverseDataFuture() throws Exception {
        new JavaTestKit(system) {{
            final Props props = SpringExtension.SpringExtProvider.get(system).props("reverser");
            final TestActorRef<Reverser> reverser = TestActorRef.create(system, props);

            final Future<Object> future = akka.pattern.Patterns.ask(reverser, "abcde", 3000);

            Assert.assertTrue(future.isCompleted());
            Assert.assertEquals(Message.DONE, Await.result(future, Duration.Zero()));

        }};
    }

    @Test
    public void testReverseData() throws Exception {
        new JavaTestKit(system) {{
            final Props props = SpringExtension.SpringExtProvider.get(system).props("reverser");
            final ActorRef reverser = system.actorOf(props);

            reverser.tell("abcde", getRef());

            expectMsgEquals(Duration.create(1000, "millis"), Message.DONE);

        }};
    }

}