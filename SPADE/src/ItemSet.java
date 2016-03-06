import java.util.ArrayList;
import java.util.List;

public class ItemSet {
	List<String> items;

	public ItemSet(){
		this.items = new ArrayList<String>();
	}
	
	public ItemSet(String item){
		this.items = new ArrayList<String>();
		items.add(item);
	}

	public ItemSet(List<String> items){
		this.items = items;
	}

	public void add(String item){
		items.add(item);
	}

	public void addAll(List<String> it,boolean flag){
		for(String item : it)
			if(flag) this.items.add(item);
			else 
				if(!items.contains(item)) this.items.add(item);
	}
	
	public List<String> get(){
		return items;
	}
	
	/**
	 * 
	 * Count how many equivalence items in an itemset
	 * 
	 **/
	public int countSameItems(){
		int result = 0;
		String temp = items.get(0);
		for(String item : items)
			if(temp.equals(item)) result += 1;
		return result;
	}

	/**
	 * 
	 * Check if its equal to ItemSet B
	 * 
	 **/
	public boolean equal(ItemSet B){
		boolean result = true;
		if(items.size() != B.get().size()) return false;
		for(int i = 0; i < items.size(); i++)
			if(!items.get(i).equals(B.get().get(i))) result = false;
		return result;
	}

	/**
	 * 
	 * Check if its contains ItemSet B
	 * 
	 **/
	public boolean contains(ItemSet B){
		return(items.containsAll(B.get()));
	}

	/**
	 * 
	 * Deep copy itself
	 * 
	 **/
	public ItemSet cloneSelf(){
		ItemSet newClone =  new ItemSet();
		for(String temp : this.items) newClone.add(temp);
		return newClone;
	}

	/**
	 * 
	 * print items
	 * 
	 **/
	public void printTable(){
		System.out.print(items);
		System.out.print(" ");
	}
}
