package Week4;


import java.util.ArrayList;
import java.util.List;



/*
1 O banquete de javalis
Programar o seguinte problema “produtor/consumidor”.
Num banquete de javalis assados existem vários cozinheiros e glutões concorrentes e uma mesa com capacidade limitada (e.g., só cabem na mesa 10 javalis).
Cada cozinheiro repetidamente produz um javali assado e coloca-o na mesa. Caso encontre a mesa cheia fica à espera que a mesa fique com espaço disponível para depositar o seu javali.
Cada glutão repetidamente retira da mesa um javali e consome-o. Caso encontre a mesa vazia fica à espera que a mesa contenha de novo algum javali.
Sincronizar o recurso partilhado mesa por forma a coordenar devidamente os vários cozinheiros e glutões.
Utilizar ainda mensagens apropriadas para observação do comportamento dos vários atores no banquete.
Cada Javali deve ser identificado pelo cozinheiro que o produziu e por um número de ordem.
Versão 1: Considerar que os cozinheiros/glutões produzem/consomem um número fixo de javalis.
Versão 2: Considerar que os cozinheiros/glutões ficam a funcionar um determinado tempo (p.ex. 10s), após o que são interrompidos e param.
Versão 3: Teste a solução envolvendo apenas dois cozinheiros que produzem 100 javalis, dois glutões e espaço na mesa para um único javali.
Neste caso, use notify em vez de a solução recomendada, que usa notifyAll. Que problema acontece?

*/


public class MesaVersao2 {

    private List<Javali> javalis = new ArrayList<>();
    public static final int LIMITE = 10;
    private int index=0;






    public MesaVersao2(){

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
        MesaVersao2 mesaVersao2 = new MesaVersao2();

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
    public synchronized  void retira(){
        if (javalis.isEmpty()) {
            System.out.println("Glutao á espera ");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("foi comido um javali " + javalis.get(javalis.size() - 1).getId() +  " ja estao "+ (javalis.size()-1) +" javalis na mesa");
            javalis.remove(javalis.size() -1);
            notifyAll();
        }
    }

    public synchronized  void mete(MesaVersao2.Cozinheiro cozinheiro){
        if(javalis.size()==LIMITE) {
            System.out.println("Cozinheiro á espera " + cozinheiro.getCozinheiroid());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        else {

            Javali javali = new Javali(index);
            javalis.add(javali);
            index++;
            System.out.println("foi criado um javali " + javali.getId() + " pelo cozinheiro " + cozinheiro.getCozinheiroid()+ " ja estao "+ javalis.size() +" javalis na mesa");
            notifyAll();
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

