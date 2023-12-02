package functionInfo;

import dataGenerator.Domain;
import dataGenerator.Function;
import dataGenerator.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class FunctionInfo {
    static Map<Function, List<Domain>> domains = Map.ofEntries(
            entry(
                    Function.FUN1,
                    List.of(
                            Domain.createDomain(new Pair(-10, 10)),
                            Domain.createDomain(new Pair(0, 100)),
                            Domain.createDomain(new Pair(-1, 1)),
                            Domain.createDomain(new Pair(-1000, 1000))
                    )
            ),

            entry(
                    Function.FUN2,
                    List.of(
                            Domain.createDomain(new Pair(-3.14, 3.14)),
                            Domain.createDomain(new Pair(0, 7)),
                            Domain.createDomain(new Pair(0, 100)),
                            Domain.createDomain(new Pair(-100, 100))
                    )
            ),

            entry(
                    Function.FUN3,
                    List.of(
                            Domain.createDomain(new Pair(0, 4)),
                            Domain.createDomain(new Pair(0, 9)),
                            Domain.createDomain(new Pair(0, 99)),
                            Domain.createDomain(new Pair(0, 999))
                    )
            ),

            entry(
                    Function.FUN4,
                    List.of(
                            Domain.createDomain(new Pair(0, 1), new Pair(0, 1)),
                            Domain.createDomain(new Pair(-10, 10), new Pair(-10, 10)),
                            Domain.createDomain(new Pair(0, 100), new Pair(0, 100)),
                            Domain.createDomain(new Pair(-1000, 1000), new Pair(-1000, 1000))
                    )
            ),

            entry(
                    Function.FUN5,
                    List.of(
                            Domain.createDomain(new Pair(-3.14, 3.14), new Pair(-3.14, 3.14)),
                            Domain.createDomain(new Pair(0, 7), new Pair(0, 7)),
                            Domain.createDomain(new Pair(0, 100), new Pair(0, 100)),
                            Domain.createDomain(new Pair(-100, 100), new Pair(-100, 100))
                    )
            ),

            entry(
                    Function.FUN6,
                    List.of(
                            Domain.createDomain(new Pair(-10, 10), new Pair(-10, 10)),
                            Domain.createDomain(new Pair(0, 100), new Pair(0, 100)),
                            Domain.createDomain(new Pair(-1, 1), new Pair(-1, 1)),
                            Domain.createDomain(new Pair(-1000, 1000), new Pair(-1000, 1000))
                    )
            )
    );

    public static List<Domain> getDomainsOfFunction(Function fun) {
        return domains.get(fun);
    }

    public static List<String> generateAllFunctionNames(List<Function> functions, String directory) {

        List<String> fileNameList = new ArrayList<>();

        for (Function fun : functions) {
            for (Domain domain : domains.get(fun)) {

                String path = directory + "/" + fun + "/";
                String fileName = fun + "-" + domain + ".dat";
                String fullPath = path + fileName;

                fileNameList.add(fullPath);
            }
        }

        return fileNameList;
    }

}
