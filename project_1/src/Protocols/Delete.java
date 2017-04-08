package Protocols;


import Channels.ControlChannel;
import peer.Peer;

import javax.naming.ldap.Control;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

import static Utilities.Hash.sha256;

public class Delete {


    public static void Delete(String fileName, String serverId, ControlChannel mc) throws IOException {
        byte file[] = fileName.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(file);
        byte c[] = outputStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(c, c.length, mc.getAdress(), mc.getPort());
        mc.getSocket().send(packet);


    }

    public static void deleteChunks(String fileName) {
        final String filePath = System.getProperty("user.dir");
        System.out.println("current dir = " + filePath);

        String fileId = sha256(fileName);
        File file = new File(filePath);
        File[] dirListing = file.listFiles();

        for (int i = 0; i < dirListing.length; i++) {
            if (dirListing[i].isDirectory() && dirListing[i].getName().contains("Peer")) {
                File[] filesListing = dirListing[i].listFiles();
                for (int j = 0; j < filesListing.length; j++) {
                    if (filesListing[j].isDirectory() && filesListing[j].getName().equals(fileId)) {
                        File[] chunksListing = filesListing[j].listFiles();
                        for(int k = 0; k < chunksListing.length; k++){
                            chunksListing[k].delete();
                        }
                    }
                }

            }

        }


    }
}