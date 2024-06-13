package elosim; // comment this to use testPerson.jsh or testAPI.jsh

import java.util.*;

class RatingAPI {
    public static final double K = 40.0;
    public static final double CONSTANT = 400.0;
    public static final double WIN = 1.0;
    public static final double LOSE = 0.0;
    public static final double TIE = 0.5;
    public static final double C_INSPECTION_RATE = 0.2;
    private final int correctAnswer;

    RatingAPI(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    double calculateChange(Person person1, Person person2, double resultOfPerson1) { // Elo formula for person 1
        double power = (person2.getRating() - person1.getRating()) / CONSTANT;
        double expected = 1.0 /(Math.pow(10.0, power) + 1);
        double change = K * (resultOfPerson1 - expected);
        return change;
    }
    
   
    void updateGroupRating(Person A, Person B, Person C, int Aanswer, int Banswer, int Canswer) {

        // initialise the changes to 0
        double AChange = 0.0;
        double BChange = 0.0;
        double CChange = 0.0;
        
        

        if (Aanswer == Banswer) {   // if A == B
            double inspection = new Random().nextDouble();
                
            // initialise A and B as tie : A vs B
            AChange = this.calculateChange(A, B, TIE);
            BChange = this.calculateChange(B, A, TIE);


            double CvA = 0.0;
            double CvB = 0.0;

            // 20 % chance of C being inspected
            if (inspection <= C_INSPECTION_RATE) { // C is inspected
                
                if (Aanswer == Canswer) { // if A = B = C, A and B are tie, ***NO CHANGE TO C 
                    // do nothing --> all do nothing
                    AChange = 0.0;
                    BChange = 0.0;
                    CChange = 0.0;
    
                } else {    // if A = B != C
                    
                    if (Aanswer == correctAnswer && Canswer != correctAnswer) {   // if A and B are correct, then C is wrong
                    
                        // A and B are tie, A vs C and B vs C, ***C is the sum of the changes
                        CvA = this.calculateChange(C, A, LOSE);
                        CvB = this.calculateChange(C, B, LOSE);
                        CChange = CvA + CvB;
    
                    } else if (Aanswer != correctAnswer) {    // if A and B are wrong
                        
                        if (Canswer == correctAnswer) {    // if C is correct
                            
                            // A and B are tie, A vs C and B vs C, C take average
                            CvA = this.calculateChange(C, A, WIN);
                            CvB = this.calculateChange(C, B, WIN);
                            CChange = CvA + CvB; // ***NO TAKING AVERAGE
    
                        } else {    // if all wrong                       
    
                            // A vs C, B vs C, C take average (A and B not tie as they both increase)
                            AChange = this.calculateChange(A, C, TIE);
                            BChange = this.calculateChange(B, C, TIE);
                            CvA = -AChange;
                            CvB = -BChange;
                            CChange = CvA + CvB;
    
                        }
                    }
                }
                
    
            } else { // C is not inspected, only A and B are compared to C's answer, C doesn't change


                if (Aanswer == Banswer && Banswer == Canswer) { // if all same: do nothing
                    AChange = 0.0;
                    BChange = 0.0;
                    CChange = 0.0;

                } else { // else: AB tie, C no change
                    AChange = this.calculateChange(A, B, TIE);
                    BChange = this.calculateChange(B, A, TIE);

                }
                
    
            }
    
            

        } else {    // if A != B, if all different, no updates

            // C has a 20% chance of being selected
            double inspectionC = new Random().nextDouble();
            //double inspectionC = 1.0;

            if (inspectionC <= C_INSPECTION_RATE) {    // if C is inspected
                if (!(Aanswer != Banswer && Banswer != Canswer && Aanswer != Canswer)) {    // if not all different --> THE PREVIOUS ERROR
            
                    if (Canswer == this.correctAnswer) {    // if C is correct, A vs B, C no change, at least A or B is correct since not all diff
    
                        if (Aanswer == Canswer && Aanswer == correctAnswer) {    // if A is correct, A win
                            AChange = this.calculateChange(A, B, WIN);
                            BChange = this.calculateChange(B, A, LOSE);
    
                        } else if (Banswer == Canswer && Banswer == correctAnswer) {    // if B is correct, B win
                            
                            AChange = this.calculateChange(A, B, LOSE);
                            BChange = this.calculateChange(B, A, WIN);
    
                        } 
            
                    } else {    // if C is wrong
    
                        if (Aanswer != this.correctAnswer && Banswer !=this.correctAnswer && Canswer != this.correctAnswer) {    // if all wrong, all tie
    
                            // A vs C, B vs C, C take average (A and B not tie as they both increase)
                            AChange = this.calculateChange(A, C, TIE);
                            BChange = this.calculateChange(B, C, TIE);
                            double CvA = -AChange;
                            double CvB = -BChange;
                            CChange = CvA + CvB; // ***NO TAKING AVERAGE
    
                        } else {  // C loses, C compare w winner, other loser compare w winner
    
                            if (Aanswer == this.correctAnswer) {    // if A correct, B = C
                                
                                
                                BChange = this.calculateChange(B, A, LOSE);
                                CChange = this.calculateChange(C, A, LOSE);
    
                                double AvB = this.calculateChange(A, B, WIN);
                                double AvC = this.calculateChange(A, C, WIN);
                                AChange = AvB + AvC; // ***NO TAKING AVERAGE
    
    
                            } else if (Banswer == this.correctAnswer) {    // if B correct, A = C
    
                                AChange = this.calculateChange(A, B, LOSE);
                                CChange = this.calculateChange(C, B, LOSE);
    
                                double BvA = this.calculateChange(B, A, WIN);
                                double BvC = this.calculateChange(B, C, WIN);
                                BChange = BvA + BvC; // ***NO TAKING AVERAGE
    
                            }
    
                        }
                    }
                }

            } else {

                // Assume C is correct
                
                if (!(Aanswer != Banswer && Banswer != Canswer && Aanswer != Canswer)) { // A != B, but one of them equals C
                

                    if (Aanswer == Canswer && Banswer != Canswer) { // A is correct, B is wrong
                        //System.out.println("a correct, B wrong, c default");
                        AChange = this.calculateChange(A, B, WIN);
                        BChange = this.calculateChange(B, A, LOSE);
                    } else if (Banswer == Canswer && Aanswer != Canswer) { // B is correct, A is wrong
                        AChange = this.calculateChange(A, B, LOSE);
                        BChange = this.calculateChange(B, A, WIN);
                    }

                } // else if all different, no changes

            }

            

        }

        

        // update ratings for all three

        // uncomment to test output for individual output
        //System.out.println(String.format("---> Calculated Changes for A: %.3f, B: %.3f, C: %.3f", AChange, BChange, CChange));
        A.updateRating(AChange);
        B.updateRating(BChange);
        C.updateRating(CChange);
    }
}
