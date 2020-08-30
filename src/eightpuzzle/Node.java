
package eightpuzzle;

import java.awt.Point;
import java.util.List;


public class Node {

    public int size;
    public int[][] values;
    public Point zeroLoc;//boşluğun pozisyonu
    public Node parent;//node parenti
    public Node leftNode;
    public Node rightNode;
    public Node upNode;
    public Node downNode;
    public int level;//node seviyesi
    public boolean isvisited;//daha önce gezilip gezilmediği

    public Node(int[][] values, Node parent, Point zeroLoc, int size, int level) {
        this.values = values;
        this.zeroLoc = zeroLoc;
        this.parent = parent;
        this.size = size;
        this.level = level;
        this.downNode = null;
        this.leftNode = null;
        this.upNode = null;
        this.rightNode = null;
    }

    public Node(Node parent, Point zeroLoc, int size, int level) {
        this.values = new int[size][size];

        this.Clone(parent);
        this.zeroLoc = zeroLoc;
        this.parent = parent;
        this.size = size;
        this.level = level;
        this.downNode = null;
        this.leftNode = null;
        this.upNode = null;
        this.rightNode = null;
    }

    public boolean Clone(Node node) {
        //diğer nodun valuesini bu nodunkine kopyalar
        boolean isequal = true;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.values[i][j] = node.values[i][j];

            }

        }
        return isequal;

    }

    public void Print() {
        System.out.println("");
        for (int i = 0; i < this.size; i++) {

            for (int j = 0; j < this.size; j++) {
                System.out.print(" " + values[i][j]);
            }
            System.out.println("");
            //1System.out.println("|\n_____________");
        }
        System.out.println("");
    }
}
