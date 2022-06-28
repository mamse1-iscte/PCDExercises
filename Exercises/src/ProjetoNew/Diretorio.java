package ProjetoNew;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


//assim que arrancam os nos devem ser inscritos
// diretiorio funciona como servidor para os storagenodes
//é sempre textual :BufferedReadere printwriter

//caso escrevam nodes deve devolver todos os nos armazenados
public class Diretorio {
    //guardar endereço e porto dos storageNodes
    private List<String> nodeList;


    //cenas de servidor
    private class DealWithClient extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;


        public DealWithClient(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
        }

        private void serve() throws IOException {
            while (true) {
                String str = in.readLine();
                nodeList.add(str);
                if (str.equals("nodes"))
                nodeList.forEach(System.out::println);
                if (str.equals("end"))
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


    public static final int PORTO = 8080;

    public static void main(String[] args) {
        try {
            new Diretorio().startServing();
        } catch (IOException e) {
            // ...
        }
    }

    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(PORTO);
        try {
            //mudado esta parte para aceitar varios clientes
            System.out.println("Aguardando ligacoes...");
            while (true) {
                Socket socket = ss.accept();
                new Diretorio.DealWithClient(socket).start();
            }
        } finally {
            ss.close();
        }
    }



}

