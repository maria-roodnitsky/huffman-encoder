import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

/* Written by Maria Roodnitsky and Chris Wright on 10/24/19
 */

public class compressionDriver {
    //relevant file  names
    private static final String pathname = "C:\\Users\\Maria\\IdeaProjects\\CS10\\compression\\inputs\\september_test_file.txt";
    private static final String compressedPathName = pathname.substring(0, pathname.length()-4) +
            "_compressed" + pathname.substring(pathname.length()-4);
    private static final String decompressedPathName = pathname.substring(0, pathname.length()-4) +
            "_decompressed" + pathname.substring(pathname.length()-4);

    public static void main(String[] args) {
        try {
            //throws FileNotFoundException if there is not a file
            BufferedReader input = new BufferedReader(new FileReader(pathname));


            //throws a EOFException if the first value is the end of the file value [-1]
            int val = input.read();
            if (val == -1) {input.close(); throw new EOFException();}

            //instantiate a frequency map of the characters in the file
            TreeMap<Character, Integer> freqmap = new TreeMap<Character, Integer>();

            //fill in the map with the character as the key and the frequency as the value
            do {
                if (freqmap.containsKey((char)val)) { freqmap.put((char)val, freqmap.get((char)val) + 1); }
                else { freqmap.put((char)val, 1); }
                val = input.read();
            } while (val != -1);
            input.close();

            //create a priority queue from the entries in the map
            PriorityQueue<DoubleDataBinaryTree<Character, Integer>> frequencyqueue = new
                    PriorityQueue<DoubleDataBinaryTree<Character, Integer>>();

            //add the entries to the queue
            for (Map.Entry<Character, Integer> entry : freqmap.entrySet()){
                DoubleDataBinaryTree<Character, Integer> node =
                        new DoubleDataBinaryTree<Character, Integer>(entry.getKey(), entry.getValue());
                frequencyqueue.add(node);
            }

            //combine trees in the priority queue until there is one master tree with the maximum value at the top
            while (frequencyqueue.size() > 1){
                DoubleDataBinaryTree<Character, Integer> elem1 = frequencyqueue.remove();
                //System.out.println(elem1);
                DoubleDataBinaryTree<Character, Integer> elem2 = frequencyqueue.remove();
                //System.out.println(elem1);
                Integer newVal = elem1.getValue() + elem2.getValue();
                DoubleDataBinaryTree<Character, Integer> combined =
                        new DoubleDataBinaryTree<Character, Integer>(newVal, elem1, elem2);
                frequencyqueue.add(combined);
            }

            //assign the final element in the queue as the 'finalTree'
            DoubleDataBinaryTree<Character, Integer> finalTree = frequencyqueue.remove();

            //create a code map where every character is linked to its binary code through tree traversal
            HashMap<Character, String> codemap = finalTree.covertToBinaryCodeMapCompress();

            //instantiate a buffered bit writer with which to write and a buffered reader with which to read
            BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);
            BufferedReader original = new BufferedReader(new FileReader(pathname));

            //read the first character in the original file
            int currentCharacter = original.read();

            do {
                //find the code of the character in the code map
                String codeOfCharacter = codemap.get((char)currentCharacter);
                //convert the code (string) into an array of characters
                char[] codeOfCharacterArray = codeOfCharacter.toCharArray();

                //for each character, if it is 1, write a "true"; otherwise (it is a 0) write a "false" into the
                //compressed file
                for (char number: codeOfCharacterArray) {
                    if (number == '1') { bitOutput.writeBit(true);}
                    else { bitOutput.writeBit(false); }
                }

                //read the next character and do this process until you reach the end of the original file
                currentCharacter = original.read();
            } while (currentCharacter != -1);

            //close both files
            original.close();
            bitOutput.close();

            //instantiate a buffered writer with which to write and a buffered bit reader with which to read
            BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);
            BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));

            //create a floating copy of the tree that will be used for traversal
            DoubleDataBinaryTree<Character, Integer> lookupTree = finalTree;

            while(bitInput.hasNext()){
                //read the bit
                boolean bit = bitInput.readBit();

                //if we have reached a leaf, get the data from the leaf and reset the tree
                // otherwise, move left if bit is 0; move right if bit is 1
                if(lookupTree.isLeaf()) { output.write(lookupTree.getData()); lookupTree = finalTree;}
                if (bit && lookupTree.size() > 1) { lookupTree = lookupTree.getRight();}
                if (!bit && lookupTree.size() > 1) { lookupTree = lookupTree.getLeft();}
                }


            //close both files
            bitInput.close();
            output.close();
        }


        //first two should hopefully catch common exceptions, the final one is a catch-all
        catch (FileNotFoundException exception){
            System.out.println("Source file not found!");
        }
        catch(EOFException exception){
            System.out.println("File is empty!");
        }
        catch(Exception exception) {
            System.out.println("Something that has not been accounted for went wrong! Womp womp!");
        }
    }
}

