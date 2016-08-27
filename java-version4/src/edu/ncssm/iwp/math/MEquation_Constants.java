package edu.ncssm.iwp.math;

import java.util.*;

class MEquation_Constants {

    static Hashtable vars;

    public MEquation_Constants() {
	vars=new Hashtable();
	vars.put("PI.value",new Double(Math.PI));
    }
    
    public void put(String key, double value) {
	vars.put(key,new Double(value));
    }
    public double get(String key) {
	return ((Double)vars.get(key)).doubleValue();
    }
    public Enumeration keys() {
	return vars.keys();
    }
}
