package Week6.Exercise3.CyclicBarrier;



import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*
Considere 10 threads que contam de 1 até 10^6.
Inicialmente são lançados 5 threads.
Quando todos esses threads chegarem a 10^3 os restantes 5 threads iniciam a contagem.
Finalmente, quando todos os threads acabarem a contagem deve ser escrito no ecrã “Todos os threads acabaram de contar”.
Utilize a  barreira da biblioteca padrão do Java (CyclicBarrier) na sua implementação.

*/
public class Teste {
    public final static int MAX=10;
    public static  Mythread[] threads=new Mythread[MAX];





    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
Teste teste= new Teste();

  CyclicBarrier barrier = new CyclicBarrier ( MAX , new Runnable () {
                public void run () {
                    System.out.println("oi");
                    //posso ver o conteudo de todas as threads agr
 }
            });
  for (int i=0;i<MAX;i++){
      threads[i]=teste.new Mythread(i,barrier);
      threads[i].start();
  }
}
    public class Mythread extends Thread{
    CyclicBarrier barrier;
        private int id;

        public Mythread(int id,CyclicBarrier barrier){
            this.id= id;
            this.barrier=barrier;
        }

        @Override
        public void run() {
            System.out.println("o meu id "+id);
            try {
                sleep(100);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
