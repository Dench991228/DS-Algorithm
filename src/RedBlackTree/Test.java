package RedBlackTree;

import java.util.Scanner;

public class Test {
    public static void main(String[] args){
        RBTree red_back_tree = new RBTree();
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            red_back_tree.insertNode(sc.nextInt());
            System.out.println(red_back_tree);
        }
        red_back_tree.delete(7);
        System.out.println(red_back_tree);
    }
}
