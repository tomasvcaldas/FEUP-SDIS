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

	public Header(String version, String sender_id, String file_id,String chunkNumber, String replicationDegree){
		this.version = version;
		this.sender_id = sender_id;
		this.file_id = file_id;
		this.chunkNumber = chunkNumber;
		this.replicationDegree = replicationDegree;
	}


	public String getVersion(){
		return version;
	}

	public String getSenderId(){
		return sender_id;
	}

	public String getFileId(){
		return file_id;
	}

	public String getChunkNumber(){
		return chunkNumber;
	}

	public String getReplicationDegree(){
		return replicationDegree;
	}
}
