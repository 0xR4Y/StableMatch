/*
 * Ramin Roufeh
 * Comp 482
 * Project 1 - Stable Matching
 */

//package stablematch;


import java.util.ArrayList;
import java.util.List;


public class StableMatch {

    private int engagedCount;
    private String[] womenPartner;
    private boolean[] menEngaged;

    public StableMatch() {
        engagedCount = 0;
        menEngaged = new boolean[Data.N];
        womenPartner = new String[Data.N];
        calcMatches();
    }

    public void calcMatches() {
        while (engagedCount < Data.N) {
            int free;
            for (free = 0; free < Data.N; free++) {
                if (!menEngaged[free]) {
                    break;
                }
            }

            for (int i = 0; i < Data.N; i++) {
                if (menEngaged[free]) {
                    break;
                }
                int index = womenIndexOf(Data.mp[free][i]);
                if (womenPartner[index] == null) {
                    womenPartner[index] = Data.men[free];
                    menEngaged[free] = true;
                    engagedCount++;
                } else {
                    String currentPartner = womenPartner[index];
                    if (morePreference(currentPartner, Data.men[free], index)) {
                        womenPartner[index] = Data.men[free];
                        menEngaged[free] = true;
                        menEngaged[menIndexOf(currentPartner)] = false;
                    }
                }
            }
        }
        //printCouples();
    }

    public boolean morePreference(String curPartner, String newPartner, int index) {
        for (int i = 0; i < Data.N; i++) {
            if (Data.wp[index][i].equals(newPartner)) {
                return true;
            }
            if (Data.wp[index][i].equals(curPartner)) {
                return false;
            }
        }
        return false;
    }

    public int menIndexOf(String str) {
        for (int i = 0; i < Data.N; i++) {
            if (Data.men[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public int womenIndexOf(String str) {
        for (int i = 0; i < Data.N; i++) {
            if (Data.women[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    
    public void printCouples(List<Pair> pair_list) {
        for (Pair item : pair_list) {
            System.out.println(item.man + "     " + item.woman);
        }
        System.out.println("-----------------------------");
    }

    public static void main(String[] args) {
        if (!Data.readData()) {
            System.err.println("Invalid input data !");
            return;
        }
        //build all possible permutation pair
        Data.buildPossiblePairs();
        
        StableMatch gs = new StableMatch();
        
        //get all possible matches number
        int counter = 0;
        
        for (List<Pair> pair_list : Data.pair_list) {
        	
            boolean success = true;
            
            List<String> passed_men = new ArrayList<String>();
            
            for (Pair pair : pair_list) {
            	 
                String man = pair.man;
                String woman = pair.woman;
                
                passed_men.add(man);
                int woman_index = gs.womenIndexOf(woman);
                int man_index = gs.menIndexOf(man);
                
                //get woman partner index
                int woman_order = 0;
                for (int i = 0; i < Data.N; i++) {
                    if (woman.equals(Data.mp[man_index][i])) {
                        woman_order = i;
                        break;
                    }
                }
                //get prefer mans
                List<String> prefer_mans = new ArrayList<String>();
                for (int i = 0; i < Data.N; i++) {
                    if (Data.wp[woman_index][i].equals(man)) {
                        break;
                    }
                    String prefer_man = Data.wp[woman_index][i];
                    if(!passed_men.contains(prefer_man)){
                        prefer_mans.add(prefer_man);
                    }
                }
                if (!prefer_mans.isEmpty()) {
                    for (String man_item : prefer_mans) {
                        int prefer_woman_order = 0;
                        man_index = gs.menIndexOf(man_item);
                        for (int i = 0; i < Data.N; i++) {
                            if (woman.equals(Data.mp[man_index][i])) {
                                prefer_woman_order = i;
                                break;
                            }
                        }
                        if(prefer_woman_order <= woman_order){
                            success = false;
                            break;
                        }
                    }
                }
                if (!success) {
                    break;
                }
            }
            if (success) {
            	System.out.println("Man   Woman");
                gs.printCouples(pair_list);
                counter++;
            }
        }
        System.out.println("All possible stable match number: " + counter);
    }
}
