package ProjetoOld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class StorageNode extends Thread{

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	
	private String address;
	private int selfPort;
	private int dirPort;
	public CloudByte[] fileContents;
	private List<ByteBlockRequest> blockList = new ArrayList<ByteBlockRequest>();
	
	public StorageNode(String address, int dirPort,int selfPort, String fileName) throws IOException {
		this.address =  InetAddress.getLocalHost().getHostAddress();
		this.selfPort = selfPort;
		this.dirPort = dirPort;
		byte[] bytes = Files.readAllBytes(new File(fileName).toPath());
		
		fileContents = new CloudByte[bytes.length];
		for (int i = 0; i < bytes.length;i++) 
			fileContents[i] = new CloudByte(bytes[i]); 
		
		refactorBlockList();
	}
	
	public StorageNode(String address,int dirPort,int selfPort) throws IOException {
		this.address =  InetAddress.getLocalHost().getHostAddress();
		this.selfPort = selfPort;
		this.dirPort = dirPort;
		this.fileContents = null;
		
	}
	
	private boolean connectToServer() throws IOException {
		try {
		String localAd = InetAddress.getLocalHost().getHostAddress();
		//System.out.println(localAd);
		socket = new Socket(localAd,dirPort);
		System.out.println("Connection to "+localAd + " [port = "+selfPort+"]");
		
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
		return true;
		}
		catch(java.net.ConnectException e) {System.out.println("Servidor nao encontrado!"); return false; }
	}
	
	private synchronized ByteBlockRequest removeBlock() {
		//System.out.println(blockList.size());
		if(blockList.isEmpty()) return null;
		return blockList.remove(0);
	}
	
	private synchronized void addBlock(ByteBlockRequest block,CloudByte[] blockContent) {
		
		for(int i = 0; i< blockContent.length;i++)
			fileContents[i+block.startIndex] = blockContent[i]; 
	}
	
	private synchronized void refactorBlockList() {
		if(blockList.isEmpty())
		for(int i=0;i<fileContents.length/100;i+=100)
			blockList.add(new ByteBlockRequest(i, 100));
	}
	
	public synchronized CloudByte[] getFileContents(ByteBlockRequest req) {
		return Arrays.copyOfRange(fileContents,req.startIndex,req.startIndex+req.length);
	}
	
	public void CorrectByte(int i) throws IOException{

	CountDownLatch cdl = new CountDownLatch(2);
	
	Pair<List<String>,List<Integer>> pair = getNodesList();
	
	List<String> endList = pair.f1;
	List<Integer> portList = pair.f2;
	
	List<CorrectError> st = new ArrayList<CorrectError>();

	if(endList.size()<2) {
		System.out.println("Nao foi possivel corrigir o erro no byte "+i+" nodes insuficientes");
		return;
	}
	
	for(int ii=0; ii< endList.size();ii++) {
		CorrectError serve = new CorrectError(endList.get(ii),portList.get(ii),i,cdl);
		st.add(serve);
		serve.start();
	}
	 
	System.out.println("Aguardando Correcao do Erro ...");
	
	try {
		cdl.await();
	int count = 0;
	CloudByte b = null;
		for(CorrectError serve : st) {
			
			if(serve.result != null && serve.result[0].isParityOk()) {
				if(count == 0)
				{
					count++;
					b = serve.result[0];
				}
				else { 
				if(b.equals(serve.result[0])) {	
				addBlock(new ByteBlockRequest(i,1),serve.result);
				System.out.println("Erro no byte "+i+" corrigido com sucesso");
				return;	
				}
				}
			}
		}
				System.out.println("Nao foi possivel corrigir o erro no byte "+i+" respostas incoerêntes");
		
	} catch (InterruptedException e) {System.out.println("Servico interrompido!");
	
		}
	
}

	public void run () {
		try {
			if(connectToServer()) {
				
				if(fileContents == null) {
				fileContents = new CloudByte[1000000];
				refactorBlockList();
				getFileFromOthers();}
			
			
			out.println("INSC "+address+" "+selfPort);
			
			//conjunto de processos ligeiros que estão em permanência a percorrer os dados
			new ErrorCatcher().start();
			new ErrorCatcher().start();
			
			new DealWithInput().start();
			
			ServerSocket ss = new ServerSocket(selfPort);
			try {
				System.out.println("Aguardando ligações...");
				while(true) {
					Socket socket = ss.accept();
					new DealWithClient(socket).start();
				}
			} finally { ss.close();}
			}
		} catch ( IOException e) { e.printStackTrace(); }	
	}
	
	private Pair<List<String>,List<Integer>> getNodesList() throws IOException{
		
		out.println("nodes "+selfPort);
		List<String> endList = new ArrayList<>();
		List<Integer> portList = new ArrayList<>();
		
		while(true) {
			String s = in.readLine();
			if(s.equalsIgnoreCase("end")) break;

			String[] split = s.split(" ");
			String end = split[1];
			int port = Integer.parseInt(split[2]);
			if(port !=selfPort) {
				endList.add(end);
				portList.add(port);
			}			
				
		}
	
	return new Pair<List<String>, List<Integer>>(endList,portList);
	}
	
	private void getFileFromOthers() throws IOException {
		
		Pair<List<String>,List<Integer>> pair = getNodesList();
		
		List<String> endList = pair.f1;
		List<Integer> portList = pair.f2;
		
		List<GetData> st = new ArrayList<GetData>();
	
		for(int i=0; i< endList.size();i++) {
			GetData serve = new GetData(endList.get(i),portList.get(i));
			st.add(serve);
			serve.start();
		}
		 
		System.out.println("Aguardando download data ...");
		try {
		for(GetData serve : st)
				serve.join();
		} catch (InterruptedException e) {System.out.println("Servico interrompido!");
			//	e.printStackTrace();
			}
		System.out.println("Download data completo!");
		refactorBlockList();

		
	}
		
	private static CloudByte[] getCloudByte(ByteBlockRequest block, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.writeObject((Object)block);
		return (CloudByte[]) in.readObject();
	}
	
	public static void main(String[] args) throws IOException {
		//new StorageNode("127.0.0.1",8080,8081,"data.bin").run();
		try {
		new StorageNode(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),args[3]).run();}
		catch( java.lang.ArrayIndexOutOfBoundsException e) {
			new StorageNode(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2])).run();
		}
	}
	
	
private class GetData extends Thread{

		private ObjectInputStream in;
		private ObjectOutputStream out;
		private String end;
		private int port;
		private Socket socket;
		
		public GetData(String end,int port) {
			this.port = port;
			this.end = end;
		}
		
		private void serve() throws IOException {
			int numBlocks = 0;
			ByteBlockRequest block = removeBlock();
			
			try {
			while(block != null) {	
			CloudByte[] fileContents = getCloudByte(block,in, out);
			
			addBlock(block,fileContents);
			
			numBlocks++;
			block = removeBlock();
			}
			} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
			socket.close();
			System.out.println("O Node "+port+" enviou "+numBlocks+" blocos de dados!");
		}	
		
		void doConnections() throws IOException {
			try {
				socket = new Socket(InetAddress.getByName(end),port);
				out = new  ObjectOutputStream(socket.getOutputStream());
				in = new  ObjectInputStream(socket.getInputStream());
				} catch (java.net.ConnectException e) {System.out.println("Node nao encontrado!");}
		}
		
		
		public void run() {
			try {
				doConnections();
				System.out.println("Ligacao estabelecida com o node " + port);
				serve();
			} catch (IOException e) {e.printStackTrace();
			}finally {
				try { socket.close(); System.out.println("Ligacao terminada com o node " + port);} 
				catch (IOException e) {e.printStackTrace();} 
			}
		}
		
	}
		
private class DealWithClient extends Thread {
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private Socket socket;
		
		public DealWithClient(Socket socket) {
			this.socket = socket;
		}
		
		private void serve() throws IOException {
			try {
				//int debug  = 0;
			while(true) {
					try {
				ByteBlockRequest req = (ByteBlockRequest)(in.readObject());
				for(int i= req.startIndex; i!= req.startIndex + req.length; i++) 
					if(!fileContents[i].isParityOk()) {
						System.out.println("Erro detetado no byte "+i+":" + fileContents[i]);
						CorrectByte(i);
						}
					//System.out.println(++debug);
				out.writeObject(Arrays.copyOfRange(fileContents,req.startIndex,req.startIndex + req.length));	
				out.reset();
					} catch (ClassNotFoundException | java.io.EOFException e) { socket.close(); 
					//e.printStackTrace();
					}
				}
			} catch (java.net.SocketException e) {System.out.println("O cliente desconectou-se do node"); socket.close();}
		}
			
			void doConnections(Socket socket) throws IOException {
				in = new  ObjectInputStream(socket.getInputStream());
				out = new  ObjectOutputStream(socket.getOutputStream());
				//System.out.println("Nova Coneccao ao node");
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
	
private class DealWithInput extends Thread{
		
	
	public void run() {
		try {
				serve();
		} catch (IOException e) {e.printStackTrace();};
	} 
		
		
		
	public void	serve() throws IOException {
			
		BufferedReader reader = new BufferedReader(
		new InputStreamReader(System.in));
				
		System.out.println("Aguardando comandos...");
				
		while(true) {
			String input = reader.readLine();
			int i = input.indexOf(' ');
			String word = (i == -1) ? "null" : input.substring(0, i);
					
			switch (word.toUpperCase()) {

			case "ERROR" : {
				try {
				int errorByte = Integer.parseInt(input.substring(i+1).replaceAll("\\s", ""));
				if(fileContents[errorByte].isParityOk()) {
				fileContents[errorByte].makeByteCorrupt();
				System.out.println("O byte "+ errorByte +" tornou-se corrupto, "+fileContents[errorByte].toString()); }
				else System.out.println("O byte "+ errorByte +" já é corrupto, "+fileContents[errorByte].toString());
				}
				catch(NumberFormatException e) {System.out.println("Argumentos invalidos no comando error");}		
			}
				break;
						
				default: System.out.println("Comando desconhecido");	
			}
		}
	}
	}

private class CorrectError extends Thread{
		
		private int i;
		private CountDownLatch cdl;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private String end;
		private int port;
		private Socket socket;
		public CloudByte[] result;
		
		public CorrectError(String end, int port, int i, CountDownLatch cdl) {

			this.i = i;
			this.cdl = cdl;
			this.end = end;
			this.port = port;
		}
	
		private void serve() throws IOException {
			
			try {
			ByteBlockRequest req = new ByteBlockRequest(i,1);	
			
			if(req != null) {	
			result = getCloudByte(req, in, out);
			cdl.countDown();
			}
			} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
			socket.close();
		}	
		
		
		void doConnections() throws IOException {
			try {
				socket = new Socket(InetAddress.getByName(end),port);
				out = new  ObjectOutputStream(socket.getOutputStream());
				in = new  ObjectInputStream(socket.getInputStream());
				} catch (java.net.ConnectException e) {System.out.println("Node nao encontrado!");}
		}
		
		
		public void run() {
			try {
				doConnections();
				System.out.println("Ligacao estabelecida com o node " + port);
				serve();
			} catch (IOException e) {e.printStackTrace();
			}finally {
				try { socket.close(); System.out.println("Ligacao terminada com o node " + port);}
				catch (IOException e) {e.printStackTrace();} 
			}
		}	
		
	}	 	

private class ErrorCatcher extends Thread{
	
	public void run() {
		System.out.println("Error Catcher inicializado");
	while(true) {	
		ByteBlockRequest block = removeBlock();
		if(block == null) {refactorBlockList();}
		else
		try {
		for(int i= block.startIndex; i!= block.startIndex + block.length; i++) 
			if(!fileContents[i].isParityOk()) {
				System.out.println("Erro detetado no byte "+i+":" + fileContents[i]);
					CorrectByte(i);
				}
		
		sleep(1000);
		} catch (IOException | InterruptedException |  java.lang.NullPointerException e) {System.out.println("Error Catcher parou devido a um erro");return;}
	}
	}
	
	
}
}
