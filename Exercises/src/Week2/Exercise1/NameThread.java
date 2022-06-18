package Week2.Exercise1;



/*
Programe uma nova classe chamada NameThread que estende a classe Thread.
 Esta classe deve receber como argumento do construtor um inteiro que representa o seu identificador. Redefina o run desta classe de modo a que execute as seguintes instruções 10 vezes:
escrever o seu identificador na consola
adormecer um tempo aleatório entre 1 e 2 segundos.

Implemente o método main de modo a que crie duas instâncias da classe NameThread e as inicie.
Altere o main usando o método join da classe Thread de modo a que a thread que está a executar o main espere pelo fim das outras duas threads antes de escrever na consola que terminou o programa.
Altere o método main para que ao fim de 12 segundos interrompa as duas threads. Quando as threads forem interrompidas devem terminar.
*/
public class NameThread extends  Thread {
    private int id;

    public NameThread(int id){
        this.id=id;
    }



    @Override
    public void run() {
      for(int i=0;i<10;i++){
          double random =  ( Math.random() * 2 + 1);
          try {
              sleep((long) random*1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          System.out.println(this.id);
      }
    }



    public static void main(String[] args) throws InterruptedException {
        NameThread thread1= new NameThread(1);
        NameThread thread2= new NameThread(2);
        thread1.start();
        thread2.start();


        try {
        thread1.join();


        sleep (12000);
        thread1.interrupt ();
        thread1.interrupt ();
        } catch ( InterruptedException e ) {}
        System . out . println ( " Main done , all done ! " );
    }
}
