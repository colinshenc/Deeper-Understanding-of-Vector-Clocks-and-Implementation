import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;
import java.util.*;
public class Node {
    private int id;
    BlockingQueue<Message> queue;
    public Node(int id,MessageQueue queue) {
        this.id=id;
        this.queue=queue.getQueue();
    }
    public int getId(){
        return id;
    }

    public void initiateInternalEvent(){
        Message msg=new Message().senderId(id).receiverId(id).type(Type.INTERNAL);
        try {
            //blocking
            queue.put(msg);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void initiateSendReceiveEvent(int receiverId){
        if(receiverId==id){
            throw new IllegalArgumentException("SendReceive event must be external!");
        }
        Message msg=new Message().senderId(id).receiverId(receiverId).type(Type.SEND_RECEIVE);
        try {
            //blocking
            queue.put(msg);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
