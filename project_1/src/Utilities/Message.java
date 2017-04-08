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
        System.out.println("Creating delete header...");
        this.header = new Header(header_string[0], header_string[1],header_string[2],header_string[3]);
    }
    if(header_string[0].equals(MessageType.PUTCHUNK.name())){
        System.out.println("Creating putchunk header...");
        this.header = new Header(header_string[0], header_string[1], header_string[2], header_string[3], header_string[4],header_string[5]);
    }
  }

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

  public static Message getMessage(DatagramPacket packet){
    String data_received = new String(packet.getData(),0,packet.getLength());
    String[] receivedArray = data_received.split("\\r\\n\\r\\n");

    int index_body = receivedArray[0].length() + 4;
    System.out.println(packet.getLength());
    //String body = data_received.substring(index_body,data_received.length());
    byte[] bArray = array(packet.getData(), index_body, packet.getLength());
    //System.out.println("index body: " + index_body +  " len: " + data_received.length());
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

  public static String createPutHeader(int senderId, String fileId, int chunkNo, int repDeg){
    String header;
    String sender = Integer.toString(senderId);
    String chunkNumber = Integer.toString(chunkNo);
    String replicationDeg = Integer.toString(repDeg);

    header = "PUTCHUNK "+ "1.0 " + sender + " " + fileId + " " + chunkNumber + " " + replicationDeg + " " + Header.CRLF+Header.CRLF;
    return header;
  }

  public static String createDeleteHeader(String fileName, String serverID){
    return MessageType.DELETE + " 1.0 " + serverID + " " + fileName + Header.CRLF + Header.CRLF;
  }

  public static String createStoredHeader(String senderID, String fileID, int chunkNo){
      return MessageType.STORED + " 1.0 " + senderID + " " + fileID + " " + String.valueOf(chunkNo) + " " + Header.CRLF + Header.CRLF;
  }
}
