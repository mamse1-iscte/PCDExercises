package ProjetoNew;

import ProjetoOld.ByteBlockRequest;
import Week8.Exercise1.Part2.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


//devem enviar mensagem  INSC <ENDEREÇO> <PORTO>. para o diretorio funcionam como clientes
public class StorageNode {
    private CountDownLatch cdl= new CountDownLatch(2);
    public static final int PORTO = 8080;
    CloudByte[] contentCloudBytes;

    private String diretorio;
    private int portoEscuta;
    private int portoNoEscuta;
    private String ficheiro;

    //passa do ficheiro para armazenar um  CloudByte[] contentCloudBytes
    public StorageNode(String diretorio, int portoEscuta, int portoNoEscuta, String ficheiro ) throws IOException {
        this.diretorio= InetAddress.getLocalHost().getHostAddress();
        this.portoEscuta=portoEscuta;
        this.portoNoEscuta=portoNoEscuta;
        this.ficheiro=ficheiro;



        byte[] fileBytes= Files.readAllBytes(new File(ficheiro).toPath());

       contentCloudBytes= new CloudByte[fileBytes.length];
        for (int i=0;i<fileBytes.length;i++){
            contentCloudBytes[i]= new CloudByte(fileBytes[i]);
            System.out.println(fileBytes[i]);
            }
    }

    //tem de ver no diretorio a localizacao dos outros storagenodes e fazer uma copia a partir dai
    public StorageNode(String diretorio, int portoEscuta, int portoNoEscuta) throws UnknownHostException {
        this.diretorio=InetAddress.getLocalHost().getHostAddress();
        this.portoEscuta=portoEscuta;
        this.portoNoEscuta=portoNoEscuta;
    }



        private class DealWithClient extends Thread {
            private BufferedReader in;
            private PrintWriter out;
            private Socket socket;


            public DealWithClient(Socket socket) throws IOException {
                this.socket=socket;
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
            }

            private void serve() throws IOException {
                while (true) {
                    String str = in.readLine();
                    if (str.equals("FIM"))
                        break;
                    System.out.println("Eco:" + str);
                    out.println(str);
                }
            }


            @Override
            public void run() {
                try {
                    serve();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }





        public static void main(String[] args) throws IOException {
           // try {
                new StorageNode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
                /*new StorageNode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]).startServing();}
      		catch(java.lang.ArrayIndexOutOfBoundsException e){
                    new ProjetoOld.StorageNode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2])).run();
                }*/
        }

        /*
        public void startServing() throws IOException {
            ServerSocket ss = new ServerSocket(PORTO);
            try {
                //mudado esta parte para aceitar varios clientes
                System.out.println("Aguardando ligacoes...");
                while (true) {
                    Socket socket = ss.accept();
                    new StorageNode.DealWithClient(socket).start();
                }
            } finally {
                ss.close();
            }
        }

*/

    public synchronized CloudByte[] getBlocks(ByteBlockRequest req) {
        return Arrays.copyOfRange(contentCloudBytes,req.startIndex,req.startIndex+req.length);
    }

    public void correcaoDeValues() {

    }

    public  class DealWithInput extends Thread {
        private BufferedReader in;


        // tem de ir a todos os storageNodes ver se algum tem parity not ok
        //se algum tiver parity not ok ver nos outros nos se 2 têm parity ok e substituir
        @Override
        public void run() {
            serve();
        }

        public void serve() {
           while (true){
               in= new BufferedReader(new InputStreamReader(System.in));
               if(in.toString().equals("ERROR")){
                   //if()

               }

           }
        }
    }

    public  class Corretores extends Thread {
        private CountDownLatch cdl;
        private int id;

        public Corretores(int id,CountDownLatch cdl){
            this.id=id;
            this.cdl=cdl;
        }

        // tem de ir a todos os storageNodes ver se algum tem parity not ok
        //se algum tiver parity not ok ver nos outros nos se 2 têm parity ok e substituir
        @Override
        public void run() {

        }
    }
}
