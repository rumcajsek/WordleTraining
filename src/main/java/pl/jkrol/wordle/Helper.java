package pl.jkrol.wordle;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class Helper {
    public static void main(String[] args) throws PatternLengthOverflowException {
        WordLoader loader = new WordLoader();
        //WordLoader loader = new WordLoader(Arrays.asList("taunt", "tweed", "tweak"));
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
        HashMap<Character,List<Integer>> yellowLetters = new HashMap<>();
        for(int i = 0 ; i < yellows.size() ; i++) {
            var tempKey = yellows.get(i).charAt(0);
            var tempValue = Integer.parseInt(yellows.get(i+1));
            if(!yellowLetters.containsKey(tempKey)) {
                yellowLetters.put(tempKey, Stream.of(tempValue).collect(Collectors.toCollection(ArrayList::new)));
            }
            else {
                var valueSet = yellowLetters.get(tempKey);
                valueSet.add(tempValue);
                yellowLetters.put(tempKey,valueSet);
            }
            i++;
        }

        System.out.println("Insert all gray letters one after other, e.g. \"abcde\"");
        tmp = input.nextLine();
        if (!tmp.isEmpty()) {
            grayLetters = stream(tmp.split("\\s*\\s*")).toList();
        }
        input.close();

        var before = Instant.now();
        System.out.println("Loader size: " + loader.getListOfWords().size());
        System.out.println("Possible words:");
        List<String> finalGrayLetters = grayLetters;
        AtomicInteger wordCount = new AtomicInteger();
        loader.getListOfWords().stream()
                .filter(s -> Checker.eliminateGrays(s, finalGrayLetters))
                .filter(s -> Checker.checkGreens(s, greenLetters))
                .filter(s -> Checker.checkYellows(s, yellowLetters))
                .forEach(s -> {
                    System.out.println(s);
                    wordCount.incrementAndGet();
                });
        System.out.println("Words found: " + wordCount);
        var after = Instant.now();
        System.out.println("Duration: " + Duration.between(before,after).toMillis() + " ms");
    }


}
