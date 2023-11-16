package dataGenerator;

public class Header {

    int numberOfVariables;
    int numberOfConstants;
    double lowerRangeOfRandomConstants;
    double upperRangeOfRandomConstants;
    int numberOfFitnessCases;

    public Header(int numberOfVariables, int numberOfConstants, double lowerRangeOfRandomConstants, double upperRangeOfRandomConstants, int numberOfFitnessCases) {
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstants = numberOfConstants;
        this.lowerRangeOfRandomConstants = lowerRangeOfRandomConstants;
        this.upperRangeOfRandomConstants = upperRangeOfRandomConstants;
        this.numberOfFitnessCases = numberOfFitnessCases;
    }

    public Header(int numberOfVariables) {

        this(numberOfVariables, 100, -5, 5, 101);
    }

    @Override
    public String toString() {
        return numberOfVariables + " " + numberOfConstants + " " + lowerRangeOfRandomConstants + " " + upperRangeOfRandomConstants + " " + numberOfFitnessCases;
    }
}
