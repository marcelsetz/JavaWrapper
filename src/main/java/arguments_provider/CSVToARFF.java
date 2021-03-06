package arguments_provider;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVToARFF {

    /**
     * Takes a arff file and converts it into a csv file.
     * And also changes the attribute of chosen class so the program will work correctly.
     * If input was a csv file, this file will be cleaned after converting so the program can be used again
     * with the same file and a different class for example.
     *
     * @param csvFile csv file to be converted
     * @param isSmokeLabel true if output class is Smoking.Status
     * @return arffFile created csv file
     * */

    public String csvChanger(String csvFile, boolean isSmokeLabel) {
        String arffFile = "data/tmp/tmp.arff";

        try {
            CSVLoader csvloader = new CSVLoader();
            csvloader.setSource(new File(csvFile));
            Instances data = csvloader.getDataSet();

            ArffSaver arffsaver = new ArffSaver();
            arffsaver.setInstances(data);
            arffsaver.setFile(new File(arffFile));
            arffsaver.writeBatch();

        } catch (Exception e) {
            System.out.println("Failed to convert csv to arff!");
        }

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(arffFile), StandardCharsets.UTF_8));
            if (isSmokeLabel) {

                for (int i = 0; i < fileContent.size(); i++) {
                    if (fileContent.get(i).equals("@attribute Smoking.Status string")) {
                        fileContent.set(i, "@attribute Smoking.Status {current,never}");
                        break;
                    }
                }

            } else {

                for (int i = 0; i < fileContent.size(); i++) {
                    if (fileContent.get(i).equals("@attribute Gender string")) {
                        fileContent.set(i, "@attribute Gender {' f',' m'}");
                        break;
                    }
                }

            }
            Files.write(Path.of(arffFile), fileContent, StandardCharsets.UTF_8);
        } catch (Exception e) {
                System.out.println("Line not replaced!");
            }

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(csvFile), StandardCharsets.UTF_8));
            String newLine;
            if (isSmokeLabel) {

                for (int i = 0; i < fileContent.size(); i++) {
                    if (i==0) {
                        newLine = fileContent.get(i).replace(",Smoking.Status", "");
                    } else {
                        newLine = fileContent.get(i).replace(",?", "");
                    }
                    fileContent.set(i, newLine);
                }
                Files.write(Path.of(csvFile), fileContent, StandardCharsets.UTF_8);
            } else {
                for (int i = 0; i < fileContent.size(); i++) {
                    if (i==0) {
                        newLine = fileContent.get(i).replace(",Gender", "");
                    } else {
                        newLine = fileContent.get(i).replace(",?", "");
                    }

                    fileContent.set(i, newLine);
                }
            }
            Files.write(Path.of(csvFile), fileContent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arffFile;
    }
}
