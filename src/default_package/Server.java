package default_package;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;







public class Server {
	//the socket of the server
	ServerSocket serverSocket;
	//stored the user information and the socket
	private HashMap<String, Peer> user_soketHashMap;
	private HashMap<String, Resource> resourcesHashMap;
	private HashMap<Peer, HashMap<String, Resource>> peerResourceHashMap;
	private byte[] byteData = new byte[1024];
	private String fileNameString;
	private long fileSize;
	
	
	public Server() {
		try {
			//create the server socket
			serverSocket = new ServerSocket(5000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		user_soketHashMap = new HashMap<>();
		resourcesHashMap = new HashMap<>();
		peerResourceHashMap = new HashMap<>();
	}
	
       
	        

	
	public void runThread() throws IOException{
		while(true) {
			//get a client socket
			Socket cSocket = serverSocket.accept();
			//create a independent thread for this socket
			Server.ClientThread clientThread = new Server.ClientThread(cSocket);
			clientThread.start();
		}
	}
	public class ClientThread extends Thread{
		Socket clientSocket;
		Peer peer;
		
		public ClientThread(Socket s) {
			// TODO Auto-generated constructor stub
			this.clientSocket = s;
		}
		
		@Override

		public void run() {
			// TODO Auto-generated method stub
			BufferedReader bfBufferedReader;
//			DataInputStream dataInputStream;
			
			try {
				bfBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//				dataInputStream = new DataInputStream(clientSocket.getInputStream());
//				System.out.println();
				//start to read
				while (true) {
					String message = bfBufferedReader.readLine();
					System.out.println(message);
					System.out.println(peer);
					if (message.contains("&QUIT&")) {
						//remove this user from the socket
						user_soketHashMap.remove(peer.getUserName());
						for(Resource resource:resourcesHashMap.values()) {
							if (resource.getHolder().contains(peer)) {
								resource.getHolder().remove(peer);
							}
						}//if the text contains the LOGIN
					}else if (message.contains("&Login&")) {
						String[] info = message.split("-");
						//add the login into the socket
						peer = new Peer(clientSocket, info[1],Integer.valueOf(MAX_PRIORITY),Integer.valueOf(info[3]));
						HashMap<String,Resource> resources = new HashMap<String,Resource>();
						peerResourceHashMap.put(peer, resources);
						System.out.println(peer);
						user_soketHashMap.put(peer.getUserName(), peer);
					}else if (message.contains("UPLOADRESOURCE&")) {
						String[] info = message.split("&&&&");
//						//get the username
//						String file_name = info[1].split("---")[1];
//						String fileName = file_name.substring(0,file_name.length()-1);

						String user_name = info[1].split("---")[0];
						String user_message = info[1].split("---")[1];

						Resource resource;
						
//						System.out.println(user_name+", "+user_message);
//						for(Peer peer:user_soketHashMap.values()) {
//							System.out.println(peer.getUserName()+"h");
//						}
						String pathString = "/Users/gilbertyou/eclipse-workspace/MyP2PNetwork";
//						
//						File directoryFile = new File(pathString+"/"+fileName);
//						receiveFile(clientSocket, directoryFile);
						if (resourcesHashMap.containsKey(user_message)) {
							System.out.println("enter2");

							resource = resourcesHashMap.get(user_message);
							if (resource.getHolder().contains(peer)) {
								System.out.println("enter3");

								sendPrivate(peer.getUserName(), "You have uploaded this resource before");
							}else {
								resource.addMyList(peer);
								System.out.println("enter4");
								peerResourceHashMap.get(peer).get(user_message).addMyList(peer);
								

								sendBoardcase(user_name+": have successfully uploaded the "+ resource.getName());
							}
						}else {
							System.out.print("enter1");
							List<Peer> myHolder = new ArrayList<>();
							myHolder.add(peer);
							resource = new Resource(user_message,myHolder);
							resourcesHashMap.put(resource.getName(),resource);
							peerResourceHashMap.get(peer).put(resource.getName(), resource);

							sendBoardcase(peer.getUserName()+" upload the Resource: "+ resource.getName()+" Successfully");
						}
//						
						
					}else if (message.contains("ShowDHRT&&&&")) {
						sendPrivate(getDHRT(peer), peer.getUserName());
					}else if (message.contains("RemoveSource&")) {
						String[] info = message.split("&&&&");
//						//get the username
//						String user_name = info[1].split("---")[1];
//						String user_message = info[1].split("---")[0];
//						Resource resource;
////						System.out.println(user_name+", "+user_message);
////						for(Peer peer:user_soketHashMap.values()) {
////							System.out.println(peer.getUserName()+"h");
////						}						
//						if (resourcesHashMap.containsKey(user_message)) {
//
//							resource = resourcesHashMap.get(user_message);
//							if (resource.getHolder().contains(peer)) {
//								resource.getHolder().remove(peer);
//								sendBoardcase(peer.getUserName()+" has removed the resource: "+resource.getName());
//							}else {
//								sendPrivate("You haven't uploaded this message", peer.getUserName());
//							}
//						}else {
//							sendPrivate("This resource hasn't been uploaded", peer.getUserName());
//						}
						sendPrivate("File has sended from "+peer.getUserName(), info[2]);
						sendPrivate("You have sent the file to "+info[2], peer.getUserName());
						
					 
					}else if (message.contains("Request&&")) {
						String[] info = message.split("&&&&");
						//get the username
						String user_name = info[1].split("---")[1];
						String user_message = info[1].split("---")[0];
						Resource resource;
//						System.out.println(user_name+", "+user_message);
//						for(Peer peer:user_soketHashMap.values()) {
//							System.out.println(peer.getUserName()+"h");
//						}						
						if (resourcesHashMap.containsKey(user_message)) {

							resource = resourcesHashMap.get(user_message);
							
							if (resource.getHolder().size() > 0) {
								String peerString = "";
								Peer temPeer = null;
								for(Peer p:resource.getHolder()) {
									peerString=p.getUserName()+", "+peerString;
									temPeer = p;
								}
								resource.getHolder().add(peer);
								peerResourceHashMap.get(peer).put(resource.getName(), resource);

								sendPrivate("ServerPort&&&&"+peer.getServerPort()+"&&&&FileName&&&&"+resource.getName()+"&&&&"+peer.getUserName(),temPeer.getUserName());
								sendPrivate("Send the Request to the "+ temPeer.getUserName(), peer.getUserName());
							}else {
								sendPrivate("No accessible resource"+user_message,peer.getUserName());
							}
						}else {
							sendPrivate("No accessible resource"+user_message,peer.getUserName());
						}
						
					 
					}else if (message.contains("getContent&&")) {
						String[] info = message.split("&&&&");
						//get the username
						String user_name = info[1].split("---")[1];
						String user_message = info[1].split("---")[0];
						Resource resource;
//						System.out.println(user_name+", "+user_message);
//						for(Peer peer:user_soketHashMap.values()) {
//							System.out.println(peer.getUserName()+"h");
//						}						
						if (!resourcesHashMap.containsKey(user_message)) {

								sendPrivate("No such resource in the sysrem", peer.getUserName());
							}else if (! user_soketHashMap.containsKey(user_name)) {
								sendPrivate("No such peer in the sysrem", peer.getUserName());
							} else {
								resource = resourcesHashMap.get(user_message);
								Peer p = user_soketHashMap.get(user_name);
								if(resource.getHolder().contains(p)) {
									sendPrivate(peer.getUserName()+"request the content of the Resource: "+resource.getName()+"is ***, uploaded by"+user_name, peer.getUserName());
									sendPrivate(peer.getUserName()+"request your Resource: "+resource.getName(), user_name);

								}else {
									sendPrivate(user_name+"has not such resource in the sysrem", peer.getUserName());

								}
								
						}
						
					 
					}else {
						//not match
                        JOptionPane.showMessageDialog(null, "Please enter correct command_set!", "Warm", JOptionPane.WARNING_MESSAGE);

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			super.run();
		}

		private String getDHRT(Peer peer) {
			// TODO Auto-generated method stub
			String contentString = "";
			for(Resource resource:peerResourceHashMap.get(peer).values()) {
				String pString = "";
				for(Peer p:resource.getHolder()) {
					pString = pString +", "+ p.getUserName();
				}
				contentString += "\n"+"Resource: "+resource.getName()+"\n"
						+"GUID:     "+resource.getGUID()+"\n"
						+"Peers:    "+pString+"\n"
						+"----------------------"+"\n";
				
			}
			return contentString;
		}
	}
	private void receiveFile(Socket clientSocket,File file) {
		// TODO Auto-generated method stub
		try {
			OutputStream outputStream = clientSocket.getOutputStream();
			InputStream inputStream = clientSocket.getInputStream();
			
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//			dataOutputStream.write(1114514);
			
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int len = 0;
			while ((len = dataInputStream.read(byteData,0,byteData.length))!=-1) {
				
				System.out.println(len);
				fileOutputStream.write(byteData,0,len);
				fileOutputStream.flush();
				
			}
			fileOutputStream.close();
	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public void sendPrivate(String messageString, String userID) throws IOException {
		Peer peer = user_soketHashMap.get(userID);
		//check if the user is exists
		if (peer == null) {
			JOptionPane.showMessageDialog(null, "No user in our dialog");
		}else {
			PrintWriter pWriter = new PrintWriter(peer.getSocketItem().getOutputStream());
			pWriter.println(messageString);
			pWriter.flush();
		}
	}
	//send the message to all sockets
	public void sendBoardcase(String messageString) throws IOException {
		for(Peer peer:user_soketHashMap.values()) {
			System.out.println("hello");
			PrintWriter pWriter = new PrintWriter(peer.socketItem.getOutputStream());
			pWriter.println(messageString);
			pWriter.flush();
		}
	}
	
	public static void main(String[] args) {
		//start my server
		try {
			new Server().runThread();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
