package dataGenerator;

import java.util.ArrayList;
import java.util.List;

public enum Function {

    FUN1 {

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double getTargetValue(List<Double> variables) {

            double x = variables.get(0);

            return 5*Math.pow(x, 3) - 2*Math.pow(x, 2) + 3*x - 17;
        }
    },

    FUN2 {

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double getTargetValue(List<Double> variables) {

            double x = variables.get(0);

            return Math.sin(x) + Math.cos(x);
        }
    },

    FUN3 {

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double getTargetValue(List<Double> variables) {

            double x = variables.get(0);

            return 2*Math.log(x + 1);
        }
    },

    FUN4 {

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

        @Override
        public double getTargetValue(List<Double> variables) {

            double x = variables.get(0);
            double y = variables.get(1);

            return x + 2*y;
        }
    },

    FUN5 {

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

        @Override
        public double getTargetValue(List<Double> variables) {

            double x = variables.get(0);
            double y = variables.get(1);

            return Math.sin(x/2) + 2*Math.cos(x);
        }
    },

    FUN6 {

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

        @Override
        public double getTargetValue(List<Double> variables) {

            double x = variables.get(0);
            double y = variables.get(1);

            return Math.pow(x, 2) + 3*x*y - 7*y + 1;
        }
    };


    public abstract double getTargetValue(List<Double> variables);
    public abstract int getNumberOfVariables();
}
