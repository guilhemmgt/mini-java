package fr.n7.stl.block;

import java.io.File;

class Driver {

	public static void main(String[] args) throws Exception {
		Parser parser = null;
		if (args.length == 0) {
			String tests_vrais_prefixe = "vrai-test-";
			String tests_faux_prefixe = "faux-test-";
			String tests_suffixe = ".bloc";
			
			boolean lancer_tests_vrais = true;
			boolean lancer_tests_faux = false;
			boolean lancer_tests_prof = false;
			
			 File[] dirFiles = new File("./test-block/").listFiles();

			 if (lancer_tests_prof) { 
				for (File file : dirFiles) {
					String name = file.getName();
					if (name.startsWith("test0") && name.endsWith(tests_suffixe)) {
						parser = new Parser("./test-block/" + name);
						try {
							parser.parse();
						} catch (Exception e) {
							e.printStackTrace();
						}					
					}
				}
			}
			 
			 if (lancer_tests_vrais) {
				 for (File file : dirFiles) {
					 String name = file.getName();
					 if (name.startsWith(tests_vrais_prefixe) && name.endsWith(tests_suffixe)) {
						 System.out.println("\n=== TEST VRAI " + name.substring(tests_vrais_prefixe.length(), name.length() - tests_suffixe.length()) + " ===");
						 parser = new Parser("./test-block/" + name);
						 parser.parse();
					 }
				 }
			 }
			 
			 if (lancer_tests_faux) {
				 for (File file : dirFiles) {
					 String name = file.getName();
					 if (name.startsWith(tests_faux_prefixe) && name.endsWith(tests_suffixe)) {
						 System.out.println("\n=== TEST FAUX " + name.substring(tests_faux_prefixe.length(), name.length() - tests_suffixe.length()) + " ===");
						 parser = new Parser(name);
						 try {
						 parser.parse();
						 } catch (Exception e) {
							 e.printStackTrace();
						 }
					 }
				 }
			 }

		} else {
			for (String name : args) {
				parser = new Parser( name );
				parser.parse();
			}
		}
	}
	
}