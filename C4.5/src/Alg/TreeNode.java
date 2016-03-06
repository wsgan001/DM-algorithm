package Alg;

public class TreeNode {
	private String nodeName;
	private String targetValue;
	private TreeNode[] childAttrNode;
		
	public TreeNode(String nodeName , String targetValue){
        this.nodeName = nodeName;
        this.targetValue = targetValue;
    }
	
	public TreeNode[] GetChildAttrNode() {
		return childAttrNode;
	}

	public void setChildAttrNode(TreeNode[] childAttrNode) {
		this.childAttrNode = childAttrNode;
	}

	public String GetTargetValue() {
		return targetValue;
	}

	public void SetTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String GetNodeName() {
		return nodeName;
	}

	public void SetNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
