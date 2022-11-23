package default_package;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;






public class ClientFrame extends JFrame{
	JPanel clientContentJPanel;
	JLabel infoJLabel;
	JTextField inputMessageField;
	JTextArea messageArea;
	JButton uploadResourceButton;
	JButton requestButton;
	JButton dHRTButton;
	JButton removeResource;
//	JButton requestContent;
	JScrollPane chatBoxArea;
	JButton fileButton;
	JTextField fileTextField;
	File[] files;
	String fileName;
	String nameOfFileString;
	int sendPort;
	boolean isRequested = false;
	int serverPort;
	String peerNameString;
	
	 private String userName;
	 private String ip;
	 private int routing_metric;
	 
	 private Client client;
	 
	 

	
	
	public ClientFrame(String ip, String userName, Integer routing_metric,Integer serverPort) {
		// TODO Auto-generated constructor stub
		try {
			client = new Client(ip,5000,serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		infoJLabel = new JLabel(ip+"\n"+serverPort);

		this.ip = ip;
		this.routing_metric = routing_metric;
		this.userName = userName;
		this.serverPort = serverPort;
		initWindow();
		
		//create the thread and start
		ClientFrame.messageReading readingThReading = new ClientFrame.messageReading();
		readingThReading.start();
		
		ClientFrame.FileReceive fileReceive = new ClientFrame.FileReceive();
		fileReceive.start();
		
		setMyListener();
	}
	



	private void setMyListener() {
		// TODO Auto-generated method stub
		//open window and close window listener
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				//if a user login, client send the message that contains the "&Login&"
				super.windowOpened(e);
				client.sendMyMessage("&Login&-"+userName+"-"+routing_metric+"-"+serverPort);
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				int confirm = JOptionPane.showConfirmDialog(ClientFrame.this,"Sure to Quit?");
				if (confirm == JOptionPane.YES_OPTION) {
					//confirm to quit, send the messag which contains 
					client.sendMyMessage("&QUIT&-"+userName+"-"+routing_metric);
				}try {
					Thread.sleep(300);
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				
				//close the fram
				client.closeSocket();
				System.exit(0);
			}
		});
		
		//the client upload the resource to the server side
		uploadResourceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String message = inputMessageField.getText();
				String fileString = "";
				String fileNameString = null;
				fileName =fileTextField.getText();

				
				if(fileString != null) {
					for(int i =0;i<fileName.split("/").length;i++) {
						if (i != fileName.split("/").length -1) {
							fileString = fileString + fileName.split("/")[i]+"\\";
							
						}else {
							fileNameString = fileName.split("/")[i];
							fileString = fileString + fileName.split("/")[i];
						}
					}
				}
				
					File sourceFile = new File(fileName);
					File desfile = new File("/Users/gilbertyou/eclipse-workspace/MyP2PNetwork/+"+serverPort+"/"+fileNameString);
//					try {
//						copyFileUsingFileStreams(sourceFile, desfile);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					client.sendFile(file,2000);
//					client.sendMyMessage("UPLOADRESOURCE&&&&"+userName+"---"+fileNameString);
//				}
				if (message.equals("")) {
					JOptionPane.showMessageDialog(null, "Empty input is not allowed to upload");
				}else {
					client.sendMyMessage("UPLOADRESOURCE&&&&"+userName+"---"+fileNameString);
				}
//				set the input text empty
				inputMessageField.setText("");
				fileTextField.setText("");
			}
		});
		
		//the client want to konw the DHRT
		dHRTButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMyMessage("ShowDHRT&&&&");
				
			}
		});
		
		requestButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String message = inputMessageField.getText();
				if (message.equals("")) {
					JOptionPane.showMessageDialog(null, "Empty input is not allowed to upload");
				}else {
					client.sendMyMessage("Request&&&&"+message+"---"+userName);
					nameOfFileString = message;				}
				//set the input text empty
				inputMessageField.setText("");
			}
		});
		
		removeResource.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				String message = inputMessageField.getText();
//				if (message.equals("")) {
//					JOptionPane.showMessageDialog(null, "Empty input is not allowed to upload");
//				}else {
//					client.sendMyMessage("RemoveSource&&&&"+message+"---"+userName);
//				}
//				//set the input text empty
//				inputMessageField.setText("");
				fileName = "/Users/gilbertyou/eclipse-workspace/MyP2PNetwork/"+serverPort+"/"+nameOfFileString;
				File file = new File(fileName);
				System.out.println(sendPort);
				client.sendFile(file, sendPort);
				client.sendMyMessage("RemoveSource&&&&"+sendPort+"&&&&"+peerNameString);
			
				
			}
	
		});
//		requestContent.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				String message = inputMessageField.getText();
//				String peerName = JOptionPane.showInputDialog("Request Resource from: ");
//                if(peerName != null && !message.equals("")){
//                    client.sendMyMessage("getContent&&&&" + message + "---" + peerName);
//                }
//                inputMessageField.setText("");
//			}
//		});
		
		fileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jChooser = new JFileChooser();
				jChooser.setMultiSelectionEnabled(true);
				fileName = "";
				jChooser.showOpenDialog(null);
				files = jChooser.getSelectedFiles();
				for(int i = 0; i < files.length;i++) {
					fileName = fileName + files[i].getAbsolutePath();
				}
				fileTextField.setText(fileName);
			
			}
		});
	}
	

	public void initWindow() {
		setTitle("Linked Room");
		setTitle("Welcome to MY P2P Network");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300,300,600,600);
		setResizable(false);
		//set the content Panel
		clientContentJPanel = new JPanel();
		clientContentJPanel.setBackground(new Color(100,149,237));
		clientContentJPanel.setLayout(null);
		setContentPane(clientContentJPanel);
		JScrollPane contentJScrollPane = new JScrollPane();
		contentJScrollPane.setBounds(5, 35, 400, 400);
		clientContentJPanel.add(contentJScrollPane);
		
		messageArea = new JTextArea();
        messageArea.setFont(new Font("微软雅黑", Font.PLAIN, 17));
        contentJScrollPane.setViewportView(messageArea);
        messageArea.setEditable(false);

		
		JLabel inputLabel = new JLabel("Input your message here");
		inputLabel.setBounds(5,495,200,40);
		clientContentJPanel.add(inputLabel);
		
		inputMessageField = new JTextField();
		inputMessageField.setBounds(5,525,400,40);
		clientContentJPanel.add(inputMessageField);
		
		infoJLabel.setBounds(420,25,150,40);
		clientContentJPanel.add(infoJLabel);
		
		uploadResourceButton = new JButton("Upload Resource");
		uploadResourceButton.setBounds(430,75,150,40);
		clientContentJPanel.add(uploadResourceButton);
		
		dHRTButton = new JButton("show DHRT");
		dHRTButton.setBounds(430,145,150,40);
		clientContentJPanel.add(dHRTButton);
		
		removeResource = new JButton("Send");
		removeResource.setBounds(430,215,150,40);
		clientContentJPanel.add(removeResource);
		
		requestButton = new JButton("Request");
		requestButton.setBounds(430,285,150,40);
		clientContentJPanel.add(requestButton);
		
//		requestContent = new JButton("Request the Content");
//		requestContent.setBounds(430,355,150,40);
//		clientContentJPanel.add(requestContent);
		
		JLabel fileJLabel = new JLabel("File");
		fileJLabel.setBounds(5,435,200,40);
		clientContentJPanel.add(fileJLabel);
		
		fileTextField = new JTextField();
		fileTextField.setBounds(5,465,400,40);
		clientContentJPanel.add(fileTextField);
		
		fileButton = new JButton("Browse");
		fileButton.setBounds(430,465,70,40);
		clientContentJPanel.add(fileButton);
		
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ClientFrame cFrame = new ClientFrame();
//		cFrame.setVisible(true);
//		
//	}
	
	private class messageReading extends Thread{
		boolean is_rusume = true;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (is_rusume) {
//				read the message
				String messageString  = client.receiveMyMessage();
				if(messageString.contains("ServerPort&&&&")) {
					sendPort = Integer.valueOf(messageString.split("&&&&")[1]);
					nameOfFileString = messageString.split("&&&&")[3];
					peerNameString = messageString.split("&&&&")[4];

					isRequested = true;
					messageArea.append("The peer "+peerNameString+": "+ sendPort+"request the resource "+nameOfFileString +" from you" );

				}else {
					messageArea.append(messageString+"\n");
				}
				System.out.println(messageString);
				//add the message to ta
				
				
			}
		}
		
	}
	
//	private static void copyFileUsingFileStreams(File source, File dest)
//	        throws IOException {
//	    InputStream input = null;
//	    OutputStream output = null;
//	    try {
//	           input = new FileInputStream(source);
//	           output = new FileOutputStream(dest);
//	           byte[] buf = new byte[1024];
//	           int bytesRead;
//	           while ((bytesRead = input.read(buf)) != -1) {
//	               output.write(buf, 0, bytesRead);
//	           }
//	    } finally {
//	        input.close();
//	        output.close();
//	    }
//	}
//	
	private class FileReceive extends Thread{
		boolean is_resume = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (is_resume) {
//				read the message
				client.receiveFile(serverPort,nameOfFileString);
								
				
			}
		}
	}

}
