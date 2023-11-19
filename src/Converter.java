import dataConverter.DataFileConverter;

import java.io.IOException;

public class Converter {

    public static void main(String[] args) throws IOException {

        String pathToData = "data/FUN3/FUN3-[0.0,99.0].dat";
        String pathToOutputData = "outputData/FUN3/FUN3-[0.0,99.0].dat";

        DataFileConverter.convertToCSV(pathToData, pathToOutputData);
    }
}
