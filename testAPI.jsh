Person A1 = new Person("A", 1, 1400, 0.7);
Person B1 = new Person("B", 1, 1600, 0.8);
RatingAPI api = new RatingAPI(1);
int CORRECT_ANSWER = 1;

System.out.println(A1.toString() + " vs " + B1.toString());
System.out.println("If A wins: ");
double changeAwin = api.calculateChange(A1, B1, 1.0);
double changeBlose = api.calculateChange(B1, A1, 0.0);
System.out.println(" - A change = " + changeAwin + ", B change = " + changeBlose);

System.out.println("If A loses: ");
double changeAlose = api.calculateChange(A1, B1, 0.0);
double changeBwin = api.calculateChange(B1, A1, 1.0);
System.out.println(" - A change = " + changeAlose + ", B change = " + changeBwin);

System.out.println("If there is a tie: ");
double changeATie = api.calculateChange(A1, B1, 0.5);
double changeBTie = api.calculateChange(B1, A1, 0.5);
System.out.println(" - A change = " + changeATie + ", B change = " + changeBTie);


Person C1 = new Person("C", 1, 1800, 0.9);
System.out.println("\n====== All Possible Cases Test (Unit Test) ======");
double expectedA;
double expectedB;
double expectedC;

System.out.println("\n #1 A = B = C, All correct");
int a1 = 1;
int b1 = 1;
int c1 = 1;
api.updateGroupRating(A1, B1, C1, a1, b1, c1);

// Calculate expected results using Elo Calculator 
expectedA = 10.4; 
expectedB = -10.4;
expectedC = (-10.4 - 16.4) / 2.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));


System.out.println("\n #2 A = B = C, All wrong");
int a2 = 4;
int b2 = 4;
int c2 = 4;
api.updateGroupRating(new Person("A", 2, 1400.0, 0.5), new Person("B", 2, 1600.0, 0.8), new Person("C", 2, 1800.0, 0.9), a2, b2, c2);

// Calculate expected results using Elo Calculator 
expectedA = 10.4; 
expectedB = -10.4;
expectedC = (-10.4 - 16.4) / 2.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));


System.out.println("\n #3 A = B != C, C correct");
int a3 = 2;
int b3 = 2;
int c3 = 1;
api.updateGroupRating(new Person("A", 3, 1400.0, 0.5), new Person("B", 3, 1600.0, 0.8), new Person("C", 3, 1800.0, 0.9), a3, b3, c3);

// Calculate expected results using Elo Calculator 
expectedA = 10.4; 
expectedB = -10.4;
expectedC = (3.6 + 9.6) / 2.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));



System.out.println("\n #4 A = B != C, C wrong");
int a4 = 1;
int b4 = 1;
int c4 = 3;
api.updateGroupRating(new Person("A", 4, 1400.0, 0.5),new Person("B", 4, 1600.0, 0.8), new Person("C", 4, 1800.0, 0.9), a4, b4, c4);

// Calculate expected results using Elo Calculator 
expectedA = 10.4; 
expectedB = -10.4;
expectedC = (-36.4 - 30.4) / 2.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));


System.out.println("\n #5 A != B = C, C correct");
int a5 = 2;
int b5 = 1;
int c5 = 1;
api.updateGroupRating(new Person("A", 5, 1400.0, 0.5), new Person("B", 5, 1600.0, 0.8), new Person("C", 5, 1800.0, 0.9), a5, b5, c5);

// Calculate expected results using Elo Calculator 
expectedA = -9.6; 
expectedB = 9.6;
expectedC = 0.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));

System.out.println("\n #6 A != B = C, C wrong, A correct");
int a6 = 1;
int b6 = 3;
int c6 = 3;
api.updateGroupRating(new Person("A", 6, 1400.0, 0.5), new Person("B", 6, 1600.0, 0.8), new Person("C", 6, 1800.0, 0.9), a6, b6, c6);

// Calculate expected results using Elo Calculator 
expectedA = (30.4 + 36.4) / 2.0; 
expectedB = -30.4;
expectedC = -36.4;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));

System.out.println("\n #7 A != B = C, All Wrong");
int a7 = 2;
int b7 = 3;
int c7 = 3;
api.updateGroupRating(new Person("A", 7, 1400.0, 0.5),new Person("B", 7, 1600.0, 0.8), new Person("C", 7, 1800.0, 0.9), a7, b7, c7);

// Calculate expected results using Elo Calculator 
expectedA = 16.4;
expectedB = 10.4;
expectedC = (-10.4 - 16.4) / 2.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));

System.out.println("\n #8 A != B != C, Discard");
int a8 = 1;
int b8 = 2;
int c8 = 3;
api.updateGroupRating(new Person("A", 8, 1400.0, 0.5), new Person("B", 8, 1600.0, 0.8), new Person("C", 8, 1800.0, 0.9), a8, b8, c8);

// Calculate expected results using Elo Calculator 
expectedA = 0.0;
expectedB = 0.0;
expectedC = 0.0;
System.out.println(String.format("---> Expected A: %.3f, B: %.3f, C: %.3f", expectedA, expectedB, expectedC));




System.out.println(" ====== Continuous Test ======");
Person _A = new Person("A", 9, 1400, 0.5);
Person _B = new Person("B", 9, 1600, 0.8);
Person _C = new Person("C", 9, 1800, 0.9);

for (int i = 0; i < 5; i++) {
    System.out.println("\nRound #" + (i + 1));
    int Aanswer = _A.giveAnswer(CORRECT_ANSWER);
    int Banswer = _B.giveAnswer(CORRECT_ANSWER);
    int Canswer = _C.giveAnswer(CORRECT_ANSWER);
    System.out.println("A answer = " + Aanswer);
    System.out.println("B answer = " + Banswer);
    System.out.println("C answer = " + Canswer);

    api.updateGroupRating(_A, _B, _C, Aanswer, Banswer, Canswer);
    System.out.println(String.format("%s, %s, %s", _A, _B, _C));
};