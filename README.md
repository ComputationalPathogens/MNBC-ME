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
Run the following command to generate the taxonomy file of the prokaryotic chromosomes:  
````
../jdk-17.0.12/bin/java -cp MNBC_ME.jar -Xmx1G MNBC_ME taxonomy ncbi -i example/prok_seq/ -a example/assembly_summary_refseq.txt -n example/nodes.dmp -o example/taxonomy_prok.txt
````
(The following help menu displays by using ```-h```)  
```-a```:	Assembly summary file downloaded from NCBI (e.g. assembly_summary_refseq.txt downloaded from https://ftp.ncbi.nlm.nih.gov/genomes/refseq/))  
```-n```:	Taxonomy nodes.dmp file downoaded from NCBI (the file 'taxdmp.zip' is downloaded from https://ftp.ncbi.nlm.nih.gov/pub/taxonomy/)  
```-i```:	Input directory containing the (gzipped) files of reference genomes from NCBI GenBank and RefSeq (e.g. GCF_000009045.1_ASM904v1_genomic.fna.gz is a reference genome sequence file downloaded from RefSeq)  
```-o```:	Output taxonomy file for the database