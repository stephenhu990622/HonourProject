import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import java.lang.Math;

public class Goal_One {
	//a helper function for any size of identity matrix
	public static double[][] getIdentity(int size) {
         double[][] matrix = new double[size][size];
		     for(int i = 0; i < size; i++) {
		         matrix[i][i] = 1.0;
		     }
		     return matrix;
	 }
	 //a helper function to calculate the base 2 logarithm
	public static int log2(int x) {
        return (int)(Math.log(x) / Math.log(2));
    }
	 
	public static List<Double> minFractionalEdgeCover(List<Relation> relations) {
	    //define a return variable represents weights assigned.
		List<Double> weights = new ArrayList<Double>();
		// the number of relations 
		int length = relations.size();
		//initialize a hypergraph object
		Hypergraph hg = new Hypergraph(relations);
		List<String> vertices = hg.getVertices();
		List<List<String>> hyperedges = hg.getHyperedges();
		//----------------------------------------LP SCPSolver Part---------------------------------------
		int [] size = new int[length];
		// Create a Stream from our Relation List
        Stream<Relation> Stream = relations.stream();
        // Convert the stream into an array of Relation
        Relation[] relation = Stream.toArray(Relation[]::new);
        for(int i=0;i<size.length;i++) {
        	size[i]=relation[i].getSize();
        }
		//define an array used in LP constructor ; the target function is sum_i(log2(size(Ri))
		double[] d = new double[length];
		for(int i=0;i<d.length;i++) {
		    d[i]=log2(size[i]);
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
            Stream<Double> Stream3 = l2.stream();
            // Convert the stream into an array of Double
            Double[] Arr = Stream3.toArray(Double[]::new);
            //Then convert Double[] into double[]
            double[] Arr2 = new double[l2.size()];
            for(int i=0;i<Arr2.length;i++) {
            	Arr2[i]=Arr[i];
            }
            //add constraints for LP with the definition of fractional edge cover
            lp.addConstraint(new LinearBiggerThanEqualsConstraint(Arr2, 1.0, "c1"));
		}
		//add constraints with each fractional edge cover is at least >= 0
		double[][] constraint = getIdentity(length);
		for(int i=0;i<constraint.length;i++) {
			lp.addConstraint(new LinearBiggerThanEqualsConstraint(constraint[i], 0.0, "c2"));
		}
		//as we need to find the minimal fractional cover
		lp.setMinProblem(true);
		LinearProgramSolver solver  = SolverFactory.newDefault();
		double[] sol = solver.solve(lp);
		//----------------------------------------LP SCPSolver Part---------------------------------------
		for(int i=0;i<sol.length;i++) {
			weights.add(sol[i]);
		}		
		//return values
		return weights;	
	}
	
	//using overloading with same name of methods but different number of parameters
	public static List<Double> minFractionalEdgeCover(List<Relation> relations, Relation constraint) {
		//get the list of attributes for constraint relation
		List<String> ca = constraint.getAttributes();
		List<String> newR = new ArrayList<String>();
		for(String s:ca) {
			if(s!="_") {
				newR.add(s);
			}
		}
		int newR_size = constraint.getSize();
		Relation newRelation = new Relation(newR,newR_size);
		relations.add(newRelation);
		//define a return variable represents weights assigned.
		List<Double> weights = new ArrayList<Double>();
		// the number of relations 
		int length = relations.size();
		//initialize a hypergraph object
		Hypergraph hg = new Hypergraph(relations);
		List<String> vertices = hg.getVertices();
		List<List<String>> hyperedges = hg.getHyperedges();
		//----------------------------------------LP SCPSolver Part---------------------------------------
		int [] size = new int[length];
		// Create a Stream from our Relation List
        Stream<Relation> Stream = relations.stream();
        // Convert the stream into an array of Relation
        Relation[] relation = Stream.toArray(Relation[]::new);
        for(int i=0;i<size.length;i++) {
        	size[i]=relation[i].getSize();
        }
		//define an array used in LP constructor ; the target function is sum_i(log2(size(Ri))
		double[] d = new double[length];
		for(int i=0;i<d.length;i++) {
			d[i]=log2(size[i]);
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
            Stream<Double> Stream3 = l2.stream();
            // Convert the stream into an array of Double
            Double[] Arr = Stream3.toArray(Double[]::new);
            //Then convert Double[] into double[]
            double[] Arr2 = new double[l2.size()];
            for(int i=0;i<Arr2.length;i++) {
            	Arr2[i]=Arr[i];
            }
            //add constraints for LP with the definition of fractional edge cover
            lp.addConstraint(new LinearBiggerThanEqualsConstraint(Arr2, 1.0, "c1"));
		}
		//add constraints with each fractional edge cover is at least >= 0
		double[][] constraint2 = getIdentity(length);
		for(int i=0;i<constraint2.length;i++) {
			lp.addConstraint(new LinearBiggerThanEqualsConstraint(constraint2[i], 0.0, "c2"));
		}
		//as we need to find the minimal fractional cover
		lp.setMinProblem(true);
		LinearProgramSolver solver  = SolverFactory.newDefault();
		double[] sol = solver.solve(lp);
		//----------------------------------------LP SCPSolver Part---------------------------------------
		for(int i=0;i<sol.length;i++) {
			weights.add(sol[i]);
		}		
		//return values
		return weights;	
	}
	
	public static int AGMBound(List<Relation> relations) {
		double AGM_Bound =1.0;
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
			AGM_Bound *=Math.pow(relation[i].getSize(),weight[i]);
		}
		// return values
		return (int)AGM_Bound;
	}
	
	//using overloading with same name of methods but different number of parameters
	public static int AGMBound(List<Relation> relations, Relation constraint) {
		double AGM_Bound =1.0;
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
			AGM_Bound *=Math.pow(relation[i].getSize(),weight[i]);
		}
		// return values
		return (int)AGM_Bound;
	}
	
	public static void main(String[] args) {
		//test for different examples
		List<Relation> re = new ArrayList<Relation>();
		List<String> r= new ArrayList<String>();
		r.add("A");
		r.add("B");
		
		List<String> s= new ArrayList<String>();
		s.add("B");
		s.add("C");
		
		List<String> t= new ArrayList<String>();
		t.add("C");
		t.add("A");

			
		List<String> r2= new ArrayList<String>();
		r2.add("A");
		r2.add("_");

		
		Relation R = new Relation(r,100);
		Relation S = new Relation(s,100);
		Relation T = new Relation(t,100);

		Relation R2 = new Relation(r2,2);
		
		re.add(R);
		re.add(S);
		re.add(T);
//		re.add(R2);


		Hypergraph hg= new Hypergraph(re);
		System.out.println("The vertices of hypergraph is: "+hg.getVertices());
		System.out.println("The hyperedges of hypergraph is: "+hg.getHyperedges());
		System.out.println("The constraint relation is: "+ R2.getAttributes());
		System.out.println("The minimum fractional edge cover is: "+minFractionalEdgeCover(re,R2));
		System.out.println("The computed AGM Bound is: "+AGMBound(re,R2));
	}

}
