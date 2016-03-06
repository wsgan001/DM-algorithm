package Alg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class C4_5 {
	
	private List<List<String>> originalData;
	private List<List<String>> trainData;
	private List<List<String>> testData;
	private List<String> feature;
	private String filePath;
	private String target;
	private List<String> cutStr;
	private float cutRatio;
	private TreeNode dtTree;
	
	public C4_5(String filePath, float cutRatio ,String target ,List<String> cutStr){
		this.filePath = filePath;
		this.cutRatio = cutRatio;
		this.target = target;
		this.cutStr = cutStr;
	}
	
	public void Start(){
		LoadDataFile();
		TrimData();
		dtTree = Create("",trainData,feature);
		OutputTree(dtTree,"dtTree_");
		System.out.println("Accuracy is " + TestTree(testData));
	}

    /**
     * test tree to calculate the accuracy
     * @param test data
     * @return accuracy
     */	
	public String TestTree(List<List<String>> currentData){
		List<String> tmpFeature = new ArrayList<String>(feature);
		int cutNumber = tmpFeature.indexOf(target),count = 0;
		tmpFeature.remove(target);
		String Accuracy;
		double rate;
		DecimalFormat df = new DecimalFormat("0.00%");
		
		for(List<String> tmpList : currentData){
			String s = TraverseTree(tmpList,dtTree);
			//System.out.print(s+" ===> ");
			//System.out.println(tmpList.get(cutNumber));
			if(tmpList.get(cutNumber).equals(s)) count++;
			tmpList.remove(cutNumber);
			tmpList.add(s);
		}
		
		tmpFeature.add(target);
		rate = (float)count/currentData.size();
		Accuracy = df.format(rate);
		
		PrintData("result_",testData,tmpFeature);
		return Accuracy;
	};

    /**
     * traverse tree to find the result
     * @param data , node
     * @return result
     */	
	public String TraverseTree(List<String> data ,TreeNode node){
		String result = "Null";
		if(node.GetChildAttrNode()!=null){
			for(TreeNode tmpNode : node.GetChildAttrNode())
				if(data.contains(tmpNode.GetNodeName()))
					result= TraverseTree(data,tmpNode);
		}
		else{
			for(String str : data)
				if(node.GetNodeName().equals(str))
					result = node.GetTargetValue();
		}
		return result;
	}

    /**
     * create the tree node
     * @param nodeName , train data , attribute
     * @return tree node
     */	
	public TreeNode Create(String nodeName, List<List<String>> currentData, List<String> feature){
		Map<String,Long> currentValueSet ; 
		TreeNode node;
		TreeNode[] nodeArray;
		int count = 0, targetIndex = feature.indexOf(target) ,index = 0;
		double maxNumber = 0 , tmpNumber =0 ;
		InfoGain infoGain = new InfoGain(currentData,targetIndex);
		
		for(int i =0; i < feature.size(); i++){
			if(i == targetIndex) continue;
			tmpNumber = infoGain.GetGainRatio(i);
			if(tmpNumber > maxNumber){
				maxNumber = tmpNumber;
				index = i;
			}
		}
			
		currentValueSet = infoGain.GetTargetValueSet(index);
		node = new TreeNode(nodeName, feature.get(index));
		nodeArray = new TreeNode[currentValueSet.size()];
			
		for(Map.Entry<String, Long> tmpMap : currentValueSet.entrySet()){	
			String arrValue = infoGain.PureNode(tmpMap.getKey());
			if(!arrValue.isEmpty())
				nodeArray[count] = new TreeNode(tmpMap.getKey(),arrValue);			
			else{
				List<List<String>> tmpData = CutData(currentData,tmpMap.getKey(),index);
				List<String> tmpFeature = new ArrayList<String>(feature); 
				tmpFeature.remove(feature.get(index));
				nodeArray[count] = Create(tmpMap.getKey(),tmpData,tmpFeature);
			}
			count++;
		}
		
		node.setChildAttrNode(nodeArray);
			
		return node;
	}

    /**
     * cut the data list
     * @param datalist , key , index
     * @return result
     */	
	public List<List<String>> CutData(List<List<String>> data , String keyStr,int index){
		List<List<String>> result = new ArrayList();
		List<String> copyList;
		for(List<String> tmpList : data){
			if(tmpList.contains(keyStr)){
				copyList = new ArrayList<String>(tmpList);
				copyList.remove(index);
				result.add(copyList);
			}
			
		}
		return result;
	}

    /**
     * show the decision tree
     * @param tree node , deep
     */
	public void ShowTree(TreeNode node,int deep){
		if(node.GetNodeName().isEmpty()) System.out.println(node.GetTargetValue());
		deep+=3;
		for(TreeNode tmp :node.GetChildAttrNode()){
			for(int i = 0 ;i< deep ;i ++) System.out.print(" ");
			System.out.print(tmp.GetNodeName() + " = ");
			System.out.println(tmp.GetTargetValue());
			if(tmp.GetChildAttrNode()!= null) ShowTree(tmp,deep);
		}
	}

    /**
     * print the decision tree
     * @param tree node , deep , FileWriter
     */
	public void PrintTree(TreeNode node,int deep,FileWriter tgFileWriter){
		try{
			if(node.GetNodeName().isEmpty()) tgFileWriter.append(node.GetTargetValue()+"\n");
			deep+=3;
			for(TreeNode tmp :node.GetChildAttrNode()){
				for(int i = 0 ;i< deep ;i ++) tgFileWriter.append(" ");
				tgFileWriter.append(tmp.GetNodeName() + " = ");
				tgFileWriter.append(tmp.GetTargetValue());
				tgFileWriter.append("\n");
				if(tmp.GetChildAttrNode()!= null) PrintTree(tmp,deep,tgFileWriter);
			}
		}catch(IOException e){
			System.err.println("print error。"+e);   
			System.exit(-2);   
		}
	}
	
	public void OutputTree(TreeNode tree , String name){
		String[]path = filePath.split("/");
		String outputPath ="";
		for(int i = 0; i < path.length-1;i++) outputPath+=path[i]+"/";
		outputPath+=name+path[path.length-1];
		try {
			FileWriter tgFileWriter = new FileWriter(outputPath);
			PrintTree(tree,0,tgFileWriter);
			tgFileWriter.flush();
		} catch (IOException e) {
			System.err.println("output error。"+e);   
			System.exit(-2);   
		}
	}

    /**
     * print the data such as result
     * @param filename , data of print , attribute list
     */
	public void PrintData(String fileName,List<List<String>> printData ,List<String> Title){
		String[]path = filePath.split("/");
		String outputPath ="";
		for(int i = 0; i < path.length-1;i++) outputPath+=path[i]+"/";
		outputPath+=fileName+path[path.length-1];
		
		try{
			FileWriter tgFileWriter = new FileWriter(outputPath);
			for(String str : Title) tgFileWriter.append(str + ",");
			tgFileWriter.append("\n");
			for(List<String>tmpList : printData){
				for(String tmpStr : tmpList)
					tgFileWriter.append(tmpStr+",");
				tgFileWriter.append("\n");
			}
			
			tgFileWriter.flush(); 
		}catch(IOException e){
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}
		
	}
	
	/**
     * trim the original data 
     */
	public void TrimData(){
		feature = new ArrayList<String>(originalData.get(0));
		List<String> tmpList = new ArrayList<String>();
		List<String> tmpFeature;
		trainData = new ArrayList<>();
		testData = new ArrayList<>();
		int totalCount = originalData.size();
		int trainCount = Math.round((originalData.size()-1)*cutRatio);
		
		for(int i = 1 ; i < totalCount;i++){
			tmpList = originalData.get(i);
			tmpFeature = new ArrayList<String>(feature);
			for(String str : cutStr){
				tmpList.remove(tmpFeature.indexOf(str));
				tmpFeature.remove(str);
			}
			if(i < trainCount) trainData.add(tmpList);
			else testData.add(tmpList);
		}
		for(String str : cutStr) feature.remove(str);
		
		PrintData("test_",testData,feature);
		PrintData("train_",trainData,feature);
	}

	/** 
     * load file
	 */ 
	public void LoadDataFile(){
		originalData = new ArrayList<>();
		int checkLength = 0;
		try {   
			FileReader fr = new FileReader(filePath);   
			BufferedReader br = new BufferedReader(fr);
			String line = null;  
			while ((line = br.readLine()) != null) {   
				if (line.trim() != "") {
					List<String> dataList = new ArrayList<String>();
	                String[] data = line.split(",");
	                
	                if(checkLength == 0) checkLength = data.length;
	                
	                if(checkLength == data.length){
	                	//System.out.println(data.length);
		                for (int i = 0; i < data.length;i++){
		                	String str = data[i];
			                str = str.replaceAll("\"","");
			                if(str!="") dataList.add(str);
		                }
		                originalData.add(dataList); 
	                }
				}   
			}
		}catch(IOException e) {   
			System.err.println("Load error。"+e);   
			System.exit(-2);   
		}
		
	}
	
}
