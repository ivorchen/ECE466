import java.io.*; 
//import java.util.*; 
import java.net.*;

class TrafficSink {  
	public static void main (String[] args) { 
		
		PrintStream pout = null;
		int seq_num = 0;
		long time_difference = 0;
		long current_time = 0;
		try {  
			
			
			/*
			 * Open file for output 
			 */
			FileOutputStream fout =  new FileOutputStream("output2.txt");
			pout = new PrintStream (fout);
			DatagramSocket socket = new DatagramSocket(4444);
			/*
			 *  Read file line-by-line until the end of the file 
			 */
			byte[] buf = new byte[3000];
			DatagramPacket p = new DatagramPacket(buf, buf.length); 
			socket.receive(p);
			seq_num += 1;
			long start = System.nanoTime();
			pout.println(seq_num + "\t" + "0" + "\t"+ p.getLength());
			
			while (true) { 
				 
				socket.receive(p);
				current_time = System.nanoTime();
				time_difference = (current_time - start)/1000;
				
				start = current_time;
				seq_num += 1;
				pout.println(seq_num + "\t" + time_difference + "\t"+ p.getLength()); 
				
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