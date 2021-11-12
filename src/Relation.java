import java.util.ArrayList;
import java.util.List;

public class Relation {
	//number of records;
	int size;
	//fractional edge cover
	double weight = 0;
	//possible attributes
	String x = null; 
	String y = null;
	String z = null;
	String u = null;
	
	
	
	//constructor with 1 attribute
	public Relation(String x, int N) {
		this.x = x;	
		this.size = N;
		}
		
	//constructor with 2 attributes
	public Relation(String x, String y, int N) {
		this.x = x;
		this.y = y;
		this.size = N;
	}
	
	//constructor with 3 attributes
	public Relation(String x, String y, String z, int N) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = N;
	}
	
	//constructor with 4 attributes
	public Relation(String x, String y, String z, String u, int N) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.size = N;
	}
	
	public List<String> getAttributes() {
		List<String >result = new ArrayList<String>();
		if(x!=null && y==null && z==null && u==null ) {
			result.add(x);
		}else if(x!=null && y!=null && z==null && u==null) {
			result.add(x);
			result.add(y);
		}else if(x!=null && y!=null && z!=null && u==null) {
			result.add(x);
			result.add(y);
			result.add(z);
		}else {
			result.add(x);
			result.add(y);
			result.add(z);
			result.add(u);
		}
		return result;
	}
	
	public int getSize() {
		return size;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight=weight;
	}
		
}
