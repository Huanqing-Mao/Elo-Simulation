import java.util.*;

public class Simulator {
    public static final int NUM_LOOP = 1000;
    public static final int NUMQUESTIONS = 300;
    public static final double MIN = 0.5;
    public static final double MAX = 0.9;
    public static final int CORRECT_ANSWER = 1;

    ArrayList<Person> AList = new ArrayList<Person>();
    ArrayList<Person> BList = new ArrayList<Person>();
    ArrayList<Person> CList = new ArrayList<Person>();
    HashMap<Person, ArrayList<Double>> CRatingDistri = new HashMap<Person, ArrayList<Double>>();

    public static void main(String[] args) {

        // initialise the lists of Person C
        ArrayList<Person> CList = new ArrayList<Person>();
        Simulator simulator = new Simulator();
        Random random = new Random();
        RatingAPI api = new RatingAPI(CORRECT_ANSWER);
        Person c9 = null;

        for (int j = 0; j <= 8; j++) {
            double accuracyC = 0.2 + 0.1 * j;
            Person c = new Person("C", j + 1, 1800.0, accuracyC);
            if (j == 8) {
                c9 = c;
            }
            CList.add(c);
           simulator.CRatingDistri.put(c, new ArrayList<Double>());
        }


        // get distribution for each C
        for (Person c : CList) {
            
            for (int i = 0; i < NUM_LOOP; i++) {

                // initialise the list of A and the list of B
                ArrayList<Person> AList = new ArrayList<Person>();
                ArrayList<Person> BList = new ArrayList<Person>();
                
                for (int k = 0; k < 30; k++) {
                    
                    double accuracyA = MIN + (MAX - MIN) * random.nextDouble();
                    double accuracyB = MIN + (MAX - MIN) * random.nextDouble();
                    AList.add(new Person("A", k + 1, 1400.0, accuracyA));
                    BList.add(new Person("B", k + 1, 1600.0, accuracyB));
                   
                };

                
                for (int j = 0; j < NUMQUESTIONS; j++) {

                    // get random A and B
                    int indexA = random.nextInt(30);
                    int indexB = random.nextInt(30);
                    Person a = AList.get(indexA);
                    Person b = BList.get(indexB);

                    // all three answer questions acc to their accuracies and update ratings
                    int Aanswer = a.giveAnswer(CORRECT_ANSWER);
                    int Banswer = b.giveAnswer(CORRECT_ANSWER);
                    int Canswer = c.giveAnswer(CORRECT_ANSWER);
                    api.updateGroupRating(a, b, c, Aanswer, Banswer, Canswer);
                    
                }
                double currRatingOfC = c.getRating();
                simulator.CRatingDistri.get(c).add(currRatingOfC);
            }

        }

        for (Person c : CList) {
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
            System.out.println(String.format("Lowest = %.3f", cPerformance.get(0)));
            System.out.println(String.format("5%% Perc = %.3f", cPerformance.get(perc5)));
            System.out.println(String.format("20%% Perc = %.3f", cPerformance.get(perc20)));
            System.out.println(String.format("30%% Perc = %.3f", cPerformance.get(perc30)));
            System.out.println(String.format("50%% Perc = %.3f", cPerformance.get(perc50)));
            System.out.println(String.format("70%% Perc = %.3f", cPerformance.get(perc70)));
            System.out.println(String.format("90%% Perc = %.3f", cPerformance.get(perc90)));
            System.out.println(String.format("95%% Perc = %.3f", cPerformance.get(perc95)));
            System.out.println(String.format("Highest = %.3f\n", cPerformance.get(length - 1)));


        }




        


    }

}
