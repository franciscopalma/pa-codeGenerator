package pt.iscte.pidesco.codegenerator.extensibility;

import java.util.HashMap;

public interface MethodAction {
	
	/**
	 * 
	 * @return a Hasmap, a first string is method's name and second string is the full method's body 
	 * 
	 * 
	 */
	HashMap<String, String> run();

}
