package application;

public class Message {
	private String recieverId;
    private String senderId;
    private String message;
    private long timestamp;

    
    
	public Message() {
		super();
	}

	public Message(String recieverId, String senderId, String message, long timestamp) {
		super();
		this.recieverId = recieverId;
		this.senderId = senderId;
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getRecieverId() {
		return recieverId;
	}

	public void setRecieverId(String recieverId) {
		this.recieverId = recieverId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

    
}

