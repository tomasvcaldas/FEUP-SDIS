package UI;

import peer.PeerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class testApp {

    private testApp() {}

    public static void main(String[] args) {

        //String host = (args.length < 1) ? null : args[0];
        try {
            System.out.println("testApp started");
            Registry registry = LocateRegistry.getRegistry();
            PeerInterface stub = (PeerInterface) registry.lookup("processInfo");
            stub.processInfo(args[0], args);
            System.out.println("response: received " + args[0]);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}