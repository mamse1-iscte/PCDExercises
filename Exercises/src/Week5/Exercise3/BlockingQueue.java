package Week5.Exercise3;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/*
Nota prévia: nesta solução, não use variáveis condicionais e faça a coordenação usando simplesmente o mecanismo wait/notifyAll.
O  objetivo  deste exercício é desenvolver uma estrutura de dados genérica para Filas Bloqueantes . Esta estrutura consiste numa Fila ( First In First Out ) com as operações put (adiciona um elemento no fim da fila) e take (retira e devolve  o primeiro elemento da fila). Porém, as chamadas a estas operações são bloqueantes (usando wait)   nas seguintes situações :
put: no caso de a fila estar no limite da sua capacidade.
take: no caso de a fila estar vazia.
Desta forma, a fila pode ser acedida concorrentemente, e é útil para problemas cuja solução encaixa no padrão produtor-consumidor.
Implemente a estrutura numa classe chamada BlockingQueue , tendo em conta o seguinte:
Os objetos são parameterizados com um parâmetro genérico <T> para o tipo de elementos da fila.
Internamente, os elementos devem ser guardados numa estrutura apropriada para filas (convencionais), tal como LinkedList (lista ligada) ou ArrayDeque.
Deverão ser disponibilizados dois construtores:
Sem parâmetros: neste caso, a estrutura não terá uma capacidade máxima, e, assim, a operação offer nunca bloqueará e a operação poll não necessitará de notificar outras threads (pois não haverá threads bloqueadas em poll)
Com um parâmetro para definir a capacidade da fila (pré-condição: número positivo).
Para além das operações put e take, deverá existir também a operação size (para obter o número de elementos da fila) e clear (para esvaziar a fila).   As operações bloqueantes devem propagar as exceções de interrupção de thread (InterruptedException) para os clientes.

public class BlockingQueue <T> {
     private Queue <T> queue = ...
...
}
*/
public class BlockingQueue<T> {
    public static BlockingQueue blockingQueue;
    private LinkedList<T> queue = new LinkedList<>();
    private int size;

    public BlockingQueue() {


    }

    public BlockingQueue(int size) {
        if (size > 0) {
            this.size = size;
        } else {
            this.size = 0;
        }
    }


    //adiciona um elemento no fim da fila
    //no caso de a fila estar no limite da sua capacidade. bloqueia
    public synchronized void put(T object) throws InterruptedException {
        if (size()==queue.size())
            wait();
        queue.add(object);
        notifyAll();
    }

    //retira e devolve  o primeiro elemento da fila
    //no caso de a fila estar vazia. bloqueia
    public synchronized void take() throws InterruptedException {
        if (size()==0)
            wait();
        queue.remove(queue.getLast());
notifyAll();
    }





    public int size() {
        return size;
    }

    public void clear() {
        queue.remove();
    }

    public class Mythread1 extends  Thread {


        @Override
        public void run() {
            while (true){
                try {
                    sleep(1000);
                    blockingQueue.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        public  class Mythread2 extends  Thread{

            @Override
            public void run() {
                while (true){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    public static void main(String[] args) {
         blockingQueue= new BlockingQueue(5);

        for (int i=0;i<10;i++)
        blockingQueue.new Mythread1().start();


    }
}
