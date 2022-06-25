package ProjetoOld;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ByteBlockRequest implements Serializable {

	public int startIndex;
	public int length;
	
	public ByteBlockRequest(int startIndex, int length) {
		this.startIndex = startIndex;
		this.length = length;
	}
	
	
}
