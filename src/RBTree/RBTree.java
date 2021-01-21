package RBTree;

import java.util.Comparator;

/**
 * 这是我在学完算法导论红黑树之后，决定写一个红黑树的泛型类练练手
 * @param <T> 表示放在这个红黑树中的数据类型，如果继承了comparable接口，那么在构造这个红黑树的时候不需要提供Comparator，否则是需要的
 * */
public class RBTree<T> {
    /**
     * 内部枚举类，用来表示红黑树节点的颜色
     * */
    private enum NodeColor{
        RED,
        BLACK;
    }
    /**
     * 内部类，一个红黑树中的节点
     * */
    private class TreeNode{
        T Key;
        TreeNode LeftChild;
        TreeNode RightChild;
        NodeColor Color;

        public TreeNode(){
            this.Key = null;
            this.LeftChild =  RBTree.this.NIL;
            this.RightChild =  RBTree.this.NIL;
            this.Color = NodeColor.BLACK;
        }

        public TreeNode(T key, NodeColor color){
            this.Key = key;
            this.LeftChild =  RBTree.this.NIL;
            this.RightChild =  RBTree.this.NIL;
            this.Color = color;
        }
    }
    private final TreeNode NIL = new TreeNode();
    private Comparator<T> comparator = null;
    private TreeNode Root = null;
    /**
     * 默认构造器，如果T继承了Comparable就可以使用这个
     * */
    public RBTree(){
        super();
    }
    /**
     * 包含比较器的过滤器，如果T不是可比较类型的话，就必须使用这个比较器
     * @param rb_tree_comparator 需要使用到的比较器
     * */
    public RBTree(Comparator<T> rb_tree_comparator){
        super();
        this.comparator = rb_tree_comparator;
    }
    /**
     * 判断一个值是否存在
     * @param data 寻找的值
     * @return 这个值是不是在这颗红黑树里面
     * */
    public boolean containsValue(T data){

    }
    /**
     * 根据一个节点的Key去找相同的节点，如果没有就返回null
     * @param key 被找的节点的Key
     * @return 包含key的节点
     */
    private TreeNode findNode(T key){

    }
    /**
     * 往红黑树中插入一个值，如果这个值已经存在，就不管了
     * @param data 被插入的数据
     * */
    public void insert(T data){

    }
    /**
     * 从红黑树中删除一个值，如果没有的话，就什么都不做
     * @param data 被删除的值
     * */
    public void delete(T data){

    }
}
