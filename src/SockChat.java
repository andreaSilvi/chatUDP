
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SockChat implements Runnable{

	private static int port=9868;//9888;
	private DatagramSocket serverSocket;
	private byte[] receiveData;
    private byte[] sendData;
    private InetAddress IPAddress=null;
	
	public SockChat() throws SocketException{
		serverSocket=new DatagramSocket(port);
		receiveData = new byte[1024];
		sendData=new byte[1024];

	}
	
	public String getIp(){
		if(IPAddress==null)
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
		
		if(IPAddress!=null)
			Send(msg,IPAddress);
		else
			System.out.println("---ATTESA PACCHETTO---");
	}
	
	public void SetIp(InetAddress ip){
		IPAddress=ip;
	}
	
	public void SetIp(){
		System.out.print("inserisci ip: ");
		Scanner i=new Scanner(System.in);
		SetIp(i.nextLine());
	}
	
	public void SetIp(String ip){
		try {
			IPAddress=InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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
		int i=0;
		StringTokenizer s;
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			try {
				
				serverSocket.receive(receivePacket);
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
			if(i==0){
				IPAddress = receivePacket.getAddress();
	            port = receivePacket.getPort();
			}
			       
			String modifiedSentence = new String(receivePacket.getData());
			
			s=new StringTokenizer(modifiedSentence);
			s.nextToken();
			
			if(s.countTokens()==1)
				if(s.nextToken().equals("a-.-a"))
					break;
			else
				System.out.println("\n"+modifiedSentence);
			
			cleanReceive();
			
			i++;
		}
		
	close();
	}
}
