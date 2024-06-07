public class RatingAPI {
    public static final double K = 40.0;
    public static final double CONSTANT = 400.0;
    public static final double WIN = 1.0;
    public static final double LOSE = 0.0;
    public static final double TIE = 0.5;
    public static final int CORRECT_ANSWER = 1;

    RatingAPI() {}

    double calculateChange(Person person1, Person person2, double resultOfPerson1) { // Elo formula
        double power = (person2.getRating() - person1.getRating()) / CONSTANT;
        double expected = 1.0 /(Math.pow(10.0, power) + 1);
        double change = K * (resultOfPerson1 - expected);
        return change;
    }
    
    // if C is inspected
    void updateRatingInspect(Person A, Person B, Person C, int Aanswer, int Banswer, int Canswer) {
        // 0.2 chance of C being inspected (ADD LATER), if C not inspected, only A and B are compared for both cases

        double AChange;
        double BChange;
        double CChange;

        
        if (Aanswer == Banswer) {   // if A == B
            
            if (Aanswer == Canswer) {

                // A vs B
                AChange = this.calculateChange(A, B, TIE);
                BChange = this.calculateChange(B, A, TIE);

                // A vs C and B vs C, C take average
                double CvA = this.calculateChange(C, A, TIE);
                double CvB = this.calculateChange(C, B, TIE);
                CChange = (CvA + CvB) / 2.0;


            } else {    // if A = B != C, A vs B, A vs C, B vs C, C take average
                // if A and B are correct, then C is wrong
                if (Aanswer == CORRECT_ANSWER && Canswer != CORRECT_ANSWER) {

                    // A vs B



                    // A vs C and B vs C, C take average

                }
                




                // if A and B are wrong
                    // if C is correct



                    // else if C is wrong


            }

        } else {    // if A != B

            // if all different: do nothing

            // else:
                // if C is correct, A vs B, C no change





                // if C is incorrect
                    // if all wrong: all tie, C take average (del C < 0)


                    // if the different one is correct: 
                        // get who is correct (let's say B)
                        // C vs B, A vs B, B take average (del B > 0)




        }


        // update ratings for all three

            

    }

}
