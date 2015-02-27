import java.io.*; 
import java.util.*; 
import java.net.*;

/* 
 * The file is read line-by-line, values are parsed and assigned to variables,
 * values are  displayed, and then written to a file with name "output.txt"  
 */

class Generator3 {  
	public static void main (String[] args) { 
		
		PrintStream pout = null;
		BufferedReader bis = null; 
		String currentLine = null;
		long time_difference = 0;
		long time_difference_in_file = 0;
		long current_time = 0;
		long end_time = 0;
		float[] Ftime_buf = new float[53997];
		int[] Fsize_buf = new int[53997];
		final int max_size=1024;

		try {  
			
			/*
			 * Open input file as a BufferedReader
			 */ 
			File fin = new File("movietrace.data"); 
			FileReader fis = new FileReader(fin); 
			
			bis = new BufferedReader(fis);  
			
			FileOutputStream fout =  new FileOutputStream("Generator3.data");
			pout = new PrintStream (fout);
			
			currentLine = bis.readLine();
			StringTokenizer st = new StringTokenizer(currentLine); 
			String col1 = st.nextToken(); 
			String col2 = st.nextToken(); 
			String col3  = st.nextToken(); 
			String col4  = st.nextToken();


			int SeqNo 	= Integer.parseInt(col1);
			float Ftime 	= Float.parseFloat(col2);
			int Fsize 	= Integer.parseInt(col4);

			Ftime_buf[SeqNo] = Ftime;
			Fsize_buf[SeqNo] = Fsize;
			//current_time = (long)Ftime;
			
			//pout.println((SeqNo+1) + "\t" + "0" + "\t"+ Fsize);
			
			/*
			 *  Read file line-by-line until the end of the file 
			 */
			while ( (currentLine = bis.readLine()) != null) { 
				
				//System.out.println(currentLine);
				st = new StringTokenizer(currentLine); 
				col1 = st.nextToken(); 
				col2 = st.nextToken(); 
				col3  = st.nextToken(); 
				col4  = st.nextToken(); 
				
				/*
				 *  Convert each element to desired data type 
				 */
				SeqNo 	= Integer.parseInt(col1);
				Ftime 	= Float.parseFloat(col2);
				Fsize 	= Integer.parseInt(col4);
				//Ftime_buf[SeqNo-1] = Ftime;
				Fsize_buf[SeqNo] = Fsize;
				Ftime_buf[SeqNo] = Ftime;
				//start = System.nanoTime();
				//System.out.println(start);
				
				//time_difference_in_file = (long)Ftime - current_time;
				//pout.println((SeqNo+1) + "\t" + time_difference_in_file + "\t"+ Fsize);
				//Ftime_buf[SeqNo] = (time_difference_in_file)*1000;
				//current_time = (long)Ftime;
			}
			int findex=1;
			pout.println(1 + "\t" + 0 + "\t"+ Fsize_buf[0]);

			while(findex<=53996){
				time_difference_in_file=(long)Ftime_buf[findex]-(long)Ftime_buf[findex-1];
				pout.println((findex+1) + "\t" + time_difference_in_file + "\t"+ Fsize_buf[findex]);
				findex=findex+1;
			
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
			

			while(fragment>0){
				if(fragment>max_size){
					packet.setLength(max_size);
					socket.send(packet);
					fragment=fragment-max_size;
				}
				else{
				packet.setLength(fragment);
				socket.send(packet);
				fragment=0;
				}
			}

			long start = System.nanoTime();
			
			while (SeqNo < 30000) { 
				
				time_difference = ((long)Ftime_buf[SeqNo]-(long)Ftime_buf[SeqNo-1])*1000;
				
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
				
				while(fragment>0){
					if(fragment>max_size){
						packet.setLength(max_size);
						socket.send(packet);
						fragment=fragment-max_size;
					}
					else{
					packet.setLength(fragment);
					socket.send(packet);
					fragment=0;
					}
				}

				start = System.nanoTime();
				SeqNo +=1;
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

