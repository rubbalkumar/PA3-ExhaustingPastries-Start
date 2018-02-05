import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * PA3Main.java, PA3-ExhaustingPastries assignment
 * 
 */
public class PA3Main {

    public static HashMap<String, Integer> bakery = new HashMap<String, Integer>();
    // public static Set<Integer> cakeset = new HashSet<Integer>();

    public static void main(String[] args) {
        Scanner in = null;
        try {
            File file = new File(args[0]);
            in = new Scanner(file);
        } catch (FileNotFoundException fnfe) {
            System.out.println("ERROR: File not found");
            System.exit(1);
        }
        int budget = Integer.valueOf(in.nextLine());
        // System.out.println(budget);
        while (in.hasNextLine()) {
            baker(in.nextLine());
        }
        ArrayList<String> cakes = new ArrayList<String>(bakery.keySet());
        Collections.sort(cakes);
        for (String s : cakes) {
            System.out.print(s + " costs $");
            System.out.println(bakery.get(s));
        }
        System.out.println();
        customer(budget);
    }

    public static void baker(String data) {
        // Set<Integer> cakeset = new HashSet<Integer>();
        String[] parts = data.split(":");
        String[] price = parts[1].trim().split("\\s+");
        int[] numbers = new int[price.length];
        for (int i = 0; i < price.length; i++) {
            numbers[i] = Integer.parseInt(price[i]);
        }
        // System.out.println(parts[0]);
        // System.out.println(Arrays.toString(price));
        int max = price.length;
        int[] a = new int[max];
        String names = parts[0];

        enumerate(max, 0, a, numbers, names);

    }

    // This recursive function creates an array in order to enumerate
    // over all of the possible roll combinations
    public static void enumerate(int N, int k, int[] a, int[] b, String c) {
        // recurses until N is not equal to k
        int x = 0;
        for (int i = 0; i < k; i++)
            x += a[i] * (i + 1);
        if (x <= N) {
            if (N == k) {
                print(a, b, c);
                return;
            }
            // adds the number to the integer array
            for (int i = 0; i <= N; i++) {
                a[k] = i;
                enumerate(N, k + 1, a, b, c);
            }
        }
    }

    // this function prints out the roll combination called by enumerate
    public static void print(int[] a, int[] b, String c) {
        int sum_1 = 0;
        int index = 1;
        for (int i : a) {
            sum_1 += i * index;
            index = index + 1;
        }
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

    public static void customer(int budget) {
        int count = 0;
        int remaining = budget;
        HashMap<Integer, ArrayList<String>> sorted = new HashMap<Integer, ArrayList<String>>();
        for (String cake : bakery.keySet()) {
            if (sorted.containsKey(bakery.get(cake))) {
                sorted.get(bakery.get(cake)).add(cake);
            } else {
                ArrayList<String> price = new ArrayList<String>();
                price.add(cake);
                sorted.put(bakery.get(cake), price);
            }
        }
        ArrayList<Integer> cakes = new ArrayList<Integer>(sorted.keySet());
        Collections.sort(cakes);
        for (Integer s : cakes) {
            if (s <= remaining) {
                remaining -= s;
                Collections.sort(sorted.get(s));
                sorted.put(s, sorted.get(s));
                for (int i = 0; i < sorted.get(s).size(); i++) {
                    count += 1;
                    System.out.println(
                            "Can buy " + (sorted.get(s)).get(i) + " for $" + s);
                }
            }

        }
        System.out.println();
        System.out.println("The max number of unique pastries"
                + "that can be bought with " + budget + " is: " + count);
    }

    // public static HashMap<Integer, ArrayList<String>> sort(HashMap<Integer,
    // ArrayList<String>> cakes){
    // HashMap<Integer, ArrayList<String>> sorted = new HashMap<Integer,
    // ArrayList<String>>();
    // for(Integer s:)
    // }
}

