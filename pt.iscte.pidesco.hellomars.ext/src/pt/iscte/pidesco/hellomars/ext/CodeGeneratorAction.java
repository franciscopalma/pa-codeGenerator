package pt.iscte.pidesco.hellomars.ext;

import java.util.HashMap;
import java.util.Map;

import pt.iscte.pidesco.codegenerator.extensibility.MethodAction;

public class CodeGeneratorAction implements MethodAction {

	
	@Override
	public HashMap<String, String> run() {
		HashMap<String, String> map = new HashMap<String, String>();
		String name = "setName";
		String method = "\t" + "public String " + name + "(){\n\t\treturn \"\";\n\t}";
		map.put(name, method);
		String name2 = "setID";
		String method2 = "\t" + "public String " + name2 + "(){\n\t\treturn \"\";\n\t}";
		map.put(name2, method2);
		return map;

	}

}
