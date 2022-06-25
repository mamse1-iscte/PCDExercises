package ProjetoOld;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class DataClient {

		private JFrame frame;
		private InetAddress address;
		private int port;
		
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private Socket socket;
		
		
		private static final int ERROR_PARSE_INT = -777;

		public DataClient(String address,int port) throws IOException {
			this.address =  InetAddress.getByName(address);
			this.port = port;
			
			if(connectToNode()) { 
			
			frame = new JFrame("Client");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			addFrameContent();
			frame.pack();
			frame.setSize(600,150);
			
			open();
			}
	}
		private boolean connectToNode() throws IOException {
			
			try {
			socket = new Socket(address,port);
			System.out.println("Connection to "+ address + " [port = "+port+"]");
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			return true;
			} catch (java.net.ConnectException e) {System.out.println("Cliente nao encontrado!"); return false; }
		}
		

		public void open() {
			frame.setVisible(true);
		}

		private void addFrameContent() {

			JPanel UpPanel = new JPanel();
			
			JPanel IndicePanel= new JPanel();
			IndicePanel.setLayout(new GridLayout(1,2));
			
			JLabel labelIndice = new JLabel("Posição a consultar: ");
			IndicePanel.add(labelIndice);

			JTextField Indice = new JTextField();
			IndicePanel.add(Indice);
			
			UpPanel.add(IndicePanel);
			
			JPanel LengthPanel= new JPanel();
			LengthPanel.setLayout(new GridLayout(1,2));
			
			JLabel labelLength= new JLabel("Comprimento: ");
			LengthPanel.add(labelLength);
			
			JTextField Length = new JTextField();
			LengthPanel.add(Length);
			
			UpPanel.add(LengthPanel);
			
			JButton CheckButton = new JButton("Consultar");
			
			JTextArea Answer = new JTextArea("Respostas aparecerão aqui...");
			
			frame.add(Answer);
		
			CheckButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					int i = tryParse(Indice.getText());
					int l = tryParse(Length.getText());
					
					String st = (i == ERROR_PARSE_INT || l == ERROR_PARSE_INT) ? "Argumentos invalidos":getValuesFrom(i,l);
					Answer.setText(st);
				}	
				}
			);
			
			UpPanel.add(CheckButton);	
			
			frame.add(UpPanel,BorderLayout.NORTH);
			
		}

		private static int tryParse(String i) {
			try {
				 return Integer.parseInt(i);
				} catch (NumberFormatException e) { return ERROR_PARSE_INT; }
		}
		
		private String getValuesFrom(int i,int l) {
			String returnString = "";
			
			try {
			ByteBlockRequest req = new ByteBlockRequest(i,l);
			out.writeObject((Object)req);
			CloudByte[] fileContents = (CloudByte[]) in.readObject();
			
			for(CloudByte b: fileContents)
				returnString += b.toString()+" ";
		
			} catch (IOException | ClassNotFoundException e) {
				//e.printStackTrace(); 
			}
			
			return returnString;
		}
		
		
		public static void main(String[] args) throws IOException {
			@SuppressWarnings("unused")
			//DataClient cliente = new DataClient("127.0.0.1",8081);
			
			DataClient cliente = new DataClient(args[0],Integer.parseInt(args[1]));
		}
		
	}

