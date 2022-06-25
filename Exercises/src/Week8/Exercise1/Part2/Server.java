package Week8.Exercise1.Part2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



//dealwith client é uma thread nova cada vez que um cliente se liga
public class Server {

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




    public static final int PORTO = 8080;

    public static void main(String[] args) {
        try {
            new Server().startServing();
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
           new DealWithClient(socket).start();
            }
        } finally {
            ss.close();
        }
    }
}

