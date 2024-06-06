import java.util.*;


Random random = new Random();
double min = 0.5;
double max = 0.9;
double sum = 0.0;


// generate 30 A
ArrayList<Person> AList = new ArrayList<Person>();
for (int i = 0; i < 30; i++) {
    double accuracy = min + (max - min) * random.nextDouble();
    AList.add(new Person("A", i + 1, 1400, accuracy));
    sum += accuracy;
};

// test against 0.7
double average = sum / 30;

