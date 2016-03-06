package Alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoGain {
	private List<List<String>> originalData;
    private int targetIndex;
    private Map<String,Long> targetValueSet;
    
    public InfoGain(List<List<String>> originalData,int targetIndex){
    	this.targetIndex = targetIndex;
    	this.originalData = originalData;
    	this.targetValueSet = GetTargetValueSet(targetIndex);
    	
    	//System.out.println(GetGainRatio(16));
    	
    }

    /**
     * get the Entropy
     * @param 
     * @return entropy
     */
    public double GetEntropy(){
    	double result = 0 , denominator = originalData.size();
    	for(Map.Entry<String, Long> entry : targetValueSet.entrySet())
    		result += -(entry.getValue()/denominator)*Math.log(entry.getValue()/denominator)/Math.log(2);
		return result;
    }//information Gain of dataset
    
    /**
     * get the info of per feature
     * @param index
     * @return target value set
     */
    public double GetInfoFeature(int index){
    	Map<String,Long> currentSet = GetTargetValueSet(index);
    	List<Map<String,Long>> tmpList = new ArrayList<Map<String,Long>>();
    	double result = 0, TargetInfo = 0 , numerator = 0 , denominator = 0;
    	
    	for(Map.Entry<String, Long> entryValue : targetValueSet.entrySet())
			tmpList.add(GetTargetValueOfFeature(entryValue.getKey(),index));
    	
    	for(Map.Entry<String, Long> entryClass : currentSet.entrySet()){
    		String key = entryClass.getKey();
    		denominator = entryClass.getValue();
    		TargetInfo = 0;
    		for(Map<String,Long> entry : tmpList){
    			numerator = entry.get(key)==null ? 0: entry.get(key);
    			if(numerator != 0) 
    				TargetInfo += -(numerator/denominator)*Math.log(numerator/denominator)/Math.log(2);
    		}
    		result += denominator/originalData.size() * TargetInfo;
    	}
    	//System.out.println(result);
		return result;
    }
    
    /**
     * Create the target value set
     * @param index
     * @return target value set
     */
    public Map<String,Long> GetTargetValueSet(int index){
    	Map<String,Long> result = new HashMap<String,Long>();
    	for(List<String> tmpList : originalData){
    		String key = tmpList.get(index);
    		Long value = result.get(key);
    		result.put(key, value != null ? ++value:1L);
    	}
    	//System.out.println(result);
		return result;
    }
    
    /**
     * Create the target value set of feature
     * @param target index
     * @return target value set of feature
     */
    public Map<String,Long> GetTargetValueOfFeature(String feature,int index){
    	Map<String,Long> result = new HashMap<String,Long>();
    	
    	for(List<String> tmpList : originalData){
    		//System.out.println(feature);
    		if(feature.equalsIgnoreCase(tmpList.get(targetIndex))){
    			String key = tmpList.get(index);
    			Long value = result.get(key);
    			result.put(key, value != null ? ++value:1L);
    		}
    	}
    	//System.out.println(result);
		return result;
    }
    
    /**
     * get the split info
     * @param index
     * @return splitinfo
     */
    public double GetSplitInfo(int index){
    	Map<String,Long> currentSet = GetTargetValueSet(index);
    	double result = 0, numerator = 0 , denominator = originalData.size();
    	for(Map.Entry<String, Long> entryValue : currentSet.entrySet()){
    		numerator = entryValue.getValue();
    		result += -(numerator/denominator)*Math.log(numerator/denominator)/Math.log(2);
    	}
    	//System.out.println(result);
    	return result;
    }
    
    /**
     * calculate the Gaininfo
     * @param index
     * @return Gaininfo
     */
    public double GetGain(int index){
    	return GetEntropy() - GetInfoFeature(index);
    }
    
    /**
     * calculate the GainRatio
     * @param index
     * @return GainRatio
     */
    public double GetGainRatio(int index){
    	return GetGain(index)/GetSplitInfo(index);
    }
    
    /**
     * check if its leaf
     * @param index
     * @return value
     */
    public String PureNode(String str){
    	boolean isPure = true;
    	String tmpStr = "",result ="";
    	for(List<String> tmpList : originalData){
    		if(tmpList.contains(str)){
    			//System.out.println(tmpList.get(targetIndex));
    			if(tmpStr.isEmpty()) tmpStr = tmpList.get(targetIndex);
    			if(!tmpStr.equals(tmpList.get(targetIndex))){
    				isPure =false;
    				break;
    			}
    		}
    	}
    	if(isPure) result = tmpStr;
    	return result;
    }
    
}
