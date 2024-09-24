import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class MNBC_taxonomy { //Add the option to simultaneously write taxonomy information of plasmids within the reference genomes  
	public static String genomeDirPath; //Directory containing downloaded RefSeq reference genomes of interest, these genomes will be used as the reference database
	public static String refseqAssemblySummaryPath; //Example filename: assembly_summary_refseq.txt
	public static String nodeDmpPath; //Example filename: nodes.dmp
	public static String outputPath; //Example filename: refSeq_prokaryote_complete_genomes_ok_status_metainfo.txt
	public static boolean plasmid; //Whether to write taxonomy information of plasmids
	
	public static void main(String[] args) {
		if(args.length == 1) {
			printHelpInfo();
			System.exit(0);
		}
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].startsWith("-")) {
				switch(args[i].charAt(1)) {
					case 'a':
						refseqAssemblySummaryPath = args[i + 1];
						break;
					case 'n':
						nodeDmpPath = args[i + 1];
						break;
					case 'i':
						genomeDirPath = args[i + 1];
						break;
					case 'o':
						outputPath = args[i + 1];
						break;
					case 'p':
						plasmid = true;
						break;
					case 'h':
						printHelpInfo();
						System.exit(0);
				}
			}
		}
		
		if((genomeDirPath == null) || (refseqAssemblySummaryPath == null) || (nodeDmpPath == null) || (outputPath == null)) {
			System.out.println("Error: not all required parameters are set -- Run 'MNBC taxonomy -h' for help");
			System.exit(0);
		}
		
		//ArrayList<String> refseqAssemblyIDs = readAssemlyIds(genomeDirPath);
		HashMap<String, String[]> refseqAssemblyID2Taxid = readRefseqAssemblySummary(refseqAssemblySummaryPath);
		HashMap<String, String> taxid2TaxLevel = new HashMap<String, String>();
		HashMap<String, String> taxid2ParentTaxid = readNodeDmp(nodeDmpPath, taxid2TaxLevel);
		
		try {
			PrintWriter writer = new PrintWriter(outputPath);
			writer.print("Assembly ID\tType\ttaxid.species\ttaxid.genus\ttaxid.family\ttaxid.order\ttaxid.class\ttaxid.phylum\ttaxid.superkingdom\tDescription\n");
			generateGenomeTaxonomyTableRows(writer, genomeDirPath, refseqAssemblyID2Taxid, taxid2TaxLevel, taxid2ParentTaxid);
			writer.close();
		} catch(Exception e) {
			System.out.println("Error in writing results to " + outputPath);
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("done");
	}
	
	private static void generateGenomeTaxonomyTableRows(PrintWriter writer, String genomeDirPath, HashMap<String, String[]> refseqAssemblyID2Taxid, HashMap<String, String> taxid2TaxLevel, HashMap<String, String> taxid2ParentTaxid) {
		File[] genomeFiles = new File(genomeDirPath).listFiles();
		for(File genomeFile : genomeFiles) {
			String[] fields = genomeFile.getName().split("_");
			String assemblyID = fields[0] + "_" + fields[1];			
			String[] ranks = new String[7];
			//System.out.println("assemblyID " + assemblyID);
			String[] taxidAndName = refseqAssemblyID2Taxid.get(assemblyID);
			if(taxidAndName == null) {
				System.out.println("ERROR: couldn't find the assembly ID " + assemblyID + " in the assembly summary file!");
				continue;
			}			
			
			ranks[0] = taxidAndName[0];
			String supposedSpeciesLevel = taxid2TaxLevel.get(ranks[0]);
			if(!supposedSpeciesLevel.equals("species")) {
				System.out.println("ERROR: " + ranks[1] + " is supposed to be a species according to the assembly summary file, but nodes.dmp reports " + supposedSpeciesLevel + "! Exiting");
				System.exit(1);
			}				
			fillRanksArray(ranks[0], ranks, taxid2TaxLevel, taxid2ParentTaxid);			
			
			String row = assemblyID;
			if(ranks[6].equals("10239")) {
				row += "\tvirus";
			} else {
				row += "\tchromosome";
			}
			for(String rank : ranks) {
				row += "\t" + rank;
			}
			row += "\t" + taxidAndName[1] + "\n";			
			//System.out.println("One row result: " + row);
			writer.print(row);
			
			if(plasmid) {
				BufferedReader reader = null;
				try {
					if(genomeFile.getName().endsWith(".gz")) {
						reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(genomeFile)), "UTF-8"));
					} else {
						reader = new BufferedReader(new FileReader(genomeFile));
					}
					
					String line = null;
					while((line = reader.readLine()) != null) {
						if(line.startsWith(">")) {
							line = line.toLowerCase();
							if(line.contains("plasmid")) {
								row = line.split("\\s+")[0].substring(1) + "\tplasmid";
								for(String rank : ranks) {
									row += "\t" + rank;
								}
								row += "\t" + assemblyID + "\n";
								writer.print(row);
							}
						}
					}
					reader.close();
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(1);
				}				
			}			
		}
		writer.close();
	}
	
	private static void fillRanksArray(String initial, String[] ranks, HashMap<String, String> taxid2TaxLevel, HashMap<String, String> taxid2ParentTaxid) {
		String currentTaxid = initial;
		while(!currentTaxid.isEmpty()) {
			if(currentTaxid.equals("1")) {
				System.out.println("ERROR: nodes.dmp doesn't include all 7 primary-level taxon ids for " + initial + "! Exiting");
				System.exit(1);
			}
			String parentTaxid = taxid2ParentTaxid.get(currentTaxid);
			String level = taxid2TaxLevel.get(parentTaxid);
			
			if(level.equals("species")) {
				ranks[0] = parentTaxid;
				currentTaxid = parentTaxid;
				continue;
			} else if(level.equals("genus")) {
				ranks[1] = parentTaxid;
				currentTaxid = parentTaxid;
				continue;
			} else if(level.equals("family")) {
				ranks[2] = parentTaxid;
				currentTaxid = parentTaxid;
				continue;
			} else if(level.equals("order")) {
				ranks[3] = parentTaxid;
				currentTaxid = parentTaxid;
				continue;
			} else if(level.equals("class")) {
				ranks[4] = parentTaxid;
				currentTaxid = parentTaxid;
				continue;
			} else if(level.equals("phylum")) {
				ranks[5] = parentTaxid;
				currentTaxid = parentTaxid;
				continue;
			} else if(level.equals("superkingdom")) {
				ranks[6] = parentTaxid;
				currentTaxid = "";
			} else {
				currentTaxid = parentTaxid;
				continue;
			}										
		}
	}
	
	private static HashMap<String, String> readNodeDmp(String nodeDmpPath, HashMap<String, String> taxid2TaxLevel) {
		HashMap<String, String> taxid2ParentTaxid = new HashMap<String, String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nodeDmpPath));
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] fields = line.split("\\|");
				String taxid = fields[0].trim();
				taxid2ParentTaxid.put(taxid, fields[1].trim());
				taxid2TaxLevel.put(taxid, fields[2].trim());
			}
			reader.close();
		} catch(Exception e) {
			System.out.println("Error in reading nodes.dmp file: " + nodeDmpPath);
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Finished reading nodes.dmp file: " + nodeDmpPath);
		return taxid2ParentTaxid;
	}
	
	private static HashMap<String, String[]> readRefseqAssemblySummary(String refseqAssemblySummaryPath) {
		HashMap<String, String[]> refseqAssemblyID2Taxid = new HashMap<String, String[]>(); //String[0]: indicating if taxid is at species level (1) or strain level (0), String[1]: species or strain taxid, String[2]: organism name
		
		String line = null;
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(refseqAssemblySummaryPath));
			reader.readLine();
			reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] fields = line.split("\t");				
				refseqAssemblyID2Taxid.put(fields[0], new String[] {fields[6], fields[7] + (fields[8].isBlank() ? "" : " " + fields[8])});							
			}
			reader.close();
		} catch(Exception e) {
			System.out.println("Error in reading assembly summary file: " + line);
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Finished reading assembly summary file: " + refseqAssemblySummaryPath);
		return refseqAssemblyID2Taxid;
	}
	
	/*private static ArrayList<String> readAssemlyIds(String genomeDirPath) {
		ArrayList<String> refseqAssemblyIDs = new ArrayList<String>();
		
		File[] genomes = new File(genomeDirPath).listFiles();
		for(File genome : genomes) {
			String[] fields = genome.getName().split("_");
			refseqAssemblyIDs.add(fields[0] + "_" + fields[1]);
		}
		
		System.out.println("Finished reading " + refseqAssemblyIDs.size() + " assembly IDs from " + genomeDirPath);
		return refseqAssemblyIDs;
	}*/
	
	private static void printHelpInfo() {
		System.out.println("This MNBC_taxonomy tool (v2.0) generates the taxonomy file for a reference database.");
		System.out.println("-h:	Show this help menu");
		System.out.println("-a:	Assembly summary file downloaded from NCBI (e.g. assembly_summary_refseq.txt from https://ftp.ncbi.nlm.nih.gov/genomes/refseq/))");
		System.out.println("-n:	Taxonomy nodes.dmp file downoaded from NCBI (e.g. taxdmp.zip from https://ftp.ncbi.nlm.nih.gov/pub/taxonomy/)");		
		System.out.println("-i:	Input directory containing the (gzipped) files of reference sequences in the database (e.g. GCF_000009045.1_ASM904v1_genomic.fna.gz is a reference genome sequence file downloaded from RefSeq)");
		System.out.println("-p:	Also extract taxonomy of plasmids in the reference genomes");
		System.out.println("-o:	Output taxonomy file for the database");
	}
}
