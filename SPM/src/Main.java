import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import Alg.*;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String filePath = args[0];
		
		if(args[1].equals("SDB")){
			TransToSBD Trans = new TransToSBD(filePath);
			Trans.Start();
		}else{
			Float minSupport = Float.parseFloat(args[1]);
			PrefixSpan PFS = new PrefixSpan(filePath,minSupport);
	        PFS.Start();
		}
		System.out.println("It's Ok");	
	}
}
