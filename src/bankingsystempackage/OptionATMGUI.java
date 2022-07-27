package bankingsystempackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class OptionATMGUI implements ActionListener{
	
	private static JFrame frame = new JFrame();
	private static JButton withdrawal = new JButton("Withdrawal");
	private static JButton deposit = new JButton("Deposit");
	private static JButton transfer = new JButton("Transfer");
	private static JButton switchAcc = new JButton("Switch Accounts");
	private static JButton cancel = new JButton("Cancel");
	
	private static JPanel north = new JPanel();
	//private static JPanel south = new JPanel();
	private static JPanel east = new JPanel();
	private static JPanel west = new JPanel();
	//private static JPanel center = new JPanel();
	
	private static JLabel label1 = new JLabel();		//title
	private static JLabel label2 = new JLabel();		//current account
	private static JLabel label3 = new JLabel();		//money 
	private static JLabel label4 = new JLabel();
	
	JTextField userId;
	JPasswordField passText;
	
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    
    private User currentCustomer;
    private ArrayList<Account> accounts;
	
    public OptionATMGUI(RequestLogin login) throws IOException {
		
		//set up buttons and there settings
		withdrawal.setBounds(100,70,300,70);
		withdrawal.setBackground(new Color(0x4cbfff));
		withdrawal.setForeground(Color.WHITE);
		withdrawal.setFont(new Font("Futura", Font.PLAIN, 25));
		withdrawal.setFocusable(false);
		withdrawal.addActionListener(this);
		
		deposit.setBounds(100,210,300,70);
		deposit.setBackground(new Color(0x4cbfff));
		deposit.setForeground(Color.WHITE);
		deposit.setFont(new Font("Futura", Font.PLAIN, 25));
		deposit.setFocusable(false);
		deposit.addActionListener(this);
		
		transfer.setBounds(100,350,300,70);
		transfer.setBackground(new Color(0x4cbfff));
		transfer.setForeground(Color.WHITE);
		transfer.setFont(new Font("Futura", Font.PLAIN, 25));
		transfer.setFocusable(false);
		transfer.addActionListener(this);
		
		switchAcc.setBounds(100,70,300,70);
		switchAcc.setBackground(new Color(0x4cbfff));
		switchAcc.setForeground(Color.WHITE);
		switchAcc.setFont(new Font("Futura", Font.PLAIN, 25));
		switchAcc.setFocusable(false);
		switchAcc.addActionListener(this);
		
		cancel.setBounds(100,350,300,70);
		cancel.setBackground(Color.RED);
		cancel.setForeground(Color.WHITE);
		cancel.setFont(new Font("Futura", Font.PLAIN, 25));
		cancel.setFocusable(false);
		cancel.addActionListener(this);

		
		//set up labels, settings, and messages they hold
		label1.setText("Welcome " + login.getUser().getName());				//insert name here
		label1.setBounds(150,50,500,25);
		//label1.setForeground(new Color(0x00FF00))
		label1.setForeground(Color.white);
		label1.setFont(new Font("Futura", Font.BOLD, 40));
		
		label2.setText("Account #1");					//insert account 1 name and money amount here
		label2.setBounds(50,100,200,25);
		label2.setForeground(Color.white);
		label2.setFont(new Font("Futura", Font.BOLD, 15));
		
		label3.setText("Account #2");					//insert account 2 name and money amount here
		label3.setBounds(400,100,200,25);
		label3.setForeground(Color.white);
		label3.setFont(new Font("Futura", Font.BOLD, 15));
		
		//3rd account could go here 
		
		label4.setText("Current Account: ");
		label4.setBounds(50,50,200,25);
		label4.setForeground(Color.orange);
		label4.setFont(new Font("Futura ", Font.BOLD, 15));
		
		//frame
		frame.setSize(1000, 750); 					//sets frame size
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);  				//prevents frame from being resized 
		//frame.setUndecorated(true);   remove the title bar
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//exits program 
		//frame.getContentPane().setBackground(new Color(0x123456));
		
		//color panel for visual placement
		north.setBackground(new Color(0x123456));
		east.setBackground(new Color(0x006299));
		west.setBackground(new Color(0x006299));
		
		
		north.setPreferredSize(new Dimension(1000,250));
		east.setPreferredSize(new Dimension(500,625));
		west.setPreferredSize(new Dimension(500,625));
		
		north.setLayout(new BorderLayout());
		east.setLayout(null);
		west.setLayout(null);
		
		frame.add(north,BorderLayout.NORTH);
		frame.add(east,BorderLayout.EAST);
		frame.add(west,BorderLayout.WEST);
		
		
		
		//--------Top Panel--------------
											
		JPanel subn1 = new JPanel();							//create sub-panels
		//JPanel subn2 = new JPanel();							//create sub-panels
		//JPanel subn3 = new JPanel();							//create sub-panels
		
		subn1.setPreferredSize(new Dimension(1000,150));		//set dimensions of sub-panel
		//subn2.setPreferredSize(new Dimension(1000,50));
		//subn3.setPreferredSize(new Dimension(1000,50));
		
		subn1.setLayout(null);
		
		//color panel for visual placement
		//subn.setBackground(Color.pink);
		subn1.setBackground(new Color(0x123456));
		
		north.add(subn1,BorderLayout.SOUTH);		//add bottom sub-panel to top panel
		north.add(label1);			
		
		subn1.add(label2);
		subn1.add(label3);
		subn1.add(label4);
		
		//--------Left Panel--------------
		west.add(withdrawal);
		west.add(deposit);
		west.add(transfer);
		
		
		//--------Right Panel--------------
		
		east.add(switchAcc);
		east.add(cancel);

		
		frame.setVisible(true);	//makes frame visible
		
		
		//-----------------socket-----------------
		socket = new Socket("localhost", 7777);
		objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectInputStream = new ObjectInputStream(socket.getInputStream());	
		
		//save initial login data
		this.currentCustomer = (Customer)login.getUser();
		this.accounts = login.getAccounts();
	}
    
	public boolean updateAccount(Account account) {
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).getAccountID().equals(account.getAccountID())) {
				accounts.set(i, account);
				return true;
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == withdrawal) {
			
			RequestWithdraw request = new RequestWithdraw(amount, accountID, description);
			objectOutputStream.writeObject(request);
			RequestWithdraw response = (RequestWithdraw)objectInputStream.readObject();
			if (response.getStatus() == Status.SUCCESS) {
				updateAccount(response.getAccount());
			}
		}
		
		if(e.getSource() == deposit) {
			
			RequestDeposit request = new RequestDeposit(amount, accountID, description);
			objectOutputStream.writeObject(request);
			RequestDeposit response = (RequestDeposit)objectInputStream.readObject();
			if (response.getStatus() == Status.SUCCESS) {
				updateAccount(response.getAccountID());
			}
		}
	
		if(e.getSource() == transfer) {
			
			RequestTransfer request = new RequestTransfer(amount, account1, account2, description);
			objectOutputStream.writeObject(request);
			RequestTransfer response = (RequestTransfer)objectInputStream.readObject();
			if (response.getStatus() == Status.SUCCESS) {
				RequestGetCustomerAccounts request2 = new RequestCustomerAccounts(customerID);
			}
		}
	
		if(e.getSource() == switchAcc) {
		
			if (response.getStatus() == Status.SUCCESS) {
				updateAccount(response.getAccount());
			}
		}
		
		if(e.getSource() == cancel) {
			RequestLogout request = new RequestLogout();
			objectOutputStream.writeObject(request);
			socket.close();
			objectOutputStream.close();
			objectInputStream.close();
			System.exit(0);
		}
		
	}
}
