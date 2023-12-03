/*
 * Program:   tinyGP.java
 *
 * Author:    Riccardo Poli (email: rpoli@essex.ac.uk)
 *
 */

import java.util.*;
import java.io.*;

/** @noinspection ResultOfMethodCallIgnored*/
public class TinyGP {

    String outputFileName;
    String functionName;
    double [] fitness;
    char [][] pop;
    static Random random = new Random();
    static final int
        ADD = 110,
        SUB = 111,
        MUL = 112,
        DIV = 113,
        SIN = 114,
        COS = 115,
        FSET_START = ADD,
        FSET_END = COS;
    static double [] x = new double[FSET_START];
    static int supplementaryConstantsCounter = 0;
    static Dictionary<Integer, Double> supplementaryConstants = new Hashtable<>();
    static double minRandom, maxRandom;
    static char [] program;
    static int programCounter;
    static int varNumber, fitnessCases, randomNumber;
    static double bestPop = 0.0, avgPop = 0.0;
    static long seed;
    static double avgLen;
    static final int
            MAX_LEN = 10000,
            POPSIZE = 25000,
            DEPTH   = 5,
            GENERATIONS = 21,
            TSIZE = 2;
    public static final double
            PMUT_PER_NODE  = 0.05,
            CROSSOVER_PROB = 0.9;
    static double [][] targets;

    double run() { /* Interpreter */
        char primitive = program[programCounter++];
        if ( primitive < FSET_START )
            return(x[primitive]);
        if ( primitive > FSET_END)
            return(supplementaryConstants.get((int) primitive));
        switch ( primitive ) {
            case ADD : return( run() + run() );
            case SUB : return( run() - run() );
            case MUL : return( run() * run() );
            case DIV : {
                           double num = run(), den = run();
                           if ( Math.abs( den ) <= 0.001 )
                               return( num );
                           else
                               return( num / den );
                       }
            case SIN : return( Math.sin( run() + run() ) );
            case COS : return( Math.cos( run() + run() ) );
        }
        return( 0.0 ); // should never get here
    }

    int traverse( char [] buffer, int bufferCount ) {
        if ( buffer[bufferCount] < FSET_START )
            return( ++bufferCount );

        return switch (buffer[bufferCount]) {
            case ADD, SUB, MUL, DIV, SIN, COS -> (traverse(buffer, traverse(buffer, ++bufferCount)));
            default -> (0);
        };
        // should never get here
    }

    void setupFitness(String fileName) {
        try {
            int i,j;
            String line;

            BufferedReader in =
                new BufferedReader(
                        new
                        FileReader(fileName));
            line = in.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            varNumber = Integer.parseInt(tokens.nextToken().trim());
            randomNumber = Integer.parseInt(tokens.nextToken().trim());
            minRandom =	Double.parseDouble(tokens.nextToken().trim());
            maxRandom =  Double.parseDouble(tokens.nextToken().trim());
            fitnessCases = Integer.parseInt(tokens.nextToken().trim());
            targets = new double[fitnessCases][varNumber +1];
            if (varNumber + randomNumber >= FSET_START )
                System.out.println("too many variables and constants");

            for (i = 0; i < fitnessCases; i ++ ) {
                line = in.readLine();
                tokens = new StringTokenizer(line);
                for (j = 0; j <= varNumber; j++) {
                    targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
                }
            }
            in.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("ERROR: Please provide a data file");
            System.exit(0);
        }
        catch(Exception e ) {
            System.out.println("ERROR: Incorrect data format");
            System.exit(0);
        }
    }

    double fitnessFunction(char [] program ) {
        int i = 0, len;
        double result, fit = 0.0;

        len = traverse( program, 0 );
        for (i = 0; i < fitnessCases; i ++ ) {
            if (varNumber >= 0) System.arraycopy(targets[i], 0, x, 0, varNumber);
            TinyGP.program = program;
            programCounter = 0;
            result = run();
            fit += Math.abs( result - targets[i][varNumber]);
        }
        return(-fit );
    }

    int grow( char [] buffer, int pos, int max, int depth ) {
        char prim = (char) random.nextInt(2);
        int one_child;

        if ( pos >= max )
            return( -1 );

        if ( pos == 0 )
            prim = 1;

        if ( prim == 0 || depth == 0 ) {
            prim = (char) random.nextInt(varNumber + randomNumber);
            buffer[pos] = prim;
            return(pos+1);
        }
        else  {
            prim = (char) (random.nextInt(FSET_END - FSET_START + 1) + FSET_START);
            switch (prim) {
                case ADD, SUB, MUL, DIV, SIN, COS -> {
                    buffer[pos] = prim;
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0)
                        return (-1);
                    return (grow(buffer, one_child, max, depth - 1));
                }
            }
        }
        return( 0 ); // should never get here
    }

    int printIndiv(char []buffer, int bufferCounter ) {
        int a1=0, a2;
        if ( buffer[bufferCounter] < FSET_START ) {
            if ( buffer[bufferCounter] < varNumber)
                System.out.print( "X"+ (buffer[bufferCounter] + 1 )+ " ");
            else
                System.out.print( x[buffer[bufferCounter]]);
            return( ++bufferCounter );
        }
        if ( buffer[bufferCounter] > FSET_END){
            System.out.print( supplementaryConstants.get((int) buffer[bufferCounter]) + " ");
            return( ++bufferCounter );
        }
        switch (buffer[bufferCounter]) {
            case ADD -> {
                System.out.print("(");
                a1 = printIndiv(buffer, ++bufferCounter);
                System.out.print(" + ");
            }
            case SUB -> {
                System.out.print("(");
                a1 = printIndiv(buffer, ++bufferCounter);
                System.out.print(" - ");
            }
            case MUL -> {
                System.out.print("(");
                a1 = printIndiv(buffer, ++bufferCounter);
                System.out.print(" * ");
            }
            case DIV -> {
                System.out.print("(");
                a1 = printIndiv(buffer, ++bufferCounter);
                System.out.print(" / ");
            }
            case SIN -> {
                System.out.print("SIN(");
                a1 = printIndiv(buffer, ++bufferCounter);
                System.out.print(" + ");
            }
            case COS -> {
                System.out.print("COS(");
                a1 = printIndiv(buffer, ++bufferCounter);
                System.out.print(" + ");
            }
        }
        a2= printIndiv( buffer, a1 );
        System.out.print( ")");
        return( a2);
    }

    boolean canOptimize(char[] program, int[] bufferCounter, int[] maxIndex){
        boolean a1=true, a2;
        if ( program[bufferCounter[0]] < FSET_START ) {
            if ( program[bufferCounter[0]] >= varNumber) {
                if (maxIndex[0] < bufferCounter[0])
                    maxIndex[0] = bufferCounter[0];
                return true;
            }
            else {
                if (maxIndex[0] < bufferCounter[0])
                    maxIndex[0] = bufferCounter[0];
                return false;
            }
        }
        if ( program[bufferCounter[0]] > FSET_END){
            if (maxIndex[0] < bufferCounter[0])
                maxIndex[0] = bufferCounter[0];
            return true;
        }
        switch (program[bufferCounter[0]]){
            case ADD, SUB, DIV, MUL, SIN, COS -> {
                bufferCounter[0]++;
                a1 = canOptimize(program, bufferCounter, maxIndex);
                bufferCounter[0]++;
                a2 = canOptimize(program, bufferCounter, maxIndex);
                return a1 && a2;
            }
        }
        return false;
    }

    char[] optimizeFun(char[] old_program, boolean verbose){
        int len = old_program.length;
        int i = 0;
        while (i < len) {
            if (old_program[i] < FSET_START) {
                i++;
                continue;
            }
            int[] maxIndex = new int[1];
            maxIndex[0] = i;
            int[] staticBufferCounter = new int[1];
            staticBufferCounter[0] = i;
            if (canOptimize(old_program, staticBufferCounter, maxIndex)) {
                char[] subProgram = new char[maxIndex[0] - i + 1];
                System.arraycopy(old_program, i, subProgram, 0, maxIndex[0] - i + 1);
                if(verbose) {
                    System.out.print("Optimized: ");
                    printIndiv(subProgram, 0);
                    System.out.print(" to: ");
                }
                programCounter = 0;
                TinyGP.program = subProgram;
                double result = run();
                if(verbose)
                    System.out.print(result + "\n");
                supplementaryConstantsCounter++;
                int newConstantIndex = FSET_END + supplementaryConstantsCounter;
                supplementaryConstants.put(newConstantIndex, result);
                old_program[i] = (char) newConstantIndex;
                System.arraycopy(old_program, maxIndex[0] + 1, old_program, i + 1, len - maxIndex[0] - 1);
                len = len - (maxIndex[0] - i);
            }
            i++;
        }
        char[] optimizedProgram = new char[len];
        System.arraycopy(old_program, 0, optimizedProgram, 0, len);
        return optimizedProgram;
    }

    void saveStats(String line) {
        String path = "evolutionMonitoring/" + functionName + "/";
        String fileName = outputFileName.split("\\.dat")[0] + ".csv";
        String fullPath = path + fileName;

        new File(path).mkdirs();

        File file = new File(fullPath);

        try {
            file.createNewFile();

            FileWriter fw = new FileWriter(fullPath, true);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(line.replace(".", ","));
            writer.newLine();

            writer.close();
        }
        catch (IOException ioe) {

            ioe.printStackTrace();
        }
    }

    static char [] buffer = new char[MAX_LEN];
    char [] createRandomIndiv(int depth ) {
        char [] ind;
        int len;

        len = grow( buffer, 0, MAX_LEN, depth );

        while (len < 0 )
            len = grow( buffer, 0, MAX_LEN, depth );

        ind = new char[len];

        System.arraycopy(buffer, 0, ind, 0, len );
        return( ind );
    }

    char [][] createRandomPop(int n, int depth, double [] fitness ) {
        char [][]pop = new char[n][];
        int i;

        for ( i = 0; i < n; i ++ ) {
            pop[i] = createRandomIndiv( depth );
            fitness[i] = fitnessFunction( pop[i] );
        }
        return( pop );
    }

    char[] optimize(char[] from, boolean verbose){
        char[] fromCopy = new char[from.length];
        char[] to;
        System.arraycopy(from, 0, fromCopy, 0, from.length);
        if (verbose) {
            System.out.print("\n-------- Optimization Info --------\n");
        }
        to = optimizeFun(fromCopy, verbose);
        if (verbose) {
            System.out.print("Length before optimization: " + from.length + " Length after optimization: " + to.length + "\n");
            System.out.print("-----------------------------------\n");
        }
        return to;
    }

    void stats( double [] fitness, char [][] pop, int gen ) {
        int i, best = random.nextInt(POPSIZE);
        int node_count = 0;
        bestPop = fitness[best];
        avgPop = 0.0;

        for ( i = 0; i < POPSIZE; i ++ ) {
            node_count +=  traverse( pop[i], 0 );
            avgPop += fitness[i];
            if ( fitness[i] > bestPop) {
                best = i;
                bestPop = fitness[i];
            }
        }
        avgLen = (double) node_count / POPSIZE;
        avgPop /= POPSIZE;
        System.out.print("Generation="+gen+" Avg Fitness="+(-avgPop)+
                " Best Fitness="+(-bestPop)+" Avg Size="+ avgLen +
                "\nBest Individual: ");
        printIndiv( optimize(pop[best], true), 0 );
        System.out.print( "\n\n");
        System.out.flush();

        saveBestIndivToCsv(pop[best], 0); //my own code
        saveStats(gen + ";" + (-avgPop) + ";" + (-bestPop)); //my own code
    }

    int tournament( double [] fitness, int tournamentSize ) {
        int best = random.nextInt(POPSIZE), i, competitor;
        double  fbest = -1.0e34;

        for ( i = 0; i < tournamentSize; i ++ ) {
            competitor = random.nextInt(POPSIZE);
            if ( fitness[competitor] > fbest ) {
                fbest = fitness[competitor];
                best = competitor;
            }
        }
        return( best );
    }

    int negativeTournament(double [] fitness, int tournamentSize ) {
        int worst = random.nextInt(POPSIZE), i, competitor;
        double fworst = 1e34;

        for ( i = 0; i < tournamentSize; i ++ ) {
            competitor = random.nextInt(POPSIZE);
            if ( fitness[competitor] < fworst ) {
                fworst = fitness[competitor];
                worst = competitor;
            }
        }
        return( worst );
    }

    char [] crossover( char []firstParent, char [] secondParent ) {
        int xo1start, xo1end, xo2start, xo2end;
        char [] offspring;
        int len1 = traverse( firstParent, 0 );
        int len2 = traverse( secondParent, 0 );
        int lenoff;

        xo1start =  random.nextInt(len1);
        xo1end = traverse( firstParent, xo1start );

        xo2start =  random.nextInt(len2);
        xo2end = traverse( secondParent, xo2start );

        lenoff = xo1start + (xo2end - xo2start) + (len1-xo1end);

        offspring = new char[lenoff];

        System.arraycopy( firstParent, 0, offspring, 0, xo1start );
        System.arraycopy( secondParent, xo2start, offspring, xo1start,
                (xo2end - xo2start) );
        System.arraycopy( firstParent, xo1end, offspring,
                xo1start + (xo2end - xo2start),
                (len1-xo1end) );

        return( offspring );
    }

    char [] mutation( char [] parent, double mutationProbability ) {
        int len = traverse( parent, 0 ), i;
        int mutsite;
        char [] parentcopy = new char [len];

        System.arraycopy( parent, 0, parentcopy, 0, len );
        for (i = 0; i < len; i ++ ) {
            if ( random.nextDouble() < mutationProbability ) {
                mutsite =  i;
                if ( parentcopy[mutsite] < FSET_START )
                    parentcopy[mutsite] = (char) random.nextInt(varNumber + randomNumber);
                else
                    switch (parentcopy[mutsite]) {
                        case ADD, SUB, MUL, DIV, SIN, COS -> parentcopy[mutsite] =
                                (char) (random.nextInt(FSET_END - FSET_START + 1)
                                        + FSET_START);
                    }
            }
        }
        return( parentcopy );
    }

    void printParms() {
        System.out.print("-- TINY GP (Java version) --\n");
        System.out.print("SEED="+seed+"\nMAX_LEN="+MAX_LEN+
                "\nPOPSIZE="+POPSIZE+"\nDEPTH="+DEPTH+
                "\nCROSSOVER_PROB="+CROSSOVER_PROB+
                "\nPMUT_PER_NODE="+PMUT_PER_NODE+
                "\nMIN_RANDOM="+ minRandom +
                "\nMAX_RANDOM="+ maxRandom +
                "\nGENERATIONS="+GENERATIONS+
                "\nTSIZE="+TSIZE+
                "\n----------------------------------\n");
    }

    public TinyGP(String fname, long s ) {
        fitness =  new double[POPSIZE];
        seed = s;
        if ( seed >= 0 )
            random.setSeed(seed);
        setupFitness(fname);
        for ( int i = 0; i < FSET_START; i ++ )
            x[i]= (maxRandom - minRandom)* random.nextDouble()+ minRandom;
        pop = createRandomPop(POPSIZE, DEPTH, fitness );

        //my own code
        String[] names = fname.split("/");
        outputFileName = names[2];
        functionName = names[1];
    }

    void evolve() {
        int gen = 0, indivs, offspring, parent1, parent2, parent;
        double newfit;
        char []newind;
        printParms();
        stats( fitness, pop, 0 );
        for ( gen = 1; gen < GENERATIONS; gen ++ ) {
            if (  bestPop > -1e-5 ) {
                System.out.print("PROBLEM SOLVED\n");
                System.exit( 0 );
            }
            for ( indivs = 0; indivs < POPSIZE; indivs ++ ) {
                if ( random.nextDouble() < CROSSOVER_PROB  ) {
                    parent1 = tournament( fitness, TSIZE );
                    parent2 = tournament( fitness, TSIZE );
                    newind = crossover( pop[parent1],pop[parent2] );
                }
                else {
                    parent = tournament( fitness, TSIZE );
                    newind = mutation( pop[parent], PMUT_PER_NODE );
                }
                newfit = fitnessFunction( newind );
                offspring = negativeTournament( fitness, TSIZE );
                pop[offspring] = newind;
                fitness[offspring] = newfit;
            }
            stats( fitness, pop, gen );
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
        //System.exit( 1 );
    }

    //my own method
    void saveBestIndivToCsv(char []buffer, int bufferCounter) {

        String path = "outputData/" + functionName + "/";
        String fileName = outputFileName;
        String fullPath = path + fileName;

        new File(path).mkdirs();

        File file = new File(fullPath);

        try {
            file.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));

            int k = saveBestIndiv(buffer, bufferCounter, writer);

            writer.close();
        }
        catch (IOException ioe) {

            ioe.printStackTrace();
        }
    }

    //my own method
    int saveBestIndiv(char []buffer, int bufferCounter, BufferedWriter writer ) throws IOException {
        int a1=0, a2;

        if ( buffer[bufferCounter] < FSET_START ) {
            if ( buffer[bufferCounter] < varNumber)
                writer.write( "X"+ (buffer[bufferCounter] + 1 )+ " ");
            else
                writer.write( x[buffer[bufferCounter]] + "");
            return( ++bufferCounter );
        }
        switch (buffer[bufferCounter]) {
            case ADD -> {
                writer.write("(");
                a1 = saveBestIndiv(buffer, ++bufferCounter, writer);
                writer.write(" + ");
            }
            case SUB -> {
                writer.write("(");
                a1 = saveBestIndiv(buffer, ++bufferCounter, writer);
                writer.write(" - ");
            }
            case MUL -> {
                writer.write("(");
                a1 = saveBestIndiv(buffer, ++bufferCounter, writer);
                writer.write(" * ");
            }
            case DIV -> {
                writer.write("MyDivide(");
//                writer.write( "(");
                a1 = saveBestIndiv(buffer, ++bufferCounter, writer);
//                writer.write( " / ");
                writer.write(" ; ");
            }
            case SIN -> {
                writer.write("SIN(");
                a1 = saveBestIndiv(buffer, ++bufferCounter, writer);
                writer.write(" + ");
            }
            case COS -> {
                writer.write("COS(");
                a1 = saveBestIndiv(buffer, ++bufferCounter, writer);
                writer.write(" + ");
            }
        }
        a2= saveBestIndiv( buffer, a1, writer );
        writer.write( ")");
        return( a2);
    }

    public static void main(String[] args) {

        String fileName = "data/FUN4/FUN4-[0.0,100.0]-[0.0,100.0].dat";
        long s = -1;

        if ( args.length == 2 ) {
            s = Integer.parseInt(args[0]);
            fileName = args[1];
        }
        if ( args.length == 1 ) {
            fileName = args[0];
        }

        TinyGP gp = new TinyGP(fileName, s);
        gp.evolve();
    }
};
