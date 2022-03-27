public class Event {
    private Node node;
    private Type type;
    private InternalClock clock;
    public Event(Type type,Node node){
        this.node=node;
        this.type=type;
    }
    public Node getNode(){
        return node;
    }
    public Type getType(){
        return type;
    }
    public InternalClock getClock(){
        return clock;
    }
}
