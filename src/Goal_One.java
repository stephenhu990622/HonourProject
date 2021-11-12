import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import java.lang.Math;

public class Goal_One {
	public static List<Double> minFractionalEdgeCover(List<Relation> relations) {
		//define a return variable represents weights assigned.
		List<Double> weights = new ArrayList<Double>();
		// the number of relations 
		int length = relations.size();
		//initialize a hypergraph object
		Hypergraph hg = new Hypergraph(relations);
		List<String> vertices = hg.getVertices();
		List<List<String>> hyperedges = hg.getHyperedges();
		
		//define an array used in LP constructor
		double[] d = new double[length];
		for(int i=0;i<d.length;i++) {
			d[i]=1.0;
		}
		//define LP object
		LinearProgram lp = new LinearProgram(d);		
		List<List<Double>> store_list = new ArrayList<List<Double>>();
		for(String s:vertices) {
			List<Double> list = new ArrayList<Double>();
			for(List<String> he:hyperedges) {
				if(he.contains(s)) {
					list.add(1.0);
				}else {
					list.add(0.0);
				}
			}
			store_list.add(list);
		}
		
		//check index if attribute appear in hyperedge then 1 else 0
//		for(List<Double> l:store_list) {
//			System.out.println(l);
//		}
		//set constraint for based on each set of index
		for(List<Double> l2:store_list) {			
			 // Create a Stream from our List
            Stream<Double> Stream = l2.stream();
            // Convert the stream into an array of Double
            Double[] Arr = Stream.toArray(Double[]::new);
            //Then convert Double[] into double[]
            double[] Arr2 = new double[l2.size()];
            for(int i=0;i<Arr2.length;i++) {
            	Arr2[i]=Arr[i];
            }
            //add constraints for LP
            lp.addConstraint(new LinearBiggerThanEqualsConstraint(Arr2, 1.0, "c"));
		}
		//as we need to find the minimal fractional cover
		lp.setMinProblem(true);
		LinearProgramSolver solver  = SolverFactory.newDefault();
		double[] sol = solver.solve(lp);
		for(int i=0;i<sol.length;i++) {
			weights.add(sol[i]);
		}		
		//return values
		return weights;	
	}
	
	public static int AGMBound(List<Relation> relations) {
		int AGM_Bound =1;
		List<Double> weights =minFractionalEdgeCover(relations);
		 // Create a Stream from our Double List
        Stream<Double> Stream = weights.stream();
        // Convert the stream into an array of Double
        Double[] Arr = Stream.toArray(Double[]::new);
        //Then convert Double[] into double[]
        double[] weight = new double[weights.size()];
        for(int i=0;i<weight.length;i++) {
        	weight[i]=Arr[i];
        }
        
        // Create a Stream from our Relation List
        Stream<Relation> Stream2 = relations.stream();
        // Convert the stream into an array of Relation
        Relation[] relation = Stream2.toArray(Relation[]::new);
        for(int i=0;i<weight.length;i++) {
        	relation[i].setWeight(weight[i]);
        }
		for(int j=0;j<relation.length;j++) {
			AGM_Bound *=Math.pow(relation[j].getSize(), relation[j].getWeight());
		}
		
		return AGM_Bound;
	}
	
	public static void main(String[] args) {
		List<Relation> r = new ArrayList<Relation>();
		Relation R = new Relation("A","B",100);
		Relation S = new Relation("B","C",100);
		Relation T = new Relation("C","A",100);	
//		Relation U = new Relation("B","C","E",100);	
		r.add(R);
		r.add(S);
		r.add(T);
//		r.add(U);
		Hypergraph hg= new Hypergraph(r);
		System.out.println("The vertices of hypergraph is: "+hg.getVertices());
		System.out.println("The hyperedges of hypergraph is: "+hg.getHyperedges());
		System.out.println("The minimum fractional edge cover is: "+minFractionalEdgeCover(r));
		System.out.println("The computed AGM Bound is: "+AGMBound(r));
	}
}
