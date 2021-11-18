import java.util.ArrayList;
import java.util.List;
public class Hypergraph2 {
	List<String> vertices = new ArrayList<String>();
	List<List<String>> hyperedges = new ArrayList<List<String>>();
	public Hypergraph2(List<Relation2> relations) {
		for(Relation2 r: relations) {
			List<String> v = r.getAttributes();
			hyperedges.add(v);
			for(String s: v) {
				if(!vertices.contains(s)) {
					vertices.add(s);
				}
			}
		}		
	}
	
	public List<String> getVertices(){
		return this.vertices;  
	}
	
	public List<List<String>> getHyperedges(){
		return this.hyperedges;
	}
}
