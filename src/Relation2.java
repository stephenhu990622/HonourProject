import java.util.ArrayList;
import java.util.List;

public class Relation2 {
	//number of records
	int size;
	//number of attributes
	List<String> attributes;
	
	public Relation2(List<String> attribute, int N) {
		this.size = N;
		this.attributes = new ArrayList<String>();
		for(String s: attribute) {
			this.attributes.add(s);
		}
	}
	
	public List<String> getAttributes(){
		return attributes;
	}
	
	public int getSize() {
		return size;
	}
	
}
