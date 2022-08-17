package pl.jkrol.wordle;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

public class Helper {
    public static void main(String[] args) throws PatternLengthOverflowException {
        WordLoader loader = new WordLoader();
        Scanner input = new Scanner(System.in);
        List<String> yellows = new ArrayList<>();
        List<String> grayLetters = new ArrayList<>();

        System.out.println("Insert all green letters in format e.g. \"b__o_\" for \"baton\" (if no greens, then leave \"_____\")");
        String greenLetters = input.nextLine();
        if(greenLetters.length() != 5) {
            throw new PatternLengthOverflowException("Incorrect length of the pattern (not 5!): " + greenLetters);
        }

        System.out.println("Insert all yellow letters with their respective positions, e.g. \"b,2,c,5\", etc.");
        var tmp = input.nextLine();
        if(!tmp.isEmpty())  {
            yellows = stream(tmp.split("\\s*,\\s*")).toList();
        }
        List<Character> yellowKeys = IntStream.range(0, yellows.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(yellows::get)
                .map(s -> s.charAt(0))
                .toList();
        List<Integer> yellowValues = IntStream.range(0,yellows.size())
                .filter(i -> i % 2 != 0)
                .mapToObj(yellows::get)
                .map(Integer::parseInt)
                .toList();
        HashMap<Character,Integer> yellowLetters = IntStream.range(0, Math.min(yellowValues.size(),yellowKeys.size()))
                        .collect(HashMap::new,
                                (m,i) -> m.put(yellowKeys.get(i),yellowValues.get(i))
                                ,HashMap::putAll);

        System.out.println("Insert all gray letters one after other, e.g. \"abcde\"");
        tmp = input.nextLine();
        if (!tmp.isEmpty()) {
            grayLetters = stream(tmp.split("\\s*\\s*")).toList();
        }
        input.close();

        System.out.println("Loader size: " + loader.getListOfWords().size());
        System.out.println("Possible words:");
        List<String> finalGrayLetters = grayLetters;
        loader.getListOfWords().stream()
                .filter(s -> eliminateGrays(s, finalGrayLetters))
                .filter(s2 -> checkGreens(s2, greenLetters))
                .filter(s3 -> checkYellows(s3, yellowLetters))
                .forEach(System.out::println);
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
        for(String letter : grays) {
            if(word.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkYellows(String word, HashMap<Character,Integer> yellows) {
        if(yellows.isEmpty()) {
            return true;
        }
        int yellowHitCount = 0;
        for(int i = 0 ; i < word.length() ; i++) {
            if(yellows.containsKey(word.charAt(i))) {
                if(Integer.parseInt(yellows.get(word.charAt(i)).toString()) == (i + 1)) {
                    return false;
                }
                ++yellowHitCount;
            }
        }
        return yellowHitCount == yellows.size();
    }
}
