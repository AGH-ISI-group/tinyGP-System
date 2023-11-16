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

        List<String> arrayOfInputsAndTargets = generateInputsAndTargets(fun, domain, header.numberOfFitnessCases);

        for(int index = 0; index < header.numberOfFitnessCases; index ++) {

            writer.write(arrayOfInputsAndTargets.get(index));
            writer.newLine();
        }

        writer.close();
    }

    static List<String> generateInputsAndTargets(Function fun, Domain domain, int numberOfFitnessCases) {

        List<String> arrayOfInputsAndTargets = new ArrayList<>();

        for(int i = 0; i < numberOfFitnessCases; i++) {

            StringBuilder line = new StringBuilder();
            List<Double> inputValues = new ArrayList<>();

            for(int j = 0; j < domain.numberOfVariables; j++) {

                double inputVal = (domain.getPair(j).to - domain.getPair(j).from) / numberOfFitnessCases * i + domain.getPair(j).from ;
                line.append(inputVal).append(" ");

                inputValues.add(inputVal);
            }

            double targetVal = fun.getTargetValue(inputValues);
            line.append(targetVal);

            arrayOfInputsAndTargets.add(line.toString());
        }

        return arrayOfInputsAndTargets;
    }

}
