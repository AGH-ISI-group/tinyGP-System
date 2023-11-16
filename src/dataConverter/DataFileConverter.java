package dataConverter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataFileConverter {

    public static final Map<String, String> converterOfVariables = Map.of(
            "X1", "A",
            "X2", "B",
            "X3", "C"
    );

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

            writer.write(lineFromData.replace(".", ",").replace(" ", ";") + ";");

            String tinyGPConvertedFunction = tinyGPFunction;
            for(Map.Entry<String, String> entry : converterOfVariables.entrySet()) {

                tinyGPConvertedFunction = tinyGPConvertedFunction.replace(entry.getKey(), entry.getValue() + i);
            }

            writer.write("=" + tinyGPConvertedFunction);
            writer.newLine();
            i++;
        }

        writer.close();
        brOutputData.close();
    }
}
