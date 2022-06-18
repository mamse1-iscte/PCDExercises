package Exercise2.Part3;

import java.util.Observable;

public class Car extends Observable implements Runnable {
	private int id;
	private int limit;
	private int position=0;
	private Track track;
	
	public int getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}

	public Car(int id, int limit, Track track) {
		super();
		this.id = id;
		this.limit = limit;
		this.track=track;
	}


	@Override
	public void run() {
		while(position<limit) {
//pq é um runnable
			double random =  ( Math.random());
			try {
				java.lang.Thread.sleep((long) (random*100));
				track.update(this,track);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			track.moveCar(id, position);
			position++;
			if(position==limit){
				System.out.println("O carro "+id+" chegou ao fim!!!");
			}
		}
	}
}
