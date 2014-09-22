package nu.dyn.caapi.coinalyzer.nn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.nn.normalizers.TanHNormalizer;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.springframework.beans.factory.annotation.Autowired;

//http://annstock.herokuapp.com/
//http://www.slideshare.net/mring33/using-java
//https://fedcsis.org/proceedings/2013/pliks/277.pdf
public class MyPerceptron {

	public NeuralNetwork perceptron;
	DataNormalizer normalizer;
	
	@Autowired
	AppConfig appConfig;

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
			double[] i_data = dataset.get(i).getNormalizedInputArray();
			double[] o_data = dataset.get(i).getOutputArray();
			trainingSet.addRow(new DataSetRow(i_data, o_data));
		}

		// create perceptron neural network
		Vector<Integer> layers = new Vector<Integer>();
		layers.add(5);
		layers.add(2 * 5 + 1);
		layers.add(5);

		perceptron = new MultiLayerPerceptron(layers,
				TransferFunctionType.TANH);

		MomentumBackpropagation myRule = new MomentumBackpropagation();
		myRule.setBatchMode(false);
		myRule.setLearningRate(appConfig.nn.getLearningRate());
		myRule.setMomentum(appConfig.nn.getMomentum());
		myRule.setMaxError(appConfig.nn.getMaxError());
		myRule.setMaxIterations(appConfig.nn.getMaxIterations());
		myRule.setNeuralNetwork(perceptron);
		
		perceptron.setLearningRule(myRule);

		// learn the training set
		perceptron.learn(trainingSet);

		// test perceptron
		System.out.println("Testing trained perceptron with " + trainingSet.getInputSize() + " inputs");
		testNeuralNetwork(perceptron, trainingSet);

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

		// DataSet n_testSet = normalizer.denormalize(testSet);

		for (int i = 0; i < testSet.size(); i++) {

			DataSetRow trainingElement = testSet.getRowAt(i);
			neuralNet.setInput(trainingElement.getInput());
			neuralNet.calculate();

			double[] networkOutput = neuralNet.getOutput();
			// double[] n_networkOutput =
			// normalizer.denormalize(neuralNet.getOutput());

			System.out.print("Input: "
					+ Arrays.toString(normalizer.denormalize(testSet
							.getRowAt(i).getInput())));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
		}

	}
	
	public void load(String name) {

		perceptron =  NeuralNetwork.createFromFile("perceptron_" + name + ".nn");
	
	}

	public void save(String name) {

		perceptron.save("perceptron_" + name + ".nn");
	
	}
}
