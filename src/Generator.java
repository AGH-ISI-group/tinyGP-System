import dataGenerator.*;

import java.io.File;
import java.io.IOException;

public class Generator {

    public static void main(String[] args) throws IOException {

        Function fun = Function.FUN1;
        Domain domain = Domain.createDomain(new Pair(-10, 10));
        Header header = new Header();
//        Header header = new Header(2, 100, -5, 5, 50);

        DataFileGenerator.generate(fun, domain, header);
    }
}
