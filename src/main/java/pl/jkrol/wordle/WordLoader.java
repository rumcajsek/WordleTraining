package pl.jkrol.wordle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class WordLoader {
    private List<String> listOfWords;
    public WordLoader() {
        List<String> templist = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\jakub\\OneDrive\\Dokumenty\\wordleWords.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                templist.add(str);
            }
            in.close();
        } catch (Exception e) {}
        setListOfWords(templist);
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
