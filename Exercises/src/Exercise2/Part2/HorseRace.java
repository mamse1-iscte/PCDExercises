package Exercise2.Part2;




/*
Considere uma aplicação para simular uma corrida de cavalos. Na corrida participam sempre 3 cavalos que correm de forma independente. A evolução de cada cavalo deve ser visível num campo do tipo JTextField, tal como se exemplifica na figura em baixo. A interface da aplicação inclui, ainda, um botão para iniciar a corrida. A movimentação dos cavalos deve obedecer aos seguintes requisitos:
a) o percurso total dos cavalos é constituído por 30 movimentos;
b) um movimento consiste em subtrair uma unidade aos movimentos que faltam;
c) após cada movimento o cavalo deve dormir um tempo aleatório.
Deve ser a classe Cavalo, que estende Thread, a controlar o avanço dos movimentos de cada cavalo.
Exemplo:

Sugestão: para facilitar a implementação, sugere-se que a classe correspondente ao Cavalo (que estende Thread) receba no construtor um JTextField que deve guardar num atributo. Assim, cada vez que o cavalo avançar uma unidade pode atualizar o JTextField , através do método setText.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HorseRace {
    private JFrame frame;

    public HorseRace(int horse1, int horse2, int horse3) {
        frame = new JFrame("Horse");

        // para que o botao de fechar a janela termine a aplicacao
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setTitle("Horse");
        frame.setSize(250, 200);


        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(dimension.width / 3, dimension.height / 3);

        addFrameContent(horse1, horse2, horse3);

        // para que a janela se redimensione de forma a ter todo o seu conteudo visivel
        // frame.pack();
    }

    public void open() {
        // para abrir a janela (torna-la visivel)
        frame.setVisible(true);
    }

    private void addFrameContent(int horse1, int horse2, int horse3) {

		/* para organizar o conteudo em grelha (linhas x colunas)
		se um dos valores for zero, o numero de linhas ou colunas (respetivamente) fica indefinido,
		e estas sao acrescentadas automaticamente */
        frame.setLayout(new GridLayout(4, 1));


        JTextField h1 = new JTextField(Integer.toString(horse1));
        frame.add(h1);

        JTextField h2 = new JTextField(Integer.toString(horse2));
        frame.add(h2);


        JTextField h3 = new JTextField(Integer.toString(horse3));
        frame.add(h3);

        JButton button = new JButton("inicia");
        button.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {

                Horse horse1= new Horse(h1);
                Horse horse2= new Horse(h2);
                Horse horse3= new Horse(h3);
                horse1.start();
                horse2.start();
                horse3.start();



            }
        });
        frame.add(button);


    }


    public static void main(String[] args) {
        HorseRace window = new HorseRace(30, 30, 30);
        window.open();
    }







    public class Horse extends Thread {
        JTextField mov;

        public Horse( JTextField mov){
            this.mov=mov;
        }

        @Override
        public void run() {
            String temp = this.mov.getText();
            int movimentos = Integer.parseInt(temp);
            while(movimentos>0){
                movimentos--;
                mov.setText(Integer.toString(movimentos));

                double random =  ( Math.random());

                try {
                    sleep((long) (random*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
