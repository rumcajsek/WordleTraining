package pl.jkrol.wordle;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Checker {
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer i = Integer.valueOf(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean runPredicates(String word, String pattern, List<String> grays, HashMap<Character,List<Integer>> yellows) throws ExecutionException, InterruptedException {
        ExecutorService checkExecutorService = Executors.newFixedThreadPool(3);
        var cf1 = CompletableFuture.supplyAsync(() -> {
            return checkGreens(word,pattern);
        }, checkExecutorService);
        var cf2 = CompletableFuture.supplyAsync(() -> {
            return eliminateGrays(word, grays);
        }, checkExecutorService);
        var cf3 = CompletableFuture.supplyAsync(() -> {
            return checkYellows(word, yellows);
        }, checkExecutorService);
        var tmp = cf1.thenCombine(cf2, (c1,c2) -> c1 && c2).thenCombine(cf3, (c1,c3) -> c1 && c3).get();
        checkExecutorService.shutdown();
        return tmp;
    }

    public static boolean checkGreens(String word, String pattern) {
        if(Objects.equals(pattern, "_____")) {
            return true;
        }
        for (int i = 0 ; i < word.length() ; i++) {
            if(pattern.toLowerCase().charAt(i) != '_' && pattern.toLowerCase().charAt(i) != word.toLowerCase().charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean eliminateGrays(String word, List<String> grays) {
        if (grays.isEmpty()) {
            return true;
        }
        for (String letter : grays) {
            if (word.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkYellows(String word, HashMap<Character,List<Integer>> yellows) {
        if(yellows.isEmpty()) {
            return true;
        }
        HashMap<Character,List<Integer>> tempMap = new HashMap<>(yellows);
        Set<Integer> positions = new HashSet<>();
        for(Iterator<Character> yellowIterator = tempMap.keySet().iterator(); yellowIterator.hasNext() ; ) {
            Character yellowLetter = yellowIterator.next();
            for(int i = 0 ; i < word.length() ; i++) {
                if(word.charAt(i) == yellowLetter) {
                    positions.add(i+1);
                }
            }
            for(Integer yellowPosition : tempMap.get(yellowLetter)) {
                if(positions.contains(yellowPosition) ) { //|| positions.size() != tempMap.get(yellowLetter).size()) {
                    return false;
                }
            }
            if(!positions.isEmpty()) {
                yellowIterator.remove();
                positions.clear();
            }
        }
        return tempMap.isEmpty();
    }

}
