package Week6.Exercise1.Barrier;

import java.util.concurrent.CyclicBarrier;


/*

Adapte a solução para a procura distribuída de texto abordada na teórica (em anexo) para usar um CountDownLatch em vez de uma Barreira.
Relembrando: pretende-se procurar um pequeno texto em N documentos, sendo a procura em cada documento feita por uma thread dedicada.
Para coordenar a contagem dos resultados, é usada uma barreira. Esta utilização da Barreira pode, porém, e com vantagem, ser substituída por um CountDownLatch.
Compare o desempenho e o tempo de terminação das threads entre as duas versões.
No código anexado, cada documento é simplesmente uma sequência aleatória de carateres, e a palavra a procurar está definida numa constante.
O número de documentos a considerar também está definido numa constante.

*/


public class MainBarreira {
	static final int NUM_DOCUMENTS_TO_BE_SEARCHED=1000;
	static final int STRING_LENGTH=1024;
	static final String STRING_TO_BE_FOUND="huik";
	
	public static void main(String[] args) {
		final long initTime=System.currentTimeMillis();
		SearcherThread[] threads=new SearcherThread[NUM_DOCUMENTS_TO_BE_SEARCHED];
		CyclicBarrier barrier= new CyclicBarrier(NUM_DOCUMENTS_TO_BE_SEARCHED,new Runnable() {
			@Override
			public void run() {
				int count=0;
				for(SearcherThread t:threads)
					if(t.getResult()!=-1){
						System.out.println("Found at "+t.getResult());//+" in "+t.getMyText());
						count++;
					}
				System.out.println("Search DONE. Found:"+count+" Time:"+
					(System.currentTimeMillis()-initTime));

			}
		});
		RandomString rs=new RandomString(STRING_LENGTH);
		for(int i=0; i!=NUM_DOCUMENTS_TO_BE_SEARCHED;i++){
			threads[i]=new SearcherThread(rs.nextString(), 
					STRING_TO_BE_FOUND, barrier);
			threads[i].start();
		}
	}
}
