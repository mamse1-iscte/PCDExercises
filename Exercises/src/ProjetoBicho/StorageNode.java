package ProjetoBicho;

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
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import ProjetoBicho.ByteBlockRequest.ByteBlockRequest;
import ProjetoBicho.CloudByte.CloudByte;

public class StorageNode extends Thread{

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket; 

	private InetAddress endereco; 
	private int portoDir;
	private int portoNode; 

	private int nodesnumber;
	private Lock lock = new ReentrantLock();

	CountDown countdown = new CountDown(MIN_NUMBER_ANSWER_TO_CORRECT);
	CloudByte[] storedData = new CloudByte[STORED_DATA_LENGTH];

	List<String> nodesDisponiveis = new ArrayList<>();
	List<ByteBlockRequest> lista = new ArrayList<>();
	List<Etiqueta> ordenarData = new ArrayList<Etiqueta>();
	List<CloudByte> erroData = new ArrayList<CloudByte>();

	public static final int MIN_NUMBER_ANSWER_TO_CORRECT = 2;
	public static final int STORED_DATA_LENGTH= 1000000;

	public StorageNode(String enderecoDir, int portoDir, int portoNode, String fileName) throws IOException { 
		this.endereco = InetAddress.getByName(enderecoDir);
		this.portoDir = portoDir;
		this.portoNode= portoNode;
		byteToCloudByte(fileName);
	}

	public StorageNode(String enderecoDir, int portoDir, int portoNode) throws UnknownHostException {
		this.endereco = InetAddress.getByName(enderecoDir);
		this.portoDir = portoDir;
		this.portoNode= portoNode;
	}

	public void byteToCloudByte (String fileName) throws IOException {
		byte[] byteInfo = Files.readAllBytes(new File(fileName).toPath());
		for(int i = 0; i < STORED_DATA_LENGTH; i++) {
			storedData[i] = new CloudByte(byteInfo[i]);
		}
	}

	public void runNodeWithFile() throws ClassNotFoundException, InterruptedException {
		try {
			connectToServer();
			inscrever();
			Thread t1 = new ManualError(); 
			Thread t2 = new startServing();
			Thread t3 = new CorrectError();
			t1.start();
			t2.start();
			t3.start();
		} 
		catch (IOException e) { 
			e.printStackTrace(); // ERRO...
		} 
		finally {
			//a fechar...
		}
	}

	void connectToServer() throws IOException {    // connect to dir	
		System.out.println("endereco: " + endereco);
		socket = new Socket(endereco, portoDir);
		System.out.println("Socket: " + socket);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	}


	void inscrever() throws IOException {         // INSC to dir
		out.println("INSC " + endereco +" "+ portoNode );
		System.out.println("O Nó com o Endereço: " + endereco + " e Porto :" + portoNode + " foi Inscrito no Diretorio \n");
	}


	public class startServing extends Thread {
		ServerSocket ss;
		@Override
		public void run() {
			try { 
				ss = new ServerSocket(portoNode);
				System.out.println("Aguardando ligações ... ");
				while(true) {
					Socket socketC = ss.accept(); 
					System.out.println("Foi aceite a ligação com socket: " + socketC);
					new DealWithClient(socketC).start();
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
	}

	public class DealWithClient extends Thread{

		private ObjectInputStream in;
		private ObjectOutputStream out;
		public Socket socketC;

		public DealWithClient(Socket socketC) throws IOException {
			this.socketC = socketC;
			out= new ObjectOutputStream (socketC.getOutputStream());
			in= new ObjectInputStream(socketC.getInputStream());
		}

		@Override   
		public void run() {
			while(true) {
				try {		
					ByteBlockRequest req = (ByteBlockRequest) in.readObject();
					System.out.println("transferência: " + req); //
					for(int i= req.StartIndex; i!=req.StartIndex + req.length; i++) {
							if(! storedData[i].isParityOk()) {
									System.err.println("Data query: Error was detected at "+ i +":"+ storedData[i]);
									getCorrectedByteFromOtherNodes(i);
							}
					}	
					out.reset();	
					out.writeObject(Arrays.copyOfRange(storedData, req.StartIndex, req.StartIndex + req.length));
					
				}catch (ClassNotFoundException | IOException | InterruptedException e) {
					e.printStackTrace();
					try {
						System.out.println("A socket seguinte foi fechada : " + socketC);
						socketC.close();
						break;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				finally {	
				}
			}
		}
	}
	
		public class CorrectError extends Thread{ // está sempre a corrigir erros de outros nós
			@Override
			public void run() {
				while(true) {
					try {
						for(int i= 0; i < STORED_DATA_LENGTH; i++) {
							if(!storedData[i].isParityOk()) {
								System.err.println("Data query: Error was detected at "+ i +":"+ storedData[i]);
								getCorrectedByteFromOtherNodes(i);
							}
						}	
					}catch (ClassNotFoundException | IOException | InterruptedException e) {
						e.printStackTrace();
					}	
				}
			}
		}
		
		
	    public void getCorrectedByteFromOtherNodes(int error) throws IOException, ClassNotFoundException, InterruptedException {
	    	consultar();
	    	if (nodesnumber < 2) {
				System.err.println("Não existem nodes suficientes na cloud para correção dos erros nos dados");
				return;
			}
			Thread[] threads = new Thread[nodesnumber];
			sleep(100);
			System.err.println("\nA iniciar a correção do erro, dos seguintes nodes: ");
			for(int i = 0; i < threads.length; i++) {
				System.out.println(nodesDisponiveis.get(i));
				String[] splited = nodesDisponiveis.get(i).split(" ");
				//threads[i] = new DealWithError("localhost", Integer.valueOf(splited[2]), error); // em vez de local host é para meter splited[1]
				threads[i] = new DealWithError(splited[1].substring(1), Integer.valueOf(splited[2]), error);
			}
			for(int i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			countdown.await();
			
			if(erroData.get(0).equals(erroData.get(1)))
			{
				storedData[error] = erroData.get(0);
				System.err.println("\nO erro foi corrigido, com o novo valor de: " + storedData[error]);
				System.out.println("Aguardar ligações... ");
			}
			else {
				 throw new IOException();
			}
	    }
	    
	    public class DealWithError extends Thread{

			private ObjectInputStream in;
			private ObjectOutputStream out;
			private int error;
			public Socket socketN;

			public DealWithError(String nodeAddress, int nodePort, int error) throws IOException {
				this.error = error;
				socketN = new Socket(InetAddress.getByName(nodeAddress), nodePort);
				out= new ObjectOutputStream (socketN.getOutputStream());
				in= new ObjectInputStream(socketN.getInputStream());
			}

			@Override   
			public void run() {
				try {	
						ByteBlockRequest req = new ByteBlockRequest(error, 1);
						out.writeObject(req);
						CloudByte[] data = (CloudByte[]) in.readObject();
						coloca(data[0]);
						countdown.countDown();
				}catch (ClassNotFoundException | IOException | InterruptedException e) {
					e.printStackTrace();
					try {
						socketN.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
	    }

	public void runNodeWithOutFile() throws ClassNotFoundException, InterruptedException {
		try {
			connectToServer();
			inscrever();
			consultar();
			connectToNode();
			ordenarCloud();
			Thread t1 = new ManualError(); 
			Thread t2 = new startServing();
			Thread t3 = new CorrectError();
			t1.start();
			t2.start();
			t3.start();
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
		}
	}

	public class DealWithNode extends Thread{

		private ObjectInputStream in;
		private ObjectOutputStream out;
		public Socket socketN;

		public DealWithNode(String nodeAddress, int nodePort) throws IOException {
			socketN = new Socket(InetAddress.getByName(nodeAddress), nodePort);
			out= new ObjectOutputStream (socketN.getOutputStream());
			in= new ObjectInputStream(socketN.getInputStream());
		}

		@Override   
		public void run() {
			int i = 0;
			ByteBlockRequest req;
			try {	
				while((req = retirar())!=null) {
					out.writeObject(req);
					CloudByte[] data = (CloudByte[]) in.readObject();
					Etiqueta etiqueta = new Etiqueta(data, req.StartIndex);
					coloca(etiqueta);
					i++;
				}
			}catch (ClassNotFoundException | IOException | InterruptedException e) {
				e.printStackTrace();
				try {
					socketN.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println(socketN + " Retirei: "+i);
		}
	}    

	void connectToNode() throws IOException, ClassNotFoundException, InterruptedException {
		if (nodesnumber == 0) {
			System.err.println("Não existem nodes na cloud para tranferencia de dados");
			return;
		}
		iniciarListaRequest();
		Thread[] threads = new Thread[nodesnumber];
		
		System.err.println("\nA iniciar o Download dos dados, dos seguintes nodes: ");
		for(int i = 0; i < threads.length; i++) {
			System.out.println(nodesDisponiveis.get(i));
			String[] splited = nodesDisponiveis.get(i).split(" ");
		//	threads[i] = new DealWithNode("localhost", Integer.valueOf(splited[2])); // em vez de local host é para meter splited[1]
			threads[i] = new DealWithNode(splited[1].substring(1), Integer.valueOf(splited[2])); 
		}
		System.err.println("A realizar o download, por favor aguarde ... \n");
		for(int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for(int i = 0; i < threads.length; i++) {
			threads[i].join();	
		}
	}


	void iniciarListaRequest() {
		for(int i = 0; i < STORED_DATA_LENGTH; i = i + 100) {   
			lista.add(new ByteBlockRequest(i, 100));
		}
	}	
	
	void ordenarCloud() {
			Collections.sort(ordenarData, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					Etiqueta p1 = (Etiqueta) o1;
					Etiqueta p2 = (Etiqueta) o2;
					return p1.getEtiqueta() < p2.getEtiqueta() ? -1 : (p1.getEtiqueta() > p2.getEtiqueta() ? +1 : 0);
				}
			});
		int t = 0;
		for(int i = 0; i < STORED_DATA_LENGTH - 1; i = i + 100){
			for(int j = 0; j < 100; j++) {
				storedData[j+i] = ordenarData.get(t).getData()[j];
			}
			t++;
		}
		System.out.println("Acabou o Download dos dados. ");
	}

	public ByteBlockRequest retirar() throws InterruptedException {
		lock.lock();
		try{
			if(lista.isEmpty()) {
				return null;
			}
			return lista.remove(0);
		} finally {
			lock.unlock();
		}
	}
	
	public void coloca(Etiqueta etiqueta) throws InterruptedException {
		lock.lock();
		try{
			ordenarData.add(etiqueta);
		} finally {
			lock.unlock();
		}
	}
	
	public void coloca(CloudByte a) throws InterruptedException {
		lock.lock();
		try{
			erroData.add(a);
		} finally {
			lock.unlock();
		}
	}

	public class ManualError extends Thread{
		@Override
		public void run() {
			while(true) {
				Scanner s = new Scanner(System.in);
				String error = s.next();
				int a = s.nextInt();
				if(error.equals("ERROR")) {
					storedData[a].makeByteCorrupt();
					System.err.println ("Erro injetado no byte: " + a);
				}
				else {
					System.err.println ("Para injetar um erro no dados, escreva: ERROR e em seguida o numero do byte desejado" );
				}
			}
		}
	}

	void consultar() throws IOException { 
		nodesnumber = 0; 
		out.println("nodes");
		String str;
		System.out.println("Nodes Inscritos no Diretório: ");
		while((str = in.readLine()) != null) {
			if(str.equals("end")) {
				System.out.println(str);
				break;
			}
			String[] splited = str.split(" ");
			if( ! (Integer.valueOf(splited[2]) == portoNode) ) {
				nodesnumber++;
				nodesDisponiveis.add(str);
			}
			System.out.println(str);
		}
	}


	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		if(!(args.length == 3 || args.length == 4)) {
			throw new IllegalArgumentException();
		}
		String host = args[0]; 
		int dirPort = Integer.parseInt(args[1]);
		int nodePort = Integer.parseInt(args[2]);

		if(args.length == 3) {
			StorageNode node = new StorageNode(host, dirPort, nodePort);
			node.runNodeWithOutFile();
		}
		if(args.length == 4){
			String file = args[3];
			StorageNode node = new StorageNode(host, dirPort, nodePort, file);
			node.runNodeWithFile();
		}
	}
}