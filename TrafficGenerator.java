import java.io.*; 
import java.util.*; 
import java.net.*;

class TrafficGenerator {  
	public static void main (String[] args) { 
		
		//PrintStream pout = null;
		BufferedReader bis = null; 
		String currentLine = null;
		long time_difference = 0;
		//long time_difference_in_file = 0;
		long current_time = 0;
		long end_time = 0;

		try {  
			
			/*
			 * Open input file as a BufferedReader
			 */ 
			File fin = new File("poisson33.data"); 
			FileReader fis = new FileReader(fin);  
			bis = new BufferedReader(fis);  
			
			//FileOutputStream fout =  new FileOutputStream("output_cmp.txt");//
			//pout = new PrintStream (fout);//
			
			currentLine = bis.readLine();
			StringTokenizer st = new StringTokenizer(currentLine); 
			String col1 = st.nextToken(); 
			String col2 = st.nextToken(); 
			String col3  = st.nextToken(); 
			
			int SeqNo 	= Integer.parseInt(col1);
			float Ftime 	= Float.parseFloat(col2);
			int Fsize 	= Integer.parseInt(col3);

			byte[] buf = new byte[2000];
			Arrays.fill(buf,(byte)1);
			InetAddress addr = InetAddress.getByName("localhost");
			DatagramPacket packet = new DatagramPacket(buf, Fsize, addr, 4444);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			long start = System.nanoTime();
			//current_time = (long)Ftime;
			
			//pout.println(SeqNo + "\t" + "0" + "\t"+ Fsize);//
			
			/*
			 *  Read file line-by-line until the end of the file 
			 */
			while ( (currentLine = bis.readLine()) != null) { 
				
				/*
				 *  Parse line and break up into elements 
				 */
				//System.out.println(SeqNo);
				//start = System.nanoTime();
				//System.out.println(start);
				
				st = new StringTokenizer(currentLine); 
				col1 = st.nextToken(); 
				col2 = st.nextToken(); 
				col3  = st.nextToken(); 
				
				/*
				 *  Convert each element to desired data type 
				 */
				SeqNo 	= Integer.parseInt(col1);
				Ftime 	= Float.parseFloat(col2);
				Fsize 	= Integer.parseInt(col3);
				
				//start = System.nanoTime();
				//System.out.println(start);
				
				time_difference = ((long)Ftime - current_time - 40)*1000;
				//time_difference_in_file = (long)Ftime - current_time;//
				//pout.println(SeqNo + "\t" + time_difference_in_file + "\t"+ Fsize);//
				while (true){
					if (time_difference < 20000){
						break;
					}
					end_time = System.nanoTime();
					if (end_time > time_difference + start){
						break;
					}
				}
				
	
				packet.setLength(Fsize);
				socket.send(packet);
				start = System.nanoTime();
				//System.out.println(start);
				current_time = (long)Ftime;
			}
			
			
		} catch (IOException e) {  
			// catch io errors from FileInputStream or readLine()  
			System.out.println("IOException: " + e.getMessage());  
		} finally {  
			// Close files   
			if (bis != null) { 
				try { 
					bis.close(); 
					//pout.close();//
				} catch (IOException e) { 
					System.out.println("IOException: " +  e.getMessage());  
				} 
			} 
		} 
	}  
}