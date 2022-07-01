package ProjetoBicho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DirServer extends Thread{
	
	ServerSocket ss;
	public static final int porto = 8080;
	List<String> nodesInscritos = new ArrayList<>();
	
	public DirServer() throws IOException{
		 ss = new ServerSocket(porto);
	}

		@Override
		public void run() {
			try { 
				System.out.println("Serviço a iniciar...");
				while(true) {
					Socket socket = ss.accept(); 
					new DealWithClient(socket).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					ss.close(); 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public class DealWithClient extends Thread{

			private BufferedReader in;
			private PrintWriter out;
			public Socket socket;
			int portoSocket;

			public DealWithClient(Socket socket) throws IOException {
				this.socket = socket;
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			}

			@Override   
			public void run() {
				while(true) {
					try {	
						String str = in.readLine();
						String[] splited = str.split(" ");
						if(splited[0].equals("INSC")) {
							if(noInscrito(splited[1], splited[2])) {
								nodesInscritos.add("node " + splited[1] + " " + splited[2]);
								portoSocket = Integer.valueOf(splited[2]);
								System.out.println("Cliente inscrito:" + socket.getInetAddress() + " " + portoSocket);
							}
							else {
								System.err.println("O cliente já está inscrito");
							}
						}	
						else if(splited[0].equals("nodes")) {
							System.out.println("Mensagem recebida: nodes");
							for(int i = 0 ; i < nodesInscritos.size(); i++) {
								out.println(nodesInscritos.get(i));
							}
							out.println("end");
						}
						else{
							System.err.println("Erro na mensagem recebida");
						}
					} catch (IOException e) {
						e.printStackTrace();
						try {
							socket.close();
							retirarlista(socket);
							break;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} 
					finally {
						
					}
				}
			}

			private void retirarlista(Socket socket) {
				String host = socket.getInetAddress().toString();
				for(int i= 0; i < nodesInscritos.size(); i++) {
					String[] splited = nodesInscritos.get(i).split(" ");
					if(splited[1].equals(host) && (Integer.valueOf(splited[2]) == portoSocket)) {
						nodesInscritos.remove(i);
					}
				}
			}

			private boolean noInscrito(String endereco, String porto) {
				for(int i= 0; i < nodesInscritos.size(); i++) {
					String[] splited = nodesInscritos.get(i).split(" ");
					if(splited[1].equals(endereco) && splited[2].equals(porto)) {
						return false;
					}
				}
				return true;
			}
		}
		
		public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
			new DirServer().run();	
		}
}

