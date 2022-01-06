/*
Michael Sloan
1180-02 / 1180L-06
Wallace Neikirk / Luke Holt
October 28, 2021
This program is a sentence decoder game in which the program reads a file and chooses a
random sentence from that file. Once it chooses that sentence it then encodes the specified
sentence using a permuted alphabet. The program then outputs the sentence and constantly asks 
the user if they would like to guess one letter, or the entire sentence. Once the user has had 
enough letters revealed to guess the entire sentence, their number of correct and incorrect 
guesses are displayed on the screen and they are given the option to play again.
*/
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class SentenceDecoderGame {
    //method to choose random sentence from file
    public static ArrayList<String> getSentences(File f) throws FileNotFoundException {
        Scanner scan = new Scanner(f);
        //adding sentences to array list
        ArrayList<String> sentences = new ArrayList<>();
        while (scan.hasNextLine()) {
           sentences.add(scan.nextLine());
        }
        
        return sentences;
    }

    public static String chooseSentence(ArrayList<String> allSentences) {
        Collections.shuffle(allSentences);
        return allSentences.get(0);
    }
    
    //method to randomize alphabet
    public static ArrayList<Character> randomizedAlphabet() {
        //array list of alphabet
        ArrayList<Character> alpha = new ArrayList<Character>(List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
        //shuffling of alphabet to randomize it
        Collections.shuffle(alpha);
        return alpha;
    }
    
    //method to get the permuted sentence
    public static String permutedSentence(String sentence, ArrayList<Character> permAlphabet) throws FileNotFoundException {
        String permutedSentence = "";
        ArrayList<Character> alpha = new ArrayList<Character>(List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
        //nested for loop to add permuted alphabet to normal sentence
        for (int i = 0; i < sentence.length(); i++) {
            for (int j = 0; j < alpha.size() +1; j++) {
                if (j == 26) {
                    //adding space
                    permutedSentence += " ";
                    break;
                }
                if (sentence.charAt(i) == alpha.get(j)) {
                    permutedSentence += permAlphabet.get(j);
                    break;
                }
                
            }
        }
        return permutedSentence;
    }

    
    //main method
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner (System.in);
        //setting file path
        File f = new File("/Users/michaelsloan/Downloads/code/test.txt");
        ArrayList<Character> alphabet = new ArrayList<Character>(List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
        //variables to keep track of correct and incorrect guesses
        int correctGuesses = 0;
        int incorrectGuesses = 0;
        //calling all methods
        ArrayList<Character> permutedAlphabet = randomizedAlphabet();
        ArrayList<String> allSentences = getSentences(f);
        String randomSentence = "";
        if (allSentences.size()!= 0) {
            randomSentence = chooseSentence(allSentences);
            allSentences.remove(randomSentence);
        }
        else {
            System.out.println("You have reached the max number of sentences.");
            System.exit(0);
        }
        String permutedSentence = permutedSentence(randomSentence, permutedAlphabet);
        ArrayList <String> usedRandomSentences = new ArrayList();
        //creating an array list with all of the random sentences chosen
        usedRandomSentences.add(randomSentence);
        //array list for the correct answers
        ArrayList <Character> correctEncryptedAnswers = new ArrayList();
        ArrayList <Character> correctActualAnswers = new ArrayList();
        //program runs in a do while loop
        do {
            System.out.println(permutedSentence);
            System.out.println("Do you want to 1) guess a letter or 2) guess the sentence?");
            //scanning the user answer as an integer of 1 or 2
            int userAnswer = scan.nextInt();
            //conditional if user picks 1
            if (userAnswer == 1) {
                System.out.print("Encrypted letter?");
                char encryptedAnswer = scan.next().charAt(0);
                //letting user know if they have already decoded letter
                if (correctEncryptedAnswers.contains(encryptedAnswer)) {
                    System.out.println("You have already decoded that letter!");
                    continue;
                }
                System.out.print("\nActual letter?");
                char actualAnswer = scan.next().charAt(0);
                //loop to cycle through sentence length
                    for (int i = 0; i < randomSentence.length(); i++) {
                        //checking if permuted sentence doesn't contain encrypted answer
                        if (!permutedSentence.contains(encryptedAnswer + "")) {
                            System.out.println("Sorry, that's not right.");
                            incorrectGuesses += 1; 
                            break; 
                        }
                        //checking if permuted sentence matches encrypted answer and actual
                        if (permutedSentence.charAt(i) == encryptedAnswer) {
                            if (randomSentence.charAt(i) == actualAnswer) {
                                System.out.println("That's right!");
                                //adding the answers to respective array lists
                                correctEncryptedAnswers.add(encryptedAnswer);
                                correctActualAnswers.add(actualAnswer);
                                correctGuesses += 1;
                                //replacing the correctly guessed encrypted with the actual characters
                                permutedSentence = permutedSentence.replaceAll(encryptedAnswer + "", actualAnswer + "");  
                            }
                            else {
                                System.out.println("Sorry, that's not right.");
                                incorrectGuesses += 1;
                            }
                            break;
                        }
                    }
            }
            //allowing the user to guess the whole sentence
            else if (userAnswer == 2) {
                System.out.print("Ok, what do you think the sentence is?");
                scan.nextLine();
                String sentenceAnswer = scan.nextLine();
                //if sentence matches what it should, displaying the correct and incorrect guesses
                if (sentenceAnswer.equalsIgnoreCase(randomSentence)) {
                    System.out.println("That's right! It took you " +correctGuesses+ " correct and " +incorrectGuesses+ " incorrect letter guesses.");
                    //option to play again
                    System.out.println("Would you like to play another round of the game?");
                    String playAgain = scan.nextLine();
                    if (playAgain.equalsIgnoreCase("yes")) {
                        //setting the guesses back to zero and recalling the methods for a new sentence
                        correctGuesses = 0;
                        incorrectGuesses = 0;
                        permutedAlphabet = randomizedAlphabet();
                        if (allSentences.size()!= 0) {
                        randomSentence = chooseSentence(allSentences);
                        allSentences.remove(randomSentence);
                        }
                        else {
                        System.out.println("You have reached the max number of sentences.");
                        System.exit(0);
                        }
                        permutedSentence = permutedSentence(randomSentence, permutedAlphabet);
                        usedRandomSentences.add(randomSentence);
                        continue;
                    }
                    else if (playAgain.equalsIgnoreCase("no")) {
                    System.out.println("Ok goodbye.");
                    break;
                    }
                    //handling of invalid answer choices
                    else {
                    System.out.println("That is not a valid option, please type 'yes' or 'no'.");
                    }
                }
                else {
                    //asking if they'd like to play again after they incorrectly guess sentence
                    System.out.println("Incorrect! Please be more cautious next time.");
                    System.out.println("Would you like to play another round of the game?");
                    String playAgain = scan.nextLine();
                    if (playAgain.equalsIgnoreCase("yes")) {
                        //setting the guesses back to zero and recalling the methods for a new sentence
                        correctGuesses = 0;
                        incorrectGuesses = 0;
                        permutedAlphabet = randomizedAlphabet();
                        if (allSentences.size()!= 0) {
                        randomSentence = chooseSentence(allSentences);
                        allSentences.remove(randomSentence);
                        }
                        else {
                        System.out.println("You have reached the max number of sentences.");
                        System.exit(0);
                        }
                        permutedSentence = permutedSentence(randomSentence, permutedAlphabet);
                        usedRandomSentences.add(randomSentence);
                        continue;
                    }
                    else if (playAgain.equalsIgnoreCase("no")) {
                    System.out.println("Ok goodbye.");
                    break;
                    }
                    //handling of invalid answer choices
                    else {
                    System.out.println("That is not a valid option, please type 'yes' or 'no'.");
                    }
                    
                }
            }
            else {
                System.out.println("That is not a valid option, please choose 1 or 2.");
            }
        }
        //do while condition in order to keep it running
        while (true);
        scan.close();
    }
}
