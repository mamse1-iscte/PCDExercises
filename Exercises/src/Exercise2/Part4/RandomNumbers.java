package Exercise2.Part4;


import Exercise2.Part2.HorseRace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/*

Gerador de números aleatórios
Escreva um programa com uma janela com um botão com o texto "Stop" (JButton ("Stop")). Quando o programa começa deve criar e iniciar duas threads de classes diferentes:
A classe ThreadA deve gerar constantemente números aleatórios entre 1000 e 9999 até ser interrompida. Os números gerados devem ser escritos na consola. Não deve ser chamado o método Sleep. Nota: deve verificar usando o método interrupted() para verificar se a thread foi interrompida.
A classe ThreadB deve gerar constantemente números aleatórios entre 1 e 9 até ser interrompida. Os números gerados devem ser escritos na consola. Entre a geração de cada número a thread deve dormir (sleep) 0.5 segundo.
Ambas as threads devem manter o registo de quantos números foram já gerados.
Quando o botão Stop for pressionado ambas as threads devem ser interrompidas e terminar. Só após as duas threads terem terminado deve ser escrito na consola o número de valores gerados por cada uma das threads.

*/
public class RandomNumbers {
    private JFrame frame;


    public RandomNumbers(){


        ThreadA a= new ThreadA();
        ThreadB b= new ThreadB();
        a.start();
        b.start();
        frame = new JFrame("Horse");

        // para que o botao de fechar a janela termine a aplicacao
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setTitle("Random Numbers");
        frame.setSize(250, 200);


        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(dimension.width / 3, dimension.height / 3);



        JButton button = new JButton("Stop");
        button.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
a.interrupt();
b.interrupt();


            }
        });
        frame.add(button);


        // para que a janela se redimensione de forma a ter todo o seu conteudo visivel
        // frame.pack();


        frame.setVisible(true);
    }



    public static void main(String[] args) {
        RandomNumbers window = new RandomNumbers();

    }





    public class ThreadA extends Thread{

        @Override
        public void run() {
while (true){
    Random r = new Random();
    int low = 1000;
    int high = 9999;
    int result = r.nextInt(high-low) + low;
    try {
        sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("ThreadA "+result);

}
        }
    }
    public class ThreadB extends Thread{
        @Override
        public void run() {
            while (true){
                Random r = new Random();
                int low = 1;
                int high = 9;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = r.nextInt(high-low) + low;
                System.out.println("ThreadB "+result);

            }
        }
    }
}
