import java.io.*; 
import java.util.*; 
import java.net.*;

/* 
 * The file is read line-by-line, values are parsed and assigned to variables,
 * values are  displayed, and then written to a file with name "output.txt"  
 */

class Generator{  
	public static void main (String[] args) { 
		//handle input arguments
		if(args.length!=3){
		System.out.println("Invalid number of arguments! Three input arguments needed");
		System.exit(1);
		}
		
		final int Tinterval		= Integer.parseInt(args[0]);
		final int NPackets		= Integer.parseInt(args[1]);
		final int Lbytes		= Integer.parseInt(args[2]);

		if(Tinterval>0&&NPackets>0&&Lbytes>0){
		if(Tinterval<20){
		System.out.println("Time interval is too small! Use a number >=20");
		System.exit(1);
		}
		System.out.println("Every "+Tinterval+" usec transmit a group of "
					+NPackets+" packets back-to-back, each with a size of "
					+Lbytes+" bytes");
		}
		else{
		System.out.println("Invalid arguments! Arguments must be greater than 0");
		System.exit(1);
		}

                
		PrintStream pout = null;
		BufferedReader bis = null; 
		String currentLine = null;
		
		int dp=10000;
		long time_difference = 0;
		long time_difference_in_file = 0;
		long current_time = 0;
		long end_time = 0;
		
		long[] Ftime_buf = new long[dp];
		int[]  Fsize_buf = new int[dp];
		final int max_size=1024;

		try {  			
			FileOutputStream fout =  new FileOutputStream("Generator.data");
			pout = new PrintStream (fout);
			
			byte[] buf = new byte[2000];
			Arrays.fill(buf,(byte)1);
			InetAddress addr = InetAddress.getByName("localhost");
			System.out.println("Generator using port 4444");
						
			DatagramPacket packet = new DatagramPacket(buf, Lbytes, addr, 4444);
			DatagramSocket socket = new DatagramSocket();
			
			long cur=0;
			long prev=0;
			long diff;
			Ftime_buf[0]=0;
			Fsize_buf[0]=Lbytes;
			prev=System.nanoTime();
			int i;
			for(i=1;i<NPackets&&i<dp;i++){
			cur=System.nanoTime();
			Ftime_buf[i]=cur-prev;
			Fsize_buf[i]=Lbytes;
			socket.send(packet);
			prev=System.nanoTime();
			}
			
			//sleep Tinterval msec
			Generator.timer(Tinterval);

			while(i<dp){
			for(int inner=0;inner<NPackets&&i<dp;inner++,i++){
			cur=System.nanoTime();
			Ftime_buf[i]=cur-prev;
			Fsize_buf[i]=Lbytes;
			socket.send(packet);
			prev=System.nanoTime();
			}
			Generator.timer(Tinterval);
			}
			
			for(int j=0;j<5;j++){
			packet.setLength(0);
			socket.send(packet);
			}

			System.out.println("End sending");
			System.out.println("Saving data");

			pout.println("SeqNo\tTime interval(in usec)\tSize(in Bytes)");
			int seq_num=0;
			while (seq_num < dp){
				pout.println((seq_num+1) + "\t\t" + Ftime_buf[seq_num]/1000 + "\t\t"+ Fsize_buf[seq_num]);
				seq_num +=1;
			}
			socket.close();
			
		} catch (Exception e) {  
			e.printStackTrace();
		} finally {  
			// Close files   
					pout.close();
			} 
		}

		private static void timer(int interval){
		long end_time,start;
		long diff=(long)(interval*1000);
		start = System.nanoTime();
			while (true){
					end_time = System.nanoTime();
					if (end_time > diff + start){
						break;
					}
				}
		return;
		}
	}  

