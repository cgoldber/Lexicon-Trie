
import java.util.Iterator;
import java.util.Set;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * An implementation of the Lexicon interface that stores words in a tree. A path through the trie traces out 
 * a sequence of letters that represents a prefix or word in a lexicon. The root of the trie is blank.
 * 
 * @author Casey Goldberg
 */

public class LexiconTrie implements Lexicon{
    
    /**
     * The root of a LexiconTrie object.
     */
    private LexiconNode root;
    
    /**
     * The current number of in a LexiconTrie object.
     */
    private int numWords;
    
    /**
     * Constructs a LexiconTrie object.
     */
    public LexiconTrie(){
         root = new LexiconNode(' ', false, null);
         numWords = 0;
    }
    
    /**
     * Adds the specified word to the lexicon. Should run in time proportional to
     * the length of the word being added. Returns whether the lexicon was
     * modified by adding the word.
     *
     * @param word
     *          The lowercase word to add to the lexicon.
     * @return True if the word was added and false if the word was already part
     *         of the lexicon.
     */
    public boolean addWord(String word){
        LexiconNode finger = root;
        for(int i = 0; i < word.length(); i++){
            char charOfInt = word.charAt(i);
            if(finger.getChild(charOfInt) == null){
                boolean isFinalLetter = i == word.length() - 1;
                LexiconNode newChild = new LexiconNode(charOfInt, isFinalLetter, finger);
                finger.addChild(newChild);
                if(isFinalLetter){
                    numWords++;
                    return true;
                }
            }
            finger = finger.getChild(charOfInt);   
        }
        return false;
    }

    /**
     * Reads the words contained in the specified file and adds them to the
     * lexicon. The format of the given file is expected to be one word per line
     * of the file. All words should be converted to lowercase before adding.
     * Returns the number of new words added, or -1 if the file could not be read.
     *
     * @param filename
     *          The name of the file to read.
     * @return The number of new words added, or -1 if the file could not be read.
     */
    public int addWordsFromFile(String filename){
        try{
            int wordsAdded = 0;
            Scanner scan = new Scanner(new File(filename));
            while(scan.hasNext()){
                String word = scan.next().toLowerCase();
                addWord(word);
                wordsAdded++;
            }
            return wordsAdded;
        }catch(FileNotFoundException e){
            return -1;    
        }
    }

    /**
     * Finds the final node in a sequence of nodes that makes up a given prefix.
     * 
     * @param prefix The given prefix.
     * @return The final node in the sequence. Returns null if the sequence doesn't exist in the tree.
     */
    public LexiconNode finalNode(String prefix){
        LexiconNode finger = root;
        for(int i = 0; i < prefix.length(); i ++){ 
            finger = finger.getChild(prefix.charAt(i));
            if(finger == null){
                return null;    
            }
        }
        return finger; 
    }
    
    /**
     * Deletes the nodes in a LexiconTrie that don't eventually lead up to a full word node.
     * 
     * @param endNode The node lowest in the tree of the sequence of nodes that are being removed.
     */
    private void cleanUp(LexiconNode endNode){
        if(!endNode.hasChildren()){
            LexiconNode parent = endNode.getParent();
            parent.removeChild(endNode);
            cleanUp(parent);
        }
    }
    
    /**
     * Attempts to remove the specified word from the lexicon. If the word appears
     * in the lexicon, it is removed and true is returned. If the word does not
     * appear in the lexicon, the lexicon is unchanged and false is returned.
     * Should run in time proportional to the length of the word being removed. It
     * is implementation-dependent whether unneeded prefixes as a result of
     * removing the word are also removed from the lexicon.
     *
     * @param word
     *          The lowercase word to remove from the lexicon.
     * @return Whether the word was removed.
     */
    public boolean removeWord(String word){
        LexiconNode finalNode = finalNode(word);
        if(finalNode != null){
            finalNode.setIsFullWordNode(false);
            cleanUp(finalNode);
            numWords--;
            return true;
        }
        return false;
    }

    /**
     * Returns the number of words contained in the lexicon. Should run in
     * constant time.
     *
     * @return The number of words in the lexicon.
     */
    public int numWords(){
        return numWords;
    }

    /**
     * Checks whether the given word exists in the lexicon. Should run in time
     * proportional to the length of the word being looked up.
     *
     * @param word
     *          The lowercase word to lookup in the lexicon.
     * @return Whether the given word exists in the lexicon.
     */
    public boolean containsWord(String word){
        LexiconNode endNode = finalNode(word);
        if(endNode != null){
            return endNode.getIsFullWordNode();
        }
        return false;
    }

    /**
     * Checks whether any words in the lexicon begin with the specified prefix. A
     * word is defined to be a prefix of itself, and the empty string is defined
     * to be a prefix of everything. Should run in time proportional to the length
     * of the prefix.
     *
     * @param prefix
     *          The lowercase prefix to lookup in the lexicon.
     * @return Whether the given prefix exists in the lexicon.
     */
    public boolean containsPrefix(String prefix){
        return finalNode(prefix) != null;
    }

    /**
     * Returns an iterator over all words contained in the lexicon. The iterator
     * should return words in the lexicon in alphabetical order.
     */
    @Override
    public Iterator<String> iterator(){
        ArrayList<String> words = new ArrayList<String>();
        getWordsHelper(words, root, "");
        Iterator<String> iter = words.iterator();
        return iter;
    }
    
    /**
     * Keeps track of partial words and adds all words in a tree to a list.
     * 
     * @param words The partially constructed word.
     * @param currRoot The current node that is being examined.
     * @param wordSoFar The partially constructed word.
     */
    private void getWordsHelper(ArrayList<String> words, LexiconNode currRoot, String wordSoFar){
        if(currRoot.hasChildren()){
            for(LexiconNode child : currRoot){
                String currWord = wordSoFar + child.getNodeChar();
                if(child.getIsFullWordNode()){
                     words.add(currWord);
                }
                getWordsHelper(words, child, currWord);
            }
        }
    }
    
    /**
     * Returns a set of words in the lexicon that are suggested corrections for a
     * given (possibly misspelled) word. Suggestions will include all words in the
     * lexicon that are at most maxDistance distance from the target word, where
     * the distance between two words is defined as the number of character
     * positions in which the words differ. Should run in time proportional to the
     * length of the target word.
     *
     * @param target
     *          The target word to be corrected.
     * @param maxDistance
     *          The maximum word distance of suggested corrections.
     * @return A set of all suggested corrections within maxDistance of the target
     *         word.
     */
    public Set<String> suggestCorrections(String target, int maxDistance){
        Set<String> suggestions = new HashSet<String>();
        suggestCorrectionsHelper(target, maxDistance, root, suggestions, "");
        return suggestions;
    }

    /**
     * Keeps track of potential words that are within the distance of the target, and adds all of the completed words 
     * within this distance to a list.
     * 
     * @param currTarget The portion of the target that is being searched for.
     * @param distToGo How much the word being constructed can still deviate from the target.
     * @param currRoot The node that is being examined.
     * @param suggestions The potential words that are within the maximum distance from the target.
     * @param wordSoFar A portion of a word that is being constructed.
     */
    private void suggestCorrectionsHelper(String currTarget, int distToGo, LexiconNode currRoot, 
    Set<String> suggestions, String wordSoFar){
        if(distToGo < 0){
            return;
        }else if(currTarget.isEmpty()){
            if(currRoot.getIsFullWordNode()){
                suggestions.add(wordSoFar);
            }
            return;
        }else{
            for(LexiconNode child : currRoot){
                String currWord = wordSoFar + child.getNodeChar();
                int currDistToGo = distToGo;
                if(child.getNodeChar() != currTarget.charAt(0)){
                    currDistToGo--;
                }
                suggestCorrectionsHelper(currTarget.substring(1), currDistToGo, child, suggestions, currWord);
            }
        }
    }
    
    /**
     * Returns a set of all words in the lexicon that match the given regular
     * expression pattern. The regular expression pattern may contain only letters
     * and wildcard characters '*', '?', and '_'.
     *
     * @param pattern
     *          The regular expression pattern to match.
     * @return A set of all words in the lexicon matching the pattern.
     */
    public Set<String> matchRegex(String pattern){
        Set<String> matchedWords = new HashSet<String>();
        matchRegexHelper(pattern, root, matchedWords, "");
        return matchedWords;
    }
    
    /**
     * Constructs and keeps track of potential words that match the regular expression pattern, and adds complete words
     * to a list. The potential wildcard characters are '_', which matches any one character, '*', which matches any 
     * sequence of zero or more characters, and '?', which matches either zero or one characters.
     * 
     * @param currPattern The portion of the pattern that the potential words should match.
     * @param myRoot The node being examined.
     * @param matchedWords The words that match the given regex pattern.
     * @param wordSoFar A portion of a word that is being constructed.
     */
    private void matchRegexHelper(String currPattern, LexiconNode myRoot, Set<String> matchedWords, String wordSoFar){
        if(currPattern.isEmpty()){
            if(myRoot.getIsFullWordNode()){
                matchedWords.add(wordSoFar);
            }
            return;
        }
        char charOfInt = currPattern.charAt(0);
        if(charOfInt == '*'){
            matchRegexHelper(currPattern.substring(1), myRoot, matchedWords, wordSoFar);
            char nextChar = ' ';
            for(int i = 0; i < currPattern.length(); i++){
                if (currPattern.charAt(i) != '*'){
                    nextChar = currPattern.charAt(i);
                    break;
                } 
            }
            for(LexiconNode child : myRoot){
                String currWord = wordSoFar + child.getNodeChar();
                String nextPattern;
                if(child.getNodeChar() == nextChar){
                    nextPattern = currPattern.substring(1);
                } else {
                    nextPattern = currPattern;
                }
                matchRegexHelper(nextPattern, child, matchedWords, currWord);
            }
        }else if(charOfInt == '_'){
            for(LexiconNode child : myRoot){
                String editedPattern = child.getNodeChar() + currPattern.substring(1);
                matchRegexHelper(editedPattern, myRoot, matchedWords, wordSoFar);  
            }
        }else if(charOfInt == '?'){
            matchRegexHelper(currPattern.substring(1), myRoot, matchedWords, wordSoFar);
            matchRegexHelper('_' + currPattern.substring(1), myRoot, matchedWords, wordSoFar);
        }else{ 
            for(LexiconNode child : myRoot){
                String currWord = wordSoFar;
                if(charOfInt == child.getNodeChar()){
                    currWord += charOfInt;
                    matchRegexHelper(currPattern.substring(1), child, matchedWords, currWord);
                }
            }
        }
    }
}
