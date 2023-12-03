import dataConverter.DataFileConverter;
import dataGenerator.DataFileGenerator;
import dataGenerator.Domain;
import dataGenerator.Function;
import dataGenerator.Header;
import java.io.IOException;
import java.util.List;
import static functionInfo.FunctionInfo.generateAllFunctionNames;
import static functionInfo.FunctionInfo.getDomainsOfFunction;

public class MainTinyGP {

    public static void main(String[] args) throws IOException {

        List<Function> functions = List.of(Function.FUN4);

        generateAllDataFiles(functions);
        generateAllByTinyGp(functions);
        convertAllToCSV(functions);

    }

    public static void generateAllDataFiles(List<Function> functions) throws IOException {

        for(Function fun: functions) {
            for(Domain domain: getDomainsOfFunction(fun)) {

                Header header;
                if(fun.getNumberOfVariables() == 2)
                    header = new Header(2, 100, -5, 5, 50);
                else
                    header = new Header();

                DataFileGenerator.generate(fun, domain, header);
            }
        }
    }

    public static void generateAllByTinyGp(List<Function> functions) {

        List<String> fileNames = generateAllFunctionNames(functions, "data");

        for(String fileName: fileNames) {

            TinyGP gp = new TinyGP(fileName, -1);
            gp.evolve();
        }
    }

    public static void convertAllToCSV(List<Function> functions) throws IOException {

        List<String> dataFileNames = generateAllFunctionNames(functions, "data");
        List<String> outputDataFileNames = generateAllFunctionNames(functions, "outputData");

        for(int i = 0; i < dataFileNames.size(); i++) {

            String pathToData = dataFileNames.get(i);
            String pathToOutputData = outputDataFileNames.get(i);

            if(functions.get(i / 4).getNumberOfVariables() == 2)
                DataFileConverter.convertFunctionOf2VarToCSV(pathToData, pathToOutputData);
            else
                DataFileConverter.convertToCSV(pathToData, pathToOutputData);
        }
    }
}
