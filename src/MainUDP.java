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
		
		System.out.print("nome: ");
		String nome=in.nextLine();
		
		receive.start();
		
		String msg;
		
		System.out.print("io: ");
		msg=nome.toUpperCase()+": "+in.nextLine();
		
		if(!ip.equals("")){
			//if(ControlloIp(ip)){
				soc.Send(msg, InetAddress.getByName(ip));
			/*}
			else
				soc.Send(msg);*/
		}
		else
			soc.Send(msg);
			
			
		while(true){
			
			System.out.print("io: ");
			msg=nome.toUpperCase()+": "+in.nextLine();
			
			if(msg.equals(nome.toUpperCase()+": "+"<z"))
				break;

			soc.Send(msg);
		}
		
		soc.StopTh();
		soc.close();
		in.close();
	}

	private static boolean ControlloIp(String ip){
		
		boolean ret=false;
		int app;
		StringTokenizer s=new StringTokenizer(ip,".");
		
		if(s.countTokens()==4)
			ret=true;
		else
			ret=false;
		
		for(int i=0;i<4;i++){
			app=Integer.parseInt(s.nextToken());
			if(app>0 && app<=255 && ret==true)
				ret=true;
			else
				ret=false;
		}
		
		return ret;
	}
}
