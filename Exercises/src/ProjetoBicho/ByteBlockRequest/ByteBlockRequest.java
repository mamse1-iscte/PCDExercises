package ProjetoBicho.ByteBlockRequest;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ByteBlockRequest implements Serializable {
	
	private static final long serialVersionUID = -2098332974875907560L;
	
		public int StartIndex;
		public int length;
		
		public static final int STORED_DATA_LENGTH= 1000000;
		
	//	private Lock lock= new ReentrantLock();
//		private Condition Descarregar= lock.newCondition();
		
		 public ByteBlockRequest (int StartIndex, int length) {
			 
//			 	if(StartIndex <= 0 || length <= 0 || StartIndex > STORED_DATA_LENGTH || StartIndex + length > STORED_DATA_LENGTH) {
//			 		throw new IllegalArgumentException("Os valores s�o inteiros positos at� 1 000 000 bits");
//			 	}
			 	
			 	this.StartIndex = StartIndex;
			 	this.length = length;
		 }

		@Override
		public String toString() {
			return "ByteBlockRequest [StartIndex=" + StartIndex + ", length=" + length + "]";
		}
		 
		 
}
