package Week4.Exercise2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/*

Um lingote típico de ouro é construído com ~12.5 kg de ouro. Para fazer um lingote, são precisos diversos pedaços de ouro extraídos da natureza.
Crie uma aplicação que simule uma escavadora de ouro, uma balança e um ourives. A escavadora recolhe pequenos pedaços de ouro (cada pedaço pesa até 1 kg).
Quando a escavadora acha o ouro, é posto numa balança. Quando 12.5 kg de ouro ou mais estão na balança, o ourives recolhe o ouro para fazer o lingote.
A escavadora e o ourives devem ser threads.
A escavadora pode acrescentar ouro na balança apenas se houver menos do que 12.5 kg de ouro na balança.
Por outro lado, o ourives pode apenas recolher o ouro quando ficam 12.5 kg ou mais na balança.
Depois de o ourives recolher o ouro da balança, leva-lhe 3 segundos a transformar o ouro num lingote.
Utilize um double para representar a quantidade atual de ouro na balança.
Deverá criar uma janela com o campo de texto e um botão. O campo de texto deve mostrar o peso do ouro que está na balança.
Quando o utilizador pressiona o botão “Stop”, a escavadora e o ourives devem ser interrompidos e terminar. Antes de terminar, o ourives deve escrever na consola o número total de lingotes criados.
A sua solução deve enviar mensagens que contenham o id da thread (Thread.currentThread().toString()) bem como o tipo de operação (ex: “inicio do run”) para a consola sempre que uma thread faz:
Inicio e fim do método run
Antes de tratar um InteruptedException
Antes de fazer um join
Antes e depois de fazer um wait ou um sleep
Exemplo:System.out.println(Thread.currentThread().toString() + “ - inicio do run.”);
Dica: para atualizar a GUI, a balança deve receber no construtor uma instância de JTextField que será atualizado (usando setText) cada vez que o conteúdo da balança for atualizado

*/


public class GUI {
    private JFrame frame;


    public GUI(Balanca balanca) {

        Escavadora escavadora = new Escavadora(balanca);
        Ourives ourives = new Ourives(balanca);
        escavadora.start();
        ourives.start();

        frame = new JFrame("Balanca");

        // para que o botao de fechar a janela termine a aplicacao
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setTitle("Balanca");
        frame.setSize(200, 150);


        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(dimension.width / 3, dimension.height / 3);

      frame.setLayout(new GridLayout(4, 1));


        frame.add(balanca.getTexto());

        JButton button = new JButton("Stop");
        button.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                escavadora.interrupt();
                ourives.interrupt();

            }
        });
        frame.add(button);


    }

    public void open() {
    frame.setVisible(true);
    }


    public static void main(String[] args) {
        JTextField texto = new JTextField("0");
        Balanca balanca = new Balanca(texto);
        GUI window = new GUI(balanca);
        window.open();

    }
}
