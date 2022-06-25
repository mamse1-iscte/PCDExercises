package Week8.Exercise1.Part1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


//esta classe funciona mas tem limitações se quiser ligar mais do que um cliente espera que o cliente 1 acabe para começar o cliente2
public class SimpleClient {
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	public static void main(String[] args) {
		new SimpleClient().runClient();
	}

	public void runClient() {
		try {
			connectToServer();
			sendMessages();
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//... 
			}
		}
	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, SimpleServer.PORTO);
		System.out.println("Socket:" + socket);
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}

	void sendMessages() throws IOException {
		for (int i = 0; i < 10; i++) {
			out.println("Ola " + i);
			String str = in.readLine();
			System.out.println(str);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {//...  
			}
		}
		out.println("FIM");
	}

}
