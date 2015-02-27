import java.io.*; 
import java.util.*; 
import java.net.*;

/* 
 * The file is read line-by-line, values are parsed and assigned to variables,
 * values are  displayed, and then written to a file with name "output.txt"  
 */

class Generator2 {  
	public static void main (String[] args) { 
		
		PrintStream pout = null;
		BufferedReader bis = null; 
		String currentLine = null;
		long time_difference = 0;
		long time_difference_in_file = 0;
		long current_time = 0;
		long end_time = 0;
		int dp =10000;
		float[] Ftime_buf = new float[dp];
		int[] Fsize_buf = new int[dp];
		final int max_size=1024;

		try {  
			
			/*
			 * Open input file as a BufferedReader
			 */ 
			File fin = new File("poisson3.data"); 
			FileReader fis = new FileReader(fin); 
			
			bis = new BufferedReader(fis);  
			
			FileOutputStream fout =  new FileOutputStream("Generator2.data");
			pout = new PrintStream (fout);
			
			currentLine = bis.readLine();
			StringTokenizer st = new StringTokenizer(currentLine); 
			String col1 = st.nextToken(); 
			String col2 = st.nextToken(); 
			String col3  = st.nextToken(); 
			
			int SeqNo 	= Integer.parseInt(col1);
			float Ftime 	= Float.parseFloat(col2);
			int Fsize 	= Integer.parseInt(col3);

			Ftime_buf[SeqNo-1] = 0;
			Fsize_buf[SeqNo-1] = Fsize;
			current_time = (long)Ftime;
			
			pout.println(SeqNo + "\t" + "0" + "\t"+ Fsize);
			
			/*
			 *  Read file line-by-line until the end of the file 
			 */
			while ( (currentLine = bis.readLine()) != null) { 
				
				//System.out.println(currentLine);
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
				//Ftime_buf[SeqNo-1] = Ftime;
				Fsize_buf[SeqNo-1] = Fsize;
				//start = System.nanoTime();
				//System.out.println(start);
				
				time_difference_in_file = (long)Ftime - current_time;
				pout.println(SeqNo + "\t" + time_difference_in_file + "\t"+ Fsize);
				Ftime_buf[SeqNo-1] = (time_difference_in_file-7)*1000;
				current_time = (long)Ftime;
			}
			
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
			
			while (SeqNo < dp) { 
				
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

			for(int i=0;i<5;i++){
				packet.setLength(0);
				socket.send(packet);

			}
			
			
			
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

