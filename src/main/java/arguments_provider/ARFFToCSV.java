package arguments_provider;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;
import java.io.File;

public class ARFFToCSV {

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
            System.out.println("Still Arff!");
        }

        return csvFile;
    }
}
