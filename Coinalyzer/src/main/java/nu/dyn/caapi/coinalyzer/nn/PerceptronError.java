package nu.dyn.caapi.coinalyzer.nn;

import java.util.ArrayList;
import java.util.List;

public class PerceptronError {

	 double currentError = 0;
	 long iterations = 1;

	 private double minError = Double.POSITIVE_INFINITY;;
	 
	 long minErrorIteration = 0;

	 long maxIterations;

	 List<Double> errors = new ArrayList<Double>();
	 List<Double> errors_testset = new ArrayList<Double>();
	 
	public PerceptronError(long maxIterations) {

		this.maxIterations = maxIterations;
		
	}

	public void addError(double err) {

		errors.add(err);
		
	}
	

	public void addTestsetError(double[] output, double[] desiredOutput) {
		
		double err = 0;
		
		for (int i = 0; i < output.length; i++) {
			err += Math.abs(desiredOutput[i] - output[i]);
		}
		
		errors_testset.add(err);
		

	}
	
	public void addOutputError(double[] output, double[] desiredOutput) {

		for (int i = 0; i < output.length; i++) {
			currentError += Math.abs(desiredOutput[i] - output[i]);
		}

	}

	public Double getCurrentError() {

		return currentError;

	}

	public void nextIteration() {

		errors.add(currentError);
		
		++iterations;
		currentError = 0;

	}

	public boolean isNewMinError() {

		if (currentError < minError) {
			minError = currentError;
			minErrorIteration = iterations;
			return true;
		} else {
			return false;
		}
	
	}
	
	public boolean isLastIteration() {

		if (iterations >= maxIterations)
			return true;
		else
			return false;

	}

	public long getIterations() {

		return iterations;

	}

	public long getMinErrorIteration() {

		return minErrorIteration;

	}

	public List<Double> getErrors() {
		return errors;
	}
	
	public List<Double> getErrors_testSet() {
		return errors_testset;
	}
}
