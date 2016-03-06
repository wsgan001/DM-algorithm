package Alg;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;

public class Apriori {
	
	private int MinSup;
	private static List<Set<String>> dataTrans;
	long supTime,confTime,startTime,endTime;
	double maxMemory = 0,MinConf;
	
	public Apriori(float MinSup ,double MinConf, List<Set<String>> dataTrans){
		this.MinConf = MinConf;
		this.MinSup = (int) (dataTrans.size()*MinSup);
		this.dataTrans = dataTrans;
	}
	
	public void AlgStart() throws IOException{
		
		supTime = 0; 
		startTime = System.currentTimeMillis(); 
		
		long totalItem = 0; 
		FileWriter tgFileWriter = new FileWriter("DataSet/result.txt");  
		Map<Set<String>, Integer> firstSet = FindFirtFItem(dataTrans); //find the first frequent item set
		
        Map<Set<String>, Integer> result = firstSet; 
        Map<Set<String>, Integer> allItem = new LinkedHashMap<Set<String>, Integer>();
        allItem.putAll(result);
        
        totalItem += printMap(result, tgFileWriter); //count the total item
        do {      
        	//System.out.println(result.size());
            result = NextKItem(result); //find next k item
            allItem.putAll(result);
            //checkMemory();
            
        } while(result.size() != 0);  
        //checkMemory();
        endTime = System.currentTimeMillis(); 
        supTime = endTime - startTime;
        
		confTime = 0; 
		startTime = System.currentTimeMillis(); 
		
		
        List<String> allRule = ruleGen(allItem);
        
        endTime = System.currentTimeMillis(); 
        confTime = endTime - startTime;
		
		System.out.printf("Algorithm run memory :%.2f mb\n",maxMemory);
		System.out.println("Algorithm find frequent set time :"+supTime+"ms");
		System.out.println("Algorithm rule generation time :"+confTime+"ms");
		
        System.out.println("Totle [" + allItem.size() + "] Frequent Itemset");
        System.out.println("Totle [" + allRule.size() + "] Rules");
        
		//Set<Set<String>> set = FirstSet.keySet();
		//for (Set<String> key : set) System.out.println(key + "==>" + FirstSet.get(key));
		 
	}
	
	private Map<Set<String>, Integer> FindFirtFItem(List<Set<String>> dataTrans) {  
		Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();  
        Map<String, Integer> itemCount = new HashMap<String, Integer>(); 
        
        for(Set<String> i : dataTrans){  
            for(String j : i){
                if(itemCount.containsKey(j))
                    itemCount.put(j, itemCount.get(j) + 1);  
                else  
                    itemCount.put(j, 1); 
            }  
        }//count the itemset
        
        for(Map.Entry<String, Integer> ic : itemCount.entrySet()){  
            if(ic.getValue() >= MinSup){
            	Set<String> tmp = new HashSet<String>();  
                tmp.add(ic.getKey());  
                result.put(tmp, ic.getValue());
            }
        }//add the support
        
        return result;  
    } //first frequent item set
  
	private Map<Set<String>, Integer> NextKItem(Map<Set<String>, Integer> preMap) {
		
		 Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();  
		
	     List<Set<String>> preSetArray = new ArrayList<Set<String>>();
	  
	        for(Map.Entry<Set<String>, Integer> preMapItem : preMap.entrySet())  
	            preSetArray.add(preMapItem.getKey()); 
	        
	        int preSetLength = preSetArray.size();
	        
	        for (int i = 0; i < preSetLength - 1; i++) {  
	            for (int j = i + 1; j < preSetLength; j++) {  
	                String[] strA1 = preSetArray.get(i).toArray(new String[0]);  
	                String[] strA2 = preSetArray.get(j).toArray(new String[0]);  
	                if (Link(strA1, strA2)) { // check if k-1 item set can link k item set　  
	                    Set<String> set = new TreeSet<String>();  
	                    for (String str : strA1) {  
	                        set.add(str);  
	                    }
	                    
	                    set.add((String) strA2[strA2.length - 1]); //create k item set
	                    if (!Cut(preMap, set)) result.put(set, 0);// if !cut then add k item set
	                    
	                }  
	            }  
	        }
	        return Check(result);//check if its frequent item set
	    }  
	
	private boolean Link(String[] strA1, String[] strA2) {  
        boolean flag = true;
        if(strA1.length != strA2.length) return false;  
        else {
            for(int i = 0; i < strA1.length - 1; i++){  
                if(!strA1[i].equals(strA2[i])){  
                    flag = false;  
                    break;  
                }
            }
            if(strA1[strA1.length -1].equals(strA2[strA1.length -1])) flag = false;
        }
        return flag;  
    }
	
	private boolean Cut(Map<Set<String>, Integer> preMap, Set<String> set) {  
		boolean flag = false;  
		List<Set<String>> subSets = SubSets(set);
		//System.out.println(set);
		//System.out.println(subSets);
		for(Set<String> subSet : subSets){  
			if(!preMap.containsKey(subSet)){  
				flag = true;  
				break;  
			}  
		}  
		return flag;  
	}
	
	private List<Set<String>> SubSets(Set<String> set) {  
		String[] setArray = set.toArray(new String[0]);  
		List<Set<String>> result = new ArrayList<Set<String>>();  
	        for(int i = 0; i < setArray.length; i++){  
	        	Set<String> subSet = new HashSet<String>();  
	        	for(int j = 0; j < setArray.length; j++)  
	                if(j != i) subSet.add(setArray[j]);    
	            result.add(subSet);  
	        }
	    return result;  
	}//create all subset array
	 
	private Map<Set<String>, Integer> Check(Map<Set<String>, Integer> AllKItem) {
		Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();  
		for(Set<String> kItem : AllKItem.keySet()){
			for(Set<String> data : dataTrans){  
				boolean flag = true;  
				for(String str : kItem){
					if(!data.contains(str)){  
						flag = false;  
	                    break;  
	                }  
	            }  
	            if(flag) AllKItem.put(kItem, AllKItem.get(kItem) + 1);  
	        }
			if(AllKItem.get(kItem) >= MinSup) result.put(kItem, AllKItem.get(kItem));  
		}
		return result;  
	}

	private List<String> ruleGen(Map<Set<String>, Integer> allItem){
		Set<String> tmpSet = new TreeSet();
		List<String> result = new ArrayList<String>();
		for(Entry<Set<String>, Integer> currentitem : allItem.entrySet()){
			checkMemory();
			int itemLength = currentitem.getKey().size();
			ArrayList<Set<String>> subSet = new ArrayList<Set<String>>();
			if(itemLength>1){
				ArrayList<String> itemList = new ArrayList<String>(currentitem.getKey());
				for(int i=1;i<itemList.size();i++){
					checkMemory();
					ArrayList<Set<String>> tmpItemSet = AllSubSet(itemList,i);
					subSet.addAll(tmpItemSet);
				}
				List<String> qq = CheckConf(allItem,subSet,currentitem.getKey());
				result.addAll(qq);
			}
		}
		return result;
	}
	
	private ArrayList<Set<String>> AllSubSet(ArrayList<String> list,int n){	
		checkMemory();
		if(list.size() < n || n <= 0) return null;

		ArrayList<Set<String>> result = new ArrayList<Set<String>>();
		
		Set<String> tSet = new HashSet<String>(list);
		
		if(list.size()==n) result.add(tSet);
		
		else
		{
			ArrayList<String> list1=(ArrayList<String>) list.clone();
			list1.remove(0);
			ArrayList<Set<String>> res1=AllSubSet(list1,n-1);
			if(res1!=null){
				for(Set<String> obj : res1){
					obj.add(list.get(0));
					result.add(obj);
				}
			}
			else{
				Set<String> temp=new HashSet<String>();
				temp.add(list.get(0));
				result.add(temp);
			}
			//doesn't contain the first element
			ArrayList<Set<String>> res2=AllSubSet(list1,n);
			//System.out.println(result+"____"+n);
			if(res2!=null)
				for(Object obj : res2)
					result.add((Set<String>) obj);
		}
		//System.out.println(result+"____"+n+"  total");
		return result;
	}
	
	private List<String> CheckConf(Map<Set<String>, Integer> allItem,ArrayList<Set<String>> checkItem,Set<String> currentitem){
		
		//System.out.println(checkItem);
		List<String> result = new ArrayList<String>();
		for(Set<String> set : checkItem){
			checkMemory();
			Set<String> tmpItem = new HashSet<String>(currentitem);
			for(String str: set)
				tmpItem.remove(str);
			double conf = (double)(allItem.get(set))/(double)(allItem.get(tmpItem));
			DecimalFormat changeDf = new DecimalFormat("##.00%");
			String tmpStr = set+" --> "+tmpItem+" = "+changeDf.format(conf);
			if(conf>=MinConf) result.add(tmpStr);
		}
		return result;
	}
	
	private int printMap(Map<Set<String>, Integer> f1Map, FileWriter tgFileWriter) throws IOException {  
		for(Map.Entry<Set<String>, Integer> f1MapItem : f1Map.entrySet()){  
			for(String p : f1MapItem.getKey()){  
				tgFileWriter.append(p + " ");  
			}  
			tgFileWriter.append(": " + f1MapItem.getValue() + "\n");  
		}  
		tgFileWriter.flush();  
		return f1Map.size();  
	}
	
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
