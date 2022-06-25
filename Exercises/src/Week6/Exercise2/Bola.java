package Week6.Exercise2;

import java.awt.Color;
import java.util.Observable;

//incremento aleatorio 0.01 a 0.1
//sleep 100ms
// Bola.bolaAtingiuLimite para o fim
//setChanged e notifyObservers
public class Bola extends Observable implements DrawableBall, Runnable {
	private float estado=0;
	private Color color=new Color((int)(Math.random()*256), 
			(int)(Math.random()*256), (int)(Math.random()*256));


	@Override
	public void run() {
		// TODO
		while(true){
			double random =  ( Math.random() * 4 + 1);
			try {
				Thread.sleep((long) random*50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.estado=estado+(float)0.01;
		this.setChanged();
		this.notifyObservers();
		if(bolaAtingiuLimite()){
			estado=1;
				return;
			}
	}}

	public boolean bolaAtingiuLimite(){
		return estado>=1;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public float getX() {
		return estado;
	}

	@Override
	public int getSize() {
		return 10;
	}

}
