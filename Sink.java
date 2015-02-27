import java.io.*; 
import java.util.*; 
import java.net.*;

class Sink {  
	public static void main (String[] args) { 
		
		PrintStream pout = null;
		int seq_num = 0;
		long time_difference = 0;
		long current_time = 0;

		int dp=10000;
		long[] Ftime_buf = new long[dp];
		int[] Fsize_buf = new int[dp];
		
		try {  
			
			DatagramSocket socket = new DatagramSocket(4445);
			System.out.println("Sink using port 4445");
			
			byte[] buf = new byte[2000];
			DatagramPacket p = new DatagramPacket(buf, buf.length); 
			socket.receive(p);
			long start = System.nanoTime();
			Fsize_buf[seq_num] = p.getLength();
			Ftime_buf[seq_num] = 0;
			seq_num += 1;
			while (seq_num < dp) { 
				socket.receive(p);
				current_time = System.nanoTime();
				if(p.getLength()==0)break;
				Fsize_buf[seq_num] = p.getLength();
				Ftime_buf[seq_num] = current_time - start;
				start = current_time;
				seq_num += 1;
			}
			
			FileOutputStream fout =  new FileOutputStream("Sink.data");
			pout = new PrintStream (fout);
			int end=seq_num;
			seq_num = 0;
			pout.println("SeqNo\tTime interval(in usec)\tSize(in Bytes)");
			while (seq_num <= end){
				pout.println((seq_num+1) + "\t\t" + Ftime_buf[seq_num]/1000 + "\t\t"+ Fsize_buf[seq_num]);
				seq_num +=1;
			}
			
		} catch (IOException e) {  
			// catch io errors from FileInputStream or readLine()  
			System.out.println("IOException: " + e.getMessage());  
		} finally {  
			// Close files   
			if (pout != null) { 
				//bis.close(); 
				pout.close(); 
			} 
		} 
	}


	
}
