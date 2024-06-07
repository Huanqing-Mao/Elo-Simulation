
public class RatingAPI {
    public static final double K = 40.0;
    public static final double CONSTANT = 400.0;

    RatingAPI() {}

    double calculateChange(Person person1, Person person2, double resultOfPerson1) { // Elo formula
        double power = (person2.getRating() - person1.getRating()) / CONSTANT;
        double expected = 1.0 /(Math.pow(10.0, power) + 1);
        double change = K * (resultOfPerson1 - expected);
        return change;
    }
    
    // if C is inspected
    void updateRatingInspect(Person A, Person B, Person C, int Aanswer, int Banswer, int Canswer) {

        // if A and B are the same

            // 0.2 chance of C being inspected (ADD LATER), if C not inspected, only A and B are compared
            // if all the same, all tie, C take average


            // if A = B != C, A1 vs A2, A1 vs C, A2 vs C, C take average



        





        // if A and B are not the same
            











    }

}
