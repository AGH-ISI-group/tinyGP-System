package dataGenerator;

public class Pair {

    double from;
    double to;

    public Pair(double from, double to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[" + from + "," + to + "]";
    }
}
