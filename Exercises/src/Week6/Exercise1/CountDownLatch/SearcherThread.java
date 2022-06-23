package Week6.Exercise1.CountDownLatch;

import java.util.concurrent.CountDownLatch;


class SearcherThread extends Thread{
		private String myText;
		private String textToFind;
		private int result=-1;

	private CountDownLatch cdl;

	public SearcherThread(String myText, String textToFind,
						  CountDownLatch cdl) {
			this.myText = myText;
			this.textToFind = textToFind;
			this.cdl = cdl;
		}

		public String getMyText() {
			return myText;
		}

		public int getResult() {
			return result;
		}

		@Override
		public void run() {
			result=myText.indexOf(textToFind);
				cdl.countDown();
				System.err.println("Thread finishing at:"+System.currentTimeMillis());

		}
		
		
	}

