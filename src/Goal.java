import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Stream;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import java.lang.Math;

public class Goal {
	
	//----------------------------------------------------First Goal---------------------------------------------------------------
	
	
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
		//----------------------------------------LP SCPSolver Part(start)---------------------------------------
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
		//set the optimization goal
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
//		for(List<Double> l:store_list) {
//			System.out.println(l);
//		}
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
		//----------------------------------------LP SCPSolver Part(end)---------------------------------------
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
		//----------------------------------------LP SCPSolver Part(Start)---------------------------------------
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
//		for(List<Double> l:store_list) {
//			System.out.println(l);
//		}
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
		//----------------------------------------LP SCPSolver Part(end)---------------------------------------
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
	
	
	
	//----------------------------------------------- Second Goal---------------------------------------------------------------
	
		
	//return the fractional hypertree width of a given hypertree decomposition
	public static Double getFractionalHTW(Hypergraph hg, HypertreeDecomposition hd) {
		Double fractional_width = 0.0;
		//store the guards of the bag
		TreeMap<String,List<List<String>>> guards = new TreeMap<String,List<List<String>>>();
		//get the bags of hypertree decomposition
		TreeMap<String,List<String>> bags = hd.getBags();
		//get a list of relations
		List<List<String>> relations = hg.getHyperedges();
		for(Map.Entry<String, List<String>> entry: bags.entrySet()) {
			List<List<String>> re_list = new ArrayList<List<String>>();
			for(List<String> relation:relations) {
				//check if bag cover current relation
				if(entry.getValue().containsAll(relation)) {
					re_list.add(relation);
					guards.put(entry.getKey(), re_list);
				}
			}
		}
		TreeMap<String,List<Relation>> new_guards = new TreeMap<String,List<Relation>>();
		for(Map.Entry<String, List<List<String>>> entry2: guards.entrySet()) {
			List<Relation> re_list = new ArrayList<Relation>();
			List<List<String>> re = new ArrayList<List<String>>();
			//get the guards (list of guardian relations)
			re = entry2.getValue();
			for(List<String> ls: re) {
				Relation r = new Relation(ls,100);
				re_list.add(r);
			}
			new_guards.put(entry2.getKey(), re_list);
		}
		
		//test
//		for(List<Relation> r: new_guards.values()) {
//			System.out.println(r.toString());
//		}
//		
		List<Double> weight_list = new ArrayList<Double>();
		for(List<Relation> ls: new_guards.values()) {
			//using AGM Bound algorithm 
			List<Double> weight = new ArrayList<Double>();
			weight = minFractionalEdgeCover(ls);
			Double sum =0.0;
			for(Double d: weight) {
				sum+=d;
			}
			weight_list.add(sum);						
		}		
		fractional_width = Collections.max(weight_list);	
		return fractional_width;
	}
	
	//return the hypertree width of a given hypertree decomposition
	public static int getHTW(Hypergraph hg, HypertreeDecomposition hd) {
		int hypertree_width =0;
		//store the guards of the bag
		TreeMap<String,List<List<String>>> guards = new TreeMap<String,List<List<String>>>();
		//get the bags of hypertree decomposition
		TreeMap<String,List<String>> bags = hd.getBags();
		//get a list of relations
		List<List<String>> relations = hg.getHyperedges();
		for(Map.Entry<String, List<String>> entry: bags.entrySet()) {
			List<List<String>> re_list = new ArrayList<List<String>>();
			for(List<String> relation:relations) {
				//check if bag cover current relation
				if(entry.getValue().containsAll(relation)) {
					re_list.add(relation);
					guards.put(entry.getKey(), re_list);
				}
			}
		}
		TreeMap<String,List<Relation>> new_guards = new TreeMap<String,List<Relation>>();
		for(Map.Entry<String, List<List<String>>> entry2: guards.entrySet()) {
			List<Relation> re_list = new ArrayList<Relation>();
			List<List<String>> re = new ArrayList<List<String>>();
			//get the guards (list of guardian relations)
			re = entry2.getValue();
			for(List<String> ls: re) {
				Relation r = new Relation(ls,100);
				re_list.add(r);
			}
			new_guards.put(entry2.getKey(), re_list);
		}
				
		//test
//		for(List<Relation> r: new_guards.values()) {
//			System.out.println(r.toString());
//		}
//				
		List<Double> weight_list = new ArrayList<Double>();
		for(List<Relation> ls: new_guards.values()) {
			//using AGM Bound algorithm 
			List<Double> weight = new ArrayList<Double>();
			weight = minFractionalEdgeCover(ls);
			Double sum =0.0;
			for(Double d: weight) {
				sum+=d;
			}
			weight_list.add(sum);						
		}		
		hypertree_width = (int)Math.round(Collections.max(weight_list));				
		return hypertree_width;
	}
	
	
	//-------------------------------------------------------Test Part--------------------------------------------------------------
	
	
	//return a k-path query of any given size integer k; assume that size of each relation is 100
	public static List<Relation> PathQueries(int k){
		List<Relation> relations = new ArrayList<Relation>();
		if(k<2) {
			System.out.println("The value of input must be an integer that greater or equal to 2.");
		}else {
			for(int i=0;i<k;i++) {
				List<String> attributes = new ArrayList<String>();
				attributes.add("x"+String.valueOf(i));
				attributes.add("x"+String.valueOf(i+1));
				Relation r = new Relation(attributes,100);
				relations.add(r);
			}
		}
		
		return relations;
	}
	
	//return a k-clique query of any given size integer k; assume that size of each relation is 100
	public static List<Relation> CliqueQueries(int k){
		List<Relation> relations = new ArrayList<Relation>();
		String[] atts = new String[k];
		for(int i=0;i<k;i++) {
			atts[i]= "x"+String.valueOf(i);
		}
		if(k<3) {
			System.out.println("The value of input must be an integer that greater or equal to 3.");
		}else {
			for(int i=0;i<k;i++) {				
				for(int j=i+1;j<k;j++) {
					List<String> attributes = new ArrayList<String>();
					attributes.add(atts[i]);
					attributes.add(atts[j]);
					
					Relation r = new Relation(attributes,100);
					if(!relations.contains(r)) {
						relations.add(r);
					}else {
						;//pass
					}	
				}
			}
		}
		return relations;
	}
	
	//return a k-Loomis-Whitney query of any given size integer k; assume that size of each relation is 100
	public static List<Relation> LoomisWhitneyQueries(int k){
		List<Relation> relations = new ArrayList<Relation>();
		String[] atts = new String[k];
		for(int i=0;i<k;i++) {
			atts[i]= "x"+String.valueOf(i);
		}
		if(k<3) {
			System.out.println("The value of input must be an integer that greater or equal to 3.");
		}else {
			for(int i=0;i<k;i++) {
				List<String> attributes = new ArrayList<String>();
				for(int j=0;j<k;j++) {					
					if(i!=j) {
						attributes.add(atts[j]);
					}else {
						;//pass
					}
				}
				Relation r = new Relation(attributes,100);
				relations.add(r);
			}
		}
		return relations;
	}
	
	public static void main(String[] args) {
		//test case 1
//		List<List<String>> bags = new ArrayList<List<String>>();
//		List<String> bag1 = new ArrayList<String>();
//		bag1.add("3");
//		bag1.add("4");
//		List<String> bag2 = new ArrayList<String>();
//		bag2.add("0");
//		bag2.add("1");
//		bag2.add("2");
//		bag2.add("3");
//		bags.add(bag1);
//		bags.add(bag2);
//		HypertreeDecomposition hd = new HypertreeDecomposition(bags);
//		
//		List<String> a0 = new ArrayList<String>();
//		a0.add("0");
//		a0.add("1");
//		a0.add("2");
//		Relation r0 = new Relation(a0,100);
//		List<String> a1 = new ArrayList<String>();
//		a1.add("2");
//		a1.add("3");
//		Relation r1 = new Relation(a1,100);
//		List<String> a2 = new ArrayList<String>();
//		a2.add("0");
//		Relation r2 = new Relation(a2,100);
//		List<String> a3 = new ArrayList<String>();
//		a3.add("1");
//		Relation r3 = new Relation(a3,100);
//		List<String> a4 = new ArrayList<String>();
//		a4.add("0");
//		a4.add("3");
//		Relation r4 = new Relation(a4,100);
//		List<String> a5 = new ArrayList<String>();
//		a5.add("3");
//		a5.add("4");
//		Relation r5 = new Relation(a5,100);
//		List<String> a6 = new ArrayList<String>();
//		a6.add("4");
//		Relation r6 = new Relation(a6,100);
//		List<Relation> re = new ArrayList<Relation>();
//		re.add(r0);
//		re.add(r1);
//		re.add(r2);
//		re.add(r3);
//		re.add(r4);
//		re.add(r5);
//		re.add(r6);
//		Hypergraph hg = new Hypergraph(re);
		
		
		
		
		//test case2
//		List<List<String>> bags = new ArrayList<List<String>>();
//		List<String> bag0 = new ArrayList<String>();
//		bag0.add("4");
//		bag0.add("5");
//		bag0.add("6");
//		bag0.add("14");
//		bag0.add("15");
//		List<String> bag1 = new ArrayList<String>();
//		bag1.add("4");
//		bag1.add("5");
//		bag1.add("6");
//		bag1.add("11");
//		bag1.add("12");
//		bag1.add("14");
//		List<String> bag2 = new ArrayList<String>();
//		bag2.add("2");
//		bag2.add("4");
//		bag2.add("5");
//		bag2.add("6");
//		bag2.add("9");
//		bag2.add("11");
//		bag2.add("13");
//		bag2.add("14");
//		List<String> bag3 = new ArrayList<String>();
//		bag3.add("2");
//		bag3.add("10");
//		bag3.add("11");
//		List<String> bag4 = new ArrayList<String>();
//		bag4.add("2");
//		bag4.add("4");
//		bag4.add("7");
//		bag4.add("8");
//		bag4.add("9");
//		List<String> bag5 = new ArrayList<String>();
//		bag5.add("0");
//		bag5.add("1");
//		bag5.add("2");
//		bag5.add("3");
//		bag5.add("4");
//		bag5.add("5");
//		bag5.add("6");
//		bags.add(bag0);
//		bags.add(bag1);
//		bags.add(bag2);
//		bags.add(bag3);
//		bags.add(bag4);
//		bags.add(bag5);
//		HypertreeDecomposition hd = new HypertreeDecomposition(bags);
//		
//		List<String> a0 = new ArrayList<String>();
//		a0.add("0");
//		a0.add("1");
//		a0.add("2");
//		Relation r0 = new Relation(a0,100);
//		List<String> a1 = new ArrayList<String>();
//		a1.add("1");
//		a1.add("3");
//		a1.add("4");
//		Relation r1 = new Relation(a1,100);
//		List<String> a2 = new ArrayList<String>();
//		a2.add("3");
//		a2.add("5");
//		a2.add("6");
//		Relation r2 = new Relation(a2,100);
//		List<String> a3 = new ArrayList<String>();
//		a3.add("7");
//		a3.add("8");
//		a3.add("2");
//		Relation r3 = new Relation(a3,100);
//		List<String> a4 = new ArrayList<String>();
//		a4.add("8");
//		a4.add("9");
//		a4.add("4");
//		Relation r4 = new Relation(a4,100);
//		List<String> a5 = new ArrayList<String>();
//		a5.add("9");
//		a5.add("5");
//		a5.add("6");
//		Relation r5 = new Relation(a5,100);
//		List<String> a6 = new ArrayList<String>();
//		a6.add("10");
//		a6.add("11");
//		a6.add("2");
//		Relation r6 = new Relation(a6,100);
//		List<String> a7 = new ArrayList<String>();
//		a7.add("11");
//		a7.add("12");
//		a7.add("4");
//		Relation r7 = new Relation(a7,100);
//		List<String> a8 = new ArrayList<String>();
//		a8.add("12");
//		a8.add("5");
//		a8.add("6");
//		Relation r8 = new Relation(a8,100);
//		List<String> a9 = new ArrayList<String>();
//		a9.add("13");
//		a9.add("14");
//		a9.add("2");
//		Relation r9 = new Relation(a9,100);
//		List<String> a10 = new ArrayList<String>();
//		a10.add("14");
//		a10.add("15");
//		a10.add("4");
//		Relation r10 = new Relation(a10,100);
//		List<String> a11 = new ArrayList<String>();
//		a11.add("15");
//		a11.add("5");
//		a11.add("6");
//		Relation r11 = new Relation(a11,100);
//		List<Relation> re = new ArrayList<Relation>();
//		re.add(r0);
//		re.add(r1);
//		re.add(r2);
//		re.add(r3);
//		re.add(r4);
//		re.add(r5);
//		re.add(r6);
//		re.add(r7);
//		re.add(r8);
//		re.add(r9);
//		re.add(r10);
//		re.add(r11);
//		Hypergraph hg = new Hypergraph(re);
		
		
		
		//test case3
		List<List<String>> bags = new ArrayList<List<String>>();
		List<String> bag0 = new ArrayList<String>();
		bag0.add("26");
		bag0.add("25");
		bag0.add("24");
		bag0.add("10");
		bag0.add("12");
		List<String> bag1 = new ArrayList<String>();
		bag1.add("26");
		bag1.add("24");
		bag1.add("10");
		bag1.add("12");
		bag1.add("14");
		bag1.add("13");
		List<String> bag2 = new ArrayList<String>();
		bag2.add("24");
		bag2.add("10");
		bag2.add("12");
		bag2.add("14");
		bag2.add("13");
		bag2.add("19");
		bag2.add("20");
		List<String> bag3 = new ArrayList<String>();
		bag3.add("24");
		bag3.add("10");
		bag3.add("12");
		bag3.add("11");
		bag3.add("14");
		bag3.add("13");
		bag3.add("19");
		List<String> bag4 = new ArrayList<String>();
		bag4.add("24");
		bag4.add("10");
		bag4.add("9");
		bag4.add("12");
		bag4.add("11");
		bag4.add("18");
		bag4.add("19");
		List<String> bag5 = new ArrayList<String>();
		bag5.add("24");
		bag5.add("10");
		bag5.add("7");
		bag5.add("9");
		bag5.add("18");
		List<String> bag6 = new ArrayList<String>();
		bag6.add("24");
		bag6.add("23");
		bag6.add("7");
		bag6.add("8");
		bag6.add("5");
		bag6.add("17");
		bag6.add("18");
		List<String> bag7 = new ArrayList<String>();
		bag7.add("23");
		bag7.add("22");
		bag7.add("5");
		bag7.add("6");
		bag7.add("3");
		bag7.add("16");
		bag7.add("17");
		List<String> bag8 = new ArrayList<String>();
		bag8.add("22");
		bag8.add("3");
		bag8.add("1");
		bag8.add("4");
		bag8.add("15");
		bag8.add("16");
		bag8.add("21");
		List<String> bag9 = new ArrayList<String>();
		bag9.add("2");
		bag9.add("1");
		bag9.add("0");
		bag9.add("15");
		bag9.add("21");
		
		bags.add(bag0);
		bags.add(bag1);
		bags.add(bag2);
		bags.add(bag3);
		bags.add(bag4);
		bags.add(bag5);
		bags.add(bag6);
		bags.add(bag7);
		bags.add(bag8);
		bags.add(bag9);
		HypertreeDecomposition hd = new HypertreeDecomposition(bags);
		
		List<String> a0 = new ArrayList<String>();
		a0.add("0");
		a0.add("1");
		a0.add("2");
		Relation r0 = new Relation(a0,100);
		List<String> a1 = new ArrayList<String>();
		a1.add("1");
		a1.add("3");
		a1.add("4");
		Relation r1 = new Relation(a1,100);
		List<String> a2 = new ArrayList<String>();
		a2.add("3");
		a2.add("5");
		a2.add("6");
		Relation r2 = new Relation(a2,100);
		List<String> a3 = new ArrayList<String>();
		a3.add("5");
		a3.add("7");
		a3.add("8");
		Relation r3 = new Relation(a3,100);
		List<String> a4 = new ArrayList<String>();
		a4.add("7");
		a4.add("9");
		a4.add("10");
		Relation r4 = new Relation(a4,100);
		List<String> a5 = new ArrayList<String>();
		a5.add("9");
		a5.add("11");
		a5.add("12");
		Relation r5 = new Relation(a5,100);
		List<String> a6 = new ArrayList<String>();
		a6.add("11");
		a6.add("13");
		a6.add("14");
		Relation r6 = new Relation(a6,100);
		List<String> a7 = new ArrayList<String>();
		a7.add("0");
		a7.add("15");
		a7.add("2");
		Relation r7 = new Relation(a7,100);
		List<String> a8 = new ArrayList<String>();
		a8.add("15");
		a8.add("16");
		a8.add("4");
		Relation r8 = new Relation(a8,100);
		List<String> a9 = new ArrayList<String>();
		a9.add("16");
		a9.add("17");
		a9.add("6");
		Relation r9 = new Relation(a9,100);
		List<String> a10 = new ArrayList<String>();
		a10.add("17");
		a10.add("18");
		a10.add("8");
		Relation r10 = new Relation(a10,100);
		List<String> a11 = new ArrayList<String>();
		a11.add("18");
		a11.add("19");
		a11.add("10");
		Relation r11 = new Relation(a11,100);
		List<String> a12 = new ArrayList<String>();
		a12.add("19");
		a12.add("20");
		a12.add("12");
		Relation r12 = new Relation(a12,100);
		List<String> a13 = new ArrayList<String>();
		a13.add("20");
		a13.add("13");
		a13.add("14");
		Relation r13 = new Relation(a13,100);
		List<String> a14 = new ArrayList<String>();
		a14.add("0");
		a14.add("21");
		a14.add("2");
		Relation r14 = new Relation(a14,100);
		List<String> a15 = new ArrayList<String>();
		a15.add("21");
		a15.add("22");
		a15.add("4");
		Relation r15 = new Relation(a15,100);
		List<String> a16 = new ArrayList<String>();
		a16.add("22");
		a16.add("23");
		a16.add("6");
		Relation r16 = new Relation(a16,100);
		List<String> a17 = new ArrayList<String>();
		a17.add("23");
		a17.add("24");
		a17.add("8");
		Relation r17 = new Relation(a17,100);
		List<String> a18 = new ArrayList<String>();
		a18.add("24");
		a18.add("25");
		a18.add("10");
		Relation r18 = new Relation(a18,100);
		List<String> a19 = new ArrayList<String>();
		a19.add("25");
		a19.add("26");
		a19.add("12");
		Relation r19 = new Relation(a19,100);
		List<String> a20 = new ArrayList<String>();
		a20.add("26");
		a20.add("13");
		a20.add("14");
		Relation r20 = new Relation(a20,100);
		List<Relation> re = new ArrayList<Relation>();
		re.add(r0);
		re.add(r1);
		re.add(r2);
		re.add(r3);
		re.add(r4);
		re.add(r5);
		re.add(r6);
		re.add(r7);
		re.add(r8);
		re.add(r9);
		re.add(r10);
		re.add(r11);
		re.add(r12);
		re.add(r13);
		re.add(r14);
		re.add(r15);
		re.add(r16);
		re.add(r17);
		re.add(r18);
		re.add(r19);
		re.add(r20);
		Hypergraph hg = new Hypergraph(re);
			
//		List<Relation> re = LoomisWhitneyQueries(3);
//		Hypergraph hg= new Hypergraph(re);
		System.out.println("The vertices of hypergraph is: "+hg.getVertices());
		System.out.println("The hyperedges of hypergraph is: "+hg.getHyperedges());
		System.out.println("The minimum fractional edge cover is: "+minFractionalEdgeCover(re));
		System.out.println("The computed AGM Bound is: "+AGMBound(re));
		System.out.println("The bags of hypertree decomposition are:"+hd.getBags());
		System.out.println("The fractional hypertree width of given hypertree decomposition is:" +getFractionalHTW(hg,hd));
		System.out.println("The hypertree width of given hypertree decomposition is:" +getHTW(hg,hd));
		
	}

}
