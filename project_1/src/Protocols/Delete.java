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

        String message = createDeleteHeader(fileName, serverId);
        byte file[] = message.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(file);
        byte c[] = outputStream.toByteArray();

        DatagramPacket packet = new DatagramPacket(c, c.length, mc.getAdress(), mc.getPort());

        mc.getSocket().send(packet);


    }

    public static void deleteChunks(String fileName) {
        final String filePath = System.getProperty("user.dir");

        String fileId = sha256(fileName);
        File file = new File(filePath);
        File[] dirListing = file.listFiles();

        for (int i = 0; i < dirListing.length; i++) {
            if (dirListing[i].isDirectory() && dirListing[i].getName().contains("Peer")) {
                File[] filesListing = dirListing[i].listFiles();

                for (int j = 0; j < filesListing.length; j++) {
                    if(filesListing[j].getName().contains(fileId)){
                        File[] chunksList = filesListing[j].listFiles();
                        for(int k = 0; k < chunksList.length; k++){
                            chunksList[k].delete();
                            System.out.println("chunk deleted");
                        }
                        if(chunksList.length== 0){
                            System.out.println("folder empty, deleting");
                            filesListing[j].delete();
                        }
                    }
                }
            }
        }
    }
}