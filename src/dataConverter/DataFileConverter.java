package dataConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataFileConverter {

    public static void convertToCSV(String pathToData, String pathToOutputData) throws IOException {

        String[] names = pathToData.split("/");

        String path = "convertedData/" + names[1] + "/";
        String fileName = names[2].split(".dat")[0] + ".csv";
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


        BufferedReader brOutputData = new BufferedReader(new FileReader(pathToOutputData));
        String tinyGPFunction = brOutputData.readLine().replace(".", ",");
        brOutputData.close();


        BufferedReader brData = new BufferedReader(new FileReader(pathToData));
        String headerFromData = brData.readLine();
        String[] headerArray = headerFromData.split(" ");

        BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));

        int i = 1;
        String lineFromData;
        while((lineFromData = brData.readLine()) != null) {

            writer.write(lineFromData.replace(".", ",").replace(" ", "|") + "|");

            String tinyGPConvertedFunction = tinyGPFunction;

            tinyGPConvertedFunction = tinyGPConvertedFunction.replace("X1", "A" + i);

            writer.write("=" + tinyGPConvertedFunction);
            writer.newLine();
            i++;
        }

        writer.close();
        brOutputData.close();
    }

    public static void convertFunctionOf2VarToCSV(String pathToData, String pathToOutputData) throws IOException {

        String[] names = pathToData.split("/");

        String path = "convertedData/" + names[1] + "/";
        String fileName = names[2].split(".dat")[0] + ".csv";
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


        BufferedReader brOutputData = new BufferedReader(new FileReader(pathToOutputData));
        String tinyGPFunction = brOutputData.readLine();
        brOutputData.close();

        tinyGPFunction = tinyGPFunction.replace("X1", "B$1");
        tinyGPFunction = tinyGPFunction.replace("X2",  "$A2");


        BufferedReader brData = new BufferedReader(new FileReader(pathToData));
        String headerFromData = brData.readLine();
        String[] headerArray = headerFromData.split(" ");

        List<String> inputValues = new ArrayList<>();
        List<String> outputTargetValues = new ArrayList<>();

        int i = 1;
        String lineFromData;
        while((lineFromData = brData.readLine()) != null) {

            String[] parameters = lineFromData.split(" ");

            inputValues.add(parameters[0]);
            outputTargetValues.add(parameters[2]);

            i++;
        }

        brData.close();

        List<String> uniqueInputValues = inputValues.stream().distinct().toList();


        BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));

        // writing tinyGP Data for chart

        for (String inputVal: uniqueInputValues)
            writer.write( "|" + inputVal.replace(".", ","));

        int j = 0;
        for (String inputVal: uniqueInputValues) {

            writer.newLine();
            writer.write(inputVal.replace(".", ","));

            if(j == 0)
                writer.write( "|" + "=" + tinyGPFunction.replace(".", ","));

            j++;
        }

        writer.newLine();
        writer.newLine();

        // writing target data for chart

        for (String inputVal: uniqueInputValues)
            writer.write( "|" + inputVal.replace(".", ","));

        int k = 0;
        for (String inputVal: uniqueInputValues) {

            writer.newLine();
            writer.write(inputVal.replace(".", ","));

            for(int l = 0; l < uniqueInputValues.size(); l++) {

                writer.write("|" + outputTargetValues.get(k * uniqueInputValues.size() + l).replace(".", ","));
            }
            k++;
        }

        writer.close();
    }
}
