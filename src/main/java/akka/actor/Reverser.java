package akka.actor;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.message.Message;

/**
 * Actor that receives a String, and prints it in reserve order
 */
public class Reverser extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public Reverser() {

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof String){
            //Splits the chunk and sends each of the chunks to a new actor
            log.info("Chunk: {}", getReverseChunk((String)msg));

            //Once the chunk has been processed, tells the Sender is DONE
            getSender().tell(Message.DONE, getSelf());
        }
        else{
            unhandled(msg);
        }
    }

    private String getReverseChunk(String chunk){

        if (chunk.isEmpty()){
            return "";
        }else{
            return chunk.charAt(chunk.length()-1) + getReverseChunk(chunk.substring(0,chunk.length()-1));
        }
    }
}
