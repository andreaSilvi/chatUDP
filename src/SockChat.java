
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class SockChat implements Runnable{

	private static int port=9888;
	private DatagramSocket serverSocket;
	private byte[] receiveData;
    private byte[] sendData;
    private InetAddress IPAddress=null;
    private boolean flag=true;
    private boolean stopTH=true;
	
	public SockChat() throws SocketException{
		serverSocket=new DatagramSocket(port);
		receiveData = new byte[1024];
		sendData=new byte[1024];

	}
	
	public void StopTh(){flag=false;stopTH=false;}
	
	public void Send(String msg) throws IOException{
		
		if(IPAddress!=null){
			cleanSend();
			flag=false;
			
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			       
			serverSocket.send(sendPacket);
			
			flag=true;
		}
		else{
			System.out.println("---ATTESA PACCHETTO---");
		}
	}
	
	public void Send(String msg, InetAddress ip) throws IOException{
			
			IPAddress=ip;
			Send(msg);
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
		while(true){
			
			if(flag){
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
				System.out.println("\n"+modifiedSentence);
				cleanReceive();
				
				i++;
			}
			if(!stopTH)
				break;
		}		
	}
}
