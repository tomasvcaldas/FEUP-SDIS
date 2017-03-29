package Utilities;

import fileManage.MessageType;

public class Header {
	public static final String CRLF = "\r\n";   // CR:0xd + LF:0xa

	public MessageType type;
	public String version;
	public String sender_id;
	public String file_id;
	public String chunkNumber;
	public String replicationDegree;
}
