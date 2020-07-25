/*
 * Ramin Roufeh
 * Comp 482
 * Project1 - Stable Matching
 */

//package stablematch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {

    public static int N;
    public static String[] men;
    public static String[] women;
    public static String[][] mp;
    public static String[][] wp;

    public static String INPUT_PATH = "Data/input1.txt";

    private static List<int[]> man_permutation = new ArrayList<int[]>();
    private static List<int[]> woman_permutation = new ArrayList<int[]>();
    
    public static List<List<Pair>> pair_list = new ArrayList<List<Pair>>();

    public static boolean readData() {
    	
        File input_file = new File(INPUT_PATH);
        if (!input_file.exists()) {
            return false;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(input_file));
            String line = null;
            
            //read first line and get number
            line = reader.readLine();
            N = Integer.parseInt(line);
            
            //read preference list for man first
            mp = new String[N][N];
            for (int i = 0; i < N; i++) {
                line = reader.readLine();
                mp[i] = line.split("\\s+");
                if (mp[i].length != N) {
                    return false;
                }
            }
            //read preference list for woman second
            wp = new String[N][N];
            for (int i = 0; i < N; i++) {
                line = reader.readLine();
                wp[i] = line.split("\\s+");
                
                if (wp[i].length != N) {
                    return false;
                }
            }
            
            //get all men and woman
            men = sortArray(wp[0]);
            women = sortArray(mp[0]);
            
        } catch (Exception e) {
            return false;
        }
        if(men == null || men.length == 0 || women == null || women.length == 0){
            return false;
        }
        return true;
    }
    
    //sort array
    private static String[] sortArray(String[] input) { 
    	
        int size = input.length;
        String[] myArray = new String[size];
        for (int i = 0; i < size; i++) {
            myArray[i] = input[i];
        }
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < myArray.length; j++) {
                if (myArray[i].compareTo(myArray[j]) > 0) {
                    String temp = myArray[i];
                    myArray[i] = myArray[j];
                    myArray[j] = temp;
                }
            }
        }
        return myArray;
    }

    private static void permute(List<Integer> arr, int k) {
    	
        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);
            permute(arr, k + 1);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            man_permutation.add(arr.stream().mapToInt(i->i).toArray());
            woman_permutation.add(arr.stream().mapToInt(i->i).toArray());
        }
    }

    public static void buildPossiblePairs() {
    	
        //build permutation
        List<Integer> index_list = new ArrayList<Integer>();
        
        for(int i = 0; i < N; i++){
            index_list.add(i);
        }
        
        permute(index_list, 0);
        //build all possible pairs
        
        int size = man_permutation.size();
        
        //build all pair list
        
        for (int m = 0; m < size; m++) {
            List<Pair> one_pair = new ArrayList<Pair>();
            for (int i = 0; i < N; i++) {
                Pair pair = new Pair(men[man_permutation.get(m)[i]], women[i]);
                one_pair.add(pair);
            }
            pair_list.add(one_pair);
        }
    }
}
