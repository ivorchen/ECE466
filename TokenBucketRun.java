import TokenBucket.TokenBucket;

public class TokenBucketRun {
	public static void main(String[] args){
		TokenBucket lb = new TokenBucket(4444,"localhost", 4445, 1024, 100*1024, 10000, 5000, "bucket.txt");
		new Thread(lb).start();
	}
		

}
