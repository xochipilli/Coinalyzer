package nu.dyn.caapi.coinalyzer.nn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.controllers.HomeController;
import nu.dyn.caapi.coinalyzer.nn.normalizers.TanHNormalizer;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//http://annstock.herokuapp.com/
//http://www.slideshare.net/mring33/using-java
//https://fedcsis.org/proceedings/2013/pliks/277.pdf
public class MyPerceptron implements LearningEventListener {

	String name;
	
	public NeuralNetwork perceptron;
	DataNormalizer normalizer;
	
	DataSet train_dataset;
	DataSet test_dataset;
	
	private static final Logger logger = LoggerFactory.getLogger(MyPerceptron.class);
	
	/**
	 * Initialize NN with input datasets
	 * 
	 * @param dataset
	 * @return Normalized data
	 */
	public MyPerceptron(ArrayList<TrainDataItem> trainset, ArrayList<TrainDataItem> testset, 
			String name, AppConfig.NN nn_config) {
		
		this.name = name;
		
		// init normalizer with trainset+testset and normalize each arraylist
		ArrayList<TrainDataItem> dataset = new ArrayList<TrainDataItem>();
		dataset.addAll(trainset);
		dataset.addAll(testset);
		normalizer = new TanHNormalizer(dataset);
		trainset = normalizer.normalize(trainset);
		testset = normalizer.normalize(testset);

		// convert from Arraylist to Dataset
		train_dataset = getDataset(trainset);
		test_dataset  = getDataset(testset);
		
		// create perceptron neural network
		Vector<Integer> layers = new Vector<Integer>();
		layers.add(5);
		layers.add(2 * 5 + 1);
		layers.add(5);

		perceptron = new MultiLayerPerceptron(layers, TransferFunctionType.TANH);

		MomentumBackpropagation prop = new MomentumBackpropagation();
		prop.setBatchMode(false);
		prop.setLearningRate(nn_config.getLearningRate());
		prop.setMomentum(nn_config.getMomentum());
		prop.setMaxError(nn_config.getMaxError());
		prop.setMaxIterations(nn_config.getMaxIterations());
		prop.setNeuralNetwork(perceptron);
		prop.addListener(this);
		
		perceptron.setLearningRule(prop);
		
	
	}
	
	DataSet getDataset(ArrayList<TrainDataItem> tdiset) {
		
		DataSet dataset = new DataSet(5, 5);

		for (int i = 0; i < tdiset.size(); i++) {
			double[] i_data = tdiset.get(i).getNormalizedInputArray();
			double[] o_data = tdiset.get(i).getOutputArray();
			dataset.addRow(new DataSetRow(i_data, o_data));
		}
		
		return dataset;
	}
	
	public void train() {

		logger.info("Training perceptron " + name);
		perceptron.learn(train_dataset);
		logger.info("Finished training perceptron " + name);
		
	}
	
	public void test() {
		
		test(test_dataset);
		
	}
	
	/**
	 * Prints network output for the each element from the specified testing set.
	 * 
	 * @param neuralNet
	 *            neural network
	 * @param testSet
	 *            data set used for testing
	 */
	public void test(DataSet testSet) {

		logger.info("Testing trained perceptron "+ name +" with " + test_dataset.size() + " inputs");

		// DataSet n_testSet = normalizer.denormalize(testSet);

		for (int i = 0; i < testSet.size(); i++) {

			DataSetRow trainingElement = testSet.getRowAt(i);
			perceptron.setInput(trainingElement.getInput());
			perceptron.calculate();

			double[] networkOutput = perceptron.getOutput();
			// double[] n_networkOutput =
			// normalizer.denormalize(neuralNet.getOutput());

			logger.info("Input: "
					+ Arrays.toString(normalizer.denormalize(testSet
							.getRowAt(i).getInput())));
			logger.info(" Output: " + Arrays.toString(networkOutput));
		}

	}
	
	@Override
	public void handleLearningEvent(LearningEvent event) {
		 final MomentumBackpropagation bp = (MomentumBackpropagation) event.getSource();
		 System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
		 
		
//	    BackPropagation bp = (BackPropagation) le.getSource();
//	    net = (MultiLayerPerceptron) bp.getNeuralNetwork();
//
//	    iterator = validationSet.iterator();
//
//	    while (iterator.hasNext()) {
//	        DataSetRow dataSetRow = iterator.next();
//	        net.setInput(dataSetRow.getInput());
//	        net.calculate();
//
//	        double[] desiredOutput  = dataSetRow.getDesiredOutput();
//	        double[] output         = net.getOutput();
//
//	        double[] outputError    = new double[desiredOutput.length];
//
//	        for (int i=0; i<output.length; i++) {
//	            outputError[i] = desiredOutput[i] - output[i];
//	        }
//
//	        errorFunction.addOutputError(outputError);
//
//	    }
//
//	    currentError = errorFunction.getTotalError();
//	    errorFunction.reset();
//
//	    if(currentError<minError){
//	        minError = currentError;
//	        iterationAtMinimum = bp.getCurrentIteration();
//	        net.save("temporaryNeuralNetwork.nnet");
//	        System.out.println("Minimum");
//	    }
//
//	    if(currentError>previousError){
//	        iterationsCounter++;
//	    }
//	    else{
//	        iterationsCounter = 0;
//	    }
//
//	    if(iterationsCounter >= MAXOVERFITTINGITERATIONS){
//	        net.stopLearning();
//	        net = (MultiLayerPerceptron)
//	                MultiLayerPerceptron.createFromFile
//	                ("temporaryNeuralNetwork.nnet");
//	        System.out.println("\nOVERFITTING - Minimum got at iteration "+iterationAtMinimum);
//	    }
//	    else
//	        previousError = currentError;
	}
	
	public void load(String name) {

		perceptron =  NeuralNetwork.createFromFile("perceptron_" + name + ".nn");
	
	}

	public void save(String name) {

		perceptron.save("perceptron_" + name + ".nn");
	
	}
}
