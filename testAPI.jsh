Person A1 = new Person("A", 1, 1400, 0.7);
Person B1 = new Person("B", 1, 1600, 0.8);
RatingAPI api = new RatingAPI();

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
