package akka.tests;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Stringer;
import akka.message.Message;
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
@ContextConfiguration(classes = TestAppConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TestsStringer {

    @Autowired
    private ActorSystem system;


    @Test
    public void testCreatingStringer() throws Exception {
        new JavaTestKit(system) {{
            final Props props = SpringExtension.SpringExtProvider.get(system).props("stringer");
            final TestActorRef<Stringer> ref = TestActorRef.create(system, props);
            final Stringer actor = ref.underlyingActor();

            Assert.assertEquals(actor.getData(), "Test1 Test2");
        }};

    }

    @Test
    public void testSetDataWithTestKit() throws Exception {
        new JavaTestKit(system) {{
            final Props props = SpringExtension.SpringExtProvider.get(system).props("stringer");
            final TestActorRef<Stringer> stringer = TestActorRef.create(system, props);

            stringer.tell(Message.SPLIT, getTestActor());

            Assert.assertEquals("Test1 Test2", stringer.underlyingActor().getData());

            stringer.tell(Message.IS_DONE, getTestActor());

            new AwaitCond(
                    duration("3 second"),  // maximum wait time
                    duration("100 millis") // interval at which to check the condition
            ) {
                // do not put code outside this method, will run afterwards
                protected boolean cond() {
                    // typically used to wait for something to start up
                    return expectMsgEquals(Boolean.TRUE);
                }
            };
        }};
    }

}