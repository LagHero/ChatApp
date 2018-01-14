import model.Info;
import network.ICommunicationService;
import network.MockCommunicationService;
import org.apache.commons.lang3.SerializationUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AppService {

    private final ICommunicationService commService;
    private static final BlockingQueue<Info> infoQueue = new LinkedBlockingQueue<Info>();

    public AppService() {
        commService = new MockCommunicationService();
        try {
            Method infoReceivedMethod = getClass().getDeclaredMethod("infoReceived", byte[].class);
            commService.registerInfoHandler(infoReceivedMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will send the given message into the network.
     * @param info
     */
    public void sendInfo(Info info){
        // Convert the info object into a byte array
        byte[] serializedInfo = SerializationUtils.serialize(info);

        // Send the serialized info object into the network
        commService.sendInfo(serializedInfo);
    }

    /**
     * @return True if there are any info objects ready to be received.
     * @see #receiveInfo()
     */
    public boolean isUnreceivedInfo(){
        return !infoQueue.isEmpty();
    }

    /**
     * @return The first message received from the network, otherwise null.
     * @see #isUnreceivedInfo()
     */
    public Info receiveInfo(){
        return infoQueue.isEmpty() ? null : infoQueue.poll();
    }

    /**
     * @return The first message received from the network if available, otherwise waits until
     * there is a message available to return.
     */
    public Info waitToReceiveInfo() throws InterruptedException {
        return infoQueue.take();
    }

    /**
     * This method will be called from the communication service when there is new info received.
     * @param serializedInfo
     */
    public static void infoReceived(byte[] serializedInfo){
        try{
            // Convert the byte array into an info object
            Info info = SerializationUtils.deserialize(serializedInfo);
            /*
             * REVIEW: How to handle malicious code from being received, hopefully
             * SerializationUtils will just fail to convert the bad bytes into an info object.
             */

            // Store the received objects in a queue, using add()
            // Use add so we don't keep the communication service waiting
            infoQueue.add(info);
        }
        catch (Exception e){
            // TODO: Handle the error better
            e.printStackTrace();
        }
    }
}
