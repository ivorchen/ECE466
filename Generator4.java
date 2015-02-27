import java.io.*; 
import java.util.*; 
import java.net.*;

/* 
 * The file is read line-by-line, values are parsed and assigned to variables,
 * values are  displayed, and then written to a file with name "output.txt"  
 */

class Generator4{  
	public static void main (String[] args) { 
		
		PrintStream pout = null;
		BufferedReader bis = null; 
		String currentLine = null;
		long time_difference = 0;
		long time_difference_in_file = 0;
		long current_time = 0;
		long end_time = 0;
		float[] Ftime_buf = new float[100001];
		int[] Fsize_buf = new int[100001];
		final int max_size=1024;

		try {  
			
			/*
			 * Open input file as a BufferedReader
			 */ 
			File fin = new File("BC-pAug89-small.TL"); 
			FileReader fis = new FileReader(fin); 
			
			bis = new BufferedReader(fis);  
			
			FileOutputStream fout =  new FileOutputStream("Generator4.data");
			pout = new PrintStream (fout);
			
			currentLine = bis.readLine();
			StringTokenizer st = new StringTokenizer(currentLine); 
			String col1 = st.nextToken(); 
			String col2 = st.nextToken(); 
			//String col3 = st.nextToken();
			long Ftime 	= (long)((Float.parseFloat(col1))*1000000);
			System.out.println(col1+" first time is "+Ftime);
			int Fsize 	= Integer.parseInt(col2);
			int SeqNo	=0;
			Ftime_buf[SeqNo] = 0;
			Fsize_buf[SeqNo] = Fsize;
			current_time = (long)Ftime;
			
			pout.println((SeqNo+1) + "\t" + "0" + "\t"+ Fsize);

			SeqNo=SeqNo+1;
			/*
			 *  Read file line-by-line until the end of the file 
			 */
			while ( (currentLine = bis.readLine()) != null) { 
				
				//System.out.println(currentLine);
				st = new StringTokenizer(currentLine); 
				col1 = st.nextToken(); 
				col2 = st.nextToken(); 
				
				/*
				 *  Convert each element to desired data type 
				 */
				Ftime 	= (long)((Float.parseFloat(col1))*1000000);
				Fsize 	= Integer.parseInt(col2);
				//Ftime_buf[SeqNo-1] = Ftime;
				Fsize_buf[SeqNo] = Fsize;
				//start = System.nanoTime();
				//System.out.println(start);
				
				time_difference_in_file = Ftime - current_time;
				pout.println((SeqNo+1) + "\t" + time_difference_in_file + "\t"+ Fsize);
				Ftime_buf[SeqNo] = time_difference_in_file*1000;
				current_time = Ftime;
				SeqNo=SeqNo+1;
			}
			System.out.println("Ready to send "+SeqNo+" packets");
			SeqNo = 1;
			byte[] buf = new byte[2000];
			Arrays.fill(buf,(byte)1);
			InetAddress addr = InetAddress.getByName("localhost");
			System.out.println("Generator using port 4444");
			int fragment=0;
			if(Fsize_buf[0]>max_size){
				fragment=Fsize_buf[0]-max_size;
				Fsize_buf[0]=max_size;
			}
			
			DatagramPacket packet = new DatagramPacket(buf, Fsize_buf[0], addr, 4444);
			DatagramSocket socket = new DatagramSocket();
			//current_time = (long)Ftime_buf[0];
			socket.send(packet);

			if(fragment>0){
				packet.setLength(fragment);
				socket.send(packet);
			}

			long start = System.nanoTime();
			
			while (SeqNo <30001) { 
				
				time_difference = (long)Ftime_buf[SeqNo];
				
				while (true){
					if (time_difference < 1000){
						break;
					}
					end_time = System.nanoTime();
					if (end_time > time_difference + start){
						break;
					}
				}
				
				if(Fsize_buf[SeqNo]>max_size){
					fragment=Fsize_buf[SeqNo]-max_size;
					Fsize_buf[SeqNo]=max_size;
				}

				packet.setLength(Fsize_buf[SeqNo]);
				socket.send(packet);

				if(fragment>0){
				packet.setLength(fragment);
				socket.send(packet);
				}

				start = System.nanoTime();
				SeqNo +=1;
			}
			
			System.out.println("End sending");
			
		} catch (IOException e) {  
			// catch io errors from FileInputStream or readLine()  
			System.out.println("IOException: " + e.getMessage());  
		} finally {  
			// Close files   
			if (bis != null) { 
				try { 
					bis.close(); 
					pout.close();
				} catch (IOException e) { 
					System.out.println("IOException: " +  e.getMessage());  
				} 
			} 
		} 
	}  
}

