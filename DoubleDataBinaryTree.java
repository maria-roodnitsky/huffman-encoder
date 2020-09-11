import java.util.*;
import java.io.*;

/**
 * Double data binary tree, stores two types of data, is basically a new and improved binary tree
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Spring 2016, minor updates to testing
 * @author Tim Pierson, Winter 2018, added code to manually build tree in main
 * @author(s) Maria Roodnitsky and Chris Wright, 2019, extended this to hold more data
 */

public class DoubleDataBinaryTree<Character, Integer> implements Comparable<DoubleDataBinaryTree<Character, Integer>>{
    private DoubleDataBinaryTree<Character, Integer> left, right;    // children; can be null
    private Character data;
    private Integer value;

    /**
     * Constructs leaf node -- left and right are null, data is the character, value is the character count
     */
    public DoubleDataBinaryTree(Character data, Integer value) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.value = value;
    }

    /**
     * Constructs inner node which does not hold character data, but takes two trees and stores the sum of their values
     */
    public DoubleDataBinaryTree(Integer value, DoubleDataBinaryTree<Character, Integer> left, DoubleDataBinaryTree<Character, Integer> right) {
        this.value = value;
        this.left = left;
        this.right = right;
        this.data = null;
    }

    /**
     * Is it a leaf node?
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /**
     * Does it have a left child?
     */
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Does it have a right child?
     */
    public boolean hasRight() {
        return right != null;
    }

    public DoubleDataBinaryTree<Character, Integer> getLeft() {
        return left;
    }

    public DoubleDataBinaryTree<Character, Integer> getRight() {
        return right;
    }

    public Character getData() {
        return data;
    }

    public Integer getValue() { return value; }

    /**
     * Number of nodes (inner and leaf) in tree
     */
    public int size() {
        int num = 1;
        if (hasLeft()) num += left.size();
        if (hasRight()) num += right.size();
        return num;
    }

    /**
     * Longest length to a leaf node from here
     */
    public int height() {
        if (isLeaf()) return 0;
        int h = 0;
        if (hasLeft()) h = Math.max(h, left.height());
        if (hasRight()) h = Math.max(h, right.height());
        return h + 1;                        // inner: one higher than highest child
    }

    /**
     * Returns a string representation of the tree
     */
    public String toString() {
        return toStringHelper("");
    }

    /**
     * Recursively constructs a String representation of the tree from this node,
     * starting with the given indentation and indenting further going down the tree
     */
    public String toStringHelper(String indent) {
        String res = indent + "key:"+ data + " " + "value:" + value;
        if (hasLeft()) res += left.toStringHelper(indent + "  ");
        if (hasRight()) res += right.toStringHelper(indent + "  ");
        return res;
    }

    /**
     * Order the tree with respect to value (make that the implicit compare type)
     */
    @Override
    public int compareTo(DoubleDataBinaryTree<Character, Integer> tree2){
        if ((int)value < (int)tree2.getValue()) return -1;
        else if ((int)value > (int)tree2.getValue()) return 1;
        else return 0;
    }

    /**
     * Convert the tree into a map that stores each character as a key and the binary code of the key as
     * the value
     */

    public HashMap<Character, String> covertToBinaryCodeMapCompress(){
        HashMap<Character, String> codeMap = new HashMap<>();
        String code;
        if (size() == 1) { code = "1"; }
        else { code = ""; }
        convertToBinaryCodeMapHelperCompress(codeMap, code);
        return codeMap;
    }

    /**
     * Helper method for creating the code map
     */
    public void convertToBinaryCodeMapHelperCompress(HashMap<Character, String> map, String code){
        // has left
        if (hasLeft()) { left.convertToBinaryCodeMapHelperCompress(map, code + "0");}
        // has right
        if (hasRight()) { right.convertToBinaryCodeMapHelperCompress(map, code + "1");}
        // leaf
        if (isLeaf()) { map.put(data, code); }
    }

}