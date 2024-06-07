public class RatingAPI {
    public static final double K = 40.0;
    public static final double CONSTANT = 400.0;
    public static final double WIN = 1.0;
    public static final double LOSE = 0.0;
    public static final double TIE = 0.5;
    private final int correctAnswer;

    RatingAPI(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    double calculateChange(Person person1, Person person2, double resultOfPerson1) { // Elo formula
        double power = (person2.getRating() - person1.getRating()) / CONSTANT;
        double expected = 1.0 /(Math.pow(10.0, power) + 1);
        double change = K * (resultOfPerson1 - expected);
        return change;
    }
    
    // if C is inspected
    void updateRating(Person A, Person B, Person C, int Aanswer, int Banswer, int Canswer) {
        // 0.2 chance of C being inspected (ADD LATER), if C not inspected, only A and B are compared for both cased
        // here assumes C is inspected:

        double AChange = 0.0;
        double BChange = 0.0;
        double CChange = 0.0;

        
        if (Aanswer == Banswer) {   // if A == B

            // initialise A and B as tie : A vs B
            AChange = this.calculateChange(A, B, TIE);
            BChange = this.calculateChange(B, A, TIE);

            double CvA = 0.0;
            double CvB = 0.0;

            
            if (Aanswer == Canswer) {   // if A = B = C, A and B are tie, A vs C and B vs C, C take average
                
                CvA = this.calculateChange(C, A, TIE);
                CvB = this.calculateChange(C, B, TIE);
                CChange = (CvA + CvB) / 2.0;


            } else {    // if A = B != C
                
                if (Aanswer == correctAnswer && Canswer != correctAnswer) {   // if A and B are correct, then C is wrong

                    // A and B are tie, A vs C and B vs C, C take average
                    CvA = this.calculateChange(C, A, LOSE);
                    CvB = this.calculateChange(C, B, LOSE);
                    CChange = (CvA + CvB) / 2.0;

                } else if (Aanswer != correctAnswer) {    // if A and B are wrong
                       
                    if (Canswer == correctAnswer) {    // if C is correct
                        
                        // A and B are tie, A vs C and B vs C, C take average
                        CvA = this.calculateChange(C, A, WIN);
                        CvB = this.calculateChange(C, B, WIN);
                        CChange = (CvA + CvB) / 2.0;

                    } else {    // if all wrong

                        // A vs C, B vs C, C take average (A and B not tie as they both increase)
                        AChange = this.calculateChange(A, C, TIE);
                        BChange = this.calculateChange(B, C, TIE);
                        CvA = -AChange;
                        CvB = -BChange;
                        CChange = (CvA + CvB) / 2.0;

                    }
                }
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
