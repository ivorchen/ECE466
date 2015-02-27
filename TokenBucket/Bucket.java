package TokenBucket;

/**
 * Token generator and bucket to store generated tokens.
 * For efficiency Bucket calculates number of tokens only when needed instead of
 * continually generating tokens and holding them in bucket.
 */
public class Bucket implements Runnable
{
	// bucket size (in tokens)
	private int size;
	// time between generation of tokens (nanoseconds)
	private long tokenInterval;
	// last time number of tokens was updated
	private long lastTime;
	// number of tokens in bucket (at last update)
	// NOTE: this variable should never be used without previous call to updatNoTokens()
	private int noTokens;
	// number of data points
	private final int dp=10000;
	// array to store the elapsed time
	//long[] Ftime_buf=new long[dp];
	// array to store data for X
	long[] X_buf=new long[dp];
	// array to store data for L
	long[] L_buf=new long[dp];
	// current data point
	int index=1;
	
	/**
	 * Constructor.
	 * @param size Size of bucket in bytes.
	 * @param rate Token generating rate (tokens/sec).
	 */
	public Bucket(int size, int rate)
	{
		this.size = size;
		this.tokenInterval = 1000000000l/rate;
		this.noTokens = size;
	}
	
	/**
	 * Start generating tokens.
	 */
	public void run()
	{
		// when started there are size tokens, and starting time is last update time for
		// this number of tokens
		lastTime = System.nanoTime();
		//Ftime_buf[0]=0;
		//System.out.println(0+"\t"+noTokens+"\t"+Ftime_buf[0]);
	}
	
	/**
	 * Update number of tokens in the bucket.
	 * Using last update time this method calculates number of tokens that would have been generated 
	 * from last update until present time and adds it to number of tokens in buffer.
	 * Number of tokens in bucket doesn't exceed bucket size (excess tokens are discarded).
	 */
	private void updateNoTokens()
	{
		// Currently this code segment is empty. 
		//
		// In Lab 2B, you  add the code that performs the following tasks:
		//     1. Compute the elapsed time since the last time  the token bucket was updated 
		//        and update the variable "lastTime" 
		//     2. Update the variable noTokens, which stores the updated content of the token bucket
		//

		//current time
		long currentTime=System.nanoTime();
		//Ftime_buf[index]=currentTime-lastTime;
		
		long tmp=currentTime-lastTime;
		int generatedTokens=(int)(tmp/tokenInterval);
		//Ftime_buf[index]/=1000;
		noTokens+=generatedTokens;
		if(noTokens>size)noTokens=size;
		//System.out.println(index+"\t"+noTokens+"\t"+Ftime_buf[index]);
		index++;
		lastTime=currentTime;
	}
	
	/**
	 * Returns number of tokens in bucket.
	 * @return Number of tokens in bucket.
	 */
	public synchronized int getNoTokens()
	{
		updateNoTokens();
		return noTokens;
	}
	
	/**
	 * Removes specified number of tokens from bucket.
	 * @param noToRemove Number of tokens to remove.
	 * @return True if there was enough tokens in bucket to remove noToRemove tokens, else false.
	 */
	public synchronized boolean removeTokens(int noToRemove)
	{
		updateNoTokens();
		
		// Currently this code segment is empty. 
		//
		// In Lab 2B, you  add the code that removes the required number of tokens 
		// and returns false if there are not enough tokens.
		if(noTokens<noToRemove)
		return false;
		noTokens=noTokens-noToRemove;
		return true;
	}
	
	/**
	 * Calculates waiting time (in nanoseconds) until bucket has tokensToWaitFor tokens.
	 * There is no guarantee that after this time there will be tokensToWaitFor tokens in bucket (another
	 * user of bucket can remove tokens from bucket during waiting time).
	 * @param tokensToWaitFor Number of tokens in bucket for which to get waiting time.
	 * @return Waiting time (in nanoseconds). 
	 */
	public synchronized long getWaitingTime(int tokensToWaitFor)
	{
		updateNoTokens();
		
		// Currently this code segment always returns a waiting time of zero. 
		//
		// In Lab 2B, you  add the code that sets the correct  time until the bucket 
		// contains the required number  tokensToWaitFor tokens 
		if(noTokens<tokensToWaitFor){
		return (tokensToWaitFor-noTokens)*tokenInterval;
		}

		return (0);
	}
}
