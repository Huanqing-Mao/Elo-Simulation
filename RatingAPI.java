public class RatingAPI {
    public static final int K = 40;
    public static final int CONSTANT = 400;

    RatingAPI() {}

    double calculateChange(Person person1, Person person2, int resultOfPerson1) {
        return 0.0;
    }
    
    // if C is inspected
    void updateRatingInspect(Person A, Person B, Person C, boolean isAcorr, boolean isBcorr, boolean isCcorr) {}

    // if C is not inspected (TO BE ADDED LATER)
    void updateRatingNoInspect(Person A, Person B, boolean isAcorr, boolean isBcorr) {}
}
