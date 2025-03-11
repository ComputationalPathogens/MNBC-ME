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
		System.out.println("MNBC (v1.2): a taxonomic read classifier");
		System.out.println("Step 1: generate taxonomy file -- Run 'MNBC taxonomy -h' for help");
		System.out.println("Step 2: build reference database -- Run 'MNBC build -h' for help");
		System.out.println("Step 3: classify reads -- Run 'MNBC classify -h' for help");		
	}
}
