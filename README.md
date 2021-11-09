Author: Marcel Setz  
Version: 1.0  

Name: Methylation_JavaWrapper

Description:  
This program reads a csv or arff file with methylation rates of specific CpG islands  
and tries to guess if the person smokes or not or if the person is male or female.  
The program uses a Weka API which uses machine learning algorithms to label the class.  
The algorithm this program uses are J48 and RandomForest.  
The output of the program are two files (either csv or arff) with the given values  
plus an extra column with the chosen labeled class.

Installation:  
First of all, make sure java 17 or higher is downloaded on your computer.  
You can download the latest java version via https://www.oracle.com/java/technologies/downloads/

Usage:  
- First open the command prompt (terminal)
- After that head to the project directory with cd.  
- If you are in here type: "cd out/artifacts/CommandLineJAR" or "cd out/artifacts/Thema09_JavaWrapper_jar"  
depending on which program you want to use. You can run the first program with options given in the commandline.  
The second program will ask you for options.  
- Next type "java -jar ./Thema09_JavaWrapper.jar" and when in the first directory also with the options.  
To see which options are available type the above command in the first directory + "-h",  
this will give you a summary of the possible options.  
- After "-f" provide the path to the file which you want to be labeled.  
- To correctly enter a valid file I've provided a template in both formats,  
just replace the question marks with values between 0 and 1. Don't touch anything else in these files  
or the program won't work and make sure every question mark is replaced!
- You can find these templates in the data/input directory within the jar directory.

Further Information:  
For the whole project (including this program and the data analysis), please visit my repositories linked below.
Data Analysis: https://github.com/marcelsetz/AnalysisT9
Java Wrapper: https://github.com/marcelsetz/JavaWrapper

Special Thanks:  
I would like to thank Hanzehogeschool Groningen for making it possible to do this project  
and special thanks to Michiel Noback for supervising the project.
