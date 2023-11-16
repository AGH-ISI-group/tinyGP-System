package dataGenerator;

import java.util.Arrays;
import java.util.List;

public class Domain {

    int numberOfVariables;
    List<Pair> domain;

    public Domain(List<Pair> domain) {

        this.numberOfVariables = domain.size();
        this.domain = domain;
    }

    public static Domain createDomain(Pair... values) {

        return new Domain(Arrays.asList(values));
    }

    public Pair getPair(int index) {
        return domain.get(index);
    }

    @Override
    public String toString() {

        StringBuilder domainText = new StringBuilder();

        for(int i = 0; i < domain.size(); i++) {
            domainText.append(domain.get(i).toString());

            if(i != domain.size() - 1)
                domainText.append("-");
        }

        return domainText.toString();
    }
}
