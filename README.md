# MNBC-ME

The MNBC-ME tool for mobile element identification

*********************************************************************************************************  
## Install Java
Please download and install Java JDK (version >= 17.0.4) from https://www.oracle.com/ca-en/java/technologies/downloads. For example, use the following Linux command to download:  
````
wget https://download.oracle.com/java/17/archive/jdk-17.0.12_linux-x64_bin.tar.gz
````
Decompress the file with the following command.  
````
tar -xzvf jdk-17.0.12_linux-x64_bin.tar.gz
````
Then a new folder 'jdk-17.0.12' appears.<br/>

(Alternatively, you can use the command "<b>mamba create -n java -c conda-forge openjdk</b>" to install Java JDK in mamba/conda, then run the command "<b>mamba activate java</b>" to activate the Java environment)  
## Install MNBC-ME
Please download this repository using the following command, then a new folder 'MNBC' appears.  
````
git clone https://github.com/ComputationalPathogens/MNBC-ME
````
Change to the 'example' subfolder using the following command.  
````
cd MNBC-ME/example
````
Decompress the files 'assembly_summary_refseq.zip' and 'nodes.zip' using the following command, then the files 'assembly_summary_refseq.txt' and 'nodes.dmp' appear.  
````
unzip assembly_summary_refseq.zip
````
````
unzip nodes.zip
````
*********************************************************************************************************  

## Run MNBC-ME on the example
The 'example' folder includes a small demo, which is described below to demonstrate how to use the tool. All input files used and output files produced in the following 3 steps are included in this folder, so the commands can be directly run in a terminal window.  

### Problem description:  
The 'reads.fasta' file contains 6 simulated short reads to be classified. The 2 reads (read_95 and read_32) are from the chromosomes of Pseudomonas aeruginosa strain=R11. The 2 reads (read_13 and read_6) are from the Escherichia coli strain GN05476 plasmid unnamed7. The 2 reads (read_4 and read_8) are from the Wencheng Sm shrew coronavirus. The database contains three reference sequences: GCF_038431905.1 is Pseudomonas aeruginosa strain=2016N06-105, NZ_CP124330.1 is Escherichia coli strain AVS0787 plasmid pAVS0787-B, and NC_048211 is Wencheng Sm shrew coronavirus.  

### Tool usage (3 steps):  
Change to the 'MNBC-ME' folder using the following command.  
````
cd ..
````

<b>(Optional pre-step) add Java to Linux PATH variable:</b>  
Replace <parent_directory> in the following line with the actual path, then append this line to the end of the '.bashrc' file in your home directory.  
````
export PATH=<parent_directory>/jdk-17.0.12/bin:${PATH}
````
Either open a new terminal window, or run the following command in the current window.
````
source ~/.bashrc
````
Then the commands in the following 3 steps can be simplified to "<b>java -cp MNBC.jar -Xmx1G MNBC ...</b>".

(If you installed Java JDK in mamba/conda, then the 3 commands can be also simplified to "<b>java -cp MNBC.jar -Xmx1G MNBC ...</b>")  

<b>Step 1</b>:  
Run the following command to generate the taxonomy file for prokaryotic chromosomes from NCBI GenBank and RefSeq:  
````
../jdk-17.0.12/bin/java -cp MNBC_ME.jar -Xmx1G MNBC_ME taxonomy ncbi -i example/prok_seq/ -a example/assembly_summary_refseq.txt -n example/nodes.dmp -o example/taxonomy_prok.txt
````
(The following help menu displays by using ```-h```)  
```-a```:	Assembly summary file downloaded from NCBI (e.g. assembly_summary_refseq.txt downloaded from https://ftp.ncbi.nlm.nih.gov/genomes/refseq/))  
```-n```:	Taxonomy nodes.dmp file downoaded from NCBI (the file 'taxdmp.zip' is downloaded from https://ftp.ncbi.nlm.nih.gov/pub/taxonomy/)  
```-i```:	Input directory containing the (gzipped) files of reference genomes from NCBI GenBank and RefSeq (e.g. GCF_000009045.1_ASM904v1_genomic.fna.gz is a reference genome sequence file downloaded from RefSeq)  
```-o```:	Output taxonomy file

Run the following command to generate the taxonomy file for plasmids from the PLSDB database:  
````
../jdk-17.0.12/bin/java -cp MNBC_ME.jar -Xmx1G MNBC_ME taxonomy plsdb -i example/plsdb_seq/ -n example/plsdb_nuccore.tsv -t example/plsdb_taxonomy.csv -o example/taxonomy_plsdb.txt
````
(The following help menu displays by using ```-h```)  
```-i```:	Input directory containing plasmidic reference sequences in PLSDB, each file containing one plasmid sequence  
```-n```:	Nuccore file in PLSDB (i.e. nuccore.tsv)  
```-t```:	Taxonomy file in PLSDB (i.e. taxonomy.csv)  
```-o```:	Output taxonomy file

Run the following command to generate the taxonomy file for viruses from the Virus-Host DB database:  
````
../jdk-17.0.12/bin/java -cp MNBC_ME.jar -Xmx1G MNBC_ME taxonomy virushostdb -i example/virus_seq/ -s example/virushostdb.tsv -t example/taxid2parents_VH.tsv -o example/taxonomy_virus.txt
````
(The following help menu displays by using ```-h```)  
```-i```:	Input directory containing viral reference sequences in Virus-Host DB, each file containing one virus sequence  
```-s```:	Summary file in Virus-Host DB (i.e. virushostdb.tsv)  
```-t```:	Taxonomy file in Virus-Host DB (i.e. taxid2parents_VH.tsv)  
```-o```:	Output taxonomy file

Merge the three files 'taxonomy_prok.txt', 'taxonomy_plsdb.txt' and 'taxonomy_virus.txt' into one file 'taxonomy.txt', and the three folders 'prok_seq', 'plsdb_seq' and 'virus_seq' into one folder 'seq'.

<b>Tip</b>:
Sometimes a genome assembly accession (e.g. GCF_021047685.1) does not exist in the assembly summary file, or its taxon number does not exist in the nodes.dmp file. If this happens MNBC will output error information for manual check.

<b>Step 2</b>:  
Run the following command to build the database:  
````
../jdk-17.0.12/bin/java -cp MNBC_ME.jar -Xmx1G MNBC_ME build -c 2 -i example/seq/ -o example/db/ -p
````
(The following help menu displays by using ```-h```)  
```-c```:	Number of threads  
```-i```:	Input directory containing the (gzipped) files of reference sequences (e.g. GCF_000834455.1_ASM83445v1_genomic.fna.gz is a reference genome sequence file downloaded from RefSeq)  
```-o```: Existing output database directory (please first make this directory if it doesn't already exist)  
```-k (optional)```: K-mer length (an integer between 1 and 15 inclusive) (default 15)  
```-p (optional toggle)```: If set, build index files for plasmid sequences as well  
```-f (optional)```: Filtering threshold on the sequence length (an integer >= 0). Sequences with lengths below this threshold are skipped.(default 0: all sequences are retained).  
```-b (optional)```: Log file of the previous prematurely killed run (i.e. .out file in Slurm). This allows breakpoint resumption after the previous run exits abnormally.

<b>Step 3</b>:  
Run the following command to classify the reads against the database:  
````
../jdk-17.0.12/bin/java -cp MNBC_ME.jar -Xmx1G MNBC_ME classify -c 2 -d example/db/ -m example/taxonomy.txt -o example/result.txt -u 0 -t 1 example/reads.fasta
````
(The following help menu displays by using ```-h```)  
```-c```: Number of threads  
```-d```: Input database directory  
```-m```:	Input taxonomy file  
```-o```:	Output classification file  
```-t```:	Type of reads (paired-end: 2, single-end: 1). Paired-end reads have two following (gzipped) .fasta/.fastq files. Single-end reads have one following (gzipped) .fasta/.fastq file.  
```-k (optional)```: K-mer length (an integer between 1 and 15 inclusive) (default 15)  
```-u (optional)```: Filtering threshold on the ratio of common read-genome minimizers over all read minimizers (default 0.35). Higher values lead to less reads classified, though with higher confidence. Lower values lead to more reads classified (i.e. leave less reads unclassified). When set to 0, the read will be left unclassified only if all reference sequences share no minimizers with it.  
```-p (optional)```: Penalty for absent minimizers (default -2000)  
```-e (optional)```: Threshold on the difference between adjacent scores (default 1500)

<b>Tip</b>:
When using a large reference database, increase the memory amount that MNBC-ME can use in Steps 2 and 3 by adjusting the '-Xmx' parameter (e.g. -Xmx200G), and also increase the number of CPU cores by adjusting the '-c' parameter (e.g. -c 100) to accelerate.

## Format of the taxonomy file
In the tab-delimited taxonomy file 'taxonomy.txt' produced in Step 1, the 1st row contains column headers, and each subsequent row gives the taxonomy information for a reference sequence in the database:
````
Accession	Type	Species	Genus	Family	Order	Class	Phylum	Kingdom	Domain	Organism  
GCF_038431905.1	chromosome	287	286	135621	72274	1236	1224	3379134	2	Pseudomonas aeruginosa strain=2016N06-105  
NZ_CP124330.1	plasmid	562	561	543	91347	1236	1224	null	2	Escherichia coli strain AVS0787 plasmid pAVS0787-B, complete sequence  
NC_048211	virus	1508228	693996	11118	76804	2732506	2732408	2732396	10239	Wencheng Sm shrew coronavirus  
````
The 1st column is the sequence accession. The 2nd column is the general type of the sequence. The 3rd to 10th columns are the taxon numbers of the prokaryotic or viral genome from the species level to the domain level, or the host taxon numbers of the plasmid. The 11th column is the string name (species and strain) of the genome, or the string name of the host of the plasmid.

## Format of the classification file
In the tab-delimited classification file 'result.txt' produced in Step 3, the 1st row contains column headers, and each subsequent row gives the classification for a read:  
````
Read	Species	Genus	Family	Order	Class	Phylum	Kingdom	Domain	Candidates  
SRR227300.1.1	562	561	543	91347	1236	1224	3379134	2	GCF_022869985.1  
SRR227300.2.1	562	561	543	91347	1236	1224	3379134	2	GCF_022869985.1  
SRR227300.26.1	562	561	543	91347	1236	1224	3379134	2	GCF_022869985.1  
SRR227300.27.1	562	561	543	91347	1236	1224	3379134	2	GCF_022869985.1  
SRR227300.28.1	562	561	543	91347	1236	1224	3379134	2	GCF_022869985.1  
SRR032501.1.2	29485	629	1903411	91347	1236	1224	3379134	2	GCF_000834455.1  
SRR032501.2.2	29485	629	1903411	91347	1236	1224	3379134	2	GCF_000834455.1  
SRR032501.3.2	29485	629	1903411	91347	1236	1224	3379134	2	GCF_000834455.1  
SRR032501.4.2	29485	629	1903411	91347	1236	1224	3379134	2	GCF_000834455.1  
SRR032501.5.2	29485	629	1903411	91347	1236	1224	3379134	2	GCF_000834455.1  
SRR095845.1746	unclassified  
SRR095845.1745	unclassified  
SRR095845.1747	unclassified  
SRR095845.1748	unclassified  
SRR095845.1744	unclassified  
````
The 1st column is the read ID, the 2nd to 9th columns are assigned taxon numbers from the species level to the domain level, and the last column is the accessions of all candidate reference genomes determining the predicted species. It can be seen that all 15 reads were correctly classified (i.e. assigned the correct species-level taxon numbers, or labelled as unclassified).