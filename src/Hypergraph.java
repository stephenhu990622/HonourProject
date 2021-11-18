import java.util.ArrayList;
import java.util.List;
public class Hypergraph {
	List<String> vertices = new ArrayList<String>();
	List<List<String>> hyperedges = new ArrayList<List<String>>();
	public Hypergraph(List<Relation> relations) {
		for(Relation r: relations) {
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
