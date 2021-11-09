package arguments_provider;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;
import java.io.File;

public class ARFFToCSV {

    /**
     * Takes a arff file and converts it into a csv file.
     * @param arffFile arff file to be converted
     * @return csvFile created csv file
     * */

    public String arffChanger (String arffFile) {
        String csvFile = arffFile.replace(".arff", ".csv");

        try {
            ArffLoader arffLoader = new ArffLoader();
            arffLoader.setSource(new File(arffFile));
            Instances data = arffLoader.getDataSet();

            CSVSaver csvSaver = new CSVSaver();
            csvSaver.setInstances(data);
            csvSaver.setFile(new File(csvFile));
            csvSaver.writeBatch();
        } catch (Exception e) {
            System.out.println("Failed to convert arff to csv!");
        }

        return csvFile;
    }
}
