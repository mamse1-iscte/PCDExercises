package Week3.Exercise3;

/*
Conta bancária
Crie uma classe ContaBancaria e um método depositar.
class Account {
    void deposit(int amount); // put money into the account

    int getBalance(); // get the current balance of the account
}
Crie 10 Threads Cliente, cada um vai fazer depósitos contínuos de um valor aleatório (por exemplo entre 0 e 100€) até ser interrompido. Cada cliente guarda o total que ele próprio depositou.
O main, fará o seguinte:
Lança os 10 clientes
dorme 10 segundos,
interrompe todos os clientes,
espera que terminem (usando o join()),
imprime o saldo da conta
soma o total depositado por todos os clientes para confirmação.
Experimente com e sem sincronização do metodo deposit().
Dica: para detetar se uma Thread foi interrompida, pode ser usado o método interrupted().
*/


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Account {
    List threads= new ArrayList<Thread>();

    public Account() throws InterruptedException {
        for(int i=0;i<10;i++){
            Cliente c0= new Cliente(i,0);
            c0.start();
            threads.add(c0);
   }
        java.lang.Thread.sleep(10000);
        for(Object x :threads){

           // x.join();
           // System.out.println(c0.depositado);

        }
        }


    void deposit(int amount) { // put money into the account
    }



    int getBalance() { // get the current balance of the account
 return 1;
    }


    public static void main(String[] args) throws InterruptedException {
        Account c= new Account();




    }




    public class Cliente extends  Thread{
        private int id;
        private int depositado=0;
        public Cliente(int id, int depositado){
            this.id= id;
            this.depositado=depositado;
        }

        @Override
        public void run() {
            Random r = new Random();
            int low = 0;
            int high = 100;
            int deposit = r.nextInt(high-low) + low;
            depositado=depositado+deposit;
        }
    }
}
