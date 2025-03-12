/**
 * 
 * @author Ruipeng Lu (ruipeng.lu@inspection.gc.ca)
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class MNBC_ME_taxonomyPlsdb {
	private static String seqDirPath; // /Drives/O/USERS/rlu/3types/plsdb_seq/
	private static String nuccoreFilePath; // /Drives/O/USERS/rlu/3types/plsdb_nuccore.tsv
	private static String taxonomyFilePath; // /Drives/O/USERS/rlu/3types/plsdb_taxonomy.csv
	private static String outputFilePath;
	
	public static void execute(String[] args) {
		for(int i = 2; i < args.length; i++) {
			if(args[i].startsWith("-")) {
				switch(args[i].charAt(1)) {
					case 'i':
						seqDirPath = args[i + 1];
						break;
					case 'n':
						nuccoreFilePath = args[i + 1];
						break;
					case 't':
						taxonomyFilePath = args[i + 1];
						break;
					case 'o':
						outputFilePath = args[i + 1];
						break;
					case 'h':
						printHelpInfo();
						System.exit(0);
				}
			}
		}
		
		HashMap<String, String> seqId2Name = new HashMap<>();
		HashMap<String, String> seqId2Tax = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nuccoreFilePath));
			String line = reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] fields = line.split("\t");
				seqId2Tax.put(fields[1], fields[12]);
				if(fields[2].startsWith("\"")) {
					seqId2Name.put(fields[1], fields[2].substring(1, fields[2].length() - 1));
				} else {
					seqId2Name.put(fields[1], fields[2]);
				}
				
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("seqId2Tax size: " + seqId2Tax.size() + ", seqId2Name size: " + seqId2Name.size());
		
		HashMap<String, String[]> taxId2Ids = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(taxonomyFilePath));
			String line = reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				String[] taxIds = new String[8];
				taxIds[7] = fields[11].split("\\.")[0];
				taxIds[6] = null;
				taxIds[5] = fields[12].split("\\.")[0];
				taxIds[4] = fields[13].split("\\.")[0];
				taxIds[3] = fields[14].split("\\.")[0];
				taxIds[2] = fields[15].split("\\.")[0];
				taxIds[1] = fields[16].split("\\.")[0];
				taxIds[0] = fields[17].split("\\.")[0];
				taxId2Ids.put(fields[0], taxIds);
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("taxId2Ids size: " + taxId2Ids.size());
		
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath));
			writer.print("Accession\tType\tSpecies\tGenus\tFamily\tOrder\tClass\tPhylum\tKingdom\tDomain\tOrganism\n");
		
			File[] seqFiles = new File(seqDirPath).listFiles();
			for(File seqFile : seqFiles) {
				String filename = seqFile.getName();
				String seqId = filename.substring(0, filename.length() - 7);
				writer.print(seqId + "\tplasmid");
				String[] taxIds = taxId2Ids.get(seqId2Tax.get(seqId));
				for(String taxId : taxIds) {
					writer.print("\t" + taxId);					
				}
				writer.print("\t" + seqId2Name.get(seqId) + "\n");
			}
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("done");
	}
	
	private static void printHelpInfo() {
		System.out.println("This program generates the taxonomy file for plasmids from the PLSDB database.");
		System.out.println("-h:	Show this help menu");
		System.out.println("-i:	Input directory containing plasmid sequences in PLSDB, each file containing one plasmid sequence");
		System.out.println("-n:	Nuccore file in PLSDB (i.e. nuccore.tsv)");		
		System.out.println("-t:	Taxonomy file in PLSDB (i.e. taxonomy.csv)");
		System.out.println("-o:	Output taxonomy file");
	}
}
