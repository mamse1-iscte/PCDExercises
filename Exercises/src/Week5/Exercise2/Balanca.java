package Week5.Exercise2;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Balanca {

    private Lock lock =new ReentrantLock();
    private Condition lingote = lock.newCondition();

    private double pesoTotalOuro = 0;
    private JTextField texto;
    public static final double MAX = 12.5;

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


    public  void escavar() {
        try {

        lock.lock();
        double kgOuroRecolhido = Math.random();
        adicionarPesoOuro(kgOuroRecolhido);
        System.out.println("KG recolhidos " + kgOuroRecolhido);
        System.out.println("Na balanca está " + getPesoTotalOuro());

        if(getPesoTotalOuro()>12.5) {
            lingote.signalAll();
        }
    }            finally {
            lock.unlock ();
        }
    }


    public  void criarLingote() {


            if (getPesoTotalOuro() < MAX) {
                try {
                    lock.lock();
                    lingote.await();
                } catch (InterruptedException e) {
                    System.out.println("parou ourives!");
                    return;
                }
                finally {
                    lock.unlock ();
                }
            } else {
                adicionarPesoOuro(-12.5);
                System.out.println("lingote criado!");
            }
    }



    }
