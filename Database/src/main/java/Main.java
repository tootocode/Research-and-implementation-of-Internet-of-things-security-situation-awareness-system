import queue.ConsumerMessage;

public class Main {
    public static void main(String[] argv) throws InterruptedException {

        while(true){
            ConsumerMessage consumerMessage=new ConsumerMessage();
            consumerMessage.storeResult();
            Thread.sleep(1000*60*60*24);
        }

    }
}
