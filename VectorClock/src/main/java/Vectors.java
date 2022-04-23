import java.util.concurrent.*;
import java.util.*;
public class Vectors {
    int numNodes;
    List<long[]> vectors;
    Snapshots snapshots;
    public Vectors(int numNodes, Snapshots snapshots){
        this.numNodes=numNodes;
        this.snapshots=snapshots;
        vectors=new ArrayList<>();
        for(int i=0;i<numNodes;i++){
            vectors.add(new long[numNodes]);
        }
    }

    public long[] getVector(int nodeId){
        return vectors.get(nodeId);
    }

    public void update(Message msg) throws Exception{
        Type type=msg.getType();
        if(type.equals(Type.INTERNAL)){
            if(msg.getSenderId()!=msg.getReceiverId()){
                throw new Exception("internal node receiver and sender id should be identical!");
            }
            int id=msg.getSenderId();
            //System.out.println(id);
            long[] timeStamp=vectors.get(id);
            timeStamp=increment(timeStamp,id);
            vectors.set(id,timeStamp);
            snapshots.registerEvent(Type.INTERNAL,id,vectors);
        }else if(type.equals(Type.SEND_RECEIVE)){
            int senderId=msg.getSenderId();
            int receiverId=msg.getReceiverId();
            long[] stimeStamp= vectors.get(senderId);
            stimeStamp=increment(stimeStamp,senderId);
            vectors.set(senderId,stimeStamp);
            snapshots.registerEvent(Type.SEND,senderId,vectors);

            long[] rtimeStamp=vectors.get(receiverId);
            rtimeStamp=increment(rtimeStamp,receiverId);
            rtimeStamp=merge(stimeStamp,rtimeStamp);
            vectors.set(receiverId,rtimeStamp);
            snapshots.registerEvent(Type.RECEIVE,receiverId,vectors);
        } else {
            throw new Exception("event type error!");
        }
    }


    private long[] increment(long[] timeStamp,int id){
        timeStamp[id]=timeStamp[id]+1;
        return timeStamp;
    }

    private long[] merge(long[]ts1,long[]ts2){
        long[] newTs=new long[ts1.length];
        for(int i=0;i<ts1.length;i++){
            newTs[i]=Math.max(ts1[i],ts2[i]);
        }
        return newTs;
    }

}
