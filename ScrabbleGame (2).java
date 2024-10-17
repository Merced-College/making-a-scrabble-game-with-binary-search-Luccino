// Alexander Ruvalcaba, Dynya McKinney, Adrian Martinez, Agustin Villicana

//Improvement: Give the user different amounts of points for different size words
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class ScrabbleGame {
    //ArrayList to hold word objects that make up the dictionary
    private ArrayList<Word> dictionary;

    public ScrabbleGame(String filename) throws FileNotFoundException {
        dictionary = new ArrayList<>(); //Initialize the dictionary as an empty list
        readDictionary(filename); //Read words from the file into the dictionary
    }

    // Read dictionary words from the file and store them in an ArrayList
    private void readDictionary(String filename) throws FileNotFoundException {
        //Create scanner to read from the file
        Scanner fileScanner = new Scanner(new File(filename));
        //Read words line by line then add them to the dictionary
        while (fileScanner.hasNext()) {
            String word = fileScanner.nextLine().toLowerCase();  //change each word to lowercase
            dictionary.add(new Word(word));
        }
        Collections.sort(dictionary); // Sort the dictionary after reading
        fileScanner.close(); //close scanner
    }

    // Generate 4 random letters
    private char[] getRandomLetters() {
        Random rand = new Random(); //create random object for generating random numbers
        char[] letters = new char[4]; //array to hold 4 letters
        String alphabet = "abcdefghijklmnopqrstuvwxyz"; //defines alphabet to choose letters

        //Loop to generate 4 random letters
        for (int i = 0; i < 4; i++) {
            letters[i] = alphabet.charAt(rand.nextInt(alphabet.length()));
        }
        return letters; //Return array of random letters
    }

    // Check if the user's word is valid based on the random letters assigned
    private boolean isWordValid(String word, char[] letters) {
        for (char c : word.toCharArray()) {
            boolean letterFound = false; //track if the character matches one of the letters
            for (char letter : letters) { //loop througheach random letter to see if the character is there
                if (c == letter) {
                    letterFound = true;
                    break;
                }
            }
            //return false if any character in the word does not match
            if (!letterFound) return false;  // If any character in the word doesn't match, return false
        }
        return true; //return true if all characters are valid
    }

    // Main game logic
    public void playGame() {
        Scanner scanner = new Scanner(System.in); //read user input
        boolean continueGame = true;  // Controls the loop

        //loop that repeatss until player wants to stop
        while (continueGame) {
            char[] randomLetters = getRandomLetters(); //generate 4 random letters
            System.out.println("Your random letters are: " + new String(randomLetters)); // Output random letters

            //lets the user enter a word using random letters
            System.out.print("Enter a word made from these letters: ");
            String userInput = scanner.nextLine().toLowerCase();  // Convert input to lowercase

            // Check if word is valid
            if (isWordValid(userInput, randomLetters)) {
                // Do binary search to check if the word exists in text file
                if (Collections.binarySearch(dictionary, new Word(userInput)) >= 0) {
                    int points = calculatePoints(userInput); // Assign points based on word length
                    System.out.println("Good word! You earned " + points + " points.");
                } else {
                    System.out.println("Invalid Word. Try Again.");  // Output if word is not in the dictionary
                }
            } else {
                System.out.println("Your word contains invalid characters.");  // Output if word contains invalid characters
            }

            // Ask if the user wants to keep playing
            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("no")) {
                continueGame = false;  // End the game
            }
        }

        System.out.println("Thank you for playing!");
        scanner.close();
    }

    // Calculate points based on the word length
    private int calculatePoints(String word) {
        return word.length(); // One point per letter
    }

    public static void main(String[] args) {
        try {
            ScrabbleGame game = new ScrabbleGame("CollinsScrabbleWords_2019.txt");
            game.playGame(); // Start the game
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found.");
        }
    }
}

// Class representing a word in the game, ensuring case-insensitive comparison
class Word implements Comparable<Word> {
    private String word;

    public Word(String word) {
        this.word = word.toLowerCase();  // Convert word to lowercase
    }

    public String getWord() {
        return word;
    }

    @Override
    public int compareTo(Word other) {
        return this.word.compareTo(other.word);  // Make sure input is not case-sensitive
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Word other = (Word) obj;
        return word.equalsIgnoreCase(other.word);
    }

    @Override
    public String toString() {
        return word;
    }
}
