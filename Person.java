import java.util.*;

class Person {

    private String type;
    private int id;
    private double rating;
    private double accuracy;

    Person(String type, int id, double rating, double accuracy) {
        this.type = type;
        this.rating = rating;
        this.id = id;
        this.accuracy = accuracy;
    }


    double getRating() {
        return this.rating;
    }


    void updateRating(double changeInRating) {
        this.rating += changeInRating;
    }

    int giveAnswer(int correctAnswer) {
        Random random = new Random();

        if (random.nextDouble() < this.accuracy) { // random next double has a range 0 - 1
            return correctAnswer;
        } else {
            return this.getRandomIncorrect(correctAnswer);
        }

    }


    private int getRandomIncorrect(int correctAnswer) {
        ArrayList<Integer> options = new ArrayList<Integer>();
        Collections.addAll(options, 1, 2, 3, 4);

        options.remove((Object) correctAnswer); // remove the correct answer

        int index = new Random().nextInt(options.size() - 1); // random index between 0 to 2 => 1/3 chance for each wrong option
        return options.get(index);

    }

    public String toString() {
        return String.format("Person %s%d, rating = %.3f, accuracy = %.3f", this.type, this.id, this.rating, this.accuracy);
    }


}