package ProjetoBicho;

import java.util.Arrays;
import java.util.Comparator;
import ProjetoBicho.CloudByte.CloudByte;



public class Etiqueta implements Comparator<Etiqueta>{
	
			private CloudByte[] data;
			private int etiqueta;
			
			public Etiqueta(CloudByte[] data, int etiqueta) {
				this.data = data;
			 	this.etiqueta = etiqueta;
			}
			
			public CloudByte[] getData() {
				return data;
			}

			public void setData(CloudByte[] data) {
				this.data = data;
			}	
			
			public int getEtiqueta() {
				return etiqueta;
			}

			public void setEtiqueta(int etiqueta) {
				this.etiqueta = etiqueta;
			}

			public int compareTo(Etiqueta info) {
				if(this.etiqueta > info.etiqueta){
					return 1;
				}
				else if(this.etiqueta < info.etiqueta){
					return -1;
				}
					return 0;

			}

			@Override
			public String toString() {
				return "Etiqueta [data=" + Arrays.toString(data) + ", etiqueta=" + etiqueta + "]";
			}

			@Override
			public int compare(Etiqueta o1, Etiqueta o2) {
				if(o1.etiqueta > o2.etiqueta){
					return 1;
				}
				else if(o1.etiqueta < o2.etiqueta){
					return -1;
				}
					return 0;
			}
	}
