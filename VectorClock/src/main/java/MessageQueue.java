import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;
import java.util.*;
import org.apache.logging.log4j.Logger;
public class MessageQueue implements Runnable{

    Vectors vectors;
    BlockingQueue<Message>queue;
    Logger logger;

    public MessageQueue(Vectors vectors,Logger logger){
        queue=new ArrayBlockingQueue<Message>(10);
        this.vectors=vectors;
        this.logger=logger;
    }
    public BlockingQueue<Message> getQueue(){
        return queue;
    }
    @Override
    public void run(){
        //no logger for test.
        if(logger !=null){
            logger.info("Queue running...");
        }
        while(true){
            try{
                Message msg=queue.take();
                if(msg==null)continue;
                vectors.update(msg);
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }
    }
}
