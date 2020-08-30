
package eightpuzzle;

import java.awt.Point;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Board {

    Node root;  // root node
    Node target;// hedef node baslangicta belirliyoruz
    ArrayList<Node> Solution;
    
    ArrayList<ArrayList<Node>> levelobjects; //bread first arama içik her seviyeyi ayrı bir listede tutuyoruz.
    int size;
    boolean isfound = false;
    int level = 0;

    public Board(int size) {
        this.size = size;
        level=0;
        Solution = new ArrayList<Node>();
        levelobjects = new ArrayList<ArrayList<Node>>();
        // hedef nodumuzu belirtiyoruz
        target = new Node(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}}, null, new Point(size - 1, size - 1), size, 0);
    }

    public void GenerateRandomBoard() {
        
         ArrayList numberList = new ArrayList();//rasgele sayılar için bir yöntem
         //rasgele gelmesini istediğimiz sayıları bir listeye ekliyoryz
         for (int i = 1; i <= size * size; i++) {
            numberList.add(i);
        }
        
        int[][] values = new int[size][size];
        Random rnd = new Random();
        Point zeroLocation = null;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int rndorder = rnd.nextInt(numberList.size());//array list boyutunca bir index seçiyoruz
               
                int number = (int) numberList.get(rndorder); // o indexteki sayıyı alıyoruz
                if (number == size * size) // eğer sonraki sayı ise onun yerine 0 yani boşuk yazıyoruz
                {
                    zeroLocation = new Point(i, j);
                    values[i][j] = 0;
                } else //aksi taktirde seçilen sayı dizize yerleşimi yapılıyor
                {
                    values[i][j] = number;
                }
                // çekilmiş sayıyı listeden çıkarıyoruz    
                numberList.remove(rndorder);

            }

            //rasgele root
            //this.root = new Node(values, null, zeroLocation, this.size, 0); //random üretilen sayılar ile node
            //test rootu
            this.root = new Node(new int[][]{{1, 2, 3}, {4, 0, 5}, {6, 7, 8}}, null, new Point(1, 1), this.size, 0);//deneme icin node
            //root nodunu level 0 a ekliyoruz
            levelobjects.add(new ArrayList<Node>()); //root level sıfır elemanıdır
            levelobjects.get(0).add(root);// rootu level 0 a ekliyoruz
            root.Print();

           
            this.SearchBFS();// aram işlemini başlat
            PrintSolution(); // sonuç pathını yazdır 
        }
    }

    public void PrintSolution() {
        System.out.println("*** Solution Path ***");
        //tersten yazdırma
        for (int i = Solution.size(); i >= 0; i--) {
            Solution.get(i).Print();
        }
        /* düz yazdırma
        for (Node node : Solution) {
            node.Print();
        }*/
        System.out.println("***************");

    }

    public boolean SearchBFS() {
        if (isfound) {
            return true;
        }
        //aktif seviye listesini getiriyoruz
        ArrayList<Node> activelevel = levelobjects.get(level);

        for (Node node : activelevel) {
            // o listedeki tüm elemanları hedef node ile karşılaştırıyoruz
            if (CompareNodes(node, target)) {
                // eğer eşleşme varsa çözüm bulunmuştur
                isfound = true;
                //çözümü tersten solution listesine ekliyoruz
                Node tmp = node;
                while (tmp != null) {
                    //en dip nodedan parantlerine doğru en üst parent root
                    this.Solution.add(tmp);
                    tmp = tmp.parent;
                }
                // reculsive biter
                return isfound;
            }
        }
        //bu seviyedeki nodlarda bulunmadıysa yeni bir level oluşturuyor ve seviyeyi arırıyoruz
        level++;
        levelobjects.add(new ArrayList<Node>());
        for (Node node : activelevel) {
            //bir aktif level nodlarının cocuk nodlarını oluşturuyoruz
            GenerateChild(node);
            //bu yeni nodları bir sonraki yeni levele ekliyoruz
            if (node.leftNode != null) {

                levelobjects.get(level).add(node.leftNode);
            }
            if (node.rightNode != null) {
                levelobjects.get(level).add(node.rightNode);
            }
            if (node.upNode != null) {
                levelobjects.get(level).add(node.upNode);
            }
            if (node.downNode != null) {
                levelobjects.get(level).add(node.downNode);
            }

        }
        //bir sonraki seviye araması için reculsive olarak search çağırıyoruz
        SearchBFS();

        return false;
    }

    //bir nodun çocuklarını üreten fonksiyon
    public void GenerateChild(Node node) {

        //this.visitedNode.add(node);
      
        int xleft = node.zeroLoc.x - 1;
        if (xleft >= 0) // sol komşu
        {
            Point nzeroloc = new Point(xleft, node.zeroLoc.y);
            Node nnode = new Node(node, nzeroloc, node.size, (node.level + 1));
            nnode.Clone(node);

            nnode.values[xleft][node.zeroLoc.y] = node.values[node.zeroLoc.x][node.zeroLoc.y];
            nnode.values[node.zeroLoc.x][node.zeroLoc.y] = node.values[xleft][node.zeroLoc.y];
            node.downNode = nnode;
            nnode.upNode = node;
            /*node.neighbors.add(node);
            nnode.neighbors.add(node);*/
            // üretilmiş node her hangi bir nodun childı olmamalı aksi halde döngü olur
            if (CheckNodeGenerated(nnode)) {
                node.downNode = null;
                nnode = null;
            } else {
                nnode.Print();
            }
            //nnode.Print();
        }
        int xright = node.zeroLoc.x + 1;
        if (xright < this.size) // sol komşu
        {
            Point nzeroloc = new Point(xright, node.zeroLoc.y);
            Node nnode = new Node(node, nzeroloc, node.size, (node.level + 1));
            nnode.Clone(node);

            nnode.values[xright][node.zeroLoc.y] = node.values[node.zeroLoc.x][node.zeroLoc.y];
            nnode.values[node.zeroLoc.x][node.zeroLoc.y] = node.values[xright][node.zeroLoc.y];
            node.upNode = nnode;
            nnode.downNode = node;
            /*node.neighbors.add(node);
            nnode.neighbors.add(node);*/
            if (CheckNodeGenerated(nnode)) {
                node.upNode = null;
                nnode = null;
            } else {
                nnode.Print();
            }

        }
        int yleft = node.zeroLoc.y + 1;
        if (yleft < this.size) // sol komşu
        {
            Point nzeroloc = new Point(node.zeroLoc.x, yleft);
            Node nnode = new Node(node, nzeroloc, node.size, (node.level + 1));
            nnode.Clone(node);

            nnode.values[node.zeroLoc.x][yleft] = node.values[node.zeroLoc.x][node.zeroLoc.y];
            nnode.values[node.zeroLoc.x][node.zeroLoc.y] = node.values[node.zeroLoc.x][yleft];
            node.rightNode = nnode;
            nnode.leftNode = node;

            if (CheckNodeGenerated(nnode)) {
                node.rightNode = null;
                nnode = null;
            } else {
                nnode.Print();
            }

        }
        int yright = node.zeroLoc.y - 1;
        if (yright >= 0) // sol komşu
        {
            Point nzeroloc = new Point(node.zeroLoc.x, yright);
            Node nnode = new Node(node, nzeroloc, node.size, (node.level + 1));
            nnode.Clone(node);

            nnode.values[node.zeroLoc.x][yright] = node.values[node.zeroLoc.x][node.zeroLoc.y];
            nnode.values[node.zeroLoc.x][node.zeroLoc.y] = node.values[node.zeroLoc.x][yright];
            node.leftNode = nnode;
            nnode.rightNode = node;
            if (CheckNodeGenerated(nnode)) {
                node.leftNode = null;
                nnode = null;
            } else {
                nnode.Print();
            }

        }

    }
// üretilmiş bir nodun daha önce üretilip üretilmediğini kontrol eden fonnksiyın
    public boolean CheckNodeGenerated(Node node) {
        boolean isfound = false;
        //her seviyede arama yapıyoruz.
        for (int i = 0; i < levelobjects.size() && !isfound; i++) {
            ArrayList<Node> levelobjectlist = this.levelobjects.get(i);
            for (Node gnode : levelobjectlist) {
                if (CompareNodes(gnode, node)) {
                    isfound = true;
                    break;
                }
            }
        }

        return isfound;
    }

    public boolean CompareNodes(Node node1, Node node2) {
        boolean isequal = true;
        for (int i = 0; i < this.size && isequal; i++) {
            for (int j = 0; j < this.size; j++) {
                if (isequal && node1.values[i][j] != node2.values[i][j]) {
                    isequal = false;
                    break;
                }
            }

        }
        return isequal;

    }

}
