package elosim;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Simulator {
    public static int NUM_LOOP;
    public static int NUMQUESTIONS;
    public static final int NUM_AB = 30;
    public static final int NUM_C = 11;
    public static final double MEAN = 0.7;
    public static final double STD = 1.0;
    public static final double MIN = 0.4;
    public static final double MAX = 1.0;
    
    public static final int CORRECT_ANSWER = 1;
    public static final double A_DEFAULT = 1400.0;
    public static final double B_DEFAULT = 1400.0;
    public static final double C_DEFAULT = 1800.0;

    
    HashMap<Person, ArrayList<Double>> CRatingDistri = new HashMap<Person, ArrayList<Double>>();
    HashMap<String, ArrayList<Double>> ARatingDistri = new HashMap<String, ArrayList<Double>>();
    HashMap<String, ArrayList<Double>> BRatingDistri = new HashMap<String, ArrayList<Double>>();
    

    // generate normal distributions
    public static ArrayList<Person> generateNormalDistributionPerson(double mean, double stdDev, int numPoints, String type, double initialRating) {
        Random random = new Random();
        ArrayList<Person> people = new ArrayList<Person>();

        int counter = 0;
        
        while (counter < numPoints) {
            double assignedAccuracy = mean + stdDev * random.nextGaussian();
            if (assignedAccuracy >= MIN && assignedAccuracy <= MAX) {
                people.add(new Person(type, counter + 1, initialRating, assignedAccuracy));
                counter++;
            }

        }
        
        return people;

        
    }

    public static void main(String[] args) {
        // custom NUM LOOP and NUM QUESTIONS
        Scanner sc = new Scanner(System.in);
        NUM_LOOP = sc.nextInt();
        NUMQUESTIONS = sc.nextInt();

        // initialise the lists of Person C
        ArrayList<Person> CList = new ArrayList<Person>();
        Simulator simulator = new Simulator();
        Summariser summariser = new Summariser();
        Random random = new Random();
        RatingAPI api = new RatingAPI(CORRECT_ANSWER);
       

        for (int j = 0; j < NUM_C - 3; j++) {
            double accuracyC = 0.2 + 0.1 * j;
            //double accuracyC = 1.0;
            Person c = new Person("C", j + 1, C_DEFAULT, accuracyC);
            CList.add(c);
            simulator.CRatingDistri.put(c, new ArrayList<Double>());
        }

        // Add C = 0.95 and C = 0.99
        Person c95 = new Person("C", NUM_C - 2, C_DEFAULT, 0.95);
        Person c99 = new Person("C", NUM_C - 1, C_DEFAULT, 0.99);
        Person c100 = new Person("C", NUM_C, C_DEFAULT, 1.0);

        CList.add(c95);
        CList.add(c99);
        CList.add(c100);
        simulator.CRatingDistri.put(c95, new ArrayList<Double>());
        simulator.CRatingDistri.put(c99, new ArrayList<Double>());
        simulator.CRatingDistri.put(c100, new ArrayList<Double>());

        // initialise the bins of A B and put into hashmaps
        ArrayList<String> bins = new ArrayList<String>();
        for (double lo = 0.4; lo <= 0.9; lo += 0.1) {
            String domain = String.format("%.3f ~ %.3f", lo, lo + 0.1);
            bins.add(domain);
            simulator.ARatingDistri.put(domain, new ArrayList<Double>());
            simulator.BRatingDistri.put(domain, new ArrayList<Double>());
        }



        // get distribution for each C
        for (int num = 0; num < CList.size(); num++) {
            Person c = CList.get(num);
            
            for (int i = 0; i < NUM_LOOP; i++) {
                c.resetRating(C_DEFAULT);

                // initialise the list of A and the list of B
                ArrayList<Person> AList = generateNormalDistributionPerson(MEAN, STD, NUM_AB, "A", A_DEFAULT);
                ArrayList<Person> BList = generateNormalDistributionPerson(MEAN, STD, NUM_AB, "B", B_DEFAULT);
               
                
                // simulate A, B, C answering custom number of questions
                for (int j = 0; j < NUMQUESTIONS; j++) {

                    // get random A and B
                    int indexA = random.nextInt(NUM_AB);
                    int indexB = random.nextInt(NUM_AB);
                    Person a = AList.get(indexA);
                    Person b = BList.get(indexB);

                    // all three answer questions acc to their accuracies and update ratings
                    int Aanswer = a.giveAnswer(CORRECT_ANSWER);
                    int Banswer = b.giveAnswer(CORRECT_ANSWER);
                    int Canswer = c.giveAnswer(CORRECT_ANSWER);
                    api.updateGroupRating(a, b, c, Aanswer, Banswer, Canswer);
                    
                    
                }

                // add the rating of C after doing NUM_QUESTIONS qns to the hashmap
                double currRatingOfC = c.getRating();
                simulator.CRatingDistri.get(c).add(currRatingOfC);

                // add all the final ratings of A, B in the list into the hashmap based on their bins
                // (some may stay unchanged because they may not be chosen to answer questions at all)
                for (Person a : AList) {
                    double currRatingOfA = a.getRating();
                    int indexOfDomain = (int) ((a.getAccuracy() - 0.4) / 0.1); // calculate which bin they belong to
                    indexOfDomain = Math.max(indexOfDomain, 0);

                    String domain = bins.get(indexOfDomain);
                    simulator.ARatingDistri.get(domain).add(currRatingOfA);
                    
                }

                for (Person b : BList) {
                    double currRatingOfB = b.getRating();
                    int indexOfDomain = (int) ((b.getAccuracy() - 0.4) / 0.1); // calculate which bin they belong to

                    // comment out later
                    indexOfDomain = Math.max(indexOfDomain, 0);

                    String domain = bins.get(indexOfDomain);
                    simulator.BRatingDistri.get(domain).add(currRatingOfB);

                }

                
            }

        }
        
       
        // record and summarise C
        ArrayList<ArrayList<String>> cData = new ArrayList<ArrayList<String>>();
        double[][] cValues = new double[NUM_LOOP][NUM_C];
        String[] cAccuracies = new String[NUM_C];    
        System.out.println("=========== Summary C ============");
        System.out.println("NLoop = " + NUM_LOOP);
        System.out.println("Num QNs = " + NUMQUESTIONS);

        summariser.summariseC(CList, simulator.CRatingDistri, cData, cAccuracies, cValues);

        // export percentiles
        String[] header = {"Accuracy", "5% Percentile", "20% Percentile", "30% Percentile", "50% Percentile", "70% Percentile", "90% Percentile", "95% Percentile"};
        String csvFile = String.format("data/C Performance Distribution %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        summariser.writePercentileFile(csvFile, header, cData);

        
        // export the actual C values
        String resultsFile = String.format("data/Actual Results C %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        summariser.writeResults(resultsFile, cAccuracies, cValues);


        
        // record and summarise A --> A list
        System.out.println("=========== Summary A ============");
        ArrayList<ArrayList<String>> aData = new ArrayList<ArrayList<String>>();
        double[][] aValues = new double[10000000][bins.size()];
        String[] aAccuracies = new String[bins.size()];

        summariser.summariseAB(bins, simulator.ARatingDistri, aData, aAccuracies, aValues);

        // export percentiles of A
        String csvA = String.format("data/A Performance Distribution %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        summariser.writePercentileFile(csvA, header, aData);

        // export actual A values
        String resultsA = String.format("data/Actual Results A %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        // writeResults(resultsA, aAccuracies, aValues); --> time consuming

        System.out.println("=========== Summary B ============");
        // record and summarise B --> B list
        ArrayList<ArrayList<String>> bData = new ArrayList<ArrayList<String>>();
        double[][] bValues = new double[10000000][bins.size()];
        String[] bAccuracies = new String[bins.size()];

        summariser.summariseAB(bins, simulator.BRatingDistri, bData, bAccuracies, bValues);

        // export percentiles of B
        String csvB = String.format("data/B Performance Distribution %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        summariser.writePercentileFile(csvB, header, bData); 


        // export actual B values
        String resultsB = String.format("data/Actual Results B %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        // writeResults(resultsB, bAccuracies, bValues); --> time consuming


    }

}
