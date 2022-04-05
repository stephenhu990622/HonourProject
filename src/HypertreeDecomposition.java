import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class HypertreeDecomposition {
	
	//attributes
	TreeMap<String,List<String>> bags = new TreeMap<String,List<String>>();
	
	public HypertreeDecomposition(List<List<String>> bags) {
		int index=0;
		for(List<String> bag: bags) {
			this.bags.put("bag"+index, bag);
			index++;
		}
	}
	
	public TreeMap<String,List<String>> getBags(){
		return this.bags;
	}
	
	
	public static void main (String[] args) {
		List<String> l = new ArrayList<String>();
		l.add("A");
		l.add("B");
		l.add("C");
		List<List<String>> ls = new ArrayList<List<String>>();
		ls.add(l);
		ls.add(l);
		ls.add(l);
		
		HypertreeDecomposition hd = new HypertreeDecomposition(ls);
		System.out.println(hd.getBags());
	}
}
