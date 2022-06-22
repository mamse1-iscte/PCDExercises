package Week5.Exercise2;

public class Ourives extends Thread {
    private Balanca balanca;

    public Ourives(Balanca balanca) {
        this.balanca = balanca;

    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread().toString() + " - inicio do run.");
        while (true) {
            balanca.criarLingote();
        }
    }
    }

