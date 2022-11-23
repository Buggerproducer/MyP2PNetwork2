package default_package;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;





public class LoginFrame extends JFrame {
	
	private JPanel loginContent;
	private JLabel iPJLabel;
	private JLabel usernameJLabel;
	private JLabel rMJLabel;
	private JLabel portJLabel;
	private JTextField iPTextField;
	private JTextField rMTextField;
	private JTextField userNameTextField;
	private JTextField proTextField;
	private JButton linked_serverButton;
	
	//use this constructor to draw the login frame
	public LoginFrame(){
		//set Title
		setTitle("Welcome to MY P2P Network");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300,300,600,600);
		setResizable(false);
		//set the content Panel
		loginContent = new JPanel();
		loginContent.setBackground(new Color(100,149,237));
		loginContent.setLayout(null);
		setContentPane(loginContent);

		//initialize the controller
		iPJLabel = new JLabel("IP Address");
		usernameJLabel = new JLabel("UserName");
		rMJLabel = new JLabel("Routing Matric");
		iPTextField = new JTextField("127.0.0.1");
		userNameTextField = new JTextField();
		rMTextField = new JTextField();
		linked_serverButton = new JButton("Linked to Server");
		proTextField = new JTextField();
		portJLabel = new JLabel("Port")
;
		//set the image
		JLabel image = new JLabel(new ImageIcon("login.png"));
		loginContent.add(image);
		image.setBounds(25, 100, 350, 350);
		
//		image = new JLabel(new ImageIcon("footer.png"));
//		loginContent.add(image);
//		image.setBounds(100, 400, 350, 100);
		
		//set the title
		JLabel title = new JLabel("Welcome to MY P2P Network");
		title.setBounds(220, 55, 400, 20);
		title.setFont(new Font("΢���ź�", Font.PLAIN, 25));
		loginContent.add(title);
		
		//set the IP input-controller
		iPJLabel.setBounds(350,115,150,20);
		iPJLabel.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(iPJLabel);
		
		iPTextField.setBounds(350, 135, 200, 40);
		iPTextField.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(iPTextField);
		
		//set the userName input-controller
		usernameJLabel.setBounds(350,195,100,20);
		usernameJLabel.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(usernameJLabel);
		
		userNameTextField.setBounds(350, 215, 200, 40);
		userNameTextField.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(userNameTextField);
		
		//set the routingMatrix input-controller
		rMJLabel.setBounds(350,275,150,30);
		rMJLabel.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(rMJLabel);
		
		rMTextField.setBounds(350, 305, 200, 40);
		rMTextField.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(rMTextField);
		
		portJLabel.setBounds(350,365,150,30);
		portJLabel.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(portJLabel);
		
		proTextField.setBounds(350, 395, 200, 40);
		proTextField.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		loginContent.add(proTextField);
		
		//set link button
		linked_serverButton.setBounds(350,455,200,40);
		linked_serverButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!iPTextField.getText().equals("")&&!userNameTextField.getText().equals("")&&!rMTextField.getText().equals("")) {
					
					ClientFrame cFrame = new ClientFrame(iPTextField.getText().trim(),userNameTextField.getText().trim(),Integer.valueOf(rMTextField.getText()),Integer.valueOf(proTextField.getText()));
					cFrame.setVisible(true);
					dispose();
				}else {
					// if one of the information is null, send a dialog to warm
					JOptionPane.showMessageDialog(null, "The ip address, username and routing metric cannot be empty!", "Warm", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		loginContent.add(linked_serverButton);


		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoginFrame linkServerFrame = new LoginFrame();// �������ӷ������������
		linkServerFrame.setVisible(true);// ��ʾ���ӷ���������
	}

}
