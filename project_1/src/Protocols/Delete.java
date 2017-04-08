package Protocols;


import Channels.ControlChannel;
import fileManage.MessageType;
import peer.Peer;

import javax.naming.ldap.Control;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

import static Utilities.Hash.sha256;
import static Utilities.Message.createDeleteHeader;

public class Delete {


    public static void Delete(String fileName, String serverId, ControlChannel mc) throws IOException {
        System.out.println("1");
        String message = createDeleteHeader(fileName, serverId);
        byte file[] = message.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.out.println("2");
        outputStream.write(file);
        byte c[] = outputStream.toByteArray();
        System.out.println("3");
        DatagramPacket packet = new DatagramPacket(c, c.length, mc.getAdress(), mc.getPort());
        System.out.println(new String(c));
        mc.getSocket().send(packet);
        System.out.println("5");

    }

    public static void deleteChunks(String fileName) {
        final String filePath = System.getProperty("user.dir");
        System.out.println("current dir = " + filePath);

        new File("merda").mkdir();

        String fileId = sha256(fileName);
        File file = new File(filePath);
        File[] dirListing = file.listFiles();

        for (int i = 0; i < dirListing.length; i++) {
            if (dirListing[i].isDirectory() && dirListing[i].getName().contains("Peer")) {
                File[] filesListing = dirListing[i].listFiles();
                for (int j = 0; j < filesListing.length; j++) {
                    if (filesListing[j].isDirectory() && filesListing[j].getName().equals(fileId)) {
                        filesListing[j].delete();
                        System.out.println("All chunks and folder delete!");

                    }
                }

            }

        }


    }
}