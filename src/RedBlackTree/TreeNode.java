package RedBlackTree;

public class TreeNode {
    private int Value;
    private boolean Red;
    private TreeNode Parent;
    private TreeNode LeftChild;
    private TreeNode RightChild;
    public static final TreeNode NIL = new TreeNode();

    public TreeNode(){

    }

    public TreeNode(TreeNode n, int value, boolean red){
        this.Parent = n;
        this.Value = value;
        this.Red = red;//true是红色，false是黑色
        this.LeftChild = NIL;
        this.RightChild = NIL;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    public boolean isRed() {
        return Red;
    }

    public void setRed(boolean red) {
        Red = red;
    }

    public TreeNode getParent() {
        return Parent;
    }

    public void setParent(TreeNode parent) {
        Parent = parent;
    }

    public TreeNode getLeftChild() {
        return LeftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        LeftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return RightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        RightChild = rightChild;
    }

    @Override
    public String toString(){
        if(this!=NIL)return "("+this.Value+","+this.Red +")";
        else return "(null)";
    }
}
