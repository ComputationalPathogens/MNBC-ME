import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class MNBC_ME_taxonomyVirushostdb {
	public static void main(String[] args) {
		String virushostdbTsvFilePath = args[0]; // /Drives/O/USERS/rlu/3types/virushostdb.tsv
		String taxid2parents_VHTsvFilePath = args[1]; // /Drives/O/USERS/rlu/3types/taxid2parents_VH.tsv
		String outputFilePath = args[2];
		String virusSeqDirPath = args[3]; // /Drives/O/USERS/rlu/3types/virus_seq
		
		HashMap<String, String> seqId2TaxId = new HashMap<>();
		HashMap<String, String> seqId2VirusName = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(virushostdbTsvFilePath));
			String line = reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] fields = line.split("\t");
				String[] seqIds = fields[3].split(",");
				for(String seqId : seqIds) {
					seqId = seqId.trim();
					seqId2TaxId.put(seqId, fields[0]);
					seqId2VirusName.put(seqId, fields[1]);
				}				
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("seqId2TaxId size: " + seqId2TaxId.size() + ", seqId2VirusName size: " + seqId2VirusName.size());
		
		HashMap<String, String[]> taxId2ParentIds = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(taxid2parents_VHTsvFilePath));
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] fields = line.split("\t");
				String[] parentIds = new String[8];
				for(int i = 1; i < fields.length; i++) {
					String[] subfields = fields[i].split("\\|");
					switch(subfields[1]) {
						case "species":
							parentIds[0] = subfields[2];
							break;
						case "genus":
							parentIds[1] = subfields[2];
							break;
						case "family":
							parentIds[2] = subfields[2];
							break;
						case "order":
							parentIds[3] = subfields[2];
							break;
						case "class":
							parentIds[4] = subfields[2];
							break;
						case "phylum":
							parentIds[5] = subfields[2];
							break;
						case "kingdom":
							parentIds[6] = subfields[2];
							break;
						case "superkingdom":
							parentIds[7] = subfields[2];
							break;
					}
				}
				taxId2ParentIds.put(fields[0], parentIds);
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("taxId2ParentIds size: " + taxId2ParentIds.size());
		
		//System.out.println(Arrays.toString(taxId2ParentIds.get("2047895")));
		//System.exit(0);
		
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath));
			writer.print("Accession\tType\tSpecies\tGenus\tFamily\tOrder\tClass\tPhylum\tKingdom\tDomain\tOrganism\n");
			
			File[] virusSeqFiles = new File(virusSeqDirPath).listFiles();
			for(File virusSeqFile : virusSeqFiles) {
				String filename = virusSeqFile.getName();
				String seqId = filename.substring(0, filename.length() - 7);
				writer.print(seqId + "\tvirus");
				String[] taxIds = taxId2ParentIds.get(seqId2TaxId.get(seqId));
				for(String taxId : taxIds) {
					writer.print("\t" + taxId);
				}
				writer.print("\t" + seqId2VirusName.get(seqId) + "\n");
			}
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("done");
	}
}
