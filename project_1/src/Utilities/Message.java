package Utilities;

import java.net.DatagramPacket;

import static Utilities.Header.CRLF;

public class Message{

  public String body;
  public Header header;
  public Message(String body, String[] header){

  }

  public static Message getMessage(DatagramPacket packet){
    String data_received = new String(packet.getData(), 0, packet.getLength());
    String final_line = CRLF + CRLF;

    String[] split_array = data_received.split(final_line);
    int index_body = split_array[0].length() + 4;
    String body = new String(packet.getData(), index_body, packet.getLength());

    String[] header = split_array[0].split(" ");

    return new Message(body, header);
  }

  public static void main(String[] args){
    byte[] b = "olaolaolaola".getBytes();
    //System.out.println(splitMessage(b));
	}
}
