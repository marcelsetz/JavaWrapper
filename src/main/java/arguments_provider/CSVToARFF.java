package arguments_provider;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVToARFF {
    public String csvChanger(String csvFile) {
        String arffFile = csvFile.replace(".csv", ".arff");

        try {
            CSVLoader csvloader = new CSVLoader();
            csvloader.setSource(new File(csvFile));
            Instances data = csvloader.getDataSet();

            ArffSaver arffsaver = new ArffSaver();
            arffsaver.setInstances(data);
            arffsaver.setFile(new File(arffFile));
            arffsaver.writeBatch();

        } catch (Exception e) {
            System.out.println("still csv!");
        }

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(arffFile), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).equals("@attribute Gender string")) {
                    fileContent.set(i, "@attribute Gender {' f',' m'}");
                    break;
                }
            }

            Files.write(Path.of(arffFile), fileContent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Line not replaced!");
        }

        return arffFile;

    }

}
