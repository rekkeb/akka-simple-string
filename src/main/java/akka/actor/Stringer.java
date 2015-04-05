package akka.actor;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.message.Message;
import akka.spring.SpringExtension;

import java.util.Arrays;

/**
 * Actor that receives a String, splits it and sends the chunks
 * to Reverser actors
 */
public class Stringer extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private String data;
    private int numberOfChunks = 0;
    private int chunksProcessed = 0;

    public Stringer(){
        this.data = "This string is going to be split and reversed. Default Constructor";
    }

    public Stringer(String data) {
        this.data = data;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg == Message.SPLIT) {
            //Splits the data and sends each of the chunks to a new actor
            String[] dataSplit = data.split(" ");
            numberOfChunks = dataSplit.length;
            log.info("Split: {}", Arrays.toString(dataSplit));

            for (String chunk : dataSplit) {
                //Creates actors using Spring Beans
                getContext().actorOf(SpringExtension.SpringExtProvider.get(getContext().system()).props("reverser"))
                        .tell(chunk, getSelf());
            }

            //getSender().tell(Message.DONE, getSelf());
            //getContext().stop(getSelf());
        }
        if (msg == Message.DONE) {
            chunksProcessed++;
            if (numberOfChunks == chunksProcessed) {
                log.info("String has been processed");
            }
        }
        if (msg == Message.IS_DONE) {
            //Check If the reverser Actors have finished
            getSender().tell(numberOfChunks == chunksProcessed, getSelf());

        }else {
            unhandled(msg);
        }
    }

    public String getData() {
        return data;
    }
}
