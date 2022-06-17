package Exercise2;

import java.util.Observable;

public class Car extends Observable {
	private int id;
	private int limit;
	private int position=0;
	
	public int getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}

	public Car(int id, int limit) {
		super();
		this.id = id;
		this.limit = limit;
	}
	
	
}
