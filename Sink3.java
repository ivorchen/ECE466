import java.io.*; 
import java.util.*; 
import java.net.*;

class Sink3 {  
	public static void main (String[] args) { 
		
		PrintStream pout = null;
		int seq_num = 0;
		long time_difference = 0;
		long current_time = 0;
		long[] time_diff_buf = new long[30000];
		int[] length_buf = new int[30000];
		
		try {  
			
			DatagramSocket socket = new DatagramSocket(4445);
			System.out.println("Sink using port 4445");
			/*
			 *  Read file line-by-line until the end of the file 
			 */
			byte[] buf = new byte[4000];
			DatagramPacket p = new DatagramPacket(buf, buf.length); 
			socket.receive(p);
			long start = System.nanoTime();
			length_buf[seq_num] = p.getLength();
			time_diff_buf[seq_num] = 0;
			seq_num += 1;
			while (seq_num < 30000) { 
				 
				socket.receive(p);
				current_time = System.nanoTime();
				//time_difference = (current_time - start)/1000;
				length_buf[seq_num] = p.getLength();
				time_diff_buf[seq_num] = current_time - start;
				start = current_time;
				seq_num += 1;
			}
			
			FileOutputStream fout =  new FileOutputStream("Sink3.data");
			pout = new PrintStream (fout);
			seq_num = 0;
			
			while (seq_num < 30000){
				pout.println((seq_num+1) + "\t" + time_diff_buf[seq_num]/1000 + "\t"+ length_buf[seq_num]);
				seq_num +=1;
			}
			/*
			long[] readtime=readfile();
			int tmp=0;
			long max_diff=0;
			long diff=0;
			long total_var=0;
			double var=0;
			int max_diff_index=0;
			while (tmp<seq_num){
				if(readtime[tmp]<(time_diff_buf[tmp]/1000))
				 	diff= (time_diff_buf[tmp]/1000)-readtime[tmp];
				else
					diff= readtime[tmp]-(time_diff_buf[tmp]/1000);
				if(max_diff<diff){
					max_diff=diff;
					max_diff_index=tmp+1;
					}
				total_var=total_var+diff;
				tmp=tmp+1;

			}
			var=((double)total_var)/tmp;
			System.out.println("The max diff is "+max_diff+"index is "+max_diff_index);
			System.out.println("The var is "+var);
			*/

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


	static long[] readfile(){
		BufferedReader bis = null;
		 
		StringTokenizer st=null;
		String currentLine = null;
		String col1 =null;//= st.nextToken(); 
		String col2 = null;//st.nextToken(); 
		String col3  = null;//st.nextToken(); 
		int SeqNo;
		long Ftime;
		long[] Ftime_buf= new long[30000];
		try{
		File fin = new File("Generator.txt");
		FileReader fis = new FileReader(fin); 
		bis = new BufferedReader(fis);
		while ( (currentLine = bis.readLine()) != null) { 
				st = new StringTokenizer(currentLine); 
				col1 = st.nextToken(); 
				col2 = st.nextToken(); 
				col3  = st.nextToken(); 
				
				/*
				 *  Convert each element to desired data type 
				 */
				SeqNo 	= Integer.parseInt(col1);
				Ftime 	= (long)Integer.parseInt(col2);
				
				Ftime_buf[SeqNo-1] = Ftime;
		}
		}
		catch (IOException e) {  
			System.out.println("IOException: " + e.getMessage());
		}
		return Ftime_buf;
	}

}
