package ProjetoOld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Directory {

	//private List<StorageNode> nodeList;
	private List<String> nodeList;
	private int port;
	
	
	public Directory(int port) {
		this.nodeList = new ArrayList<String>();
		this.port = port;
		
		try {
			Start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void Start() throws IOException {
	ServerSocket ss = new ServerSocket(port);
	try {
		System.out.println("Servidor Iniciado...");
		while(true) {
			Socket socket = ss.accept();
			new DealWithNode(socket).start();
		}
	}finally { ss.close();}
	
} 
	
	public synchronized void AddNode(String node) {
	
	nodeList.add(node);	
	}
	
	public synchronized boolean RemoveNode(String node) {
		
	return nodeList.remove(node);	
	}
	
	public synchronized String GetNodes() {
		
		String toReturn = "";
		for(String st : nodeList)
		 toReturn += st+"\n";

		return toReturn+"end";
	}
	
	

private class DealWithNode extends Thread {

	private Socket socket;
	private String node;
	private BufferedReader in;
	private PrintWriter out;
	
	public DealWithNode(Socket socket) {
		this.socket = socket;
	}
	
	private void serve() throws IOException {
		try {
			while(true) {
				String input = in.readLine();			
				String[] split = input.split(" ");			
				
				switch (split[0].toLowerCase()) {
						
				case "nodes" : {
					try {
						System.out.println("Pedido nodes recebido");
					
						String nodes = GetNodes();
						/*
						System.out.println("-=======");
						System.out.println(nodes);
						System.out.println("sent to "+split[1]);
						System.out.println("=======-");
						*/
						out.println(nodes);
					}
					catch(NumberFormatException e) {System.out.println("Argumentos invalidos no comando nodes");}		
				}
					break;
					
				case "insc" : {
					try {
						InetAddress.getByName(split[1]);
						Integer.parseInt(split[2]);
						System.out.println("Nova Inscricao em "+split[2]);
						node = "node "+split[1]+" "+split[2];
						AddNode(node);
					}
					catch(NumberFormatException e) {System.out.println("Argumentos invalidos no comando insc");}		
				}
					break;
							
					default: System.out.println("Comando desconhecido");	
				}
			}
		} catch (java.net.SocketException e) {
			if(RemoveNode(node))
			System.out.println("O Node desconectou-se"); socket.close();}
	}
		
		void doConnections(Socket socket) throws IOException {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}



		public void run() {
			try {
				doConnections(socket);
				serve();
			} catch (IOException e) {e.printStackTrace();
			}finally {
				try { socket.close();} catch (IOException e) {e.printStackTrace();} 
			}
		}
	} 


public static void main(String[] args) throws IOException {
	new Directory(Integer.parseInt(args[0]));
}

}
	
	
