package RBTree;

import RedBlackTree.TreeNode;

import java.util.Comparator;
import java.util.LinkedList;

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
        TreeNode Parent;
        NodeColor Color;

        public TreeNode(){
            this.Key = null;
            this.LeftChild =  RBTree.this.NIL;
            this.RightChild =  RBTree.this.NIL;
            this.Parent = RBTree.this.NIL;
            this.Color = null;
        }

        public TreeNode(T key, NodeColor color){
            this.Key = key;
            this.LeftChild =  RBTree.this.NIL;
            this.RightChild =  RBTree.this.NIL;
            this.Parent = RBTree.this.NIL;
            this.Color = color;
        }

        @Override
        public String toString(){
            return "("+(this.Key==null?"null":this.Key.toString())+","+this.Color.toString()+")";
        }
    }
    private final TreeNode NIL = new TreeNode();
    private Comparator<T> comparator = null;
    private TreeNode Root = NIL;

    /**
     * 默认构造器，如果T继承了Comparable就可以使用这个
     * */
    public RBTree(){
        super();
        NIL.Color = NodeColor.BLACK;
    }

    /**
     * 包含比较器的过滤器，如果T不是可比较类型的话，就必须使用这个比较器
     * @param rb_tree_comparator 需要使用到的比较器
     * */
    public RBTree(Comparator<T> rb_tree_comparator){
        super();
        this.comparator = rb_tree_comparator;
        NIL.Color = NodeColor.BLACK;
    }

    /**
     * 比较两个T类型的变量大小，隐去因为T是不是Comparable的细节
     * @param o1 变量1
     * @param o2 变量2
     * @return 0表示相等，-1表示o1<o2,1表示o1<o2
     * */
    private int compare(T o1, T o2){
        if(o1 instanceof Comparable)return ((Comparable) o1).compareTo(o2);
        else{
            if(this.comparator==null)throw new UnComparableException();
            else return this.comparator.compare(o1, o2);
        }
    }

    /**
     * 根据一个节点的Key去找相同的节点，如果没有就返回null
     * @param data 被找的节点的key中的内容
     * @return 包含key的节点
     */
    private TreeNode findNode(T data){
        /*没有设置根节点，那就肯定没有*/
        if(Root==NIL)return null;

        if(data instanceof Comparable){//T 是一个Comparable类的情况
            Comparable<T> d = (Comparable<T>) data;
            TreeNode current = Root;
            while(current!=NIL&&((Comparable<T>)current.Key).compareTo(data)!=0){
                System.out.println("comparing between two comparables");
                if (((Comparable<T>) data).compareTo(current.Key)<0) {
                    System.out.println(data.toString()+" is smaller than "+current.Key.toString());
                    current = current.LeftChild;
                }
                else{
                    System.out.println(data.toString()+" is bigger than "+current.Key.toString());
                    current = current.RightChild;
                }
            }
            return current==NIL?null:current;
        }
        else{//提供Comparator的情况
            if(this.comparator==null)throw new UnComparableException();
            TreeNode current = Root;
            while(current!=NIL&&this.comparator.compare(data, current.Key)!=0){
                if(this.comparator.compare(data, current.Key)<0){
                    current = current.LeftChild;
                }
                else{
                    current = current.RightChild;
                }
            }
            return current==NIL?null:current;
        }
    }

    /**
     * 判断一个值是否存在
     * @param data 寻找的值
     * @return 这个值是不是在这颗红黑树里面
     * */
    public boolean containsKey(T data){
        return this.findNode(data)!=null;
    }

    /**
     * 红黑树的左旋，目标节点放到自己的左子节点的位置上，只要左旋之前的红黑树没问题，那么左旋前后不影响红黑树的性质
     * @param node 被左旋的节点
     * */
    private void leftRotate(TreeNode node){
        System.out.println("left rotating!");
        TreeNode new_node = node.RightChild;
        /*把y的左子节点接到node上*/
        node.RightChild = new_node.LeftChild;
        if(new_node.LeftChild!=NIL){
            new_node.LeftChild.Parent = node;
        }
        /*把new_node变成node的父节点的子节点*/
        new_node.Parent = node.Parent;
        if(node.Parent==NIL){//node是根节点
            this.Root = new_node;
        }
        else if(node.Parent.LeftChild==node){//node是父节点的左子节点
            node.Parent.LeftChild = new_node;
        }
        else node.Parent.RightChild = new_node;
        /*把node设置为new_node的左子节点*/
        new_node.LeftChild = node;
        node.Parent = new_node;
    }

    /**
     * 红黑树的右旋，自己变成自己的右子节点。只要右旋之前的红黑树没问题，那么右旋前后不影响红黑树的性质
     * @param node 被右旋的节点
     * */
    private void rightRotate(TreeNode node){
        System.out.println("right rotating");
        TreeNode new_node = node.LeftChild;//放在node位置的新节点
        /*new_node的右子节点接到node的左边*/
        node.LeftChild = new_node.RightChild;
        if(new_node.RightChild!=NIL){
            new_node.RightChild.Parent = node;
        }
        /*把new_node接到node的父节点上*/
        new_node.Parent = node.Parent;
        if(node.Parent==NIL){//node是根节点
            this.Root = new_node;
        }
        else if(node.Parent.LeftChild==node){
            node.Parent.LeftChild = new_node;
        }
        else{
            node.Parent.RightChild = new_node;
        }
        /*把node连接到new_node上*/
        new_node.RightChild = node;
        node.Parent = new_node;
    }

    /**
     * 完成插入之后可能会引入一些破坏红黑树性质的东西，所以需要修复
     * @param target 新插入的节点
     * */
    private void insertFix(TreeNode target){
        while(target.Parent.Color==NodeColor.RED){
            /*如果target的父节点是其父节点的左子节点*/
            if (target.Parent.Parent.LeftChild == target.Parent) {
                TreeNode uncle_node = target.Parent.Parent.RightChild;
                if(uncle_node.Color==NodeColor.RED){//情况一，叔节点也是红色，那就父亲和叔叔都变黑，爷爷变红，上升两层
                    uncle_node.Color = NodeColor.BLACK;
                    target.Parent.Color = NodeColor.BLACK;
                    target.Parent.Parent.Color = NodeColor.RED;
                    target = target.Parent.Parent;
                }else{
                    if(target == target.Parent.RightChild){//情况二，目标节点是父节点的右子节点，变成情况三
                        target = target.Parent;
                        leftRotate(target);
                    }
                    //情况三，叔叔节点是黑色，目标节点是其父节点的左子节点
                    target.Parent.Color = NodeColor.BLACK;
                    target.Parent.Parent.Color = NodeColor.RED;
                    rightRotate(target.Parent.Parent);
                }
            }else{
                TreeNode uncle_node = target.Parent.Parent.LeftChild;
                if(uncle_node.Color == NodeColor.RED){
                    uncle_node.Color = NodeColor.BLACK;
                    target.Parent.Color = NodeColor.BLACK;
                    target.Parent.Parent.Color = NodeColor.RED;
                    target = target.Parent.Parent;
                }else{
                    if(target == target.Parent.LeftChild){
                        target = target.Parent;
                        rightRotate(target);
                    }
                    target.Parent.Color = NodeColor.BLACK;
                    target.Parent.Parent.Color = NodeColor.RED;
                    leftRotate(target.Parent.Parent);
                }
            }
        }
        this.Root.Color = NodeColor.BLACK;
    }
    /**
     * 往红黑树中插入一个值，如果这个值已经存在，就不管了
     * @param data 被插入的数据
     * */
    public void insert(T data){
        /*如果已经存在就不忙活了*/
        if(this.containsKey(data)){
            System.out.println("no need for insert");
            return;
        }

        TreeNode node = new TreeNode(data, NodeColor.RED);
        TreeNode current = Root;//被插入的位置
        TreeNode father_current = NIL;//被插入位置的父节点
        while(current!=NIL){
            System.out.println("cursoring");
            father_current = current;
            if(compare(data, current.Key)<0){
                current = current.LeftChild;
            }
            else{
                current = current.RightChild;
            }
        }
        node.Parent = father_current;
        if(father_current==NIL){//根节点
            System.out.println("111");
            this.Root = node;
        }else if(compare(data, father_current.Key)<0){
            System.out.println("222");
            father_current.LeftChild = node;
        }else{
            System.out.println("333");
            father_current.RightChild = node;
        }
        this.insertFix(node);
    }

    /**
     * 从红黑树中删除一个值，如果没有的话，就什么都不做
     * @param data 被删除的值
     * */
    public void delete(T data){

    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(this.Root);
        while(!queue.isEmpty()){
            TreeNode current_node = queue.pollFirst();
            result.append(current_node.toString());
            if(current_node!= NIL){
                queue.addLast(current_node.LeftChild);
                queue.addLast(current_node.RightChild);
            }
        }
        return result.toString();
    }
}
