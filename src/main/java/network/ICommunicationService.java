package network;

import java.lang.reflect.Method;

public interface ICommunicationService {

    public void sendInfo(byte[] info);

    public void registerInfoHandler(Method handler);
}
