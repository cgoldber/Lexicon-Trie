
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class that describes one node within a Lexicon Trie, which is associated with a letter. 
 * 
 * @author Casey Goldberg
 */
public class LexiconNode implements Iterable<LexiconNode>{
    
    /**
     * Contains the associated children of a parent node.
     */
    private List<LexiconNode> children;
    
    /**
     * The parent associated with a LexiconNode object.
     */
    private LexiconNode parent;

    /**
     * The character associated with a LexiconNode.
     */
    private char nodeChar;
    
    /**
     * Whether a node is the last in a sequence of nodes making up a word.
     */
    private boolean isFullWordNode;

    /**
     * Constructs a LexiconNode object.
     * 
     * @param nodeChar The character associated with a LexiconNode object.
     * @param isFullWordNode Whether a node is the last in a sequence of nodes making up a word.
     * @param parent The parent node of a LexiconNode object.
     */
    public LexiconNode(char nodeChar, boolean isFullWordNode, LexiconNode parent){
        this.parent = parent;
        this.nodeChar = nodeChar;
        this.children = new ArrayList<LexiconNode>();
        this.isFullWordNode = isFullWordNode;
    }
    
    /**
     * Adds a child LexiconNode to a parent LexiconNode's list of children while maintaining an alphabetical order.
     * 
     * @param newChild The child node added to the parent's list of children.
     */
    public void addChild(LexiconNode newChild){
        if(children.size() != 0){
            for(int i = 0; i < children.size(); i++){
                char currChar = children.get(i).getNodeChar();
                if(newChild.getNodeChar() < currChar){
                    children.add(i, newChild);
                    return;
                }
            }
        }
        children.add(newChild);
    }
    
    /**
     * Removes a child from a parent LexiconNode's list of children.
     * 
     * @param exChild The child node that is removed from the parent's list of children.
     */
    public void removeChild(LexiconNode exChild){
        children.remove(exChild);
    }
    
    /**
     * Returns a child node object associated with a given character.
     * 
     * @param charOfInt The character that is being searched for.
     * @return The child associated with a given character.
     */
    public LexiconNode getChild(char charOfInt){
        for(LexiconNode child : children){
            if(child.getNodeChar() == charOfInt){
                return child;
            }
        }
        return null;
    }
    
    /**
     * Constructs an iterator object that can iterate through the children LexiconNodes of a parent node.
     * 
     * @return The child iterator.
     */
    @Override
    public Iterator<LexiconNode> iterator(){
        Iterator<LexiconNode> iter = children.iterator();
        return iter;             
    }
    
    /**
     * Returns the character associated with a node.
     * 
     * @return The associated character.
     */
    public char getNodeChar(){
        return nodeChar;
    }
    
    /**
     * Returns whether a node is the last in a sequence of nodes making up a word.
     */
    public boolean getIsFullWordNode(){
        return isFullWordNode;
    }
    
    /**
     * Sets whether a node is a full word node.
     * 
     * @param bool Whether a node is a full word node.
     */
    public void setIsFullWordNode(boolean bool){
        isFullWordNode = bool;
    }
    
    /**
     * Returns the parent of a node.
     * 
     * @return The node's parent.
     */
    public LexiconNode getParent(){
        return parent;
    }
    
    /**
     * Returns whether a node has children.
     * 
     * @return If a node has any children.
     */
    public boolean hasChildren(){
        return !children.isEmpty();
    }
}
