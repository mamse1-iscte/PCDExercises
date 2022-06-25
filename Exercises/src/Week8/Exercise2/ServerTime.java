package Week8.Exercise2;


/*

Os computadores ligados à rede podem ter o seu relógio sincronizado com servidores de tempo, para corrigir desvios indesejados nos relógios próprios.
Para tal, podem ser estabelecidas ligações permanentes com os servidores de tempo, que, ciclicamente (p.ex. todos os minutos) enviam mensagens com a hora atual.
Os computadores, que funcionam como clientes, devem inscrever-se junto do servidor de tempo, através de uma ligação a um endereço e porto bem conhecidos.
Neste caso, considere a máquina local e o porto 2424.
Após a  ligação, devem ser criados canais de comunicação de objetos, que devem ser salvaguardados para as comunicações posteriores.
O servidor de tempo enviará uma mensagem da classe TimeMessage (a desenvolver) que conterá um único atributo, do tipo long, com a hora atual em milisegundos.
Após receção desta mensagem, o cliente deve responder com uma mensagem da classe ReceptionConfirmationMessage (a desenvolver).
Caso esta mensagem não seja recebida em tempo útil (p.ex., 2s) pelo servidor de tempo, esse cliente deve ser desinscrito do serviço e não receberá notificações posteriores.

Dica: a comunicação com cada cliente ficará a cargo de uma thread diferente.
Dica: para limitar o tempo de espera nos canais de comunicação, pode usar-se o método Socket.setSoTimeout(int).
Dica: para saber o tempo atual pode usar-se o método currentTimeMillis da classe System.

*/

public class ServerTime {
}
