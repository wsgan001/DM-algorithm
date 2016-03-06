package Alg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbScan {
	private String filePath; 
    private int minPts; 
	private double eps;   
    private List<Point> allPoints;  
    private List<List<Point>> resultClusters;  
    private List<Point> noisePoint;  
    
    public DbScan(String filePath, int minPts, double eps) {  
        this.filePath = filePath;
        this.minPts = minPts;
        this.eps = eps;  
        loadDataFile();
    }
    
    public void Start(){
    	List<Point> cluster = null;  
        resultClusters = new ArrayList<>();  
        noisePoint = new ArrayList<>();
        
        for (Point p : allPoints){
            if(p.isVisited) continue;
            cluster = new ArrayList<Point>();
            recurCluster(p, cluster);
            if (cluster.size() > 0) resultClusters.add(cluster);
            else noisePoint.add(p);
        }
        printData();
        System.out.println("It's OK");
    }

	/**
     * recursive clustering point
     * @param point parentCluster
     */   
    private void recurCluster(Point point, List<Point> parentCluster) {  
        double distance = 0;  
        ArrayList<Point> cluster = new ArrayList<Point>();
        
        if (point.isVisited) return;  
        point.isVisited = true;
        
        for (Point p2 : allPoints) {  
            distance = point.calEuraDist(p2);
            //System.out.println(eps+" : "+point.getAttr()+" = "+p2.getAttr() +" --> "+distance);
            if (distance <= eps) cluster.add(p2);
        }
        //System.out.println(point.getAttr() + " ---> " +cluster.size()+"個");
  
        if (cluster.size() >= minPts) {
            addCluster(cluster , parentCluster);  
            for (Point p : cluster) recurCluster(p, parentCluster);  
        }
    }

	/**
     * add directly density-reachable cluster to parentCluster
     * @param cluster parentCluster
     */
    private void addCluster(List<Point> cluster, List<Point> parentCluster) {  
        boolean isCotained = false;  
        List<Point> addPoints = new ArrayList<Point>();  
        for (Point p : cluster) {
            isCotained = false;
            for (Point p2 : parentCluster) {  
                if (p.isSame(p2)) {  
                    isCotained = true;  
                    break;  
                }  
            }  
            if (!isCotained) addPoints.add(p);  
        }
        parentCluster.addAll(addPoints);  
    }

	/**
     * show cluster and noise
     */
    public void showData(){
    	System.out.println("Cluster: ");
    	for(List<Point> tmp :resultClusters){
    		for(Point p : tmp){
    			System.out.print(p.getAttr()+" , ");
    		}
    		System.out.println(" ");
    	}
    	
    	System.out.println("Noise: ");
    	for(Point p2 :noisePoint)
    		System.out.println(p2.getAttr());
    }

    /**
     * print cluster and noise
     * @param filename
     */
	public void printData(){
		String[]path = filePath.split("/");
		String outputPath ="";
		for(int i = 0; i < path.length-1;i++) outputPath+=path[i]+"/";
		outputPath+="Result_"+path[path.length-1];
		
		try{
			FileWriter tgFileWriter = new FileWriter(outputPath);
			tgFileWriter.append("Cluster : \n");
			for(int i =0; i<resultClusters.size(); i++){
				tgFileWriter.append("("+i+")__");
				for(Point p : resultClusters.get(i))					
					tgFileWriter.append(p.toString());
				tgFileWriter.append("\n");
			}
			
			tgFileWriter.append("Noise: \n");
	    	for(Point p2 :noisePoint)
	    		tgFileWriter.append(p2.toString());
			
			tgFileWriter.flush(); 
		}catch(IOException e){
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}	
	}
    
	/**
     * load data file
     */
    public void loadDataFile(){
    	allPoints = new ArrayList<Point>();
		try {   
			FileReader fr = new FileReader(filePath);   
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				Point tmpPoint = null;
				if (line.trim() != "") {
	                String[] data = line.split(",");
	                List<String> dataList = new ArrayList<String>();
	                for(int i = 1;i<data.length;i++)
	                	dataList.add(data[i].replaceAll(" ",""));
	                
	                System.out.println(dataList);
	                tmpPoint = new Point(dataList);
				}
				allPoints.add(tmpPoint);
			}
		}catch(IOException e) {   
			System.err.println("Load error。"+e);
			System.exit(-2);   
		}
    }
}
