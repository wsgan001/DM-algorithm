import java.io.*;
import java.util.*;

import Alg.*;

public class Main {

	public static void main(String[] args) throws IOException {
		String[] FileArray = {"D1kT10N500.txt","D10kT10N1k.txt","D100kT10N1k.txt","Mushroom.txt"};
		List<Set<String>> DataArray = new ArrayList();
		Main Fun = new Main();

		long totalTime,startTime,endTime;

		float supThreshold = (float) 0.5;
		double confThreshold = 1;
		System.out.println("Select File\n"
			+ "(Default Threshold__"+ supThreshold * 100 +"%):\n"
			+ "1."+FileArray[0]+"\n"
			+ "2."+FileArray[1]+"\n"
			+ "3."+FileArray[2]+"\n"
			+ "4."+FileArray[3]);
		
		int FileNumber = Fun.InputNumber();
		
		totalTime = 0; 
		startTime = System.currentTimeMillis(); 
		if(FileNumber > 0 && FileNumber < 5)
			DataArray = Fun.LoadAndTrans(FileArray[FileNumber-1]);
		else  DataArray = Fun.LoadAndTrans(FileArray[0]);
		//System.out.println(Data);
		
		endTime = System.currentTimeMillis();  
		totalTime = endTime - startTime;
		System.out.println("Loading file time :"+totalTime+"ms");
	
		Apriori AprFun = new Apriori(supThreshold,confThreshold,DataArray);
		AprFun.AlgStart();
		
		
	}
	
	public int InputNumber(){
		int Uin = 0;
		Boolean Next = true;
		BufferedReader stdin = 
				new BufferedReader(
				new InputStreamReader(System.in));
		do{
			try{
				Uin = Integer.parseInt(stdin.readLine());
				Next = false;
			}
			catch(Exception e){
				System.err.println("error!pls input again");
			}
		}while(Next);
		
		return Uin;
	}
	
	private List<Set<String>> LoadAndTrans(String file) {
        List<Set<String>> Datalist = new ArrayList<Set<String>>();   
        try {   
            FileReader fr = new FileReader("Dataset/"+file);   
            BufferedReader br = new BufferedReader(fr);
            
            String line = null;   
            while ((line = br.readLine()) != null) {   
                if (line.trim() != "") {   
                    Set<String> record = new HashSet<String>();   
                    String[] items = line.split(",");   
                    for (String item : items) {
                    	item = item.replaceAll(" ","");
                        record.add(item);   
                    }   
                    Datalist.add(record);   
                }   
            }   
        } catch (IOException e) {   
            System.err.println("Load error。"+e);   
            System.exit(-2);   
        }   
        return Datalist;   
    }  
	
}
