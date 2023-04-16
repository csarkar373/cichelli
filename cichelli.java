import java.util.*;

public class Cichelli {
    public static int MAXG = 4;
    private Word [] words;
    Map<Character, Integer> frequencyMap;
    Map<Character, Integer> gValuesMap;
    Set<Integer> hashcodes;

    private static class Word implements Comparable<Word> {
        String word;
        Character firstLetter;
        Character lastLetter;
        int score;
        int hashcode;

        public Word(String word) {
            this.word = word;
            this.score = 0;
            this.hashcode = -1;
        }

        @Override public String toString() {
            return this.word + " : " + this.hashcode; // + " score = " + this.score;
        }

        @Override
        public int compareTo(Word other) {
            if (this.score > other.score) {
                return -1;
            }
            if (this.score < other.score) {
                return 1;
            }
            return (this.word.compareTo(other.word));
        }
    }
    public Cichelli(String[] words) {
        this.words = new Word[words.length];
        for (int i=0; i < words.length; ++i) {
            this.words[i] = new Word(words[i]);
        }
        this.frequencyMap = new TreeMap<Character, Integer>();
        this.gValuesMap = new TreeMap<Character, Integer>();


    }

    public void go() {
        countLetters();
        scoreWords();
        Arrays.sort(this.words);
        //printWords();
        System.out.println(frequencyMap);
        System.out.println(gValuesMap);
        int collisions = 0;
        do {
            collisions = generateHashCodes();
            if (collisions > 0)
                incrementGvalues();
        } while (collisions > 0);
        printWords();
    }

    private void countLetters() {
        for(Word w : this.words) {
            w.firstLetter = w.word.charAt(0);
            w.lastLetter = w.word.charAt(w.word.length()-1);
            if (frequencyMap.containsKey(w.firstLetter)) {
                frequencyMap.put(w.firstLetter, frequencyMap.get(w.firstLetter) + 1);
            } else {
                frequencyMap.put(w.firstLetter, 1);
                gValuesMap.put(w.firstLetter, 0);
            }
            if (frequencyMap.containsKey(w.lastLetter)) {
                frequencyMap.put(w.lastLetter, frequencyMap.get(w.lastLetter) + 1);
            } else {
                frequencyMap.put(w.lastLetter, 1);
                gValuesMap.put(w.lastLetter, 0);
            }
        }
    }

    private void scoreWords() {
        for (Word w : this.words) {
            w.score = frequencyMap.get(w.firstLetter) + frequencyMap.get(w.lastLetter);
        }
    }

    private int hashFunction(Word w) {
        return (w.word.length() + gValuesMap.get(w.firstLetter) + gValuesMap.get(w.lastLetter)) % this.words.length;
    }

    // returns the number of collisions
    private int generateHashCodes() {
        // clear out the old set of hashcodes
        this.hashcodes = new HashSet<Integer> ();
        int collisions = 0;
        for(Word w : words) {
            int hashcode = hashFunction(w);
            // collision?
            if (hashcodes.contains(hashcode)) {
                ++collisions;
                System.out.println("hashcode collision: " + hashcode);
            }
            // add to the set to help detect collisions later
            hashcodes.add(hashcode);
            w.hashcode = hashcode;
        }
        return collisions;
    }

    private void incrementGvalues() {
        for (int i = words.length - 1; i >= 0; i--) {
            Character last = words[i].firstLetter;
            int gValue = gValuesMap.get(last);
            if (i == 0 && gValue == MAXG) {
                System.out.println("All g values exhausted; giving up");
                System.exit(1);
            }
            if (gValue == MAXG) {
                gValuesMap.put(last, 0);
                continue;
            }
            gValuesMap.put(last, gValue + 1 );
            break;
        }
        System.out.println(gValuesMap);
    }
    private void printWords() {
        System.out.println("Original Words:");
        for (Word w : words) {
            System.out.println(w);
        }
    }
}
