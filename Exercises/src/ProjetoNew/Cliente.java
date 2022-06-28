package ProjetoNew;

import ProjetoOld.ByteBlockRequest;
import ProjetoOld.CloudByte;
import Week1.Exercise2.MyFrame;
import Week8.Exercise1.Part2.Client;
import Week8.Exercise1.Part2.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class Cliente {

    ///
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    ///

    private JFrame frame;

        public Cliente(String title, int xValue, int yValue) {
            frame = new JFrame(title);

            // para que o botao de fechar a janela termine a aplicacao
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            frame.setTitle("Client");
            frame.setSize(xValue,yValue);


            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

            frame.setLocation( dimension.width/3, dimension.height/3);

            addFrameContent( title,  xValue, yValue);

            // para que a janela se redimensione de forma a ter todo o seu conteudo visivel
            // frame.pack();
        }



        private void addFrameContent(String title, int xValue,int yValue) {

		/* para organizar o conteudo em grelha (linhas x colunas)
		se um dos valores for zero, o numero de linhas ou colunas (respetivamente) fica indefinido,
		e estas sao acrescentadas automaticamente */

            JPanel UpPanel = new JPanel();

            JPanel IndicePanel= new JPanel();
            IndicePanel.setLayout(new GridLayout(1,2));

            JLabel labelIndice = new JLabel("Posicao a consultar: ");
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

            JTextArea Answer = new JTextArea("Respostas apareccerao aqui...");

            frame.add(Answer);
            CheckButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            UpPanel.add(CheckButton);

            frame.add(UpPanel,BorderLayout.NORTH);

            frame.setVisible(true);
        }



///
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
            socket = new Socket(endereco, Server.PORTO);
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

    public static void main(String[] args) {
        Cliente window = new Cliente("Hello",650, 200);
        window.runClient();
    }
    ///
    }


