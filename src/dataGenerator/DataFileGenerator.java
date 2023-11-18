package dataGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataFileGenerator {

    public static void generate(Function fun, Domain domain, Header header) throws IOException {

        if(fun.getNumberOfVariables() != domain.numberOfVariables || fun.getNumberOfVariables() != header.numberOfVariables)
            throw new RuntimeException("Wrong domain");

        System.out.println(domain.toString());


        String path = "data/" + fun + "/";
        String fileName = fun + "-" + domain + ".dat";
        String fullPath = path + fileName;


        //create folders
        if(new File(path).mkdirs())
            System.out.println("Folder directory: \"" +  path + "\" has been created");
        else
            System.out.println("Folder: \"" + path + "\" already exists");


        //create file
        File file = new File(fullPath);

        if(file.createNewFile())
            System.out.println("File: \"" +  file.getName() + "\" has been created");
        else
            System.out.println("File: \"" + file.getName() + "\" already exists");


        //write data to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));

        writer.write(header.toString());
        writer.newLine();
        List<String> arrayOfInputsAndTargets;

        if(header.numberOfVariables == 1)
            arrayOfInputsAndTargets = generateInputsAndTargets1(fun, domain, header.numberOfFitnessCases);
        else if (header.numberOfVariables == 2)
            arrayOfInputsAndTargets = generateInputsAndTargets2(fun, domain, header.numberOfAxisDivisions);
        else
            throw new RuntimeException("Program doesn't support function with more than two variable");

        for(int index = 0; index < header.numberOfFitnessCases; index ++) {

            writer.write(arrayOfInputsAndTargets.get(index));
            writer.newLine();
        }

        writer.close();
    }

    private static List<String> generateInputsAndTargets2(Function fun, Domain domain, int numberOfAxisDivisions) {

        List<String> arrayOfInputsAndTargets = new ArrayList<>();

        for(int i = 0; i < numberOfAxisDivisions; i++) {
                double inputVal1 = (domain.getPair(0).to - domain.getPair(0).from) / (numberOfAxisDivisions - 1) * i + domain.getPair(0).from;

            for (int j = 0; j < numberOfAxisDivisions; j++) {

                StringBuilder line = new StringBuilder();
                List<Double> inputValues = new ArrayList<>();

                double inputVal2 = (domain.getPair(1).to - domain.getPair(1).from) / (numberOfAxisDivisions - 1) * j + domain.getPair(1).from;

                inputValues.add(inputVal1);
                inputValues.add(inputVal2);

                double targetValue = fun.getTargetValue(inputValues);

                line.append(inputVal1).append(" ").append(inputVal2).append(" ").append(targetValue);

                arrayOfInputsAndTargets.add(line.toString());
            }
        }

        return arrayOfInputsAndTargets;
    }

    static List<String> generateInputsAndTargets1(Function fun, Domain domain, int numberOfFitnessCases) {

        List<String> arrayOfInputsAndTargets = new ArrayList<>();

        for(int i = 0; i < numberOfFitnessCases; i++) {

            StringBuilder line = new StringBuilder();
            List<Double> inputValues = new ArrayList<>();

            double inputVal = (domain.getPair(0).to - domain.getPair(0).from) / (numberOfFitnessCases - 1) * i + domain.getPair(0).from;
            line.append(inputVal).append(" ");

            inputValues.add(inputVal);

            double targetVal = fun.getTargetValue(inputValues);
            line.append(targetVal);

            arrayOfInputsAndTargets.add(line.toString());
        }

        return arrayOfInputsAndTargets;
    }

}
