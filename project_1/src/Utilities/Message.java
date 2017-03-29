package Utilities;

import java.net.DatagramPacket;


public class Message{

  private String body;
  private Header header;

  public Message(String body, String[] header_string){
    this.body = body;

    this.header.version = header_string[1];
    this.header.sender_id = header_string[2];
    this.header.file_id = header_string[3];
    this.header.chunkNumber = header_string[4];
    this.header.replicationDegree = header_string[5];
  }

  public static Message getMessage(DatagramPacket packet){
    String data_received = new String(packet.getData(), 0, packet.getLength());
    String final_line = Header.CRLF+ Header.CRLF;

    String[] split_array = data_received.split(final_line);
    int index_body = split_array[0].length() + 4;
    String body = new String(packet.getData(), index_body, packet.getLength());

    String[] header_string = split_array[0].split("\\r");


    return new Message(body, header_string);
  }

  public static void main(String[] args){
    byte[] b = "olaolaolaola".getBytes();
    //System.out.println(splitMessage(b));
	}
}
