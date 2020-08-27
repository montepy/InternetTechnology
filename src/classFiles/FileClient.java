package classFiles;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileClient extends JFrame {

	   static private Socket socket;
	   static private OutputStream op = null; 
	   static private BufferedReader reader = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	   JFrame gui = new JFrame();
	   gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   gui.setSize(600,600);
	   
	   JLabel look = new JLabel();
	   look.setText("Choose File:");
	   
	   JFileChooser choice = new JFileChooser();
	   FileNameExtensionFilter filt = new FileNameExtensionFilter("MSWord","doc","docx");
	   choice.setFileFilter(filt);
	   
	   JTextField addrField= new JTextField(20);
	   JButton upload = new JButton();
	   upload.setText("Connect and Upload");
	   
	   JLabel alabel = new JLabel();
	   alabel.setText("Server Log");
	   
	   JTextArea area = new JTextArea(10,20);
	   JScrollPane scroll = new JScrollPane(area);
	   area.setEditable(false);
	   upload.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent arg0) {
			   File up = choice.getSelectedFile();
			try {
				socket = new Socket(addrField.getText(),5520);
				op = socket.getOutputStream();
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (UnknownHostException e) {
				area.append("Host not recognized.\n");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				area.append("Issue initializing socket.\n");
				return;
			}
			area.append("Connected\n");
			String toServ = up.getName()+'\0'+up.length()+'\0';
			try {
				op.write(toServ.getBytes(StandardCharsets.US_ASCII));
				area.append("Sent file name: "+ up.getName()+'\n');
				area.append("Sent file length: " + up.length()+'\n');
				byte[] fileContent = Files.readAllBytes(up.toPath());
				area.append("Sending file...\n");
				op.write(fileContent);
				area.append("File sent. Waiting for the Server...\n");
			} catch (IOException e) {
				area.append("file failed upload\n");
				return;
			}
			try {
				if(reader.readLine().equals("@")) {
					area.append("Disconnected");
					socket.close();
					op.close();
					reader.close();
				}
				
			} catch (IOException e) {
				area.append("Upload failed\n");
				return;
			}
			
			   
		   }
	   });
	   JPanel panel = new JPanel();
	   panel.add(look);
	   panel.add(choice);
	   panel.add(addrField);
	   panel.add(alabel);
	   panel.add(upload);
	   panel.add(scroll);
	   
	   gui.setContentPane(panel);
	   gui.setVisible(true);
	   
	   
	   
	}

	private static void sendNullTerminatedString(String s,PrintWriter pw) {
		pw.write(s);
	}
	
	private static void sendFile(String fullPathFileName,PrintWriter pw){
		File file = new File(fullPathFileName);
	}
}
