
package Token;

// A simple pairs class used to store two strings together. (Though it can work with any combination of variable types, this program only uses it with strings.)

public class Pair<L,R> {
	    private L l; // the first variable of the pair
	    private R r; // the second variable of the pair
	    
	    // creates Pair
	    public Pair(L l, R r){
	        this.l = l;
	        this.r = r;
	    }
	    
	    // returns requested element
	    public L getL(){ return l; } // returns first variable
	    public R getR(){ return r; } // returns second variable
	    
	    // changes requested element
	    public void setL(L l){ this.l = l; } // changes first variable
	    public void setR(R r){ this.r = r; } // changes second variable 
	
}
