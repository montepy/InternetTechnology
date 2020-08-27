package classFiles;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class KnockKnock extends JFrame {

	//make all class objects
	static private PrintWriter pw = null;
	static private BufferedReader br = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static private Socket socket = null;
	
	public static void main(String args[]) {
	   //instantiate and set settings for all elements of Jframe
	   JFrame gui = new JFrame();
       gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       gui.setSize(300,600);
       
       JLabel addr = new JLabel();
       addr.setText("IP Address");
       
       JTextField addrField = new JTextField(20);
       addrField.setText("constance.cs.rutgers.edu");
       addrField.setEditable(true);
       
       JLabel port = new JLabel();
       port.setText("Port Number");
       
       JTextField portField = new JTextField(10);
       portField.setText("5520");
       portField.setEditable(true);
       
       JButton connection = new JButton();
       connection.setText("Connect");
       
       JLabel message = new JLabel("Message to Server");
       
       JTextField messageField = new JTextField(25);
       messageField.setEditable(true);
       
       JButton send = new JButton();
       send.setText("Send");
       
       JLabel comms = new JLabel("Client/Server Communication");
       
       JTextArea commsField = new JTextArea(25,20);
       
       JScrollPane scrollPane = new JScrollPane(commsField);
       commsField.setEditable(false);
       //add actionlistener to button so code can result in action
       send.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent arg0) {//This is what the send button does. 
    		   try {
    			   String out = messageField.getText();
    			   messageField.setText("");
    			   pw.println(out);
    			   commsField.append("Client:" +out+"\n");
    			   String servout = br.readLine();
    			   commsField.append("Server:" + servout+"\n");//read and append client and server output
					//System.out.println("What");
    			   if (servout.equals("Good Bye!")) {
    				   //test for if server sends goodbye, and disconnects if so
            		   connection.setText("Connect");
    			       commsField.append("Disconnected!\n");
        			   socket.close();
        			   pw.close();
        			   br.close();
    			   }
    			   
    		   } catch (IOException e) {
    			   e.printStackTrace();
    		   }
    	   }
       });
       //add action listener to connection button
       connection.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent arg0) {
    		   if(socket == null || socket.isClosed()) {//try instantiating if button clicked
		    		   try {
		        		   socket = new Socket(addrField.getText(),Integer.parseInt(portField.getText()));
		        		   pw = new PrintWriter(socket.getOutputStream(),true);
			    		   br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;//if failed, return before text changes
					}
			    	   connection.setText("Disconnect");
	        		   commsField.append("Connected to Server\n");
    		   } else {
    			   try {//try to close all class objects
    			   socket.close();
    			   pw.close();
    			   br.close();
    			   } catch(IOException e) {
    				   e.printStackTrace();
    				   return;//return before text change
    			   }
        		   connection.setText("Connect");
			       commsField.append("Disconnected!\n");
    		   }
    	   }
       });
       JPanel panel = new JPanel();//add all declared Jframe objects to the gui
       panel.add(addr);
       panel.add(addrField);
       panel.add(port);
       panel.add(portField);
       panel.add(connection);
       panel.add(message);
       panel.add(messageField);
       panel.add(send);
       panel.add(comms);
       panel.add(scrollPane);
       
       gui.setContentPane(panel);
       gui.setVisible(true);//make gui usable
       
       
	}
	

}
