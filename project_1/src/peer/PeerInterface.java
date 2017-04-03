package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PeerInterface extends Remote {
    public void process(String type, String[] args) throws RemoteException;
    //public void backup() throws RemoteException;
    //public void delete() throws RemoteException;
    //public void restore() throws RemoteException;
}