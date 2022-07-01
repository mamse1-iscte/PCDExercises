package ProjetoBicho;

public class CountDown {
	private int count ;
	
	public CountDown ( int count ) {
		this.count = count ;
	}
	
	public int getCount() {
		return count;
	}
	
	public synchronized void await () throws InterruptedException {
		while (count >0) {
			wait ();
		}
	}

	public synchronized void countDown (){
		count--;
		if(count == 0) {
			notifyAll ();
		}
	}
}
