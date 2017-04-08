package Utilities;

import fileManage.MessageType;

public class Header {
	public static final String CRLF = "\r\n";   // CR:0xd + LF:0xa

	private MessageType msgType;
	private String version;
	private String sender_id;
	private String file_id;
	private String chunkNumber;
	private String replicationDegree;

	public Header(String type,String version, String sender_id, String file_id,String chunkNumber, String replicationDegree){
		this.msgType = MessageType.valueOf(type);
		this.version = version;
		this.sender_id = sender_id;
		this.file_id = file_id;
		this.chunkNumber = chunkNumber;
		this.replicationDegree = replicationDegree;
	}

	public Header(String type, String version, String sender_id, String file_id){
		this.msgType = MessageType.valueOf(type);
		this.version = version;
		this.sender_id = sender_id;
		this.file_id = file_id;
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

	public MessageType getType(){
		return this.msgType;
	}
}
