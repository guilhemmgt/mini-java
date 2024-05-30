package fr.n7.stl.block;

import java.io.File;

class Driver {

	public static void main(String[] args) throws Exception {

		Parser parser = null;
		if (args.length == 0) {
			String tests_ok_suffixe = "_OK";
			String tests_ko_suffixe = "_KO";
			String tests_suffixe = ".mjava";
			String path = "./test-mjava/";

			boolean lancer_ok = true;
			boolean lancer_ko = false;

			File[] dirFiles = new File(path).listFiles();
			int ok_succes = 0, ok_echec = 0;
			int ko_succes = 0, ko_echec = 0;

			if (lancer_ok) {
				for (File file : dirFiles) {
					String name = file.getName();
					if (name.endsWith(tests_ok_suffixe + tests_suffixe)) {
						System.out.println("\n=== OK "
								+ name.substring(0, name.length() - (tests_ok_suffixe + tests_suffixe).length())
								+ " ===");
						try {
							parser = new Parser(path + name);
							parser.parse();
							ok_succes++;
						} catch (Exception e) {
							e.printStackTrace();
							ok_echec++;
						}
					}
				}
			}

			if (lancer_ko) {
				for (File file : dirFiles) {
					String name = file.getName();
					if (name.endsWith(tests_ko_suffixe + tests_suffixe)) {
						System.out.println("\n=== KO "
								+ name.substring(0, name.length() - (tests_ko_suffixe + tests_suffixe).length())
								+ " ===");
						try {
							parser = new Parser(path + name);
							parser.parse();
							ko_succes++;
						} catch (Exception e) {
							e.printStackTrace();
							ko_echec++;
						}
					}
				}
			}

			System.out.println("\n--------------------");
			if(lancer_ok)
				System.out.println("OK: " + (ok_succes + ok_echec) + " tests, " + ok_succes + " succès, " + ok_echec + " échecs.");
			if (lancer_ko)
				System.out.println("KO: " + (ok_succes + ok_echec) + " tests, " + ko_echec + " échecs, " + ko_succes + " succès.");

		} else {
			// lancer des tests précis
			for (String name : args) {
				parser = new Parser(name);
				parser.parse();
			}
		}
	}

}