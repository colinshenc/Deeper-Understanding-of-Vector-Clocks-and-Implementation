import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Api {
    public static Order compareTwoEvents(int event1Id,int event2Id,Snapshots snapshots,Logger logger) throws Exception{
        boolean later = false;//event 1>event2
        boolean earlier = false;//event 1<event2

        Thread.sleep(1000);
        long[] event1Timestamp = snapshots.getEventTimestamp(event1Id);
        long[] event2Timestamp = snapshots.getEventTimestamp(event2Id);
//        System.out.println(Arrays.toString(event1Timestamp));
//        System.out.println(Arrays.toString(event2Timestamp));

        for (int i = 0; i < event1Timestamp.length; i++) {
            if (event1Timestamp[i] > event2Timestamp[i]) {
                later = true;
            } else if (event1Timestamp[i] < event2Timestamp[i]) {
                earlier = true;
            }
        }

        Order result=null;
        if(later&&earlier){
            result= Order.CONCURRENT;//concurrent
        } else if(later&&!earlier){
            result= Order.HAPPENED_AFTER;//event 1 happened later than event2
        }else if(!later&&earlier){
            result= Order.HAPPENED_BEFORE;//event 1 happened earlier than event 2
        }else {
            throw new Exception("order error! Neither later nor earlier, not comparable!");
        }
        logger.info("Event "+event1Id+" "+result+" event "+event2Id);
        return result;
    }
}
