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

    /**
     *删除时候的基本操作，把新的节点下面的东西放到删除掉的节点上
     * @param sacrifice 被删除掉的节点
     * @param subsidize 占领新位置的节点
     * */
    private void transplant(TreeNode sacrifice, TreeNode subsidize){
        if(sacrifice.getParent()==TreeNode.NIL){//sacrifice是根节点
            this.Root = subsidize;
        }
        else if(sacrifice == sacrifice.getParent().getLeftChild()){//sacrifice是它的父节点的左子节点
            sacrifice.getParent().setLeftChild(subsidize);
        }
        else{//右子节点
            sacrifice.getParent().setRightChild(subsidize);
        }
        subsidize.setParent(sacrifice.getParent());
    }

    /**
     * 删除时候的另一个基本操作，找到以这个节点为根节点的子树的最小值
     * @param subRoot 目标子树树根
     * @return 这个子树门下的最小值
     * */
    private TreeNode minimalNode(TreeNode subRoot){
        TreeNode current = subRoot;
        while(current.getLeftChild()!=TreeNode.NIL){
            current = current.getLeftChild();
        }
        return current;
    }

    /**
     * 删除一个节点之后，可能会破坏原有的结构，因此需要使用这个方法修复树的结构
     * @param problemNode 可能会出问题的节点
     * */
    private void fixUp(TreeNode problemNode){
        while(problemNode!=this.Root&&!problemNode.isRed()){
            if(problemNode == problemNode.getParent().getLeftChild()){//如果它是父节点的左子节点
                TreeNode sibling = problemNode.getParent().getRightChild();
                if(sibling.isRed()){//兄弟节点是红色，那就把兄弟节点变成黑色，把父节点变成红色，然后在父节点处左旋
                    sibling.setRed(false);
                    problemNode.getParent().setRed(true);
                    leftRotate(problemNode.getParent());
                    sibling = problemNode.getParent().getRightChild();
                }

                if(!sibling.getLeftChild().isRed()&&!sibling.getRightChild().isRed()){//x的兄弟节点有两个红色子节点把兄弟节点变成红色，然后自己往上一层
                    sibling.setRed(true);
                    problemNode = problemNode.getParent();
                }
                else{//有一个子节点不是黑色
                    if(!sibling.getRightChild().isRed()){//右节点是黑色：变成右节点不是黑色的情况
                        sibling.getLeftChild().setRed(false);
                        sibling.setRed(true);
                        rightRotate(sibling);
                        sibling = problemNode.getParent().getRightChild();
                    }
                    sibling.setRed(problemNode.getParent().isRed());
                    problemNode.getParent().setRed(false);
                    sibling.getRightChild().setRed(false);
                    leftRotate(problemNode.getParent());
                    problemNode = this.Root;
                }
            }
            else{
                TreeNode sibling = problemNode.getParent().getLeftChild();

                if(sibling.isRed()){//同上，只不过是左兄弟，而且是右旋
                    sibling.setRed(false);
                    problemNode.getParent().setRed(true);
                    rightRotate(problemNode.getParent());
                    sibling = problemNode.getParent().getLeftChild();
                }

                if(!sibling.getLeftChild().isRed()&&!sibling.getRightChild().isRed()){//同上，只不过是左兄弟
                    sibling.setRed(true);
                    problemNode = problemNode.getParent();
                }
                else{//有一个子节点不是黑色
                    if(!sibling.getLeftChild().isRed()){//左节点是黑色：变成左节点不是黑色的情况
                        sibling.getLeftChild().setRed(false);
                        sibling.setRed(true);
                        leftRotate(sibling);
                        sibling = problemNode.getParent().getLeftChild();
                    }
                    sibling.setRed(problemNode.getParent().isRed());
                    problemNode.getParent().setRed(false);
                    sibling.getLeftChild().setRed(false);
                    rightRotate(problemNode.getParent());
                    problemNode = this.Root;
                }
            }
        }
        problemNode.setRed(false);
    }

    /**
     * 删除掉value的节点
     * @param value 被删掉的节点的值
     * */
    public void delete(int value){
        TreeNode target = containsValue(value);//拥有value值的节点
        if(target == TreeNode.NIL)return;//如果不存在是这个值的节点，就直接返回
        TreeNode origin = target;//标记value值最初的位置
        TreeNode problem;//可能会被破坏性质的节点
        boolean origin_color = origin.isRed();
        if(target.getLeftChild()==TreeNode.NIL){//左孩子是空，或者干脆就是没有子节点，根节点到problem下面的节点，黑高度少了1
            problem = target.getRightChild();
            transplant(target, problem);
        }
        else if(target.getRightChild()==TreeNode.NIL){//右孩子是空，problem的问题是一样的
            problem = target.getLeftChild();
            transplant(target, problem);
        }
        else{//有两个子节点
            target = minimalNode(target.getRightChild());//要被诺走的节点
            origin_color = target.isRed();
            problem = target.getRightChild();//可能会出问题是因为最小节点要被拉上去，所以它的右子节点要被拉上来（这个节点必定没有左子节点）
            if(target.getParent() == origin){//如果被删除节点的右子节点没有左子树
                problem.setParent(target);
            }
            else{//被删除的节点的右子树有左子树
                /*先把要挪上去的节点的右子树取代它的位置*/
                transplant(target, target.getRightChild());
                /*把被删除节点右节点子树最小的值放到被删除的节点处*/
                target.setRightChild(origin.getRightChild());
                target.setParent(origin.getParent());
            }
            transplant(origin, target);//对挪上来的节点进行transplant彻底完成挪动工作
            target.setLeftChild(origin.getLeftChild());//把被删除节点的左子树接到被挪上去的节点上
            target.getLeftChild().setParent(target);//把新接上来的左子树的父亲节点设为挪上去的节点
            target.setRed(origin.isRed());
        }
        if(!origin_color){//如果挪上去了一个黑色节点，那就要修复
            fixUp(problem);
        }
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
