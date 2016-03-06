package Alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Point {
	private List<Double> attr;
	public boolean isVisited;
	
	public Point(List<String> attrList) {
		attr = new ArrayList<Double>();
		for(String str : attrList)
			attr.add(Double.parseDouble(str));
        this.isVisited = false;  
    }  
	
	public List<Double> getAttr(){
		return this.attr;
	}

	public String toString(){
		String result = "[";
		for(double i : this.attr)
			result += String.valueOf(i)+",";
		result = result.substring(0,result.length()-1);
		result+="] ";
		return result;
	}
    /**
     * get euclidean distance between p and this point
     * @param p
     * @return distance
     */
	public double calEuraDist(Point p) {  
        double distance = 0.0;  
        int length = this.attr.size() < p.getAttr().size() ? this.attr.size() : p.getAttr().size();
        for(int i = 0;i<length;i++)
        	distance += Math.pow((this.attr.get(i) - p.getAttr().get(i)), 2);
        distance = Math.sqrt(distance);  
        return distance;  
    }  
	/**
     * check if p same of this point
     * @param p
     * @return boolean
     */
	public boolean isSame(Point p) {  
        boolean isSamed = true;
        if(this.attr.size() == p.getAttr().size())
        	for(int i = 0; i<p.getAttr().size();i++)
        		if(this.attr.get(i)!=p.getAttr().get(i)) isSamed = false;
        
        return isSamed;  
    }  
	
}
