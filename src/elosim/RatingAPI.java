package elosim; // comment this to use testPerson.jsh or testAPI.jsh

import java.util.*;

class RatingAPI {
    public static final double K = 40.0;
    public static final double CONSTANT = 400.0;
    public static final double WIN = 1.0;
    public static final double LOSE = 0.0;
    public static final double TIE = 0.5;
    public static final double C_INSPECTION_RATE = 0.2;
    public static final Person score1800 = new Person("1800", 1, 1800.0, 1.0);
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
            // double inspection = new Random().nextDouble();
                
            // initialise A and B as tie : A vs B
            //AChange = this.calculateChange(A, B, TIE);
            //BChange = this.calculateChange(B, A, TIE);


            double CvA = 0.0;
            double CvB = 0.0;

                
            if (Aanswer == Canswer) { // if A = B = C, A and B are tie, ***NO CHANGE TO C 
                // do nothing --> all do nothing
                AChange = 0.0;
                BChange = 0.0;
                CChange = 0.0;

            } else {    // if A = B != C
                
                if (Aanswer == correctAnswer && Canswer != correctAnswer) {   // if A and B are correct, then C is wrong
                
                    // new logic: A, B compare with 1800 as well

                    // check A score: 
                    if (A.getRating() < 1800) { // A win C, A tie 1800
                        AChange = this.calculateChange(A, C, WIN) + this.calculateChange(A, score1800, TIE);
                    } else { // A win C
                        AChange = this.calculateChange(A, C, WIN);
                    }


                    // check B score:
                    if (B.getRating() < 1800) { // B win C, B tie 1800
                        BChange = this.calculateChange(B, C, WIN) + this.calculateChange(B, score1800, TIE);

                    } else {  // B win C
                        BChange = this.calculateChange(B, C, WIN);
                    }

                    // C still same logic: C lose A, C lose B
                    CvA = this.calculateChange(C, A, LOSE);
                    CvB = this.calculateChange(C, B, LOSE);
                    CChange = CvA + CvB;

                } else if (Aanswer != correctAnswer) {    // if A and B are wrong
                    
                    if (Canswer == correctAnswer) {    // if C is correct
                     
                        // check A: A lose C, A lose 1800
                        AChange = this.calculateChange(A, C, LOSE) + this.calculateChange(A, score1800, LOSE);

                        // check B: B lose C, B lose 1800
                        BChange = this.calculateChange(B, C, LOSE) + this.calculateChange(B, score1800, LOSE);
                        
                        // C wins A and C wins B
                        CvA = this.calculateChange(C, A, WIN);
                        CvB = this.calculateChange(C, B, WIN);
                        CChange = CvA + CvB; // ***NO TAKING AVERAGE

                    } else {    // if all wrong
                                         

                        // check A : lose 1800 twice
                        AChange = this.calculateChange(A,score1800, LOSE) + this.calculateChange(A,score1800, LOSE);
                

                        // check B: lose 1800 twice
                        BChange = this.calculateChange(B, score1800, LOSE) + this.calculateChange(B, score1800, LOSE);


                        // check C: lose 1800 twice
                        
                        CChange = this.calculateChange(C, score1800, LOSE) + this.calculateChange(C, score1800, LOSE);

                    }
                }
            }
                


        } else {    // if A != B, if all different, no updates

            // C has a 20% chance of being selected
            double inspectionC = new Random().nextDouble();
            //double inspectionC = 0.0; // for extreme value test

            if (inspectionC <= C_INSPECTION_RATE) {    // if C is inspected
                if (!(Aanswer != Banswer && Banswer != Canswer && Aanswer != Canswer)) {    // if not all different --> THE PREVIOUS ERROR
                    //System.out.println("Yes");
                    if (Canswer == this.correctAnswer) {    // if C is correct, A vs B, C no change, at least A or B is correct since not all diff
    
                        if (Aanswer == Canswer && Aanswer == correctAnswer && Banswer != correctAnswer) {    // if A is correct, A win, B wrong
                          

                            // check A
                            if (A.getRating() < 1800) { // A win B, A tie 1800
                                
                                AChange = this.calculateChange(A, B, WIN) + this.calculateChange(A, score1800, TIE);

                            } else { // A win B
                                AChange = this.calculateChange(A, B, WIN);
                            }



                            // check C
                            if (C.getRating() < 1800) { // C win B, C tie 1800
                                CChange = this.calculateChange(C, B, WIN) + this.calculateChange(C, score1800, TIE);

                            } else { // C win B
                                CChange = this.calculateChange(C, B, WIN);

                            }



                            // check B: B lose A and B lose C
                            BChange = this.calculateChange(B, A, LOSE) + this.calculateChange(B, C, LOSE);


    
                        } else if (Banswer == Canswer && Banswer == correctAnswer && Aanswer != correctAnswer) {    // if B is correct, B win, A wrong
                           

                            // check B
                            if (B.getRating() < 1800) { // B win A and B tie 1800
                                BChange = this.calculateChange(B, A, WIN) + this.calculateChange(B, score1800, TIE);

                            } else { // B win A
                                BChange = this.calculateChange(B, A, WIN);

                            }

                            // check C
                            if (C.getRating() < 1800) { // C win A, C tie 1800
                                CChange = this.calculateChange(C, A, WIN) + this.calculateChange(C, score1800, TIE);
                            } else { // C win A
                                CChange = this.calculateChange(C, A, WIN);
                            }


                            // check A : A lose B, A lose C
                            AChange = this.calculateChange(A, B, LOSE) + this.calculateChange(A, C, LOSE);
                        } 
            
                    } else {    // if C is wrong
    
                        if (Aanswer != this.correctAnswer && Banswer !=this.correctAnswer && Canswer != this.correctAnswer) {    // if all wrong, all tie
                          
                            // A lose 1800 x 2, B lose 1800 x 2, C lose 1800 x 2
                            AChange = this.calculateChange(A, score1800, LOSE) + this.calculateChange(A, score1800, LOSE); 
                            BChange = this.calculateChange(B, score1800, LOSE) + this.calculateChange(B, score1800, LOSE); 
                            CChange = this.calculateChange(C, score1800, LOSE) + this.calculateChange(C, score1800, LOSE); 
                        
    
                        } else {  // C loses, C compare w winner, other loser compare w winner
                          
                            if (Aanswer == this.correctAnswer) {    // if A correct, B = C
                                //System.out.println("Yes");
                                AChange = this.calculateChange(A, B, WIN) + this.calculateChange(A, C, WIN); // A win B, win C
                                BChange = this.calculateChange(B, score1800, LOSE) + this.calculateChange(B, A, LOSE); // B lose A, lose 1800
                                CChange = this.calculateChange(C, score1800, LOSE) + this.calculateChange(C, A, LOSE); // C lose A, lose 1800
    
    
                            } else if (Banswer == this.correctAnswer) {    // if B correct, A = C

                                BChange = this.calculateChange(B, C, WIN) + this.calculateChange(B, A, WIN); // B win A, win C
                                AChange = this.calculateChange(A, score1800, LOSE) + this.calculateChange(A, B, LOSE); // A lose 1800, lose B
                                CChange = this.calculateChange(C, score1800, LOSE) + this.calculateChange(C, B, LOSE); // C lose 1800, lose B
    
                            }
    
                        }
                    }
                }

            } else {

                // Assume C is correct, C is not inspected
                
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
