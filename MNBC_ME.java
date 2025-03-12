/**
 * 
 * @author Ruipeng Lu (ruipeng.lu@inspection.gc.ca)
 *
 */

public class MNBC_ME {
	public static void main(String[] args) {
		if(args == null || args.length == 0) {
			help();
			System.exit(0);
		}
		
		if(args[0].equals("taxonomy")) {
			if(args.length == 1) {
				help();
				System.exit(0);
			}
			
			if(args[1].equals("ncbi")) {
				MNBC_ME_taxonomy.execute(args);
			} else if(args[1].equals("plsdb")) {
				MNBC_ME_taxonomyPlsdb.execute(args);
			} else if(args[1].equals("virushostdb")) {
				MNBC_ME_taxonomyVirushostdb.execute(args);
			} else {
				help();
			}
			
		} else if(args[0].equals("build")) {
			MNBC_ME_build.execute(args);
		} else if(args[0].equals("classify")) {
			MNBC_ME_classify.execute(args);
		} else {
			help();
		}
	}
	
	private static void help() {
		System.out.println("MNBC-ME (v1.0): a tool for mobile element identification");
		System.out.println("Step 1: generate taxonomy file for NCBI prokaryoptic genomes, PLSDB plasmids or Virus-Host DB viruses -- Run 'MNBC_ME taxonomy ncbi -h', 'MNBC_ME taxonomy plsdb -h' or 'MNBC_ME taxonomy virushostdb -h' for help");
		System.out.println("Step 2: build index files for reference sequences -- Run 'MNBC_ME build -h' for help");
		System.out.println("Step 3: classify reads -- Run 'MNBC_ME classify -h' for help");		
	}
}
