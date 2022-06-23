package Week6.Exercise2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;


/*
a) Crie uma aplicação que crie uma janela com um botão de início. Inicialmente existem 25 bolas, que são apresentadas no lado esquerdo da janela.
Quando o botão de início é premido, deve ser criada uma "thread pool" com 6 processos.
Cada processo ligeiro, que deve ser desenvolvido na classe Bola, deve escolher um incremento aleatório entre 0.01 e 0.1.
Quando arrancar, o processo ligeiro deve ir sucessivamente acrescentando este incremento ao seu estado (inicialmente a 0),
até que chegue a 1 (considere o método Bola.bolaAtingiuLimite para este efeito).
Em cada iteração desta incrementação deve ser feito um Thread.sleep (p.ex. de 100ms), de forma a que os movimentos sejam bem visíveis na IG.
A classe BallPainter (no anexo) é usada para desenhar as bolas na GUI.
Para adicionar uma bola à instância de BallPainter é chamado o método add, como pode ver no método addContent da classe IG.
As suas bolas (da classe Bola) implementam a interface DrawableBall e Runnable, e  estendem a classe Observable.
O desenvolvimento segue o padrão de desenho Observador/Observado, sendo os processos ligeiros Ball os Observados, e a classe BallPainter da interface gráfica o Observador.
(Ver vídeo em anexo). Para que o observador seja devidamente notificado, a classe Bola, de cada vez que alterar a sua posição, deve invocar os métodos setChanged e notifyObservers, por esta ordem.
Procure as etiquetas TODO para ver o que precisa fazer: criar a ThreadPool e dar-lhe os processos, na classe IG, e desenvolver o progresso, na classe Bola.
Nota: para facilitar o desenvolvimento, no código partilhado os processos são lançados normalmente, através do método start.
Adicional e opcionalmente, crie um novo Observador, que escreve unicamente para consola "A bola n acabou" quando verificar que tal aconteceu.
b) Use uma barreira para conseguir parar todas as threads ainda em funcionamento (ou em espera de ser executadas) logo que cheguem 3 threads ao fim da corrida.
Dica: deve usar o método shutdownNow da ThreadPool.
*/
public class IG {
	ArrayList<Bola> bolas= new ArrayList<>();
	
	public void addContent(){
		JFrame janela= new JFrame("hh");
		janela.setLayout(new BorderLayout());
		BallPainter painter=new BallPainter();
		janela.add(painter, BorderLayout.CENTER);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for(int i=0;i<25;i++){
			Bola bola=new Bola();
			bola.addObserver(painter);
			bolas.add(bola);
			painter.addBall(bola);
		}
		
		JButton start=new JButton("Start");
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO criar ThreadPool. Aqui são simplesmente lançadas as threads.
				ExecutorService pool = Executors . newFixedThreadPool (6);
				for(Bola bola:bolas) {
				//	new Thread(bola).start();
					pool.submit(bola);
				}
				
				
			}
		});
		janela.add(start, BorderLayout.SOUTH);
		janela.setSize(800, 600);
		janela.setVisible(true);
	}
	public static void main(String[] args) {
		new IG().addContent();

	}

}
