import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;
public class InternalClock {
    //the "vector" in vector clock, used to store logical time stamp info.
    private long[] vectorStamp;
    public InternalClock(int numNodes){

        vectorStamp=new long[numNodes];
    }
//    /**
//     * Register an node and its timestamp for a clock
//     * @param id
//     * @param ts
//     */
//    public void registerToVector(String id,long ts){
//        if(id==null||id.length()==0){
//            throw new IllegalArgumentException("missing id!");
//        }
//        vectorStamp.put(id,ts);
//    }

    //    /**
//     * Remove an node from a clock
//     * @param id
//     */
//    public void removeFromVector(String id){
//        if(!vectorStamp.containsKey(id)){
//            throw new IllegalArgumentException("id does not exist");
//        }
//        vectorStamp.remove(id);
//    }
    public long[] getvectorStamp(){
        return vectorStamp;
    }
    public String getTimeStamp(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<vectorStamp.length;i++){
            sb.append("Clock").append(i+": ").append(vectorStamp[i]);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void registerEvent(final Event event){
        Node node=event.getNode();
        int nodeId=node.getId();
        long nodeTimeStamp=vectorStamp[nodeId];
        Type type=event.getType();
        if(type.equals(Type.RECEIVE)){
            long[] senderStamp=event.getClock().getvectorStamp();
            int temp=compareTwoVectors(vectorStamp,senderStamp);

        }else if(type.equals(Type.SEND)){
            long next=nodeTimeStamp+1;
            vectorStamp[nodeId]=next;

        }else {

        }
    }
    private int compareTwoVectors(long[] vector1,long[] vector2){
        boolean firstGreater=false;
        boolean secondGreater=false;
        for(int i=0;i<vector1.length;i++){
            if(vector1[i]>vector2[i]){
                firstGreater=true;
            }else{
                secondGreater=true;
            }
            if(firstGreater&&secondGreater){
                return 0;
            }
            if(firstGreater){
                return 1;
            }
            return -1;
        }
        return -2;
    }
}
