package peer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PeerInterface extends Remote {
    public void process(String type, String[] args) throws IOException;
    public void backup(String[] args) throws IOException;
    //public void delete() throws RemoteException;
    //public void restore() throws RemoteException;
}