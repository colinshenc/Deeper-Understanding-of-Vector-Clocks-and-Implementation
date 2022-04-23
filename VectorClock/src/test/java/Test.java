import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test {

    private static final Logger logger = LogManager.getLogger(Test.class);

    /**
     * Test Node class registers Id correctly
     */
    @org.junit.jupiter.api.Test
    public void simpleTestNodeClass(){
        int numNodes=3;

        Snapshots snapshots = new Snapshots(logger);

        Vectors vectors = new Vectors(numNodes,snapshots);
        MessageQueue queue=new MessageQueue(vectors,logger);
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);
        assertEquals(node0.getId(),0);
        assertEquals(node1.getId(),1);
        assertEquals(node2.getId(),2);
    }

    /**
     * Testing Snapshot class registers events correctly for a simple sequence.
     */
    @org.junit.jupiter.api.Test
    public void simpleTestSnapshotsClass() throws Exception{
        int numNodes=3;
        Snapshots snapshots = new Snapshots(logger);

        Vectors vectors = new Vectors(numNodes,snapshots);

        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);
        node2.initiateInternalEvent();
        node1.initiateSendReceiveEvent(0);
        Thread.sleep(50);
        //System.out.println(snapshots.getEventIDSnapshotlist().size());
        assertEquals(snapshots.getEventIDSnapshotlist().get(1).getType(),Type.SEND);
        assertEquals(snapshots.getEventIDSnapshotlist().get(0).getType(),Type.INTERNAL);
        assertEquals(snapshots.getEventIDSnapshotlist().get(2).getType(),Type.RECEIVE);

    }

    /**
     * Test actual timestamp can be calculated and registered correctly for a simple internal event.
     */
    @org.junit.jupiter.api.Test
    void testNodeCanExecuteInternalEvent() throws Exception{
        int numNodes=3;

        Snapshots snapshots = new Snapshots(logger);

        Vectors vectors = new Vectors(numNodes,snapshots);

        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        //initial three nodes
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);
        //Before:
        assertEquals(Arrays.toString(vectors.getVector(2)),"[0, 0, 0]");
        node2.initiateInternalEvent();
        Thread.sleep(100);
        //after
        assertEquals(Arrays.toString(vectors.getVector(2)),"[0, 0, 1]");
        //sanity test-getEventId
        assertEquals(snapshots.getEventIDSnapshotlist().get(0).getEventId(),0);

    }

    /**
     *Test actual timestamp can be calculated and registered correctly for a sequence of
     * internal and external events.
     *
     */
    @org.junit.jupiter.api.Test
    void testNodeCanExecuteExternalEvent() throws Exception{
        int numNodes=3;
        Snapshots snapshots = new Snapshots(logger);
        Vectors vectors = new Vectors(numNodes,snapshots);
        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        //initialize three nodes
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);
        //Before:
        assertEquals(Arrays.toString(vectors.getVector(1)),"[0, 0, 0]");
        assertEquals(Arrays.toString(vectors.getVector(0)),"[0, 0, 0]");
        node1.initiateSendReceiveEvent(0);
        Thread.sleep(50);
        //After:
        assertEquals(Arrays.toString(vectors.getVector(1)),"[0, 1, 0]");
        assertEquals(Arrays.toString(vectors.getVector(0)),"[1, 1, 0]");

    }

    /**
     * Test that after a sequence of multiple events, every snapshot in this sequence are
     * registered correctly.
     */
    @org.junit.jupiter.api.Test
    public void testSnapshotRegistration() throws Exception{
        int numNodes=3;
        Snapshots snapshots = new Snapshots(logger);
        Vectors vectors = new Vectors(numNodes,snapshots);
        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        //initialize three nodes
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);
        //Before:
        node2.initiateSendReceiveEvent(1);//Two events
        node1.initiateSendReceiveEvent(0);//Two events
        node0.initiateInternalEvent();
        Thread.sleep(50);
        //Check events were registered correctly in Snapshots
        assertEquals(snapshots.getEventIDSnapshotlist().size(),5);
//        System.out.println(snapshots.getEventIDSnapshotlist().get(0));

        assertEquals(snapshots.getEventIDSnapshotlist().get(0).toString(),"Event Id: 0 Associated Node: 2 Type: SEND\n" +
                "Node 0 [0, 0, 0]\n" +
                "Node 1 [0, 0, 0]\n" +
                "Node 2 [0, 0, 1]\n");
        assertEquals(snapshots.getEventIDSnapshotlist().get(1).toString(),"Event Id: 1 Associated Node: 1 Type: RECEIVE\n" +
                "Node 0 [0, 0, 0]\n" +
                "Node 1 [0, 1, 1]\n" +
                "Node 2 [0, 0, 1]\n");
        assertEquals(snapshots.getEventIDSnapshotlist().get(2).toString(),"Event Id: 2 Associated Node: 1 Type: SEND\n" +
                "Node 0 [0, 0, 0]\n" +
                "Node 1 [0, 2, 1]\n" +
                "Node 2 [0, 0, 1]\n");
        assertEquals(snapshots.getEventIDSnapshotlist().get(3).toString(),"Event Id: 3 Associated Node: 0 Type: RECEIVE\n" +
                "Node 0 [1, 2, 1]\n" +
                "Node 1 [0, 2, 1]\n" +
                "Node 2 [0, 0, 1]\n");
        assertEquals(snapshots.getEventIDSnapshotlist().get(4).toString(),"Event Id: 4 Associated Node: 0 Type: INTERNAL\n" +
                "Node 0 [2, 2, 1]\n" +
                "Node 1 [0, 2, 1]\n" +
                "Node 2 [0, 0, 1]\n");
        //assertEquals(Arrays.toString(vectors.getVector(0)),"[1, 1, 0]");
    }

    /**
     * Test Api.compareTwoEvents(), for two event on the same node.
     * @throws Exception
     */
    @org.junit.jupiter.api.Test
    public void testApiCompareTwoEventsSameNode() throws Exception{
        int numNodes=1;
        Snapshots snapshots = new Snapshots(logger);
        Vectors vectors = new Vectors(numNodes,snapshots);
        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        //initialize three nodes
        Node node0=new Node(0,queue);

        //Before:

        node0.initiateInternalEvent();
        node0.initiateInternalEvent();
        Thread.sleep(50);
        assertEquals(Order.HAPPENED_BEFORE,Api.compareTwoEvents(0,1,snapshots,logger));
        assertEquals(Order.HAPPENED_AFTER,Api.compareTwoEvents(1,0,snapshots,logger));
    }

    /**
     * Test  Api.compareTwoEvents(), for two events on different nodes, concurrent.
     * @throws Exception
     */
    @org.junit.jupiter.api.Test
    public void testApiCompareTwoEventsDifferentNodesConcurrent() throws Exception {
        int numNodes=3;
        Snapshots snapshots = new Snapshots(logger);
        Vectors vectors = new Vectors(numNodes,snapshots);
        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        //initialize three nodes
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);

        node1.initiateSendReceiveEvent(2);
        node1.initiateSendReceiveEvent(0);
        node0.initiateSendReceiveEvent(1);
        node2.initiateSendReceiveEvent(0);
        node1.initiateInternalEvent();
        Thread.sleep(50);
        assertEquals(Order.CONCURRENT,Api.compareTwoEvents(7,8,snapshots,logger));
        assertEquals(Order.CONCURRENT,Api.compareTwoEvents(8,7,snapshots,logger));
    }

    /**
     * Test  Api.compareTwoEvents(), for two events on different nodes, not concurrent.
     * @throws Exception
     */
    @org.junit.jupiter.api.Test
    public void testApiCompareTwoEventsDifferentNodesNotConcurrent() throws Exception{
        int numNodes=3;
        Snapshots snapshots = new Snapshots(logger);
        Vectors vectors = new Vectors(numNodes,snapshots);
        MessageQueue queue=new MessageQueue(vectors,logger);
        Thread thread=new Thread(queue);
        thread.start();
        //initialize three nodes
        Node node0=new Node(0,queue);
        Node node1=new Node(1,queue);
        Node node2=new Node(2,queue);
        node0.initiateInternalEvent();
        node2.initiateSendReceiveEvent(0);
        node0.initiateSendReceiveEvent(1);
        node0.initiateSendReceiveEvent(2);
        node1.initiateSendReceiveEvent(0);
        Thread.sleep(50);
        assertEquals(Order.HAPPENED_AFTER,Api.compareTwoEvents(6,3,snapshots,logger));
        assertEquals(Order.HAPPENED_BEFORE,Api.compareTwoEvents(3,6,snapshots,logger));
    }
}
