package Week5.Exercise2;

public class Escavadora extends Thread {
    private double kgOuroRecolhido = 0;
    private Balanca balanca;
    public static final double MAX = 12.5;


    public Escavadora(Balanca balanca) {
        this.balanca = balanca;
    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread().toString() + " - inicio do run.");
        while (balanca.getPesoTotalOuro() < MAX) {

            balanca.escavar();
            try {
                sleep(500);
            } catch (InterruptedException e) {
                System.out.println("parou escavadora!");
                return;
            }
        }
    }
}
