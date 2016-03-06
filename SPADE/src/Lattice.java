import java.util.*;

public class Lattice{
	Event event;
	List<Integer> sid;
	List<Integer> eid;
	List<String> label;
	HashMap<String,Integer> classConf;
	int supcount = 0;
	double utility;
	
	public Lattice(Event event){
		this.event = event;
		sid = new ArrayList<Integer>();
		eid = new ArrayList<Integer>();
		label = new ArrayList<String>();
		classConf = new HashMap<String,Integer>();
	}

	public Event getEvent(){
		return event;
	}

	public List<Integer> getSid(){
		return sid;
	}

	public List<Integer> getEid(){
		return eid;
	}

	public List<String> getLabel(){
		return label;
	}
	
	public HashMap<String,Integer> getClassAndConf(){
		return classConf;
	}
	
	public int getSup(){
		return supcount;
	}

	public int getSidByIdx(int idx){
		return sid.get(idx);
	}

	public int getEidByIdx(int idx){
		return eid.get(idx);
	}

	public String getLabelByIdx(int idx){
		return label.get(idx);
	}

	public void addSid(int value){
		sid.add(value);
	}

	public void addEid(int value){
		eid.add(value);
	}

	public void addLabel(String value){
		label.add(value);
	}

	public int getLength(){
		return sid.size();
	}

	public void setSup(int value){
		this.supcount = value;
	}
	
	public void rmClassAndConf(String key){
		this.classConf.remove(key);
	}

	public void countClassConf(String key){
		int value = classConf.get(key) == null ? 1 : classConf.get(key) + 1; 
		classConf.put(key, value);
	}
	
	/**
	 * 
	 * test patterns are in relation
	 * 
	 **/
	public boolean isRelation(Lattice pattern){
		boolean result = false;
		List<String> set1 = new ArrayList<String>();
		List<String> set2 = new ArrayList<String>();
		
		for(ItemSet temp : event.get()) set1.addAll(temp.get());
		for(ItemSet temp : pattern.getEvent().get()) set2.addAll(temp.get());
		
		set1.remove(set1.size()-1);
		set2.remove(set2.size()-1);
		
		
		if(set1.containsAll(set2)) result = true;
		
		return result;
	}

	public void findOriginItem(String Item, List<List<List<String>>> DataList, List<String> DataLabel){
		int TempIdx = -1;
		for(int i = 0; i< DataList.size();i++){
            for(int j = 0; j < DataList.get(i).size();j++){
                if(DataList.get(i).get(j).contains(Item)){
                	sid.add(i);
                    eid.add(j);
                    label.add(DataLabel.get(i));
                    if(TempIdx != i){
                        TempIdx = i;
                        supcount++;
                        countClassConf(DataLabel.get(i));
                    }
                }
            }
        }
	}
	
	/**
	 * 
	 * indicate the item of event are equivalent
	 * 
	 **/
	public void sameEventsameItem(Lattice pattern1,int limit){
		int TempIdx = -1;
		int tempSid = pattern1.getSidByIdx(0);
		int tempEid = pattern1.getEidByIdx(0);
		int repeat = 0;
		for(int i = 1; i < pattern1.getLength();i++){
			if(tempSid == pattern1.getSidByIdx(i) &&
					tempEid == pattern1.getEidByIdx(i)){
				repeat++;
			}else repeat = 0;
			if(repeat == limit){
				sid.add(pattern1.getSidByIdx(i));
				eid.add(pattern1.getEidByIdx(i));
				label.add(pattern1.getLabelByIdx(i));
				supcount++;
                countClassConf(pattern1.getLabelByIdx(i));
			}
			tempSid = pattern1.getSidByIdx(i);
			tempEid = pattern1.getEidByIdx(i);
		}
	}


	/**
	 * 
	 * pattern1's eid equal to pattern2
	 * 
	 **/
	public void inSameEvent(Lattice pattern1,Lattice pattern2){
		int TempIdx = -1;
		for(int i = 0; i < pattern1.getLength();i++){
			for(int j = 0;j <pattern2.getLength();j++){
				if(pattern1.getSidByIdx(i) == pattern2.getSidByIdx(j) &&
						pattern1.getEidByIdx(i) == pattern2.getEidByIdx(j)){
					sid.add(pattern1.getSidByIdx(i));
					eid.add(pattern2.getEidByIdx(j));
					label.add(pattern2.getLabelByIdx(j));
                    if(TempIdx != pattern1.getSidByIdx(i)){
                        TempIdx = pattern1.getSidByIdx(i);
                        supcount++;
                        countClassConf(pattern2.getLabelByIdx(j));
                    }// Count the Support of this Event
                    
                    continue;
				}
			}
		}
	}


	/**
	 * 
	 * pattern1's eid must be less than pattern2
	 * 
	 **/
	public void inOrderEvent(Lattice pattern1,Lattice pattern2){
		int TempIdx = -1;
		
		for(int i = 0; i < pattern1.getLength();i++){
			for(int j = 0;j <pattern2.getLength();j++){
				if(pattern1.getSidByIdx(i) == pattern2.getSidByIdx(j) &&
						pattern1.getEidByIdx(i) < pattern2.getEidByIdx(j)){
					sid.add(pattern1.getSidByIdx(i));
					eid.add(pattern2.getEidByIdx(j));
					label.add(pattern2.getLabelByIdx(j));
                    if(TempIdx != pattern1.getSidByIdx(i)){
                        TempIdx = pattern1.getSidByIdx(i);
                        supcount++;
                        countClassConf(pattern2.getLabelByIdx(j));
                    }// Count the Support of this Event
                    
				}
			}
		}
	}

	/**
	 * 
	 * pattern1's eid can less or equal to pattern2
	 * 
	 **/
	public void inMergeEvent(Lattice pattern1,Lattice pattern2){
		int TempIdx = -1;
		for(int i = 0; i < pattern1.getLength();i++){
			for(int j = 0;j <pattern2.getLength();j++){
				if(pattern1.getSidByIdx(i) == pattern2.getSidByIdx(j) &&
						pattern1.getEidByIdx(i) <= pattern2.getEidByIdx(j)){
					sid.add(pattern1.getSidByIdx(i));
					eid.add(pattern2.getEidByIdx(j));
					label.add(pattern2.getLabelByIdx(j));
                    if(TempIdx != pattern1.getSidByIdx(i)){
                        TempIdx = pattern1.getSidByIdx(i);
                        supcount++;
                        countClassConf(pattern2.getLabelByIdx(j));
                    }// Count the Support of this Event
				}
			}
		}
	}
	
	public Event cloneEvent(){
		Event newClone= new Event();
		newClone = event.cloneSelf();
		return newClone;
	}
	
	public void printTable(){
		event.printTable();
		System.out.println();
		System.out.println("sid => "+sid);
		System.out.println("eid => "+eid);
		//System.out.println("label => "+label);
		System.out.println("support => "+supcount);
		for (Object key : classConf.keySet())
            System.out.println(key + " : " + classConf.get(key));

	}
}