package ProjetoBicho;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListaSincronizada {
	
	List<Object> lista = new ArrayList<>();
	
	private Lock lock = new ReentrantLock();
	
	public ListaSincronizada(List<Object> lista) {
		this.lista = lista;
	}
	
	public Object retirar() throws InterruptedException {
		lock.lock();
		try{
			if(lista.isEmpty()) {
				return null;
			}
			return lista.remove(0);
		} finally {
			lock.unlock();
		}
	}
	
	public void coloca(Object objeto) throws InterruptedException {
		lock.lock();
		try{
			lista.add(objeto);
		} finally {
			lock.unlock();
		}
	}
}
