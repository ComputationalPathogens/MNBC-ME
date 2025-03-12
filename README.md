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

