// riga di prova per pull a Andrea Silvi
// vedi ....
import java.io.IOException;
import java.net.InetAddress;

import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MainUDP {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Scanner in=new Scanner(System.in);
		SockChat soc=new SockChat();
		Thread receive=new Thread(soc);
		
		System.out.print("ip: ");
		String ip=in.nextLine();
		
		if(!ip.equals("")){
			String app;
			if(!(app=soc.ControlloIp(ip)).equals("null")){
				soc.SetIp(app);
				ip=app;
			}
		}
		
		System.out.print("nome: ");
		String nome=in.nextLine();
		
		receive.start();
		
		String msg;

		while(true){
			
			System.out.print("io: ");
			msg=nome.toUpperCase()+": "+in.nextLine();
			
			//controllo comadi
			
			String app;
			StringTokenizer s=new StringTokenizer(msg);
			s.nextToken();
			if(s.countTokens()>=1){
				app=s.nextToken();
				if(app.charAt(0)=='-'){
					
					if(app.equals("-help"))
						help();
					if(app.equals("-aIp")){
						if(s.countTokens()==1)
							soc.AddIp(s.nextToken());
						else{
							System.out.print("inserisci l'ip da inserire: ");
							soc.AddIp(in.nextLine());
						}
					}
					if(app.equals("-gIp")){
						System.out.println("Ip corrente: "+soc.getIp());
					}
					if(app.equals("-sIp")){
						if(s.countTokens()==1)
							soc.SetIp(s.nextToken());
						else
							soc.SetIp();
						System.out.println("Ip corrente: "+soc.getIp());
					}
					if(app.equals("-flmsg")){
						String app2;
						if(s.countTokens()==1){
							app2=s.nextToken();
							System.out.print("messaggio a "+app2+": ");
						}
						else{
							System.out.print("indirizzo ip: ");
							app2=in.nextLine();
							System.out.print("messaggio a "+app2+": ");
						}
						soc.Send(nome.toUpperCase()+": "+in.nextLine(), InetAddress.getByName(app2));
					}
					if(app.equals("-close"))
						break;
					}
				
				else	
					soc.Send(msg);
			}
			
			
		//fine controllo
		}
		in.close();
		soc.StopTh();
		receive.stop();
		
	}
	
	private static void help(){
		System.out.println(
				"Comandi per la chat:\n" +
				"	-aIp	aggiungere un indirizzo ip a cui\n" +
				"			inviare i messaggi\n\n" +
				"	-sIp	rimuove tutti gli indirizzi aggiunti\n" +
				"			e ne aggiunge solo 1\n\n" +
				"	-gIp	visualizza gli indirizzi a cui si sta\n" +
				"			inviando\n\n" +
				"	-flmsg	FlashMessage: consente di inviare un\n" +
				"			messaggio veloce a un altro indirizzo ip\n\n" +
				"	-close	ferma la ricezione e chiude il socket\n\n");
	}

}
