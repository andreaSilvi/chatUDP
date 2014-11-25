
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
				
		if(!ip.equals("")){
			//if ControlloIp ip
				soc.SetIp(ip);
		}
			
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
