package default_package;

import java.net.Socket;

public class Peer {
	
	Socket socketItem;
	int routingMetrix;
	String userName;
	String userGUID;
	int serverPort;
	
	public Socket getSocketItem() {
		return socketItem;
	}
	
	public Peer(Socket socket,String userString,int routingMatrix,int port) {
		this.socketItem = socket;
		this.userName = userString;
		this.userGUID = HashKit.sha1(userString);
		this.routingMetrix = routingMatrix;
		this.serverPort = port;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getGUID() {
		return userGUID;
		
	}
	
	public int getMetric() {
		return routingMetrix;
	}
	
	public int getServerPort() {
		return serverPort;
	}

}
