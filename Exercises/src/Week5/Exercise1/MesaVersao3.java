package Week5.Exercise1;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





/*

Implemente o problema do banquete dos javalis (ver laboratório anterior)
recorrendo a um cadeado ReentrantLock, de modo a apenas notificar as threads potencialmente afetadas por cada evento.
Para tal, crie, a partir do cadeado, variáveis condicionais associadas a cada uma das situações criadoras de bloqueio.

*/


    public class MesaVersao3 {
        private Lock lock =new ReentrantLock();
        private Condition mesaVazia = lock.newCondition();
        private Condition mesaCheia = lock.newCondition();


        private List<Javali> javalis = new ArrayList<>();
        public static final int LIMITE = 10;
        private int index=0;






        public MesaVersao3(){

            Cozinheiro cozinheiro = new Cozinheiro(1);
            Cozinheiro cozinheiro2 = new Cozinheiro(2);
            Glutao glutao = new Glutao(1);
            Glutao glutao2 = new Glutao(1);
            glutao2.start();
            cozinheiro.start();
            //cozinheiro2.start();
            glutao.start();


        }







        public static void main(String[] args) {
            MesaVersao3 mesaVersao3 = new MesaVersao3();

        }

        public class Glutao extends  Thread {
            private int id;

            public Glutao(int id){
                this.id = id;

            }
            public int getGlutaoid() {
                return id;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    retira();

                }
            }
        }
        public   void retira(){
            try{
                lock.lock();

            if (javalis.isEmpty()) {
                System.out.println("Glutao á espera ");
                try {
                    mesaVazia.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("foi comido um javali " + javalis.get(javalis.size() - 1).getId() +  " ja estao "+ (javalis.size()-1) +" javalis na mesa");
                javalis.remove(javalis.size() -1);
                mesaCheia.signalAll();
            }
            }
            finally {
                lock.unlock ();
            }
        }

        public   void mete(Cozinheiro cozinheiro){
            try {
                lock.lock();

                if (javalis.size() == LIMITE) {
                    System.out.println("Cozinheiro á espera " + cozinheiro.getCozinheiroid());
                        mesaCheia.await();

                }


            else {

                Javali javali = new Javali(index);
                javalis.add(javali);
                index++;
                System.out.println("foi criado um javali " + javali.getId() + " pelo cozinheiro " + cozinheiro.getCozinheiroid()+ " ja estao "+ javalis.size() +" javalis na mesa");
                mesaVazia.signalAll();
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }








        public class Cozinheiro extends  Thread{
            private int id;

            public Cozinheiro(int id){
                this.id = id;

            }
            public int getCozinheiroid() {
                return id;
            }

            @Override
            public void run() {
                while(true){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mete(this);
                }
            }
        }




        public class Javali {
            private int id;

            public Javali(int id) {
                this.id = id;

            }

            public int getId() {
                return id;
            }

        }
    }



