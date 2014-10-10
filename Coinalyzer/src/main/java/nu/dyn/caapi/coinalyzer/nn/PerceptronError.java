package nu.dyn.caapi.coinalyzer.nn;

public class PerceptronError {

	 double currentError = 0;
	 long iterations = 1;

	 double minError;
	 long minErrorIteration = 0;

	 long maxIterations;

	public PerceptronError(long maxIterations) {

		this.maxIterations = maxIterations;
		minError = Double.POSITIVE_INFINITY;
		
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

}
