package Week3.Exercise1;


import java.util.concurrent.atomic.AtomicInteger;

/*

Programe uma classe contador que contenha os seguintes métodos:
incrementar,
decrementar e
consultar o valor.
Programe também uma classe de processos ligeiros onde cada instância incrementa o valor do contador. O processo deve repetir esta ação 1000 vezes, após o que termina.

a) No main, crie um contador e, após lançar 4 processos ligeiros, espere que estes  terminem (usando o método join) e exiba na consola o resultado do contador. Caso detete que o resultado não corresponde ao esperado, detete as secções críticas e proceda à sua sincronização.

b) A sincronização não é a única forma de resolver as interferências associadas a secções críticas. Se estas puderem ser reduzidas a ações atómicas, deixam, por definição, de ser críticas. O contador é um bom candidato para este processo. Por isso, faça uma nova versão da classe Contador de maneira a usar variáveis e ações atómicas. Em particular, faça uso da classe AtomicInteger.

*/
public class Contador2 {
    int  n=0;
    AtomicInteger val = new AtomicInteger(0);

    public Contador2() throws InterruptedException {
        Mythread t1= new Mythread();
        Mythread t2= new Mythread();
        Mythread t3= new Mythread();
        Mythread t4= new Mythread();
        t1.start();
        t2.start();
        t3.start();
        t4.start();


        t1.join();
        t2.join();
        t3.join();
        t4.join();


        System.out.println("N "+  val.get());
    }
    public static void main(String[] args) throws InterruptedException {
        Contador2 c= new Contador2();
    }


    public class Mythread extends Thread{


        @Override
        public void run() {
            for (int i=0;i<1000;i++) {
                val.incrementAndGet();

            }
        }
    }
}
