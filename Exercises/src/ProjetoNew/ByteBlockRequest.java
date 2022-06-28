package ProjetoNew;


/*
O descarregamento dos dados deve ser feito por blocos
de 100 bytes, e deve ser tão distribuído quanto possível. Assim, inicialmente existirão 10000
blocos a descarregar (1 000 000/100), e deverão consequentemente ser criadas 10000 pedidos
de descarregamento (ByteBlockRequest, ver abaixo).
*/
public class ByteBlockRequest {
    private int startIndex;
    private int  length;

    public ByteBlockRequest(int startIndex ,int  length){
        this.startIndex=startIndex;
        this.length=length;
    }
}
