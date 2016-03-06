import java.util.*;
import java.io.*;

public class Spade{
	int offset = 9999999;
    double rate,conf;
    int threshold;
    List<List<List<String>>> DataList = new ArrayList<List<List<String>>>();
    List<Lattice> result = new ArrayList<Lattice>();
    List<String> DataLabel = new ArrayList<String>();
    Set<Event> redandent = new HashSet<Event>();
    
    public Spade(String path,double rate,double conf){
        this.rate = rate; 
        this.conf = conf;
        LoadAndTrans(path);
        this.threshold = (int)(DataList.size() * rate);
        
        List<Lattice> F1 = Find1Sequences();
        //for(Lattice la : F1) la.printTable();
        result.addAll(F1);
        List<Lattice> F2 = Find2Sequences(F1);
        result.addAll(F2);
        //for(Lattice la : F2) la.printTable();
        Enum_Freq_Seq(F2);
        //for(Lattice la : result) la.printTable();
        PrintData();
    }

    public void Enum_Freq_Seq(List<Lattice> SeqSet){
    	List<Lattice> TempList = new ArrayList<Lattice>();
        for(int i =0;i < SeqSet.size();i++){
            for(int j = i; j < SeqSet.size();j++){
            	List<Lattice> R = Join(SeqSet.get(i),SeqSet.get(j));
            	TempList.addAll(R);
            	result.addAll(R);
            }
            //Enum_Freq_Seq(TempList); //DFS
        }
        //for(Lattice la : TempList) la.printTable();
        if(TempList.size() > 0) Enum_Freq_Seq(TempList); //BFS
    }
    
    public List<String> FindSingleFreqItem(){
    	List<String> SingleItem = new ArrayList<String>();
         for(List<List<String>> Seq : DataList)
             for(List<String> Event : Seq)
                 for(String Item : Event)
                     if(!SingleItem.contains(Item)) SingleItem.add(Item);
         Collections.sort(SingleItem);
         return SingleItem;
    }
    
    public List<Lattice> Find1Sequences(){
        List<Lattice> Lattices = new ArrayList<Lattice>(); 
    	List<String> SingleItem = FindSingleFreqItem();
        for(String Item : SingleItem){
            Event latEvent = new Event(Item);
            Lattice tempLatt = new Lattice(latEvent);
            tempLatt.findOriginItem(Item,DataList,DataLabel);
	        checkValid(Lattices,tempLatt);
        }//
        
        return Lattices;
    }

    public List<Lattice> Find2Sequences(List<Lattice> SingleItem){
    	 List<Lattice> Lattices = new ArrayList<Lattice>();
    	 Lattice tempLatt;
    	 for(int i = 0; i < SingleItem.size();i++){
    		 for(int j = i;j < SingleItem.size();j++){
    			 // X & Y to X,Y  
    	         tempLatt = latticeGreater(SingleItem.get(i),SingleItem.get(j),false,0); // merge
    	         if(tempLatt.getEvent().getItemset(0).countSameItems() > 1)
    	        	 tempLatt.sameEventsameItem(SingleItem.get(i),tempLatt.getEvent().getItemset(0).countSameItems());
    	         else
    	        	 tempLatt.inSameEvent(SingleItem.get(i),SingleItem.get(j));
	    	     checkValid(Lattices,tempLatt);
    		 }
    	 }
    	 for(Lattice Item1 : SingleItem){
    		 for(Lattice Item2 : SingleItem){
    			 // X & Y to X->Y    			 
    			 tempLatt = latticeGreater(Item1,Item2,true,0); // join
    	         tempLatt.inOrderEvent(Item1,Item2);
	    	     checkValid(Lattices,tempLatt);
    		 }
    	 }
    	 return Lattices;
    }
    
    public List<Lattice> Join(Lattice pattern1,Lattice pattern2){
        List<Lattice> Lattices = new ArrayList<Lattice>();
        Lattice tempLatt;

        if(pattern1.isRelation(pattern2)){
    		
        	int caseAndIdx = pattern1.getEvent().toCompare(pattern2.getEvent());        	
        	switch(caseAndIdx){
	        	case 0 : // PX, PY
	    			tempLatt = latticeGreater(pattern1,pattern2,false,-1);//merge
	    	        tempLatt.inSameEvent(pattern1,pattern2);
	    	        checkValid(Lattices,tempLatt);
	        		break;
	    
	        	case -1 : // PX,PX
	        		tempLatt = latticeGreater(pattern1,pattern2,true,-1);//merge
	    	        tempLatt.inOrderEvent(pattern1,pattern2);
	    	        checkValid(Lattices,tempLatt);
	        		break;
	        	case -2 : // PX,P->Y
	        		tempLatt = latticeGreater(pattern1,pattern2,false,-1);//merge
	        		if(pattern1.getEvent().get().size() > pattern2.getEvent().get().size())
	        			tempLatt.inOrderEvent(pattern2,pattern1);
	        		else if(pattern1.getEvent().get().size() < pattern2.getEvent().get().size())
	        			tempLatt.inOrderEvent(pattern1,pattern2);
	        		else 
	        			tempLatt.inSameEvent(pattern1,pattern2);
	    	        checkValid(Lattices,tempLatt);
	    	        break;
	        	default: //P->X,P->Y
	            	//case P->X->Y
	        		
	        		tempLatt = latticeGreater(pattern1,pattern2,true,-1);//joint
	    	        tempLatt.inOrderEvent(pattern1,pattern2);
	    	        checkValid(Lattices,tempLatt);

	        		//case P->Y->X
	        		tempLatt = latticeGreater(pattern2,pattern1,true,-1);//joint
	    	        tempLatt.inOrderEvent(pattern2,pattern1);
	    	        checkValid(Lattices,tempLatt);

	    	        //case P->XY
	        		tempLatt = latticeGreater(pattern1,pattern2,false,-1);//merge
	    	        tempLatt.inSameEvent(pattern1,pattern2);
	    	        checkValid(Lattices,tempLatt);
	        		break;
	        }
        }
        return Lattices;
    }
    
    public Lattice latticeGreater(Lattice pattern1,Lattice pattern2,boolean join,int idx){
    	Event latEvent;
        Lattice tempLatt;
		latEvent = pattern1.cloneEvent();
		if(join && idx >= 0) latEvent.join(pattern2.cloneEvent(),idx);
		else if(join && idx < 0) latEvent.join(pattern2.cloneEvent());
		else if(!join && idx >= 0) latEvent.merge(pattern2.cloneEvent(),idx);
		else latEvent.merge(pattern2.cloneEvent());
		
		if(redandent.contains(latEvent)) tempLatt = null;
		else tempLatt = new Lattice(latEvent);
        
    	return tempLatt;
    }
    
    public void checkValid(List<Lattice> Lattices , Lattice tempLatt){
    	if(tempLatt.getSup() >= threshold ) Lattices.add(tempLatt);
    	for(Iterator<Map.Entry<String,Integer>> it = tempLatt.getClassAndConf().entrySet().iterator();it.hasNext();){
    	     Map.Entry<String, Integer> entry = it.next();
    	     if ((entry.getValue()+0.00) / tempLatt.getSup() < conf ) {
    	          it.remove();
    	     }
    	 }
    }
    
    public void PrintData(){
	
		String outputPath ="result.txt";
		
		try{
			FileWriter tgFileWriter = new FileWriter(outputPath);
			for(Lattice node : result){
				for(Object key : node.getClassAndConf().keySet()){
					for(ItemSet item: node.getEvent().get()){
						for(String str : item.get())
							tgFileWriter.append(str+" ");
						tgFileWriter.append("-1 ");
					}
					tgFileWriter.append("#SUP: ");
					tgFileWriter.append(String.valueOf(node.getSup()));
					tgFileWriter.append(" #CLASS: ");
					tgFileWriter.append(key.toString());
					tgFileWriter.append(" #CONF: ");
					java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");   
					double d = ((node.getClassAndConf().get(key)+0.00)/node.getSup());
					tgFileWriter.append(df.format(d));
					tgFileWriter.append("\n");	
				}
			}
			tgFileWriter.flush(); 
		}catch(IOException e){
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}		
	}
    
	public void LoadAndTrans(String file) {
        try {   
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            
            String line = null;   
            while ((line = br.readLine()) != null) {   
                if (line.trim() != "") {
                    List<List<String>> record = new ArrayList<List<String>>();   
                    String[] events = line.split("-1 ");
                    for(String event : events){
                        String[] items = event.split("[^A-Za-z0-9]");
                        record.add(Arrays.asList(items));     
                    }
                    record.remove(record.size()-1);//delete -2
                    DataLabel.add(record.get(record.size()-1).get(0));//get the class into datalabel
                    record.remove(record.size()-1);//remove the class from original data
                    DataList.add(record);
                }   
            }
        } catch (IOException e) {   
            System.err.println("Load error。"+e);   
            System.exit(-2);   
        } 
    }
}