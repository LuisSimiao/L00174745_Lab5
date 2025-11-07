package Lab5;
public class RedBlackTester 
{
    public static void main(String[] args) 
    {
        RedBlackTree<Integer> myTree = new RedBlackTree<Integer>();

        myTree.insert(10);
        myTree.insert(5);
        myTree.insert(15);
        myTree.insert(20);

        myTree.preOrderTraversal();
        System.out.println();

    }
}
