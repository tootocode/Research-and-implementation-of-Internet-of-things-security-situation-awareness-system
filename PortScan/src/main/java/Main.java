import queue.IPConsumer;

public class Main {
    public static void main(String[] argv){
        IPConsumer consumer=new IPConsumer();
        while(true){
            consumer.getIP();
            System.out.println(1);
        }
    }
}
