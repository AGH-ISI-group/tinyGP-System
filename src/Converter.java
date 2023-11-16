import dataConverter.DataFileConverter;

import java.io.IOException;

public class Converter {

    public static void main(String[] args) throws IOException {

        String pathToData = "data/FUN6/FUN6-[-1000.0,1000.0]-[-1000.0,1000.0].dat";
        String pathToOutputData = "outputData/FUN6/FUN6-[-1000.0,1000.0]-[-1000.0,1000.0].dat";

        DataFileConverter.convertToCSV(pathToData, pathToOutputData);
    }
}
