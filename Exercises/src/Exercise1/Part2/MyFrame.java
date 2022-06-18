package Exercise1.Part2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/*
O objetivo deste exercício é tomar contacto com as classes do Swing mais elementares, para representar janelas (JFrame), painéis (JPanel), etiquetas (JLabel), campos de texto (JTextField), botões (JButton, JCheckBox), e formas de os organizar (FlowLayout, GridLayout).

1. Experimente executar a classe em anexo (FrameTest), alterando alguns dos valores utilizados no exemplo, tais como o texto utilizado nos elementos e os valores utilizados no GridLayout.

2. Com base na estrutura da classe dada, desenvolva uma outra classe cujo aspeto da interface gráfica seja semelhante ao apresentado na figura em baixo. Deverá ser possível executar a classe configurando a janela com um texto para o título e uma dimensão inicial:

public static void main(String[] args) {
    MyFrame window = new MyFrame("Hello",300, 150);
    window.open();
}

Ao carregar no botão a janela deverá ser redimensionada de acordo com os valores nas caixas de texto, e caso a checkbox esteja selecionada, a localicação da janela deverá também ser alterada para o centro do ecrã.


Dica: A JFrame pode redimensionada utilizando o método setSize(...) da classe  JFrame  . O título pode ser alterado através de setTitle(...)

Dica: É possível obter a dimensão do ecrã da seguinte forma:
Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
Dica: Para alterar a posição da janela deve usar-se o método setLocation(int x,int y) da classe  JFrame  .
 */


public class MyFrame {
    private JFrame frame;

    public MyFrame(String title, int xValue,int yValue) {
        frame = new JFrame(title);

        // para que o botao de fechar a janela termine a aplicacao
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setTitle(title);
        frame.setSize(xValue,yValue);


        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation( dimension.width/3, dimension.height/3);

        addFrameContent( title,  xValue, yValue);

        // para que a janela se redimensione de forma a ter todo o seu conteudo visivel
       // frame.pack();
    }

    public void open() {
        // para abrir a janela (torna-la visivel)
        frame.setVisible(true);
    }

    private void addFrameContent(String title, int xValue,int yValue) {

		/* para organizar o conteudo em grelha (linhas x colunas)
		se um dos valores for zero, o numero de linhas ou colunas (respetivamente) fica indefinido,
		e estas sao acrescentadas automaticamente */
        frame.setLayout(new GridLayout(4,2));


        JLabel titleL = new JLabel("title");
        frame.add(titleL);

        JTextField titleT = new JTextField(title);
        frame.add(titleT);




        JLabel widthL = new JLabel("width");
        frame.add(widthL);

        JTextField widthT = new JTextField(Integer.toString(xValue));
        frame.add(widthT);



        JLabel heightL = new JLabel("height");
        frame.add(heightL);

        JTextField heightT = new JTextField(Integer.toString(yValue));
        frame.add(heightT);



        JCheckBox check = new JCheckBox("check");
        frame.add(check);

        JButton button = new JButton("Update");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, check.isSelected() ? "checked" : "not checked");



                String title = titleT.getText();
                String width = widthT.getText();
                String height = heightT.getText();


                frame.setTitle(title);
                frame.setSize( Integer.parseInt(width), Integer.parseInt(height));









            }
        });
        frame.add(button);



    }

    public static void main(String[] args) {
        MyFrame window = new MyFrame("Hello",250, 200);
        window.open();
    }
}