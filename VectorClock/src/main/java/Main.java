//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//public class Main {
//    private static final Logger logger = LogManager.getLogger(Main.class);
//    public static void main(String[] argv) throws Exception{
//        logger.debug("Start run...");
//        Snapshots snapshots = new Snapshots(logger);
//        int numNodes=3;//Integer.valueOf(argv[0]);
//        Vectors vectors = new Vectors(numNodes,snapshots);
//        MessageQueue queue=new MessageQueue(vectors,logger);
//        Thread thread=new Thread(queue);
//        thread.start();
//        //initial three nodes
//        Node node0=new Node(0,queue);
//        Node node1=new Node(1,queue);
//        Node node2=new Node(2,queue);
//
//        node0.initiateInternalEvent();
//        node1.initiateInternalEvent();
//        node2.initiateInternalEvent();
//        node2.initiateSendReceiveEvent(0);
//        node0.initiateSendReceiveEvent(1);
//        node2.initiateInternalEvent();
////        Api.compareTwoEvents(3,4,snapshots,logger);
//    }
//}
