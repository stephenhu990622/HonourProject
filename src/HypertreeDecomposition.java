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
	
	
}
