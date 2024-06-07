import java.util.*;


Random random = new Random();
double min = 0.5;
double max = 0.9;
double sum = 0.0;
int correctAnwer = 1;

/* Test Mean Accuracy of Group */
System.out.println("=== Test Average Accuracy For Person Group vs Expected 70 % ===");
// generate 30 A
ArrayList<Person> AList = new ArrayList<Person>();
for (int i = 0; i < 30; i++) {
    double accuracy = min + (max - min) * random.nextDouble();
    AList.add(new Person("A", i + 1, 1400, accuracy));
    sum += accuracy;
};

// test against 0.7
double average = sum / 30;
System.out.println("actual average = " + average);




/* Test Person Accuracy */
System.out.println("\n=== Test Probability of Answering Correctly Of A User vs His Given Accuracy ===");
Person A1 = AList.get(0);
System.out.println(A1.toString());


int score = 0;
for (int i = 0; i < 100000; i++) {
    int answer = A1.giveAnswer(correctAnwer);
    if (answer == correctAnwer) {
        score++;
    } 

}

// test against its own accuracy
double actualAccuracy = score / 100000.0;
System.out.print("Actual Accuracy = " + actualAccuracy);
System.out.println();