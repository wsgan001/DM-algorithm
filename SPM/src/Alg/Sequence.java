package Alg;

import java.util.*;

public class Sequence {
	
	List<List<String>> itemSequence;
	
	public Sequence() {  
        this.itemSequence = new ArrayList<List<String>>();  
    }
	
	public List<List<String>> getSequence() {  
        return itemSequence;  
    }  
  
    public void setSequence(List<List<String>> itemSetList) {  
        this.itemSequence = itemSetList;  
    }
    
    public int size(){
    	return this.itemSequence.size();
    }
	
    /** 
     * Check if single item contained in Sequence
     * @param c 
     * @return boolean
     */
	public boolean ContainedStr(String str) {
		boolean Contained = false;
        for (List<String> itemList : this.itemSequence) {  
        	Contained = false;
            for (String s : itemList) {
                if ( itemList.contains("_") ) continue; 
                if ( s.equals(str) ) {  
                	Contained = true;  
                    break;
                }
            }
            if (Contained) break;// if contained then break;  
        }
        return Contained;  
    }
    
    /** 
     * Check if item set contained in Sequence
     * @param itemList
     * @return boolean
     */  
    public boolean ContainedStrList(List<String> itemList) {  
        boolean Contained = false;
        String lastItem = itemList.get(itemList.size()-1);
        for (List<String> tempItems: this.itemSequence) {
            // 1.check in _x
            if (tempItems.size() > 1 && tempItems.get(0).equals("_") && tempItems.get(1).equals(lastItem)) {  
            	Contained = true;  
                break;  
            } else if (!tempItems.get(0).equals("_")) {  
                // 2.check in normal situation
            	Contained = IsSublist(tempItems, itemList);
            	if(Contained) break;
            }
            if (Contained) break;  
        }
        return Contained;  
    }
    
    /** 
     * check if strList2 is the sublist of strList1
     * @param strList1 
     * @param strList2 
     * @return boolean
     */  
    public boolean IsSublist(List<String> strList1, List<String> strList2){
        boolean Contained = false;
        for (int i = 0; i < strList1.size() - strList2.size() + 1; i++) {
        	Contained = true;
            for (int j = 0, k = i; j < strList2.size(); j++, k++) {  
                if (!strList1.get(k).equals(strList2.get(j))) {  
                	Contained = false;  
                    break;  
                }
            }
            if (Contained) break;
        }  
        return Contained;  
    }

    /** 
     * copy a sequence
     * @return Sequence
     */  
    public Sequence CopySequence() {  
        Sequence copySeq = new Sequence();  
        List<String> tempItemSet;
        for (List<String> itemList : this.itemSequence) {  
            tempItemSet = new ArrayList<String>(itemList);  
            copySeq.getSequence().add(tempItemSet);  
        }
        return copySeq;  
    }
    
    /** 
     * generate the projected db using single string   
     * @param str
     * @return Sequence 
     */  
    public Sequence ExtractItem(String str) {
        Sequence extractSequence = this.CopySequence();
        List<String> itemList;
        List<List<String>> deleteItems = new ArrayList<>();  
        ArrayList<String> tempItems = new ArrayList<>();  
        
        for (int k = 0; k < extractSequence.itemSequence.size(); k++) {
        	
        	itemList = extractSequence.itemSequence.get(k);
            if (itemList.size() == 1 && itemList.get(0).equals(str)) {
            	extractSequence.itemSequence.remove(k);
                break;
            }else if (itemList.size() > 1 && !itemList.get(0).equals("_")){
            	
                if (itemList.contains(str)){  
                    int index = itemList.indexOf(str);  
                    for (int j = index; j < itemList.size(); j++)  
                        tempItems.add(itemList.get(j));
                    tempItems.set(0, "_");
                    
                    if (tempItems.size() == 1) deleteItems.add(itemList);  
                    else extractSequence.itemSequence.set(k, new ArrayList<String>(tempItems));
                    
                    break;  
                } else deleteItems.add(itemList);
            }else deleteItems.add(itemList);  
        }
        
        
        for(int i =0;i<extractSequence.getSequence().size();i++)
        	for(int j = 0;j<deleteItems.size();j++)
        		if(extractSequence.getSequence().get(i) == deleteItems.get(j))
        			extractSequence.getSequence().remove(i);
        	
        //extractSequence.itemSequence.removeAll(deleteItems);//= =!error
        return extractSequence;  
    }
    
    /** 
     * generate the projected db using string list     
     * @param array 
     * @return Sequence
     */  
    public Sequence ExtractItemList(List<String> array) {  
        boolean stopExtract = false;  
        Sequence seq = this.CopySequence();  
        String lastItem = array.get(array.size() - 1);  
        List<String> tempItems;  
        List<List<String>> deleteItems = new ArrayList<>();  
  
        for (int i = 0; i < seq.itemSequence.size(); i++) {
        	
            if (stopExtract) break;  
            tempItems = seq.itemSequence.get(i);
            if (tempItems.size() > 1 && tempItems.get(0).equals("_") && tempItems.get(1).equals(lastItem)) {  
                if (tempItems.size() == 2) seq.itemSequence.remove(i);  
                else {
                    tempItems.set(1, "_");
                    tempItems.remove(0);
                }
                stopExtract = true;  
                break;  
            }else if (!tempItems.get(0).equals("_")) {  
                if (IsSublist(tempItems, array)) {    
                    int index = tempItems.indexOf(lastItem);  
                    ArrayList<String> array2 = new ArrayList<String>();  
                    for (int j = index; j < tempItems.size(); j++) array2.add(tempItems.get(j));  
                    array2.set(0, "_");
                    if (array2.size() == 1) deleteItems.add(seq.itemSequence.get(i));  
                    else seq.itemSequence.set(i, new ArrayList<String>(array2));  
                    stopExtract = true;  
                    break;  
                } else deleteItems.add(seq.itemSequence.get(i));  
            } else deleteItems.add(seq.itemSequence.get(i));  
        }  
        seq.itemSequence.removeAll(deleteItems);
        return seq;  
    }  
    
}
