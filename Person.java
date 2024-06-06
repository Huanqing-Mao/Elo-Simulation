class Person {

    String type;
    int id;
    double rating;
    double accuracy;

    Person(String type, int id, double rating, double accuracy) {
        this.type = type;
        this.rating = rating;
        this.id = id;
        this.accuracy = accuracy;
    }


    void updateRating(double changeInRating) {
        this.rating += changeInRating;
    }

    public String toString() {
        return String.format("Person %s%d, rating = %.3f, accuracy = %.3f", this.type, this.id, this.rating, this.accuracy);
    }


}