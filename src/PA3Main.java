import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * PA3Main.java, PA3-ExhaustingPastries assignment Author: Rubbal Kumar Course:
 * CSC 210 Section 1D Purpose: This program is divided between two parts that
 * are baker's perspective and customer's perspective. It reads an input file
 * from command linewhich lists every pastry the bakery is selling today. The
 * pastries can be cut into slices which are sold for different amounts
 * depending on their length.This program maximize the bakery's profits by
 * finding which combination of slices will maximizes baker's profits. Once
 * baker's profit is found, it will take the role of the customer.Then it will
 * print ind the maximum number of unique pastries the customer can purchase.The
 * customer will be buying all of any one pastry. The input file contains
 * strings in which The first integer is the budget customers have for buying
 * pastries. This is followed by a list of pastries, starting with a pastry
 * name, then followed by the price for length of 1 unit, price per length of 2
 * units, etc. The file could have any number of lines and each pastry line
 * could have any number of length costs.
 */
public class PA3Main {

    // declaring a global variable which used during recursions
    public static HashMap<String, Integer> bakery = new HashMap<String, Integer>();

    public static void main(String[] args) {
        Scanner in = null;
        int budget = 0;
        // This try statement handles the Incorrect budget input and File not
        // found exception
        try {
            File file = new File(args[0]);
            in = new Scanner(file);
            budget = Integer.valueOf(in.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Incorrect budget input");
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
            System.exit(1);
        }
        while (in.hasNextLine()) {
            baker(in.nextLine());
        }
        print_bakery(); // calls function to print bakery enumeration
        int count = customer(budget); // prints the pastries a customer can buy
        System.out.println();
        System.out.println("The max number of unique pastries"
                + " that can be bought with $" + budget + " is: " + count);
    }

    // Helper function that prints the maximized profit that baker can make
    public static void print_bakery() {
        // Sorting the elements to print alphabetically
        ArrayList<String> cakes = new ArrayList<String>(bakery.keySet());
        Collections.sort(cakes);
        for (String s : cakes) { // iterating over a HashMap
            System.out.print(s + " costs $");
            System.out.println(bakery.get(s));
        }
        System.out.println();
    }

    // This function does the calculations that involves the maximization of
    // baker's profit which calls recursive function enumerate
    public static void baker(String data) {
        String[] parts = data.split(": ");
        String[] price = null;
        int[] numbers = null;
        try {
            price = parts[1].trim().split(" ");
            numbers = new int[price.length];
            for (int i = 0; i < price.length; i++) {
                numbers[i] = Integer.parseInt(price[i]);
            }
            int max = price.length;
            int[] a = new int[max];
            String names = parts[0];
            enumerate(max, 0, a, numbers, names); // calls recursive function
        }
        // catches the possible exceptions
        catch (NumberFormatException e) {
            System.out.println("ERROR: Incorrect price input");
        } catch (ArrayIndexOutOfBoundsException e) {
            numbers = new int[1];
            numbers[0] = 0;
            price = new String[1];
            int max = price.length;
            int[] a = new int[max];
            String names = parts[0]; // continues recursion even it
            enumerate(max, 0, a, numbers, names); // it found an invalid price
        }
    }

    // This recursive function creates an array in order to enumerate
    // over all of the possible roll combinations
    public static void enumerate(int N, int k, int[] combinations,
            int[] numbers, String names) {
        // eliminates the unwanted cases and
        // recurses until N is not equal to k
        int x = 0;
        for (int i = 0; i < k; i++)
            x += combinations[i] * (i + 1);
        if (x <= N) {
            if (N == k) {
                print(combinations, numbers, names);
                return;
            }
            // adds the number to the integer array
            for (int i = 0; i <= N; i++) {
                combinations[k] = i;
                enumerate(N, k + 1, combinations, numbers, names);
            }
        }
    }

    // this function calculates the baker's combination called by enumerate
    public static void print(int[] a, int[] b, String c) {
        int sum_1 = 0;
        int index = 1;
        for (int i : a) {
            sum_1 += i * index;
            index = index + 1;
        }
        // it checks if sum is valid, calculates the sum of possible
        // combinations and maps the possible outcomes to cake name
        if (sum_1 <= a.length) {
            int value = 0;
            int sum = 0;
            for (int i = 0; i < a.length; i++) {
                value = a[i] * b[i];
                sum = sum + value;
            }
            if (!bakery.containsKey(c)) {
                bakery.put(c, sum);
            } else if (bakery.get(c) < sum) {
                bakery.put(c, sum);
            }
        }
    }

    // This function primarily maximizes number of pastries the customer can buy
    // and prints in increasing order of price. To print in alphabetical order,
    // it converts the HashMap<String,ArrayList<Integer>> to
    // HashMap<Integer,ArrayList<String>>
    public static int customer(int budget) {
        int count = 0;
        int remaining = budget;
        HashMap<Integer, ArrayList<String>> sorted = new HashMap<Integer, ArrayList<String>>();
        for (String cake : bakery.keySet()) {
            if (sorted.containsKey(bakery.get(cake))) { // creates a new HashMap
                sorted.get(bakery.get(cake)).add(cake);
            } else {
                ArrayList<String> price = new ArrayList<String>();
                price.add(cake); // Edits HashMap if cake type already available
                sorted.put(bakery.get(cake), price);
            }
        }
        ArrayList<Integer> cakes = new ArrayList<Integer>(sorted.keySet());
        Collections.sort(cakes); // sorts the hashMap to print in ascending
                                 // order
        for (Integer s : cakes) {
            if (s <= remaining) { // to find the combinations that are in budget
                remaining -= s;
                Collections.sort(sorted.get(s));
                sorted.put(s, sorted.get(s));
                for (int i = 0; i < sorted.get(s).size(); i++) {
                    count += 1; // counter to calculate total combinations
                    System.out.println(
                            "Can buy " + (sorted.get(s)).get(i) + " for $" + s);
                }
            }
        }
        return count; // returns customer's maximized possible combinations
    }

}

