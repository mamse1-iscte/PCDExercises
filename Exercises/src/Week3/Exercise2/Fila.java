package Week3.Exercise2;


import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*

Implemente uma fila de inteiros que permite um acesso concorrente.
A fila tem os seguintes métodos:
boolean	empty()
Verifica se a fila está vazia.
int	peek()
Devolve o número no início da fila. Lança IllegalStateException caso a fila esteja vazia.
int	poll()
Retira e devolve o número no início da fila. Lança IllegalStateException caso a fila esteja vazia.
void	offer(int item)
Adiciona o item no fim da fila. Lança IllegalStateException caso a fila esteja cheia.
int	size()
devolve o numero de elementos que existem na fila.
Teste a implementação criando uma sub-classe de Thread que manipule a fila, p.ex. inserindo alguns milhares de inteiros. Lance 6 instâncias deste processo ligeiro, e no fim verifique a quantidade e consistência dos dados.

Pode implementar a fila como entender, baseando-se na salvaguarda dos valores num vetor: com um único índice para o último elemento, ou com dois índices, um para o primeiro e outro para o último elemento. Relembre-se do que aprendeu em AED.

        */
public class Fila {
    static int max=10000;
    static int[] array= new int[max];
    static int size=0;
    private Lock lock =new ReentrantLock();

    public synchronized boolean empty(){
        return size==0;
    }

    public int	peek() throws IllegalStateException {
        if(empty()){
            throw new IllegalStateException();
        }
        return array[size];
    }

    public int	poll() throws IllegalStateException{

        if(empty()){
            throw new IllegalStateException();
        }


        int item=array[size];
        array[size]= 0;
        size--;
        return  item;
    }

    public synchronized void offer(int item)  throws IllegalStateException{
            if (size() == getMax()) {
                throw new IllegalStateException();
            }
            array[size] = item;
            size++;

    }

     public synchronized static int	size(){
        return size;
    }

    public synchronized static int getMax() {
        return max;
    }

    public static void main(String[] args) throws InterruptedException {
        Mythread t1= new Mythread();
        Mythread t2= new Mythread();
        Mythread t3= new Mythread();
        Mythread t4= new Mythread();
        Mythread t5= new Mythread();
        Mythread t6= new Mythread();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        for (int i=0;i<10000;i++) {
            System.out.println(array[i]);
        }
        System.out.println("size "+ size);
    }



    public static class  Mythread extends Thread{
Fila f = new Fila();
        @Override
        public void run() {
            for (int i =0;i<1000;i++){
                f.offer(2);

            }
        }
    }
}
