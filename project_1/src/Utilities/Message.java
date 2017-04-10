package Utilities;

import java.net.DatagramPacket;

import Utilities.Header;
import fileManage.MessageType;

public class Message{

  private byte[] body;
  private Header header;


  public Message(byte[] body, String[] header_string){
    this.body = body;
    if(header_string[0].equals(MessageType.DELETE.name())){
        System.out.println("Creating DELETE header...");
        this.header = new Header(header_string[0], header_string[1],header_string[2],header_string[3]);
    }
    if(header_string[0].equals(MessageType.PUTCHUNK.name())){
        System.out.println("Creating PUTCHUNK header...");
        this.header = new Header(header_string[0], header_string[1], header_string[2], header_string[3], header_string[4],header_string[5]);
    }
    if(header_string[0].equals(MessageType.STORED.name()) || header_string[0].equals(MessageType.GETCHUNK.name()) || header_string[0].equals(MessageType.CHUNK.name())){
          System.out.println("Creating STORE header...");
          this.header = new Header(header_string[0], header_string[1], header_string[2], header_string[3], header_string[4]);
    }
  }

    /**
     * Receives the packet and returns the body information in a byte[]
     * @param data Packet data
     * @param index_body index where the body starts
     * @param length packet length
     * @return body content in byte[]
     */
    public static byte[] array(byte[] data, int index_body,int length){
        int size = length - index_body;
        if(size <= 0)
            return data;
        byte[] toRet = new byte[size];

        for(int i = index_body; i < length; i++) {
            toRet[i - index_body] = data[i];
        }
        return toRet;
    }

    /**
     * Transforms the packet received in a message with separated header from body
     * @param packet Packet to handle
     * @return Message with Header and Body
     */
  public static Message getMessage(DatagramPacket packet){
    String data_received = new String(packet.getData(),0,packet.getLength());
    String[] receivedArray = data_received.split("\\r\\n\\r\\n");

    int index_body = receivedArray[0].length() + 4;

    byte[] bArray = array(packet.getData(), index_body, packet.getLength());
      
    String[] header_string = receivedArray[0].split(" ");

    return new Message(bArray, header_string);

  }

  public Header getHeader(){
    return this.header;
  }

  public byte[] getBody(){
    return this.body;
  }

  public static void main(String[] args){
  }

    /**
     * Creates Header for the PUTCHUNK
     * @param senderId putchunk sender ID
     * @param fileId putchunk file Id
     * @param chunkNo putchunk chunk Number
     * @param repDeg putchunk replication degree
     * @return Header in String
     */
  public static String createPutHeader(int senderId, String fileId, int chunkNo, int repDeg){
    String header;
    String sender = Integer.toString(senderId);
    String chunkNumber = Integer.toString(chunkNo);
    String replicationDeg = Integer.toString(repDeg);

    header = "PUTCHUNK "+ "1.0 " + sender + " " + fileId + " " + chunkNumber + " " + replicationDeg + " " + Header.CRLF+Header.CRLF;
    return header;
  }

    /**
     * Creates the message for the DELETE
     * @param fileName message file name
     * @param serverID message server Id
     * @return DELETE message with the required arguments
     */
  public static String createDeleteHeader(String fileName, String serverID){
    return MessageType.DELETE + " 1.0 " + serverID + " " + fileName + Header.CRLF + Header.CRLF;
  }

    /**
     * Creates the message for the STORED
     * @param senderID message sender Id
     * @param fileID message file Id
     * @param chunkNo message chunk number
     * @return STORED message with the required arguments
     */
  public static String createStoredHeader(String senderID, String fileID, int chunkNo){
      return MessageType.STORED + " 1.0 " + senderID + " " + fileID + " " + String.valueOf(chunkNo) + " " + Header.CRLF + Header.CRLF;
  }

    /**
     * Creates the message for the GETCHUNK
     * @param fileName message file name
     * @param serverID message serverId
     * @param chunk message chunk number
     * @return GETCHUNK message with the required arguments
     */
  public static String createGetChunkHeader(String fileName, String serverID, int chunk){
      return MessageType.GETCHUNK + " 1.0 " + serverID + " " + fileName + " " + String.valueOf(chunk) + " " + Header.CRLF + Header.CRLF;
  }

    /**
     * Creates the message for the CHUNK
     * @param senderID message sender ir
     * @param fileID message file id
     * @param chunkNo message chunk number
     * @return CHUNK message with the required arguments
     */
  public static String createChunkHeader(String senderID, String fileID, String chunkNo){
      return MessageType.CHUNK + " 1.0 " + senderID + " " + fileID + " " + chunkNo + Header.CRLF + Header.CRLF;
  }
}
