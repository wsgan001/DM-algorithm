package Alg;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class TransToSBD {
	
	private String filePath;
	
	public TransToSBD(String file){
		this.filePath = file;
	}
	
	public List<String[]> Start() {
        List<String[]> Datalist = new ArrayList<String[]>(); 
        
        try {   
            FileReader fr = new FileReader(filePath);   
            BufferedReader br = new BufferedReader(fr);
            
            String line = null;
            
            while ((line = br.readLine()) != null) {   
                if (line.trim() != "") {
                    String[] items = line.split("\\s{2,}");
                    Datalist.add(items);
                }
            }
        } catch (IOException e) {   
            System.err.println("Load error。"+e);   
            System.exit(-2);   
        }
        Trans(Datalist);
        return Datalist;   
    } 
	
	public Map<Integer,List<List<String>>> Trans(List<String[]> allList){
		int tmpTid = 1,tmpSid = 1;
		Map<Integer,List<List<String>>> result = new TreeMap<Integer,List<List<String>>>();
		List<List<String>> tmpSet = new ArrayList<List<String>>();
		List<String> tmpList = new ArrayList<String>();
		tmpSet.add(tmpList);
		System.out.println(allList.size());
		int count = 1;
		for(String[] list: allList){
			if(Integer.parseInt(list[2]) != tmpTid){
				tmpTid = Integer.parseInt(list[2]);
				tmpList = new ArrayList<String>();
				if(Integer.parseInt(list[1]) != tmpSid){
					tmpSid = Integer.parseInt(list[1]);
					tmpSet = new ArrayList<List<String>>();
					count++;
				}
				tmpSet.add(tmpList);
				
			}
			//System.out.println(tmpSet);
			tmpList.add(list[3]);
			result.put(Integer.parseInt(list[1]),tmpSet);
		}
		printMap(result);
		System.out.println(count);
		return result;
	}
	
	public void printMap(Map<Integer,List<List<String>>> allMap){
		String[]path = filePath.split("/");
		String outputPath ="";
		for(int i = 0; i < path.length-1;i++) outputPath+=path[i]+"/";
		
		outputPath+="SDB_"+path[path.length-1];
		
		try{
			FileWriter tgFileWriter = new FileWriter(outputPath);
			for(Entry<Integer, List<List<String>>> f1MapItem : allMap.entrySet()){
				//tgFileWriter.append(f1MapItem.getKey()+":");
				for(List<String> list : f1MapItem.getValue()){ 
					for(String set : list){
						tgFileWriter.append(set + " ");
					}
					tgFileWriter.append(",");
				}  
				tgFileWriter.append("\n");
			}  
			tgFileWriter.flush(); 
		}catch(IOException e){
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}
	}
}
