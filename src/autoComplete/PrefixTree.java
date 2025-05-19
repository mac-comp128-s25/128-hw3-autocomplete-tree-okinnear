package autoComplete;

import java.util.ArrayList;
import java.util.Map;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree() {
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word) {
        TreeNode currentNode = root;
        // Add each letter to tree:
        for (char c : word.toCharArray()) {
            if (currentNode.children.containsKey(c)) {
                // Move down the tree
                currentNode = currentNode.children.get(c);
            } else {
                // Create a new node
                final TreeNode newNode = new TreeNode();
                newNode.letter = c;
                currentNode.children.put(c, newNode);
                currentNode = newNode;
            }
        }
        // Only increase size if word does not already exist
        if (!currentNode.isWord) {
            currentNode.isWord = true;
            size++;
        }
    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word) {
        TreeNode currentNode = root;
        // Walk tree for word
        for (char c : word.toCharArray()) {
            currentNode = currentNode.children.get(c);
            if (currentNode == null) {
                return false;
            }
        }
        return currentNode.isWord;
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix) {
        TreeNode currentNode = root;
        // Navigate to the prefix
        for (char c : prefix.toCharArray()) {
            currentNode = currentNode.children.get(c);
            if (currentNode == null) {
                return new ArrayList<>();
            }
        }
        // Then recursively find all words under that prefix
        return searchPrefixes(prefix, currentNode);
    }

    private ArrayList<String> searchPrefixes(String prefix, TreeNode node) {
        ArrayList<String> ret = new ArrayList<>();
        // Add the current note if it is a word
        if (node.isWord) {
            ret.add(prefix);
        }
        // Recur into children
        for (Map.Entry<Character, TreeNode> entry : node.children.entrySet()) {
            ret.addAll(searchPrefixes(prefix + entry.getKey(), entry.getValue()));
        }
        return ret;
    }

    /**
     * @return the number of words in the tree
     */
    public int size() {
        return size;
    }
    
}
