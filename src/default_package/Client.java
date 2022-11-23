package default_package;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore.PrivateKeyEntry;



public class Client {
	//the socket of the client
	private Socket clientSocket;
	// the byte strem is used to read socket input stream
	private BufferedReader bfReader;
	//the writer to write the soke output stream
	private PrintWriter pWriter;
	//the file stream
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private FileInputStream fileInputStream;
	private String fileName;
	private long fileSize;
	private byte[] byteData =new byte[10240];
	private String ip;
	private int serverPort;
	
	//server socket
	ServerSocket serverSocket;
	
	public Client(String h,int p,int server_port) throws UnknownHostException, IOException {
		clientSocket =new Socket(h,p);
		ip = h;
		
		serverSocket = new ServerSocket(server_port);
		
		bfReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		pWriter = new PrintWriter(clientSocket.getOutputStream());
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	
	
	
	
	
	public void sendFile(File file,int target) {
		try {
			Socket requestSocket = new Socket(ip,target);

			fileInputStream = new FileInputStream(file);
			dataOutputStream = new DataOutputStream(requestSocket.getOutputStream());
			int len = 0;
			long progress = 0;
			System.out.println(file.length());
			while ((len = fileInputStream.read(byteData,0,byteData.length))!=-1) {
				
				dataOutputStream.write(byteData,0,len);
				dataOutputStream.flush();
				progress = progress+len;
				System.out.println("|"+(100*progress/file.length())+"%|");
				
			}
			fileInputStream.close();
			dataOutputStream.close();
			requestSocket.close();
//			clientSocket.close();
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void receiveFile(int fileName,String nString) {
		// TODO Auto-generated method stub
		try {
			Socket sendSocket = serverSocket.accept();
			DataInputStream dis = new DataInputStream(sendSocket.getInputStream());

            File directory = new File("/Users/gilbertyou/eclipse-workspace/MyP2PNetwork/"+fileName+"/"+nString+".pdf");


            FileOutputStream out = new FileOutputStream(directory);

            byte[] bytes = new byte[10240]; 

            int length = 0;
            
            

            while((length = dis.read(bytes, 0, bytes.length)) != -1) {

            		out.write(bytes, 0, length);

            		out.flush();

            }
            System.out.println(directory.length());

            System.out.println("receive successfully");

        } catch (Exception e) {

            e.printStackTrace();

        } 

    }
	public void sendMyMessage(String message) {
		pWriter.println(message);
		pWriter.flush();
	}
	
	
	
	
	
	public String receiveMyMessage() {
		try {
			return bfReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
//	private void sendFile() throws IOException {
//		// TODO Auto-generated method stub
//		int length;
//		byte[] buf;
//		t = 1;
//		fileName = file.getName();
//		fileSize = file.length();
//		//send name
//        buf = doPackage(null, 0,1);
//        dataOutputStream.write(buf);
//        t = 0;
//        //wait
//        fileInputStream.skip(dataInputStream.readLong());
//        clientSocket.setSoTimeout(30000);
//        //send context
//        length = readFromFile(byteData, 0, 10240);
//        dataOutputStream.write(doPackage(byteData, length, 0));
//        
//
//        
//		
//		
//
//	}
//	
//	private byte[] doPackage(byte[] data, int i,int type) {
//		// TODO Auto-generated method stub
//		 ByteArrayOutputStream buf = new ByteArrayOutputStream();
//	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	     DataOutputStream bufDos = new DataOutputStream(buf);
//	     DataOutputStream baosDos = new DataOutputStream(baos);
//	     if (type == 1) {
//			 try {
//			bufDos.writeInt(0x01);
//			bufDos.writeInt(fileName.getBytes().length);
//			bufDos.write(fileName.getBytes());
//			bufDos.writeLong(fileSize);
//			
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else if (type == 0) {
//			try {
//				bufDos.writeInt(0x02);
//				if ((0 + i) >= fileSize) {
//					bufDos.writeInt(0);
//	            } else {
//	                bufDos.writeInt(1);
//	            }
//	            bufDos.writeInt(i);
//	            bufDos.write(data, 0, i);
//	            baosDos.writeInt(buf.toByteArray().length);
//	            baosDos.write(buf.toByteArray());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//		} 
//	    
//		return baos.toByteArray();
//	}
	
	//read from file
    private int readFromFile(byte[] data,int off,int length) {
        int len=0;
        try {
            len = fileInputStream.read(data,off,length);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return len;
    }
    
    

	public void closeSocket() {
		try {
			clientSocket.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
