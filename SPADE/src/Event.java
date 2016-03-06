import java.util.*;

public class Event{
	int offset = 9999999;
	List<ItemSet> event;

	public Event(){
		this.event = new ArrayList<ItemSet>();
	}

	public Event(String str){
		this.event = new ArrayList<ItemSet>();
		ItemSet preEvents = new ItemSet(str);
		event.add(preEvents);
	}
	
	public Event(List<String> items){
		this.event = new ArrayList<ItemSet>();
		ItemSet newItem = new ItemSet(items);
		event.add(newItem);
	}

	public void add(ItemSet item){
		event.add(item);
	}
	
	public List<ItemSet> get(){
		return this.event;
	}
	
	public ItemSet getItemset(int idx){
		return event.get(idx);
	}

	/**
	 * 
	 * Merge event by loop, one by one
	 * 
	 **/
	public void merge(Event e){
		if(e.get().size() > event.size()){
			for(int i =0;i<event.size();i++)
				e.get().get(i).addAll(event.get(i).get(), false);
			event = e.get();
		}
		else{
			for(int i =0;i<e.get().size();i++){
				event.get(i).addAll(e.get().get(i).get(), false);
			}
		}

	}// X and Y to X,Y

	/**
	 * 
	 * Merge event by index
	 * 
	 **/
	public void merge(Event e ,int idx){
		for(ItemSet items : e.get()){
			this.event.get(idx).addAll(items.get(),true);}
	}// X and Y to X,Y
	
	/**
	 * 
	 * Join a new event, if its itemset equivalent ,that add behind
	 * 
	 **/
	public void join(Event e){
		
		event.add(e.getItemset(e.get().size()-1));
		
		
	}// X and Y to X->Y
	
	/**
	 * 
	 * Join by index
	 * 
	 **/
	public void join(Event e,int idx){
		this.event.add(idx+1, e.getItemset(idx));
		
	}// X and Y to X->Y
	
	/**
	 * 
	 * Deep copy itself
	 * 
	 **/
	public Event cloneSelf(){
		Event newClone =  new Event();
		for(ItemSet temp : this.event) newClone.add(temp.cloneSelf());
		return newClone;
	}
	
	/**
	 * 
	 * Compare event B ,may have follow case
	 * result = -1, then indicate equivalent sequence ,eg. <PX> and <PX>
	 * result = -2, then indicate event to sequence, eg. <PX> and <P,Y>
	 * result = 0, that event to event ,eg.<PX> and <PY>
	 * otherwise, sequence to sequence , eg. <P,X> and <P,Y>  
	 * 
	 **/
	public int toCompare(Event B){
		int result = -1; // PX , PX
		int Asize = event.size();
		int Bsize = B.get().size();
		if(Asize != Bsize)
			result = -2;
		else{
			for(int i =0;i < Asize;i++){
				if(!event.get(i).equal(B.getItemset(i))){
					result = i;//
					break;
				}else if(!event.get(i).contains(B.getItemset(i))&&!B.getItemset(i).contains(event.get(i))){
					result = i;
					break;
				}
			}
		}
		return result;
	}
	
	public void printTable(){
		for(ItemSet item : event)
			item.printTable();
	}
}