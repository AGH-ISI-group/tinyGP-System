import dataGenerator.*;

import java.io.File;
import java.io.IOException;

public class Generator {

    public static void main(String[] args) throws IOException {

        Function fun = Function.FUN6;
        Domain domain = Domain.createDomain(new Pair(-1000 , 1000), new Pair(-1000 , 1000));
        Header header = new Header(2);

        DataFileGenerator.generate(fun, domain, header);
    }
}
