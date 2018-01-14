package network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MockCommunicationService implements ICommunicationService{

    Method receiveInfoHandler = null;

    public void sendInfo(byte[] info) {
        if (receiveInfoHandler != null){
            try {
                receiveInfoHandler.invoke(null, info);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerInfoHandler(Method handler) {
        receiveInfoHandler = handler;
    }
}
