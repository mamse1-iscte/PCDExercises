package Week6.Exercise3.CyclicBarrier;


import java.util.concurrent.CyclicBarrier;

/*
Considere 10 threads que contam de 1 até 10^6.
Inicialmente são lançados 5 threads.
Quando todos esses threads chegarem a 10^3 os restantes 5 threads iniciam a contagem.
Finalmente, quando todos os threads acabarem a contagem deve ser escrito no ecrã “Todos os threads acabaram de contar”.
Utilize a  barreira da biblioteca padrão do Java (CyclicBarrier) na sua implementação.

*/
public class Teste {




public static void main(String[] args) {
Teste teste= new Teste();

  CyclicBarrier barrier = new CyclicBarrier ( 5 , new Runnable () {
                public void run () {

 }
            });
  for (int i=0;i<5;i++){
      teste.new Mythread(i,barrier).start();

  }
}
    public class Mythread extends Thread{
    CyclicBarrier barrier;
        private int id;

        public Mythread(int id,CyclicBarrier barrier){
            this.id= id;
            this.barrier=barrier;
        }


    }
}
