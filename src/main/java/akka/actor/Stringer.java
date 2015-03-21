package akka.actor;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.message.Message;

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

    public Stringer(String data) {
        this.data = data;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg == Message.SPLIT){
            //Splits the data and sends each of the chunks to a new actor
            String[] dataSplit = data.split(" ");
            numberOfChunks = dataSplit.length;
            log.info("Split: {}", Arrays.toString(dataSplit));

            for (String chunk : dataSplit){
                getContext().actorOf(Props.create(Reverser.class)).tell(chunk, getSelf());
            }

            //getSender().tell(Message.DONE, getSelf());
            //getContext().stop(getSelf());
        }
        if (msg == Message.DONE){
            chunksProcessed++;
            if(numberOfChunks == chunksProcessed){
                log.info("String has been processed");

                //getContext().stop(getSelf());

                getContext().system().shutdown();
            }
        }
        else{
            unhandled(msg);
        }
    }

    public String getData() {
        return data;
    }
}
