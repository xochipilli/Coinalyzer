package nu.dyn.caapi.nn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import nu.dyn.caapi.nn.normalizers.TanHNormalizer;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

//http://annstock.herokuapp.com/
//http://www.slideshare.net/mring33/using-java
//https://fedcsis.org/proceedings/2013/pliks/277.pdf
public class MyPerceptron {
	
	NeuralNetwork myPerceptron;
	DataNormalizer normalizer;
	
	/**
	 * Create NN
	 * 
	 * @param dataset
	 * @return Normalized data
	 */
	public MyPerceptron(ArrayList<TrainDataItem> dataset) {

		// init normalizer and normalize
		normalizer = new TanHNormalizer(dataset);
		dataset = normalizer.normalize(dataset);

		// feed dataset to network
		DataSet trainingSet = new DataSet(5, 5);

		for (int i = 0; i < dataset.size(); i++) {

			double[] i_data = new double[] { dataset.get(i).i_open,
					dataset.get(i).i_low, dataset.get(i).i_high,
					dataset.get(i).i_close, dataset.get(i).i_volume };
			double[] o_data = new double[] { dataset.get(i).o_open,
					dataset.get(i).o_low, dataset.get(i).o_high,
					dataset.get(i).o_close, dataset.get(i).o_volume };

			trainingSet.addRow(new DataSetRow(i_data, o_data));

		}
		    
		// create perceptron neural network
		Vector<Integer> layers = new Vector<Integer>();
	    layers.add(5);
	    layers.add(2 * 5 + 1);
	    layers.add(5);
	        
	    myPerceptron = new MultiLayerPerceptron(layers, TransferFunctionType.TANH);
		
		MomentumBackpropagation myRule = new MomentumBackpropagation();
	    myRule.setBatchMode(false);
		myRule.setLearningRate(0.2);	// ?
		myRule.setMomentum(0.7);		// ?
		myRule.setMaxError(0.01);		// ?
		myRule.setNeuralNetwork(myPerceptron);
	    myPerceptron.setLearningRule(myRule);
	    
		// learn the training set
		myPerceptron.learn(trainingSet);

		// test perceptron
		System.out.println("Testing trained perceptron");
		testNeuralNetwork(myPerceptron, trainingSet);

		// save trained perceptron
		// myPerceptron.save("mySamplePerceptron.nnet");

		// load saved neural network
		// NeuralNetwork loadedPerceptron =
		// NeuralNetwork.load("mySamplePerceptron.nnet");

		// test loaded neural network
		// System.out.println("Testing loaded perceptron");
		// testNeuralNetwork(loadedPerceptron, trainingSet);
	}

	/**
	 * Prints network output for the each element from the specified training
	 * set.
	 * 
	 * @param neuralNet
	 *            neural network
	 * @param testSet
	 *            data set used for testing
	 */
	public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {
		
		//DataSet n_testSet = normalizer.denormalize(testSet);
		
		for (int i=0; i<testSet.size(); i++) {
				
			DataSetRow trainingElement = testSet.getRowAt(i);
			neuralNet.setInput(trainingElement.getInput());
			neuralNet.calculate();
			
			double[] networkOutput = neuralNet.getOutput();
			//	double[] n_networkOutput = normalizer.denormalize(neuralNet.getOutput());
			
			System.out.print("Input: " + Arrays.toString(testSet.getRowAt(i).getInput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
		}
		
	}
}
