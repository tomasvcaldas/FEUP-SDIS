package Utilities;

import java.net.DatagramPacket;

import Utilities.Header;

public class Message{

  private String body;
  private Header header;


  public Message(String body, String[] header_string){
    this.body = body;
    System.out.println("added body to message");
    this.header = new Header(header_string[1], header_string[2], header_string[3], header_string[4], header_string[5],header_string[6]);
    System.out.println("added header to message");
  }

  public static Message getMessage(DatagramPacket packet){
    String data_received = new String(packet.getData());
    //System.out.println("After creating string");
    String data_final = data_received.trim();
    String final_line = Header.CRLF+ Header.CRLF;

    String[] split_array = data_final.split(final_line);
    int index_body = split_array[0].length() + 4;
    System.out.println("before body");
    String body = new String(packet.getData(), index_body, 256);
    System.out.println("after body");

    //System.out.println(body);

    String[] header_string = split_array[0].split("\\r");

   return new Message(body, header_string);

  }

  public Header getHeader(){
    return this.header;
  }

  public String getBody(){
    return this.body;
  }

  public static void main(String[] args){
    byte[] b = "olaolaolaola".getBytes();

  }

  public static String createPutHeader(int senderId, String fileId, int chunkNo, int repDeg){
    String header;
    String sender = Integer.toString(senderId);
    String chunkNumber = Integer.toString(chunkNo);
    String replicationDeg = Integer.toString(repDeg);

    header = "PUTCHUNK "+ "1.0 " + sender + " " + fileId + " " + chunkNumber + " " + replicationDeg + " " + Header.CRLF+Header.CRLF;
    return header;
  }






}
