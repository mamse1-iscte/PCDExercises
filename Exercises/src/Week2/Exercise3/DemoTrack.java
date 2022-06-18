package Week2.Exercise3;

import javax.swing.JFrame;



/*
Faça um programa que permita simular uma corrida de vários automóveis.
A sua aplicação deve ter uma janela com apenas um componente: uma instância da classe Track (fornecida no anexo) que desenha os automóveis durante a corrida. Os carros devem arrancar logo que a aplicação seja lançada.

Cada automóvel deve ser controlado por um processo ligeiro (thread) que vai incrementando a sua posição, até atingir o limite.  A classe Car deve por isso ser alterada para conseguir ser executada enquanto processo ligeiro.
Nota importante: Repare que a classe Car já estende uma classe, pelo que não pode estender também Thread. Opte por isso por fazê-la implementar o interface Runnable. Mais detalhes sobre esta questão aqui: https://www.oreilly.com/library/view/java-for-dummies/9781118239742/a66_06_9781118239742-ch03.html
Deve usar o padrão de desenho observador-observado para fazer a atualização da pista (Track), que desempenha o papel de observador.
A demonstração exemplo já inscreve uma instância de Track como observador dos carros. Para que este padrão funcione como desejado, os carros  desempenham o papel de observado e devem invocar os métodos setChanged() e notifyObservers() após cada movimento. Mais detalhes sobre o padrão observador/observado aqui: https://docs.oracle.com/javase/8/docs/api/java/util/Observable.html

A classe Track recebe no construtor o número de automóveis que vão participar na corrida e o número de passos necessários para o automóvel chegar à meta: public Track(int numCars, int numSteps).
a) Crie a solução que usa a instância da classe Track para mostrar a posição dos vários carros.
b) Altere a sua aplicação de modo a indicar (p. ex. através da consola, ou de um JOptionPane) o nº ordinal do carro vencedor.
c) [Extra: mais difícil do que as anteriores] Altere a sua aplicação de modo a que todos os processos ligeiros que controlam os automóveis terminem quando o primeiro carro chegar à meta. Para tal ser possível, é preciso que os carros tenham acesso a referências de todas as instâncias da sua classe, para poderem interromper as outras instâncias.

Anexo: Projeto de Eclipse para ser importado.

Nota: apesar de as classes do padrão de desenho estarem classificadas como deprecated, elas podem ser utilizadas sem problemas. Estas classes foram classificadas como tal não por serem desinteressantes, mas por ter sido considerado que não fazia sentido estarem incluídas nas bibliotecas oficiais do Java, por serem de aplicação genérica e acessória do ponto de vista da linguagem Java.
*/

public class DemoTrack {

	public static void main(String[] args) {
		// GUI usage example... Change to suit exercise
		JFrame frame = new JFrame("Demo Track");
		Track track = new Track(3, 100);
		Car c1=new Car(0, 100,track);
		Car c2=new Car(1, 100,track);
		Car c3=new Car(2, 100,track);
		c1.addObserver(track);
		c2.addObserver(track);
		c3.addObserver(track);



		frame.add(track);
		frame.setSize(500, 300);
		frame.setVisible(true);



		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);
		Thread t3 = new Thread(c3);
		t1.start();
		t2.start();
		t3.start();


	}

}
