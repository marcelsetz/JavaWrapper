package arguments_provider;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.j48.Stats;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.*;
import weka.estimators.Estimator;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * If you saved a model to a file in WEKA, you can use it reading the generated java object.
 * Here is an example with Random Forest classifier (previously saved to a file in WEKA):
 * import java.io.ObjectInputStream;
 * import weka.core.Instance;
 * import weka.core.Instances;
 * import weka.core.Attribute;
 * import weka.core.FastVector;
 * import weka.classifiers.trees.RandomForest;
 * RandomForest rf = (RandomForest) (new ObjectInputStream(PATH_TO_MODEL_FILE)).readObject();
 * <p>
 * or
 * RandomTree treeClassifier = (RandomTree) SerializationHelper.read(new FileInputStream("model.weka")));
 */
public class WekaRunner {
    private final String modelFileJ48 = "testdata/J48.model";
    private final String modelFileRF = "testdata/RandomForest.model";

    public void RunWeka(String file, boolean toCSV, boolean isSmokeLabel) {
        WekaRunner runner = new WekaRunner();
        runner.start(file, toCSV, isSmokeLabel);
    }

    private void start(String unknownFile, boolean toCSV, boolean isSmokeLabel) {
        String datafile;
        if (isSmokeLabel) {
            datafile = "testdata/Smoker_Epigenetic_df_Smoke.arff";
        } else {
            datafile = "testdata/Smoker_Epigenetic_df_Gender.arff";
        }
        Process(datafile, unknownFile, toCSV, isSmokeLabel);
    }

    private void Process(String testFile, String unknownFile, boolean toCSV, boolean isSmokeLabel) {
        String arffFile;
        String csvFile;
        String ext = FilenameUtils.getExtension(unknownFile);

        File newFile = new File(unknownFile);
        if (newFile.exists()) {
            System.out.println("File exists!\n");
        } else {
            System.out.println("The given file does not exist! Please try again.");
            System.exit(0);
        }

        boolean isCsv = Objects.equals("csv", ext);

        if (isCsv) {
            csvFile = unknownFile;
        } else {
            ARFFToCSV arffConverter = new ARFFToCSV();
            csvFile = arffConverter.arffChanger(unknownFile);
        }

        if (isSmokeLabel) {
            try {
                List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(csvFile), StandardCharsets.UTF_8));

                for (int i = 0; i < fileContent.size(); i++) {
                    if (i == 0) {
                        String line = fileContent.get(i) + ",Smoking.Status";
                        fileContent.set(i, line);
                    } else {
                        System.out.println("false");
                        String line = fileContent.get(i) + ",?";
                        fileContent.set(i, line);
                    }
                }

                Files.write(Path.of(csvFile), fileContent, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println("Line not replaced!");
            }
        } else {
            try {
                List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(csvFile), StandardCharsets.UTF_8));

                for (int i = 0; i < fileContent.size(); i++) {
                    String line;
                    if (i == 0) {
                        line = fileContent.get(i) + ",Gender";
                    } else {
                        line = fileContent.get(i) + ",?";
                    }
                    fileContent.set(i, line);
                }

                Files.write(Path.of(csvFile), fileContent, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println("Line not replaced!");
            }
        }

        CSVToARFF csvConverter = new CSVToARFF();
        arffFile = csvConverter.csvChanger(csvFile, isSmokeLabel);

        try {
            Instances instances = loadData(testFile);

            RandomForest rf = buildRF(instances);
            saveRF(rf);
            RandomForest rfFromFile = loadRF();

            J48 j48 = buildJ48(instances);
            saveJ48(j48);
            J48 j48FromFile = loadJ48();

            Instances unknownInstances = loadData(arffFile);
            Instances labeledJ48 = classifyNewInstanceJ48(j48FromFile, unknownInstances);
            Instances labeledRF = classifyNewInstanceRF(rfFromFile, unknownInstances);


            ArffSaver J48saver = new ArffSaver();
            J48saver.setInstances(labeledJ48);
            J48saver.setFile(new File("testdata/labeledJ48.arff"));
            J48saver.writeBatch();

            ArffSaver RFsaver = new ArffSaver();
            RFsaver.setInstances(labeledRF);
            RFsaver.setFile(new File("testdata/labeledRF.arff"));
            RFsaver.writeBatch();

            AttributeStats as = labeledJ48.attributeStats(20);
            System.out.println(as);

            if (toCSV) {
                ARFFToCSV arffConverter = new ARFFToCSV();
                arffConverter.arffChanger("testdata/labeledJ48.arff");
                arffConverter.arffChanger("testdata/labeledRF.arff");

                File myObj = new File("testdata/labeledJ48.arff");

                if (myObj.delete()) {
                    System.out.println("Deleted the folder: " + myObj.getName());
                    System.out.println("Created the folder: labeledJ48.csv");

                } else {
                    System.out.println("Failed to delete the folder.");
                }

                File myObj2 = new File("testdata/labeledRF.arff");
                if (myObj2.delete()) {
                    System.out.println("Deleted the folder: " + myObj2.getName());
                    System.out.println("Created the folder: labeledRF.csv\n" + StringUtils.repeat("=", 100));

                } else {
                    System.out.println("Failed to delete the folder.\n" + StringUtils.repeat("=", 100));
                }

            }
        } catch (Exception e) {
            System.out.println("Failed to save!");
            ;
        }
    }

    private J48 buildJ48(Instances instances) throws Exception {
        J48 tree = new J48();
        tree.buildClassifier(instances);
        return tree;
    }

    private RandomForest buildRF(Instances instances) throws Exception {
        RandomForest rf = new RandomForest();
        rf.buildClassifier(instances);
        return rf;
    }

    private Instances loadData(String arffFile) {
        try {
            DataSource source = new DataSource(arffFile);
            Instances dataset = source.getDataSet();

            if (dataset.classIndex() == -1) {
                dataset.setClassIndex(dataset.numAttributes() - 1);
            }

            return dataset;
        } catch (Exception e) {
            System.out.println("File not loaded!");
            return null;
        }

    }

    private void savetmp(String arff) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arff));
        oos.writeObject(arff);
        oos.flush();
        oos.close();
    }

    private void saveJ48(J48 j48) throws Exception {
        weka.core.SerializationHelper.write(modelFileJ48, j48);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFileJ48));
        oos.writeObject(j48);
        oos.flush();
        oos.close();
    }

    private J48 loadJ48() throws Exception {
        return (J48) weka.core.SerializationHelper.read(modelFileJ48);
    }

    private void saveRF(RandomForest rf) throws Exception {
        weka.core.SerializationHelper.write(modelFileRF, rf);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFileRF));
        oos.writeObject(rf);
        oos.flush();
        oos.close();
    }

    private RandomForest loadRF() throws Exception {
        return (RandomForest) weka.core.SerializationHelper.read(modelFileRF);
    }

    private Instances classifyNewInstanceJ48(J48 tree, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = tree.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        return labeled;
    }

    private Instances classifyNewInstanceRF(RandomForest rf, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = rf.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        return labeled;
    }
}
