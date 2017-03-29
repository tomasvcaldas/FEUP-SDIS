package Utilities;

import java.net.DatagramPacket;


public class Message{

  private String body;
  private Header header;

  Message(String body, Header header){
    this.body = body;
    this.header = header;

  }

  public static Message getMessage(DatagramPacket packet){
    String data_received = new String(packet.getData(), 0, packet.getLength());
    String final_line = Header.CRLF+ Header.CRLF;

    String[] split_array = data_received.split(final_line);
    int index_body = split_array[0].length() + 4;
    String body = new String(packet.getData(), index_body, packet.getLength());

    String[] header = split_array[0].split("\\r");

    /*
    header.version = header[1];
    Header.sender_id = header[2];
    Header.file_id = header[3];
    Header.chunkNumber = header[4];
    Header.replicationDegree = header[5];
  */


    return new Message(body, header);
  }

  public static void main(String[] args){
    byte[] b = "olaolaolaola".getBytes();
    //System.out.println(splitMessage(b));
	}
}
