import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import java.lang.Math;

public class Goal_One2 {
	public static List<Double> minFractionalEdgeCover(List<Relation2> relations) {
		//define a return variable represents weights assigned.
		List<Double> weights = new ArrayList<Double>();
		// the number of relations 
		int length = relations.size();
		//initialize a hypergraph object
		Hypergraph2 hg = new Hypergraph2(relations);
		List<String> vertices = hg.getVertices();
		List<List<String>> hyperedges = hg.getHyperedges();
		//----------------------------------------LP SCPSolver Part---------------------------------------
		//define an array used in LP constructor
		double[] d = new double[length];
		for(int i=0;i<d.length;i++) {
			d[i]=1.0;
		}
		//define LP object using its constructor
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
		
		//check index if each attribute appear in each hyper edge then 1 else 0
		for(List<Double> l:store_list) {
			System.out.println(l);
		}
		//set constraint for based on each list of index in store_list
		for(List<Double> l2:store_list) {			
			 // Create a Stream from our Double List
            Stream<Double> Stream = l2.stream();
            // Convert the stream into an array of Double
            Double[] Arr = Stream.toArray(Double[]::new);
            //Then convert Double[] into double[]
            double[] Arr2 = new double[l2.size()];
            for(int i=0;i<Arr2.length;i++) {
            	Arr2[i]=Arr[i];
            }
            //add constraints for LP with the definition of fractional edge cover
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
	
	public static int AGMBound(List<Relation2> relations) {
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
        Stream<Relation2> Stream2 = relations.stream();
        // Convert the stream into an array of Relation
        Relation2[] relation = Stream2.toArray(Relation2[]::new);
        for(int i=0;i<weight.length;i++) {
			AGM_Bound *=Math.pow(relation[i].getSize(),weight[i]);
		}
		// return values
		return AGM_Bound;
	}
	
	public static void main(String[] args) {
		
		List<Relation2> re = new ArrayList<Relation2>();
		List<String> r= new ArrayList<String>();
		r.add("B");
		r.add("C");
		r.add("D");
		List<String> s= new ArrayList<String>();
		s.add("A");
		s.add("C");
		s.add("D");
		List<String> t= new ArrayList<String>();
		t.add("A");
		t.add("B");
		t.add("D");
		List<String> u= new ArrayList<String>();
		u.add("A");
		u.add("B");
		u.add("C");
		Relation2 R = new Relation2(r,100);
		Relation2 S = new Relation2(s,100);
		Relation2 T = new Relation2(t,100);	
		Relation2 U = new Relation2(u,100);		
		re.add(R);
		re.add(S);
		re.add(T);
		re.add(U);
		Hypergraph2 hg= new Hypergraph2(re);
		System.out.println("The vertices of hypergraph is: "+hg.getVertices());
		System.out.println("The hyperedges of hypergraph is: "+hg.getHyperedges());
		System.out.println("The minimum fractional edge cover is: "+minFractionalEdgeCover(re));
		System.out.println("The computed AGM Bound is: "+AGMBound(re));
	}

}
