package ProjetoNew;


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

//java -jar diretorio.jar 8080

//devem enviar mensagem  INSC <ENDEREÇO> <PORTO>. para o diretorio funcionam como clientes
public class StorageNode {
    public static  CountDownLatch cdl= new CountDownLatch(2);
    public static final int PORTO = 8080;
    public static CloudByte[] contentCloudBytes;
    public int countBlock=0;
    public static final int LENGTH = 100;

    private List<ByteBlockRequest> blockList = new ArrayList<>();


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
            }
    }

    //tem de ver no diretorio a localizacao dos outros storagenodes e fazer uma copia a partir dai
    public StorageNode(String diretorio, int portoEscuta, int portoNoEscuta) throws UnknownHostException {
        this.diretorio=InetAddress.getLocalHost().getHostAddress();
        this.portoEscuta=portoEscuta;
        this.portoNoEscuta=portoNoEscuta;
    }

    public void connectToServer() {
        try {
            InetAddress endereco = InetAddress.getByName(null);
            Socket socket = new Socket(endereco, 8080);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("foi feita a conexao no porto "+ PORTO+ " e endereco: "+endereco);


        } catch (IOException e) {
            System.out.println("servidor nao encontrado");
        }
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






    public void correcaoDeValues() {

    }

    private static  class DealWithInput extends Thread {
        private BufferedReader in;


        // tem de ir a todos os storageNodes ver se algum tem parity not ok
        //se algum tiver parity not ok ver nos outros nos se 2 têm parity ok e substituir
        @Override
        public void run() {
            try {
                serve();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void serve() throws IOException {
           while (true){
               in= new BufferedReader(new InputStreamReader(System.in));
               String comando=in.readLine().toUpperCase();
               if(comando.contains("ERROR")){
                   String[] partes=comando.split(" " );
                   int erro=Integer.parseInt(partes[1]);
                   if(contentCloudBytes[erro].isParityOk()){
                       contentCloudBytes[erro].makeByteCorrupt();
                       System.out.println("byte "+erro+ " tornou se corrupto");
                   }
                   else
                   {
                       System.out.println("Byte ja era corrupto");
                   }


               }
               else{
                   System.out.println("Comando nao é valido");
               }

           }
        }
    }

   public static class Corretores extends Thread {
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
            while (true) {
                for (int i = 0; i < contentCloudBytes.length; i++) {
                    if (!contentCloudBytes[i].isParityOk()) {
                        System.out.println("foi encontrado um erro no byte " + i);

                    }
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    //ao criar um novo storagenode a informação tem de ser copiada, inicialmente em blocos de  1 00 000  /100 = 100 000 pedidos para descarregar
    //as threads têm de contar os pedidos e ir metendo


    public  void CreateByteBlockList(){
        for (int i=0;i<1000000;i+=LENGTH) {
            blockList.add(new ByteBlockRequest(i, LENGTH));
        }
    }
    public synchronized CloudByte[] BlockOfCloudBytes(){
        CloudByte[] block= new CloudByte[LENGTH];
        for(int i=0;i<LENGTH;i++){
            block[i]=contentCloudBytes[countBlock];
        }
        countBlock+=1;
        return block;
    }


    public static void main(String[] args) throws IOException {
        // try {
        new StorageNode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]).connectToServer();
                /*new StorageNode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]).startServing();}
      		catch(java.lang.ArrayIndexOutOfBoundsException e){
                    new ProjetoOld.StorageNode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2])).run();
                }*/
        new DealWithInput().start();
        new Corretores(1,cdl).start();

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
}
