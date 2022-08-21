import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException;
public class MemoryGame {
    //scanner object for taking user input
    Scanner sc= new Scanner(System.in);

    //creating variable for storing words 
    ArrayList<String> words;

    //variable for game type
    int gameType;

    //variable for words used to current game
    ArrayList<String> guessWords;

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    //method that accepts file path and ruturns arrayList of words for the game
    private static ArrayList<String> readWords(String filePath) throws FileNotFoundException{
        //pass the path to the file as a parameter
        File file = new File(filePath);
        
        Scanner sc = new Scanner(file);
        ArrayList<String> words = new ArrayList<String>(); 

        //adding words from text file to array
        while (sc.hasNextLine())
            words.add(sc.nextLine());

        sc.close();
        return words;
    }

    private void showGameUI(){
        while(true){
            System.out.println("Choose type of game (easy - 4 words, 10 guesses | hard 8 words, 15 guesses):");
            System.out.println("Type 1 for easy, 2 for hard");

            while(true){
                int gameType = sc.nextInt();
                if(gameType == 1 || gameType == 2){
                    this.gameType = gameType;
                    break;
                }
                else{
                    System.out.println("Wrong input choice type again:");
                    continue;
                }
            }
            
            generateGame();
            
            System.out.println("Do you want to play again? Type 1 for yes and 2 for no");
            while(true){
                int exitChoice = sc.nextInt();
                if(exitChoice == 1 || exitChoice == 2){
                    if (exitChoice == 2) 
                        System.exit(0);
                    break;
                }
                else{
                    System.out.println("Wrong input choice type again:");
                    continue;
                }
            }
        }
    }

    private void generateGame(){
        sc= new Scanner(System.in);
        this.guessWords = new ArrayList<>();
        //applaying values for words number and possible guesses accordingly to game type 
        int wordsNumber = this.gameType == 1 ? 4 : 8;
        int possibleGuesses = this.gameType == 1 ? 10 : 15;
        this.guessWords =  getRandomWords(wordsNumber);
        //creating lists for particular rows
        ArrayList<String> ARow = this.guessWords;
        ArrayList<String> BRow = new ArrayList<String>(ARow);
        //shuffling BRow list to make it different than ARow
        Collections.shuffle(BRow);
        //creating varaible to store elements that are already guessed
        ArrayList<String> guessedWords = new ArrayList<String>();
        //counter for user guesses
        int guesses = 0; 
        //storing current user guess
        String currentGuess = "";
        //storing previous user guess
        String previousGuess = "";
        //variable for ereasing words 
        int guessEreaseCount = 0;

        while(guesses <= possibleGuesses){
            if(guessEreaseCount == 3){
                guessEreaseCount = 1;
                previousGuess = "";
            }
            String guessedWord = drawBoard(ARow, BRow, guessedWords, currentGuess, previousGuess);
            if(guessedWord != ""){
                guessedWords.add(guessedWord);
            }
            System.out.println("Give coordinates you want to uncover:");
            previousGuess = currentGuess;
            currentGuess = sc.nextLine();
            guesses++;
            guessEreaseCount++;
        }
    }

    private String drawBoard(ArrayList<String> ARow, ArrayList<String> BRow, ArrayList<String> guessedWords, String currentGuess, String previousGuess){
        //getting previous and current word
        String previousWord = "";
        String currentWord = "";
        String previousWordRow = "";
        String currentWordRow = "";

        if(previousGuess != ""){
            String [] previousWordData = getWordByCoordinates(ARow, BRow, previousGuess);
            previousWord = previousWordData[0];
            previousWordRow = previousWordData[1];
        }
        
        if(currentGuess != ""){
            String [] currentWordData = getWordByCoordinates(ARow, BRow, currentGuess);
            currentWord = currentWordData[0];
            currentWordRow = currentWordData[1];
        }
        

        System.out.print(" ");
        for(int i=0; i<ARow.size();i++){
            System.out.print(i+1 + " ");
        }
        System.out.println();
        System.out.print("A ");
        for(int i=0; i<ARow.size();i++){
            String word = ARow.get(i);
            //conditions for showing the word
            if(guessedWords.contains(word) || (word == currentWord && currentWordRow == "A")|| (word == previousWord && previousWordRow == "A")){
                System.out.print(word + " ");
            }
            else{
                System.out.print("X ");
            }
            
        }
        System.out.println();
        System.out.print("B ");
        for(int i=0; i<BRow.size();i++){
            String word = BRow.get(i);
            if(guessedWords.contains(word) || (word == currentWord && currentWordRow == "B")|| (word == previousWord && previousWordRow == "B")){
                System.out.print(word + " ");
            }
            else{
                System.out.print("X ");
            }
        }
        System.out.println();
        if(currentWord == previousWord){
            return currentWord;
        }
        return "";
    }

    //function to get word from row by coordiantes
    private String[] getWordByCoordinates(ArrayList<String> ARow, ArrayList<String> BRow, String guess){
        int index = Integer.parseInt(guess.replaceAll("\\D+",""));
        String row = guess.replaceAll("\\d", "");
        if(row.contains("A")){
            //we return info about word and row where it is 
            return new String[] { ARow.get(index-1), "A"};
        }
        return new String[] { BRow.get(index-1), "B"};
    }

    private ArrayList<String> getRandomWords(int wordsNumber){
        ArrayList<String> randomWords = new ArrayList<String>(wordsNumber);
        //shuffling words list to get random values
        Collections.shuffle(this.words);
        for(int i=0; i < wordsNumber; i++){
            randomWords.add(i, this.words.get(i));
        }
        //returning list of random words for partricular game type
        return randomWords;
    }

    public static void main(String[] args) throws FileNotFoundException {
        MemoryGame memoryGame = new MemoryGame();
        memoryGame.setWords(readWords("Words.txt"));
        memoryGame.showGameUI();
    }
}
