package Week4.Exercise2;

import javax.swing.*;

public class Balanca {
    private double pesoTotalOuro = 0;
    private JTextField texto;
    private final double MAX = 12.5;

    public Balanca(JTextField texto) {
        this.texto = texto;

    }


    public double getPesoTotalOuro() {
        return pesoTotalOuro;
    }

    public JTextField getTexto() {
        return texto;
    }

    public void setTexto(JTextField texto) {
        this.texto = texto;
    }

    public void adicionarPesoOuro(double pesoOuro) {
        this.pesoTotalOuro = pesoTotalOuro + pesoOuro;
        texto.setText(Double.toString(pesoTotalOuro));


    }


    public synchronized void escavar() {
        double kgOuroRecolhido = Math.random();
        adicionarPesoOuro(kgOuroRecolhido);
        System.out.println("KG recolhidos " + kgOuroRecolhido);
        System.out.println("Na balanca está " + getPesoTotalOuro());
        notifyAll();

    }


    public synchronized void criarLingote() {


            if (getPesoTotalOuro() < MAX) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("parou ourives!");
                    return;
                }
            } else {
                adicionarPesoOuro(-12.5);
                System.out.println("lingote criado!");
                notifyAll();
            }
    }



    }
