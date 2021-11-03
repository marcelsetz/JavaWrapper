package arguments_provider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.*;
import weka.estimators.Estimator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

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
    private final String modelFileNB = "testdata/NaiveBayes.model";

    public void RunWeka(String file, boolean toCSV) {
        WekaRunner runner = new WekaRunner();
        runner.start(file, toCSV);
    }

    private void start(String unknownFile, boolean toCSV) {

        String datafile = "testdata/Smoker_Epigenetic_df_Gender.arff";
        Process(datafile, unknownFile, toCSV);
    }

    private void Process(String testFile, String unknownFile, boolean toCSV) {
        String arffFile;
        String ext = FilenameUtils.getExtension(unknownFile);
        boolean isArff = !Objects.equals("csv", ext);

        if (isArff) {
            arffFile = unknownFile;
        } else {
            CSVToARFF csvConverter = new CSVToARFF();
            arffFile = csvConverter.csvChanger(unknownFile);
        }

        try {
            Instances instances = loadData(testFile);

            NaiveBayes nb = buildNB(instances);
            saveNB(nb);
            NaiveBayes nbFromFile = loadNB();

            J48 j48 = buildJ48(instances);
            saveJ48(j48);
            J48 j48FromFile = loadJ48();

            Instances unknownInstances = loadData(arffFile);
            Instances labeledJ48 = classifyNewInstanceJ48(j48FromFile, unknownInstances);
            Instances labeledNB = classifyNewInstanceNB(nbFromFile, unknownInstances);

            ArffSaver J48saver = new ArffSaver();
            J48saver.setInstances(labeledJ48);
            J48saver.setFile(new File("testdata/labeledJ48.arff"));
            J48saver.writeBatch();

            ArffSaver NBsaver = new ArffSaver();
            NBsaver.setInstances(labeledNB);
            NBsaver.setFile(new File("testdata/labeledNB.arff"));
            NBsaver.writeBatch();

            if (toCSV) {
                ARFFToCSV arffConverter = new ARFFToCSV();
                arffConverter.arffChanger("testdata/labeledJ48.arff");
                arffConverter.arffChanger("testdata/labeledNB.arff");

                File f = new File("testdata/labeledJ48.arff");
                File f2 = new File("testdata/labeledNB.arff");

                File myObj = new File("testdata/labeledJ48.arff");
                if (myObj.delete()) {
                    System.out.println("Deleted the folder: " + myObj.getName());
                    System.out.println("Created the folder: labeledJ48.csv");

                } else {
                    System.out.println("Failed to delete the folder.");
                }

                File myObj2 = new File("testdata/labeledNB.arff");
                if (myObj2.delete()) {
                    System.out.println("Deleted the folder: " + myObj2.getName());
                    System.out.println("Created the folder: labeledNB.csv\n" + StringUtils.repeat("=", 100));

                } else {
                    System.out.println("Failed to delete the folder.\n" + StringUtils.repeat("=", 100));
                }

            }

        } catch (Exception e) {
            System.out.println("Failed to save!");;
        }

    }

    private J48 buildJ48(Instances instances) throws Exception {
        J48 tree = new J48();
        tree.buildClassifier(instances);
        return tree;
    }

    private NaiveBayes buildNB(Instances instances) throws Exception {
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(instances);
        return nb;
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

    private void saveJ48 (J48 j48) throws Exception{
        weka.core.SerializationHelper.write(modelFileJ48, j48);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFileJ48));
        oos.writeObject(j48);
        oos.flush();
        oos.close();
    }

    private J48 loadJ48() throws Exception{
        return (J48) weka.core.SerializationHelper.read(modelFileJ48);
    }

    private void saveNB (NaiveBayes nb) throws Exception{
        weka.core.SerializationHelper.write(modelFileNB, nb);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFileNB));
        oos.writeObject(nb);
        oos.flush();
        oos.close();
    }

    private NaiveBayes loadNB() throws Exception{
        return (NaiveBayes) weka.core.SerializationHelper.read(modelFileNB);
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

    private Instances classifyNewInstanceNB(NaiveBayes nb, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = nb.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        return labeled;
    }
}
