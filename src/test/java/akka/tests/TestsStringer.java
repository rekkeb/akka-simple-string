package akka.tests;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Stringer;
import akka.message.Message;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class TestsStringer {

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
    public void testCreatingStringer() throws Exception {
        final Props props = Props.create(Stringer.class, "Test1 Test2");
        final TestActorRef<Stringer> ref = TestActorRef.create(system, props, "testA");
        final Stringer actor = ref.underlyingActor();

        Assert.assertEquals(actor.getData(), "Test1 Test2");

    }

    @Test
    public void testSetData() throws Exception {
        new JavaTestKit(system) {{
            final TestActorRef<Stringer> stringer =
                    TestActorRef.create(system, Props.create(Stringer.class, "String1 String2"), "stringer1");

            stringer.tell(Message.SPLIT, getTestActor());

            Assert.assertEquals("String1 String2", stringer.underlyingActor().getData());

            expectNoMsg();
        }};
    }
    
}