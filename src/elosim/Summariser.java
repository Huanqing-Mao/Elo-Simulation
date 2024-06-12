package elosim;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Summariser {

    // summarise data
    void summariseC(ArrayList<Person> personList, HashMap<Person, ArrayList<Double>> HM, ArrayList<ArrayList<String>> data, String[] accuracies, double[][] values) {

        int currPersonIndex = 0;
        for (Person c : personList) {
            System.out.println();
            System.out.println(c.toString());
            ArrayList<Double> cPerformance = HM.get(c);
            if (cPerformance.size() == 0) {
                continue;
            }
            Collections.sort(cPerformance);
            int length = cPerformance.size();
            
            System.out.println("length = " + length);
            int perc5 = length / 20;
            int perc20 = length / 5;
            int perc30 = 3 * length / 10;
            int perc50 = length / 2;
            int perc70 = 7 * length / 10;
            int perc90 = 9 * length / 10;
            int perc95 = length - length / 20;
            int[] indices = new int[]{perc5, perc20, perc30, perc50, perc70, perc90, perc95};

            for (int idx = 0; idx < indices.length; idx++) {
                System.out.println(String.format("%.3f Percentile = %.3f", 1.0 * indices[idx] / length, cPerformance.get(indices[idx])));

            }

            // add rows to the percentile data            
            ArrayList<String> row = new ArrayList<String>();
            row.add(String.format("%.3f", c.getAccuracy()));
            for (int id : indices) {
                row.add("" + cPerformance.get(id));
            }
            
            data.add(row);


            // add columns to the results data
            accuracies[currPersonIndex] = String.format("%.3f", c.getAccuracy());
            ArrayList<Double> results = HM.get(c);
            for (int j = 0; j < results.size();j++) {
                double rating = results.get(j);
                values[j][currPersonIndex] = rating;
            }
            currPersonIndex++;
            
        }
    }

    void summariseAB(ArrayList<String> bins, HashMap<String, ArrayList<Double>> HM, ArrayList<ArrayList<String>> data, String[] accuracies, double[][] values) {

        int currDomainIndex = 0;
        for (String domain : bins) {
      
            ArrayList<Double> performance = HM.get(domain);
            System.out.println();
            System.out.println(domain);
            if (performance.size() == 0) {
                continue;
            }

            Collections.sort(performance);
            int length = performance.size();
            System.out.println("length = " + length);
            int perc5 = length / 20;
            int perc20 = length / 5;
            int perc30 = 3 * length / 10;
            int perc50 = length / 2;
            int perc70 = 7 * length / 10;
            int perc90 = 9 * length / 10;
            int perc95 = length - length / 20;
            int[] indices = new int[]{perc5, perc20, perc30, perc50, perc70, perc90, perc95};

            for (int idx = 0; idx < indices.length; idx++) {
                System.out.println(String.format("%.3f Percentile = %.3f", 1.0 * indices[idx] / length, performance.get(indices[idx])));

            }

            // add rows to the percentile data

            ArrayList<String> row = new ArrayList<String>();
            row.add(domain);
            for (int id : indices) {
                row.add("" + performance.get(id));
            }
            data.add(row);


            // add columns to the actual results
            accuracies[currDomainIndex] = domain;
            ArrayList<Double> results = HM.get(domain);
            for (int j = 0; j < results.size();j++) {
                double rating = results.get(j);
                values[j][currDomainIndex] = rating;
            }
            currDomainIndex++;
            

        }

    }

    // export percentile data of ABC as a csv file
    void writePercentileFile(String fileName, String[] header, ArrayList<ArrayList<String>> data) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // write header
            for (int i = 0; i < header.length; i++) {
                writer.print(header[i]);
                if (i < header.length - 1) {
                    writer.print(",");
                }
            }
            writer.println();


            // write data
            for (ArrayList<String> row : data) {
                for (int i = 0; i < row.size(); i++) {
                    writer.print(row.get(i));
                    if (i < row.size() - 1) writer.print(",");
                }
                writer.println();
            }

            System.out.println("File created successfully.");
            

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // export final ratings of ABC into a csv file
    void writeResults(String fileName, String[] header, double[][] data) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // write header
            for (int i = 0; i < header.length; i++) {
                writer.print(header[i]);
                if (i < header.length - 1) {
                    writer.print(",");
                }
            }
            writer.println();


            // write data
            for (double[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    writer.print(row[i]);
                    if (i < row.length - 1) writer.print(",");
                }
                writer.println();
            }

            System.out.println("Results File created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    
}
