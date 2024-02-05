package pl.jkrol.wordle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import org.springframework.stereotype.Service;

//@Service
public class WordLoader {
    private List<String> listOfWords;
    public WordLoader() {
        List<String> templist = new ArrayList<>();
        try {
            Path path = Path.of("C:\\Users\\jakub\\OneDrive\\Dokumenty\\wordleWords.txt");
            setListOfWords(Arrays.asList(String
                    .join(" ", Files.readAllLines(path))
                    .split(" ")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public WordLoader(List<String> list) {
        this.setListOfWords(list);
    }

    public List<String> getListOfWords() {
        return listOfWords;
    }

    public void setListOfWords(List<String> listOfWords) {
        this.listOfWords = listOfWords;
    }
}
