
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class SockChat implements Runnable{

	private static int port=9888;//TODO 9888
	private String Broadcast="10.0.0.255";
	private DatagramSocket serverSocket;
	private byte[] receiveData;
    private byte[] sendData;
    private Vector<InetAddress> IPAddress=new Vector<InetAddress>();
    private Vector<InetAddress> online=new Vector<InetAddress>();
	
	public SockChat() throws SocketException{
		serverSocket=new DatagramSocket(port);
		receiveData = new byte[1024];
		sendData=new byte[1024];

	}
	
	public void setBroadcast(String ip){Broadcast=ip;}
	
	public String getBroadcast(){return Broadcast;}
	
	public String getIp(){
		if(IPAddress.size()==0)
			return "null";
		else
			return IPAddress.toString();
		}
	
	public void StopTh(){
		try {
			Send("CONSOLE: a-.-a",InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void Send(String msg) throws IOException{
		
		if(IPAddress.size()!=0){
			
			for(int i=0;i<IPAddress.size();i++)
				Send(msg,IPAddress.get(i));
		}
		else
			System.out.println("---ATTESA PACCHETTO---");
	}
	
	public void AddIp(InetAddress ip){
		IPAddress.add(ip);
	}
	
	public String ControlloIp(String ip){
		String ret=ip;
		StringTokenizer s=new StringTokenizer(ip,".");
		if(s.countTokens()==4){
			int[] v=new int[4];
			for(int i=0;i<4;i++)
				v[i]=Integer.parseInt(s.nextToken());
			
			for(int i=0;i<4 || ret.equals("null");i++){
				if(v[i]<0 && v[i]>255)
					ret="null";
			}
		}
		else{
			ret="null";
			System.out.println("ERRORE: L'indirizzo inserito non è un ip\n");
		}
			
		return ret;
	}
	
	public void sendRequest(){
		try {
			Send("CONSOLE: -ping-", InetAddress.getByName(Broadcast));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void AddIp(String ip){
		try {
			String app;
			if(!(app=ControlloIp(ip)).equals("null"))
				AddIp(InetAddress.getByName(app));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void SetIp(InetAddress ip){
		IPAddress.removeAllElements();
		AddIp(ip);
	}
	
	public void SetIp(){
		System.out.print("inserisci ip: ");
		Scanner i=new Scanner(System.in);
		SetIp(i.nextLine());
	}
	
	public void SetIp(String ip){
			IPAddress.removeAllElements();
			AddIp(ip);
	}
	
	public void Send(String msg, InetAddress ip){
			cleanSend();
			
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
			       
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	
	public void close(){
		serverSocket.close();
	}
	
	private void cleanSend(){
		Arrays.fill( sendData, (byte)0 );
	}
	
	private void cleanReceive(){
		Arrays.fill( receiveData, (byte)0 );
	}
	
	@Override
	public void run() {
		StringTokenizer s;
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			try {
				
				serverSocket.receive(receivePacket);
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
			if(IPAddress.size()==0){
				IPAddress.add(receivePacket.getAddress());
	            port = receivePacket.getPort();
			}
			       
			String modifiedSentence = new String(receivePacket.getData());
			
			s=new StringTokenizer(modifiedSentence);
			s.nextToken();
			
			//controlli sui messaggi
			if(s.countTokens()==1){
				String cmd=s.nextToken();
				if(cmd.equals("a-.-a"))
					break;
				if(cmd.equals("-ping-"))
					sendAnswers(receivePacket.getAddress());
				if(cmd.equals("-myip-"))
					online.add(receivePacket.getAddress());
			}
			//fine controlli
			else
				System.out.println("\n"+modifiedSentence);
			
			cleanReceive();
			
		}
		
	close();
	}
	
	public String getOnline(){
		return online.toString();
	}

	private void sendAnswers(InetAddress address) {
		Send("CONSOLE: -myip-",address);		
	}
}
