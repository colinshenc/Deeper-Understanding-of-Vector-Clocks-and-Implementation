import java.util.concurrent.*;
import java.util.*;
import org.apache.logging.log4j.Logger;
public class Snapshots {
    List<Snapshot> eventIDSnapshotlist;
    int eventId;
    Logger logger;
    public Snapshots(Logger logger) {
        eventIDSnapshotlist=new CopyOnWriteArrayList<>();
        this.logger=logger;
        eventId=0;
    }
    public class Snapshot {
        private Type type;
        private List<long[]> vectors;
        private int eventId;
        private int nodeId;
        public Snapshot(int eventId,int nodeId,Type type,List<long[]> vectors){
            this.eventId=eventId;
            this.nodeId=nodeId;
            this.type=type;
            this.vectors=new ArrayList<>();
            //Must be deep-copy
//            for(long[] v:vectors){
//                long[] temp=new long[v.length];
//                for(int i=0;i<v.length;i++){
//                    temp[i]=v[i];
//                }
//                this.vectors.add(temp);
//            }
            this.vectors=copyVectors(vectors);
        }

        private List<long[]> copyVectors(List<long[]> vecs){
            List<long[]>result=new ArrayList<>();
            for(long[] v:vecs){
                long[] temp=new long[v.length];
                for(int i=0;i<v.length;i++){
                    temp[i]=v[i];
                }
                result.add(temp);
            }
            return result;
        }

        public Type getType() {
            return type;
        }

        public List<long[]> getVectors() {
            return vectors;
        }

        public int getEventId() {
            return eventId;
        }

        public int getNodeId() {
            return nodeId;
        }

        @Override
        public String toString() {
            StringBuilder sb=new StringBuilder();
            sb.append("Event Id: "+eventId+ " ");
            sb.append("Associated Node: "+nodeId+" ");
            sb.append("Type: "+type);
            sb.append("\n");
            for(int i=0;i< vectors.size();i++){
                sb.append("Node "+i+" "+ Arrays.toString(vectors.get(i))+"\n");
            }
            return sb.toString();
        }
    }
    public long[] getEventTimestamp(int eventID) {
        Snapshot s=eventIDSnapshotlist.get(eventID);
        return s.getVectors().get(s.getNodeId());
    }

    public List<Snapshot> getEventIDSnapshotlist() {
        return eventIDSnapshotlist;
    }

    public void registerEvent(Type type, int nodeId, List<long[]> _vectors) throws Exception {
        Snapshot s=new Snapshot(eventId++,nodeId,type,_vectors);
        eventIDSnapshotlist.add(s);
        System.out.println("add");
        if(logger!=null){
            logger.info(s);
        }
    }

}
