package model;

import java.net.Socket;

public class PostEvent {
	
	private String message;
	private String fileRequest;
	private Socket socket;
	
	public PostEvent(String message, String fileRequest, Socket socket) {
		this.message = message;
		this.fileRequest = fileRequest;
		this.socket = socket;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getFileRequest() {
		return fileRequest;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
}