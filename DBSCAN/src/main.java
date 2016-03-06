import Alg.*;

public class main {

	public static void main(String[] args) {
		
		if(args.length<3){
			System.err.println("insufficient parameters");
			System.exit(-2);
		}else{
			String filePath = args[0];
			int minPts = Integer.parseInt(args[1]);
			double eps = Double.parseDouble(args[2]);
			DbScan engine = new DbScan(filePath,minPts,eps);
			engine.Start();
		}
		
		
	}

}
