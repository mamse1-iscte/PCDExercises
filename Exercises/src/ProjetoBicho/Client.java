package ProjetoBicho;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import ProjetoBicho.ByteBlockRequest.ByteBlockRequest;
import ProjetoBicho.CloudByte.CloudByte;

public class Client {
	
	private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
 
    public Client(String adressText, int port) throws IOException { 
    	System.err.println ("Connecting to:"+adressText+":"+port);
    	try {
    		InetAddress address= InetAddress.getByName(adressText);
    		socket = new Socket(address,port);
    		System.out.println(socket);
    		in= new ObjectInputStream(socket.getInputStream());
    		out= new ObjectOutputStream(socket.getOutputStream());
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    		socket.close();
    	}
    }   
    
        private void openWindow() { 
        	
        	final JFrame frame = new JFrame("Client");
		    frame.setSize(1000,120);
		    
		    frame.setLayout(new GridBagLayout());
		    GridBagConstraints c = new GridBagConstraints();

		    JLabel labelIndex = new JLabel("Posição a consultar: "); 
		    c.fill = GridBagConstraints.HORIZONTAL; 
		    c.gridx = 0;
		    c.gridy = 0;
		    frame.add(labelIndex, c);

		    JTextField textIndex = new JTextField("");
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 0.2;
		    c.gridx = 2;
		    c.gridy = 0;
		    frame.add(textIndex, c);
		
		    JLabel labelSize = new JLabel("Comprimento: ");
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.gridx = 3;
		    c.gridy = 0;
		    frame.add(labelSize, c);
		    
		    JTextField textSize = new JTextField("");
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 0.2;
		    c.gridx = 4;
		    c.gridy = 0;
		    frame.add(textSize, c);
		   
		    JButton button = new JButton("Consultar");
		    
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 0.1;
		    c.gridx = 5;
		    c.gridy = 0;
		    frame.add(button, c);
		    
		
		    JTextArea areaText = new JTextArea("");
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.ipady = 40;      //make this component tall
		    c.weightx = 0.0;
		    c.gridwidth = 6;
		    c.gridx = 0;
		    c.gridy = 1;
		    frame.add(areaText, c);
		    
		    button.addActionListener(new ActionListener() {
	        
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	String mensagem = null;
		        	try {
		        		ByteBlockRequest bbr = new ByteBlockRequest(Integer.valueOf(textIndex.getText()), Integer.valueOf(textSize.getText())); //int StartIndex=Integer.valueOf(textIndex.getText());int length=Integer.valueOf(textSize.getText());
		        		out.reset();
		        		out.writeObject(bbr);	
		        		CloudByte[] storedData = (CloudByte[]) in.readObject();		
						if(bbr.length == 1) {
							mensagem = storedData[0] + " " + storedData[0].isParityOk() + ", ";
						}
						else {
							mensagem = storedData[0] + " " + storedData[0].isParityOk() + ", ";
							for(int i = 1 ; i < bbr.length; i++) {
								mensagem = mensagem.concat(storedData[i] + " " + storedData[i].isParityOk() + ", ");
							}
							System.out.println("A mensagem recebida é : " + mensagem);
						}
						areaText.setText(mensagem);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}	            
		        }
		    }); 
		    frame.setVisible(true);
	    }

	public static void main(String[] args) throws IOException { // ligam-se apenas a um nó
		if(args.length != 2) {
			throw new IllegalArgumentException();
		}
		String host = args[0]; 
		int NodePort = Integer.valueOf(args[1]);
		Client window = new Client(host, NodePort);
		window.openWindow();
	}
}