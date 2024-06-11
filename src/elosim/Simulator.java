package elosim;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Simulator {
    public static int NUM_LOOP;
    public static int NUMQUESTIONS;
    public static final int NUM_AB = 30;
    public static final int NUM_C = 9;
    public static final double MIN = 0.5;
    public static final double MAX = 0.9;
    public static final int CORRECT_ANSWER = 1;

    ArrayList<Person> AList = new ArrayList<Person>();
    ArrayList<Person> BList = new ArrayList<Person>();
    ArrayList<Person> CList = new ArrayList<Person>();
    HashMap<Person, ArrayList<Double>> CRatingDistri = new HashMap<Person, ArrayList<Double>>();


    // export percentile data as a csv file
    public static void writePercentileFile(String fileName, String[] header, ArrayList<ArrayList<String>> data) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // write header
            for (int i = 0; i < header.length; i++) {
                writer.print(header[i]);
                if (i < header.length - 1) {
                    writer.print(",");
                }
            }
            writer.println();


            // write data
            for (ArrayList<String> row : data) {
                for (int i = 0; i < row.size(); i++) {
                    writer.print(row.get(i));
                    if (i < row.size() - 1) writer.print(",");
                }
                writer.println();
            }

            System.out.println("File created successfully.");
            

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // export final ratings of C into a csv file
    public static void writeCResults(String fileName, String[] header, double[][] data) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // write header
            for (int i = 0; i < header.length; i++) {
                writer.print(header[i]);
                if (i < header.length - 1) {
                    writer.print(",");
                }
            }
            writer.println();


            // write data
            for (double[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    writer.print(row[i]);
                    if (i < row.length - 1) writer.print(",");
                }
                writer.println();
            }

            System.out.println("Results File created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        // custom NUM LOOP and NUM QUESTIONS
        Scanner sc = new Scanner(System.in);
        NUM_LOOP = sc.nextInt();
        NUMQUESTIONS = sc.nextInt();

        // initialise the lists of Person C
        ArrayList<Person> CList = new ArrayList<Person>();
        Simulator simulator = new Simulator();
        Random random = new Random();
        RatingAPI api = new RatingAPI(CORRECT_ANSWER);
       

        for (int j = 0; j < NUM_C; j++) {
            double accuracyC = 0.2 + 0.1 * j;
            Person c = new Person("C", j + 1, 1800.0, accuracyC);
            CList.add(c);
            simulator.CRatingDistri.put(c, new ArrayList<Double>());
        }


        // get distribution for each C
        for (Person c : CList) {
            
            for (int i = 0; i < NUM_LOOP; i++) {
                c.resetRating(1800.0);

                // initialise the list of A and the list of B
                ArrayList<Person> AList = new ArrayList<Person>();
                ArrayList<Person> BList = new ArrayList<Person>();
                
                for (int k = 0; k < NUM_AB; k++) {
                    
                    double accuracyA = MIN + (MAX - MIN) * random.nextDouble();
                    double accuracyB = MIN + (MAX - MIN) * random.nextDouble();
                    AList.add(new Person("A", k + 1, 1400.0, accuracyA));
                    BList.add(new Person("B", k + 1, 1600.0, accuracyB));
                   
                };

                
                for (int j = 0; j < NUMQUESTIONS; j++) {
                    //System.out.println("Qm #" + (j + 1));

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

                // add the rating of C after doing NUM_QUESTIONS qns
                double currRatingOfC = c.getRating();
                simulator.CRatingDistri.get(c).add(currRatingOfC);
            }

        }



        // record and summarise data
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        double[][] cValues = new double[NUM_LOOP][NUM_C];
        String[] accuracies = new String[NUM_C];    
        System.out.println("=========== Summary ============");
        System.out.println("NLoop = " + NUM_LOOP);
        System.out.println("Num QNs = " + NUMQUESTIONS);

        int currPersonIndex = 0;
        for (Person c : CList) {
            System.out.println();
            System.out.println(c.toString());
            ArrayList<Double> cPerformance = simulator.CRatingDistri.get(c);
            Collections.sort(cPerformance);
            int length = cPerformance.size();
            System.out.println("length = " + length);
            int perc5 = length / 20;
            int perc20 = length / 5;
            int perc30 = 3 * length / 10;
            int perc50 = length / 2;
            int perc70 = 7 * length / 10;
            int perc90 = 9 * length / 10;
            int perc95 = length - length / 20;
            int[] indices = new int[]{perc5, perc20, perc30, perc50, perc70, perc90, perc95};

            for (int idx = 0; idx < indices.length; idx++) {
                System.out.println(String.format("%.3f Percentile = %.3f", 1.0 * indices[idx] / length, cPerformance.get(indices[idx])));

            }

            // add rows to the percentile data            
            ArrayList<String> row = new ArrayList<String>();
            row.add(String.format("%.1f", c.getAccuracy()));
            for (int id : indices) {
                row.add("" + cPerformance.get(id));
            }
            
            data.add(row);


            // add columns to the results data
            accuracies[currPersonIndex] = String.format("%.1f", c.getAccuracy());
            ArrayList<Double> results = simulator.CRatingDistri.get(c);
            for (int j = 0; j < results.size();j++) {
                double rating = results.get(j);
                cValues[j][currPersonIndex] = rating;
            }
            currPersonIndex++;
            
        }

        // export percentiles
        String[] header = {"Accuracy", "5% Percentile", "20% Percentile", "30% Percentile", "50% Percentile", "70% Percentile", "90% Percentile", "95% Percentile"};
        String csvFile = String.format("data/C Performance Distribution %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        writePercentileFile(csvFile, header, data);

        
        // export the actual C values
        String resultsFile = String.format("data/Actual Results %dloop %dqn.csv", NUM_LOOP, NUMQUESTIONS);
        writeCResults(resultsFile, accuracies, cValues);
        

        
        
        


    }

}
