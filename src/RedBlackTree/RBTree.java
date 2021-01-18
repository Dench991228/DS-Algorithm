package RedBlackTree;

import java.util.LinkedList;

public class RBTree {
    private TreeNode Root;
    public RBTree(){
        this.Root = TreeNode.NIL;
    }
    /**
     * 在一个节点x处左旋，把x的左子节点变成x的父节点，x变成其右子节点
     * @param x 被左旋的节点
     **/
    private void leftRotate(TreeNode x){
        TreeNode y = x.getRightChild();//新的核心点
        x.setRightChild(y.getLeftChild());//y的左子树变成x的右子树
        if(y.getLeftChild()!=TreeNode.NIL){
            y.getLeftChild().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent()==TreeNode.NIL){//x是根节点
            this.Root = y;
        }
        else if(x==x.getParent().getLeftChild()){//x是父节点的左子树
            x.getParent().setLeftChild(y);
        }
        else x.getParent().setRightChild(y);
        y.setLeftChild(x);
        x.setParent(y);
    }
    /*右旋*/
    private void rightRotate(TreeNode y){
        TreeNode x = y.getLeftChild();

        y.setLeftChild(x.getRightChild());
        if(x.getRightChild()!=TreeNode.NIL){
            x.getRightChild().setParent(y);
        }

        x.setParent(y.getParent());
        if(y.getParent()==TreeNode.NIL){
            this.Root = x;
        }
        else if(y==y.getParent().getLeftChild()){
            y.getParent().setLeftChild(x);
        }
        else y.getParent().setRightChild(x);

        x.setRightChild(y);
        y.setParent(x);
    }
    /*插入之后的修复工作*/
    private void fix(TreeNode z){
        while(z.getParent().isRed()){
            if(z.getParent() == z.getParent().getParent().getLeftChild()){//z的父节点是祖父节点的左孩子
                TreeNode y = z.getParent().getParent().getRightChild();//z的叔叔节点
                if(y.isRed()){//叔叔节点是红色，把叔叔和爸爸都变成黑色，爷爷变成红色，然后向上两格
                    z.getParent().setRed(false);
                    y.setRed(false);
                    z.getParent().getParent().setRed(true);
                    z = z.getParent().getParent();
                }
                else{
                    if(z == z.getParent().getRightChild()){//z是它爹的右孩子，转换成是左孩子的情况
                        z = z.getParent();
                        leftRotate(z);
                    }
                    //把父节点涂成黑色，祖父节点变成红色，随后在祖父节点右转
                    z.getParent().setRed(false);
                    z.getParent().getParent().setRed(true);
                    rightRotate(z.getParent().getParent());
                }
            }
            else{
                TreeNode y = z.getParent().getParent().getLeftChild();//z的叔叔节点
                if(y.isRed()){//同上，叔叔节点是红色，就和父节点一起变成黑色，爷爷变成红色，向上两格
                    z.getParent().setRed(false);
                    y.setRed(false);
                    z.getParent().getParent().setRed(true);
                    z = z.getParent().getParent();
                }
                else{
                    if(z == z.getParent().getLeftChild()){
                        z = z.getParent();
                        rightRotate(z);
                    }
                    z.getParent().setRed(false);
                    z.getParent().getParent().setRed(true);
                    leftRotate(z.getParent().getParent());
                }
            }
        }
        this.Root.setRed(false);
    }
    /*插入一个节点*/
    public void insertNode(int value){
        TreeNode new_node = new TreeNode(TreeNode.NIL, value, true);//一会要插入的节点
        TreeNode y = TreeNode.NIL;//new_node的未来插入点的父节点
        TreeNode x = Root;//当前的根节点，用来不断向下寻找合适的插入点
        while(x!=TreeNode.NIL){
            y = x;
            if(x.getValue()>value){
                x = x.getLeftChild();
            }
            else{
                x = x.getRightChild();
            }
        }
        new_node.setParent(y);
        if(y==TreeNode.NIL){//new_node是第一个节点
            this.Root = new_node;
        }
        else if(value<y.getValue()){
            y.setLeftChild(new_node);
        }
        else y.setRightChild(new_node);
        fix(new_node);
    }
    /*看一颗子树中有没有一个特定的值*/
    private TreeNode containsValueSubTree(int value, TreeNode root){
        if(root.getValue() == value)return root;
        else if(value<root.getValue()){
            if(root.getLeftChild()==TreeNode.NIL)return null;
            else return containsValueSubTree(value, root.getLeftChild());
        }
        else{
            if(root.getRightChild()==TreeNode.NIL)return null;
            else return containsValueSubTree(value, root.getRightChild());
        }
    }
    /*寻找是否包含一个值*/
    public TreeNode containsValue(int value){
        if(this.Root == TreeNode.NIL)return null;
        if(value == Root.getValue())return this.Root;
        else if(value < Root.getValue()&&Root.getLeftChild()!=TreeNode.NIL)return containsValueSubTree(value, Root.getLeftChild());
        else {
            if(Root.getRightChild()!=TreeNode.NIL)return containsValueSubTree(value, Root.getRightChild());
            else return null;
        }
    }
    /*删除一个值*/
    public void delete(int value){
        TreeNode target = containsValue(value);
        
    }
    /*删除一个节点之后如果破坏了原有的结构，那就需要修复这棵树*/
    private void fixUp(){

    }
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(this.Root);
        while(!queue.isEmpty()){
            TreeNode current_node = queue.pollFirst();
            result.append(current_node.toString());
            if(current_node!=TreeNode.NIL){
                queue.addLast(current_node.getLeftChild());
                queue.addLast(current_node.getRightChild());
            }
        }
        return result.toString();
    }
}
