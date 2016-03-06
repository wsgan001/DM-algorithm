package Alg;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class PrefixSpan {
	
	private String filePath;
	private double minSuptRate, maxMemory = 0;
	private long totalTime,startTime,endTime;
	private int minSupport;
	private ArrayList<Sequence> totalSeqs;
	private ArrayList<Sequence> totalFreSeqs = new ArrayList<Sequence>();
	private ArrayList<String> singleItems = new ArrayList<String>();
	
	public PrefixSpan(String filePath, double minSuptRate) {  
        this.filePath = filePath;  
        this.minSuptRate = minSuptRate;  
        LoadDataFile();
    }
	
	public void Start(){
		
		totalTime = 0; 
		startTime = System.currentTimeMillis();
		
		this.minSupport = (int) (this.totalSeqs.size() * this.minSuptRate);
		Sequence prefixSeq;
        Sequence tempSeq;
        ArrayList<Sequence> originalSdb;
        IniSingleFreItems();//create the single frequent items
		
		for(String str : singleItems){
			//System.out.println("prefix : "+str);
			prefixSeq = new Sequence();
			prefixSeq.getSequence().add(Arrays.asList(str));//create the single sequence
			originalSdb = new ArrayList<>();
			for(Sequence s2 : totalSeqs){
				if(s2.ContainedStr(str)){
					tempSeq = s2.ExtractItem(str);
					originalSdb.add(tempSeq);
				}
			}//create the projected db of first single sequence
			totalFreSeqs.add(prefixSeq);
			RecurSeqs(prefixSeq,originalSdb);//begin to run recursion use single sequence and original sdb
			checkMemory();
		}
		
		endTime = System.currentTimeMillis();  
		totalTime = endTime - startTime;
		
		PrintDateToFile();
	}
	
	/** 
     * recursion to run the prefixspan 
     * @param (prefix sequence) (projected database)
	 */  
	private void RecurSeqs(Sequence prefixSeq, ArrayList<Sequence> projectedDb){
		Sequence tempSeq = new Sequence();
		Sequence tempSeq2 = new Sequence();
		ArrayList<Sequence> tempSeqList;
		ArrayList<Sequence> tempSeqList2;
		
		List<String> prefixList;
		
		for(String str : singleItems){
			tempSeqList = new ArrayList<>();
			tempSeqList2 = new ArrayList<>();
			
			//two way to run prefixspan
			//1.sigle prefix ,such "a" ,"b" ,"c"
			if(LargerThanMinSup(str,projectedDb)){//check if the single prefix is greater than mininum support
				tempSeq = prefixSeq.CopySequence();
				tempSeq.getSequence().add(Arrays.asList(str));
				totalFreSeqs.add(tempSeq);//if is, add to total frequent sequence
				for(Sequence s2 : projectedDb){
					Sequence uuu = s2.ExtractItem(str);
					//System.out.println(uuu.getSequence());
					if(s2.ContainedStr(str)) tempSeqList.add(uuu);
				}
				//check if the projected database contain this single prefix and extract the new projected database
				RecurSeqs(tempSeq,tempSeqList);//run recursion use prefix sequence and projected database
			}
			
			//2.set of prefix ,such as "ab" "ac"
			tempSeq2 = prefixSeq.CopySequence();
			prefixList = tempSeq2.getSequence().get(tempSeq2.getSequence().size()-1);
			prefixList.add(str);
			if(LargerThanMinSup(prefixList,projectedDb)){//check if the set of prefix is greater than mininum support
				totalFreSeqs.add(tempSeq2);//if is, add to total frequent sequence
				for(Sequence s2 : projectedDb)
					if (s2.ContainedStrList(prefixList)) tempSeqList2.add(s2.ExtractItemList(prefixList));
				//check if the projected database contain this set of prefix and extract the new projected database
				RecurSeqs(tempSeq2,tempSeqList2);//run recursion use prefix sequence and projected database
			}
		}
		checkMemory(); 
	}
	
	/** 
     * Check if the single string larger than mininum support    
     * @param str seqList
     * @return The large one  
	 */  
	private boolean LargerThanMinSup(String str , ArrayList<Sequence> seqList) {  
        boolean Large = false;  
        int count = 0;
        for (Sequence seq : seqList) 
            if (seq.ContainedStr(str)) count++;
        if (count >= minSupport) Large = true;  
        return Large;  
    }
	
	/** 
     * Check if the string array larger than mininum support   
     * @param itemList seqList
     * @return The large one 
	 */ 
	private boolean LargerThanMinSup(List<String> itemList , ArrayList<Sequence> seqList) {
        boolean Large = false;
        int count = 0;
        if (seqList == null) return false;  
        for (Sequence seq : seqList)  
            if (seq.ContainedStrList(itemList)) count++;  
        if (count >= minSupport) Large = true; 
        return Large;  
    }
	
	/** 
     * Initialize the single frequent item
	 */ 
	public void IniSingleFreItems(){
		
		Map<String,Integer> itemMap = new TreeMap<String,Integer>();
		String key;
		int count;
		
		//create all single items
		for(Sequence seq : totalSeqs)
			 for(List<String> itemList : seq.getSequence())
				 for(String item:itemList) 
					 itemMap.put(item, 1);
		
		//count all single items
		for(Map.Entry map : itemMap.entrySet()){
			key = (String) map.getKey();
			count = 0;
			for (Sequence seq : totalSeqs) 
                if (seq.ContainedStr(key)) count++;  
			itemMap.put(key, count);
		}
		
		//create frequent single items
		for(Map.Entry map : itemMap.entrySet()){
			key = (String) map.getKey(); 
            count = (int) map.getValue();
            if(count >= minSupport){
            	System.out.println(key);
            	singleItems.add(key);
            
            }
		}
		
		checkMemory(); 
	}
	
	/** 
     * load file
	 */ 
	public void LoadDataFile(){
		totalSeqs = new ArrayList<>();  
		try {   
			FileReader fr = new FileReader(filePath);   
			BufferedReader br = new BufferedReader(fr);
			String line = null;   
			while ((line = br.readLine()) != null) {   
				if (line.trim() != "") {
					Sequence dataList = new Sequence();
	                String[] itemsList = line.split(",");   
	                for (String itemset : itemsList) {
	                	String[]item = itemset.split(" ");
	                	List<String> record = new ArrayList<String>();
	                	for(String str : item)
	                    	record.add(str);
	                    dataList.getSequence().add(record);
	                }   
	                totalSeqs.add(dataList); 
				}   
			}
		}catch(IOException e) {   
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}
		
		/*
		for(Sequence seq : totalSeqs){
			for(List<String> itemList : seq.getSequence())
				System.out.print(itemList); 
			 System.out.println("");
		}*/
		System.out.println("Original data : "+totalSeqs.size());
	}
	
	/** 
     * output
	 */ 
	private void PrintDateToFile(){
		String[]path = filePath.split("/");
		String outputPath ="";
		for(int i = 0; i < path.length-1;i++) outputPath+=path[i]+"/";
		
		
		DecimalFormat rateFormat =new DecimalFormat(".00");
		String tmpRate = rateFormat.format(minSuptRate);
		DecimalFormat memoryFormat = new DecimalFormat("##.00");
		String tmpMemo = memoryFormat.format(maxMemory);
		outputPath+="PrefixSpan_(0"+tmpRate+")_Result_"+path[path.length-1];
		
		try{
			FileWriter fileWriter = new FileWriter(outputPath);
			
			fileWriter.append("Totally find "+totalFreSeqs.size()+" frequent sequence \n");
			fileWriter.append("1. mini support rate is 0"+tmpRate+" \n");
			fileWriter.append("2. total time is "+totalTime+"ms \n");
			fileWriter.append("3. max memory is "+ tmpMemo +" mb \n\n");
			
			
			for(Sequence total : totalFreSeqs){
				fileWriter.append("<");
				for(List<String> prefixSeq :total.getSequence()){
					fileWriter.append("(");
						for(String prefix :prefixSeq)
							fileWriter.append(prefix+" ");
						fileWriter.append(")");
				}
				fileWriter.append(">\n");
			}
			fileWriter.flush();  
		}catch(IOException e){
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}
	}
	
	/** 
     * check max memory
	 */ 
	private void checkMemory(){
		double currentMemory = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory())
				/ 1024d / 1024d;
		// if higher than the maximum until now
		if (currentMemory > maxMemory) {
			// replace the maximum with the current memory usage
			maxMemory = currentMemory;
		}
	}
}
