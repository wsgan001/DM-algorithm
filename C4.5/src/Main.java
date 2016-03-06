import java.util.ArrayList;
import java.util.List;

import Alg.C4_5;;

public class Main {

	public static void main(String[] args) {
		
		if(args.length<3){
			System.out.println("Pls input the parameters");
			System.exit(-2);
		}
		else{
			List<String> strList = new ArrayList<String>();
			for(int i = 3; i <args.length;i++)
				strList.add(args[i]);
			
			C4_5 engine = new C4_5(args[0],Float.parseFloat(args[1]),args[2],strList);
			engine.Start();
		}
		
	}

}
