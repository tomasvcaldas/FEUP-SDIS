package peer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PeerInterface extends Remote {
    void processInfo(String type, String[] TestAppArgs) throws IOException;
    void backup(String[] args) throws IOException;
    void delete(String[] args) throws IOException;
    void restore(String[] args) throws IOException;

}