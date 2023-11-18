package dataGenerator;

public class Header {

    int numberOfVariables;
    int numberOfConstants;
    double lowerRangeOfRandomConstants;
    double upperRangeOfRandomConstants;
    int numberOfFitnessCases;

    int numberOfAxisDivisions;

    public Header(int numberOfVariables, int numberOfConstants, double lowerRangeOfRandomConstants, double upperRangeOfRandomConstants, int numberOfAxisDivisions) {
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstants = numberOfConstants;
        this.lowerRangeOfRandomConstants = lowerRangeOfRandomConstants;
        this.upperRangeOfRandomConstants = upperRangeOfRandomConstants;
        this.numberOfFitnessCases = Double.valueOf(Math.pow(numberOfAxisDivisions, numberOfVariables)).intValue();

        this.numberOfAxisDivisions = numberOfAxisDivisions;
    }

    public Header() {

        this(1, 100, -5, 5, 101);
    }

    @Override
    public String toString() {
        return numberOfVariables + " " + numberOfConstants + " " + lowerRangeOfRandomConstants + " " + upperRangeOfRandomConstants + " " + numberOfFitnessCases;
    }
}
