public class Message {
    int senderId;
    int receiverId;
//    long[] timeStamp;
    Type type;


    public Message(){

    }
    public Message senderId(int senderId ){
        this.senderId=senderId;
        return this;
    }
    public Message receiverId(int receiverId ){
        this.receiverId=receiverId;
        return this;
    }
//    public Message timeStamp(long[] timeStamp){
//        this.timeStamp=timeStamp;
//        return this;
//    }
    public Message type(Type type){
        this.type=type;
        return this;
    }

    public int getSenderId() {
        return senderId;
    }
    public int getReceiverId() {
        return receiverId;
    }
//    public long[] getTimeStamp() {
//        return timeStamp;
//    }
    public Type getType() {
        return type;
    }
}
