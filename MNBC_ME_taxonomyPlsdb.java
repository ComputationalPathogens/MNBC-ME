import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class MNBC_ME_taxonomyPlsdb {
	public static void main(String[] args) {
		String seqDirPath = args[0]; // /Drives/O/USERS/rlu/3types/plsdb_seq/
		String nuccoreFilePath = args[1]; // /Drives/O/USERS/rlu/3types/plsdb_nuccore.tsv
		String taxonomyFilePath = args[2]; // /Drives/O/USERS/rlu/3types/plsdb_taxonomy.csv
		String outputFilePath = args[3];
		
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
}
