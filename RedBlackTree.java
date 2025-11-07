package Lab5;
/*
 *  L00174745
 *  Luis Felipe Eleoterio Simiao
 *  Games Development 4th Year
 * 	CA1 - Lab 5
 */
public class RedBlackTree<T extends Comparable<T>> {
	// Root node of the tree
	protected Node root;
	// Used for Red-Black balancing (not always needed outside balancing logic)
	protected Node parent;

	// Insert a value into the tree and rebalance as needed
	public void insert(T value){
		Node node = new Node(value); // New nodes are always inserted as red

		// Special case: if the tree is empty, set root and make it black
		if ( root == null ) {
			root = node;
			node.nodeColourRed = false; // Root must always be black
			return;
		}

		// Insert recursively as in a standard BST
		insertRec(root, node);
		// Fix any Red-Black property violations
		handleRedBlack(node);
	}

	// Recursive helper for insertion, sets parent references
	protected void insertRec(Node subTreeRoot, Node node){
		if ( node.value.compareTo(subTreeRoot.value) < 0){
			if ( subTreeRoot.left == null ){
				subTreeRoot.left = node;
				node.parent = subTreeRoot;
				return;
			}
			else{
				insertRec(subTreeRoot.left, node);
			}
		}
		else{
			if (subTreeRoot.right == null){
				subTreeRoot.right = node;
				node.parent = subTreeRoot;
				return;
			}
			else{
				insertRec(subTreeRoot.right, node);
			}
		}
	}
	
	// In-order traversal: left, node, right
	public void inOrderTraversal() {
		recInOrderTraversal(root);
	}
	
	// Pre-order traversal: node, left, right
	public void preOrderTraversal() {
		recPreOrderTraversal(root);
	}
	
	// Post-order traversal: left, right, node
	public void postOrderTraversal() {
		recPostOrderTraversal(root);
	}
	
	// Recursive in-order traversal
	protected void recInOrderTraversal(Node subTreeRoot) {
		if(subTreeRoot == null) return;
		recInOrderTraversal(subTreeRoot.left);
		processNode(subTreeRoot);
		recInOrderTraversal(subTreeRoot.right);
	}
	
	// Recursive pre-order traversal
	protected void recPreOrderTraversal (Node subTreeRoot) {
		if(subTreeRoot == null) return;
		processNode(subTreeRoot);
		recPreOrderTraversal(subTreeRoot.left);
		recPreOrderTraversal(subTreeRoot.right);
	}
	
	// Recursive post-order traversal
	protected void recPostOrderTraversal (Node subTreeRoot) {
		if(subTreeRoot == null) return;
		recPostOrderTraversal(subTreeRoot.left);
		recPostOrderTraversal(subTreeRoot.right);
		processNode(subTreeRoot);
	}
	
	// Print a node's value (can be customized for other processing)
	protected void processNode(Node currNode) {
		System.out.println(currNode.toString());
	}
	
	// Count all nodes in the tree
	public int countNodes() {
		return recCountNodes(root);
	}
	
	// Recursive node counter
	protected int recCountNodes(Node subTreeRoot) {
		if (subTreeRoot == null) return 0;
		return 1 + recCountNodes(subTreeRoot.left) + recCountNodes(subTreeRoot.right);
	}

    // Find the maximum value in the tree
    public T findMaximum() {
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    // Find the minimum value in the tree
    public T findMinimum() {
        return recFindMinimum(root);
    }

    // Recursive minimum finder
    private T recFindMinimum(Node subTreeRoot) {
        if (subTreeRoot == null) return null;
        if (subTreeRoot.left == null) return subTreeRoot.value;
        return recFindMinimum(subTreeRoot.left);
    }
    
    // Find a value in the tree
    public T find(T searchVal) {
        return recFind(root, searchVal);
    }

    // Recursive search
    private T recFind(Node subTreeRoot, T searchVal) {
        if (subTreeRoot == null) return null;
        int cmp = searchVal.compareTo(subTreeRoot.value);
        if (cmp == 0) {
            return subTreeRoot.value;
        } else if (cmp < 0) {
            return recFind(subTreeRoot.left, searchVal);
        } else {
            return recFind(subTreeRoot.right, searchVal);
        }
    }
	
	// Alternate node counter
	public int nodeCounter() {
		return recNodeCounter(root);
	}

	private int recNodeCounter(Node subTreeRoot) {
		if (subTreeRoot == null) return 0;
		return 1 + recNodeCounter(subTreeRoot.left) + recNodeCounter(subTreeRoot.right);
	}

    /////////////////////////////////////////////////////////////////
    /// LAB 2 - ROTATIONS
    /////////////////////////////////////////////////////////////////

	// Rotate the whole tree left
	public void rotateTreeLeft() 
	{
		root = rotateSubTreeLeft(root);
		root.right = rotateSubTreeLeft(root.right);
	}

	// Left rotation for a subtree
	public Node rotateSubTreeLeft(Node subTreeRoot) 
	{
		Node pivot = subTreeRoot.right;
		Node temp = pivot.left;
		pivot.left = subTreeRoot;
		subTreeRoot.right = temp;
		return pivot;
	}

	// Rotate the whole tree right
	public void rotateTreeRight() 
	{
		root = rotateSubTreeRight(root);
		root.left = rotateSubTreeRight(root.left);
	}

	// Right rotation for a subtree
	public Node rotateSubTreeRight(Node subTreeRoot) 
	{
		Node pivot = subTreeRoot.left;
		Node temp = pivot.right;
		pivot.right = subTreeRoot;
		subTreeRoot.left = temp;
		return pivot;
	}
	
    ///////////////////////////////////////////////////////
    /// LAB 3 - RED BLACK TREE METHODS
    ///////////////////////////////////////////////////////

	// Fix Red-Black property violations after insertion
	public void handleRedBlack(Node newNode) 
	{
		// If newNode is root, just make it black
		if(newNode == root) 
		{
			newNode.nodeColourRed = false;
			return;
		}

		Node uncle;
		Node parent = newNode.parent;
		Node grandParent = parent.parent;
		
		// If parent is red, we have a violation
		if(parent.nodeColourRed) {
			// Find the uncle (sibling of parent)
			if(uncleOnRightTree(newNode)) //uncle is to the right
			{ 
				uncle = getRightUncle(newNode);
			}
			else //uncle is to the left
			{ 
				uncle = getLeftUncle(newNode);
			}
			// Check if x's uncle is red. Grandparent must be black
			if((uncle != null) && (uncle.nodeColourRed)) //if uncle is red
			{
				parent.nodeColourRed = false; //set parent to black
				uncle.nodeColourRed = false; //set uncle to black
				grandParent.nodeColourRed = true; //set grandparent to red
				handleRedBlack(grandParent); //recurse up the tree
			}
			////////////////////////////////////////////////////
			/// Lab 4 - Black Uncle Scenarios
			////////////////////////////////////////////////////
			
			else if((uncle == null) || !uncle.nodeColourRed) //if uncle is black, check left-left, left-right, right-left and right-right situation
			{
				Node inSubtreeParent = grandParent.parent;
				//System.out.println("Uncle is black"); //test
				if(grandParent.left == parent && parent.left == newNode) //check if situation is a left-left case
				{
					//if its a left-left case, apply it
					applyLeftLeftCase(inSubtreeParent, grandParent, parent);
				}
				
				else if(grandParent.right == parent && parent.right == newNode) //check if situation is a right-right case
				{
					//if its a left-left case, apply it
					applyRightRightCase(inSubtreeParent, grandParent, parent);
				}
				////////////////////////////////////////////////
				/// LAB 5 - Continuing Black Uncle Scenarios
				////////////////////////////////////////////////
				else if(grandParent.left == parent && parent.right == newNode) //check if situation is a LR case
				{
					//call method to do leftRotation and assign it to grandparent's left reference
					grandParent.left = rotateSubTreeLeft(parent);
					//apply right-right case
					applyLeftLeftCase(inSubtreeParent, grandParent, parent);
				}
					
				else if(grandParent.right == parent && parent.left == newNode) //check if situation is a RL case
				{
					//call method to do rightRotation and assign it to grandparent's right reference
					grandParent.right = rotateSubTreeRight(parent);
					//apply right-right case
					applyRightRightCase(inSubtreeParent, grandParent, parent);
				}
			}
		}
	}

	// LAB 3 FUNCTIONS
	// Returns true if the uncle is on the right side of the grandparent
	public boolean uncleOnRightTree(Node newNode) {
		Node parent = newNode.parent;
		Node grandParent = parent.parent;
		if(grandParent.right == parent) {
			return true;
		}
		else {
			return false;
		}
	}

	// Get the right uncle node
	public Node getRightUncle(Node newNode) {
		Node parent = newNode.parent;
		Node grandParent = parent.parent;
		return grandParent.right;
	}

	// Get the left uncle node
	public Node getLeftUncle(Node newNode) {
		Node parent = newNode.parent;
		Node grandParent = parent.parent;
		return grandParent.left;
	}

	// LAB 4/5 FUNCTIONS
	private void applyLeftLeftCase(Node inSubtreeParent, Node inGrandParent, Node inParent)
	{
		//1. Assign color to template
		boolean grandParentColor = inGrandParent.nodeColourRed; //must be false
		boolean parentColor = inParent.nodeColourRed; //this must be true
		boolean temp = parentColor; //temp saves parentColor
				
		//2. swap parent and grandparent colour (black - red)
		inParent.nodeColourRed = grandParentColor;
		inGrandParent.nodeColourRed = temp;
				
		//3. rotate SubTree (makes 2 the new root and 1/3 the child)
		Node subTreeRoot = rotateSubTreeRight(inGrandParent);
				
		//4.  update subtree parent
		if (inSubtreeParent == null) //if its null, the new node is the new root
		{
		     this.root = subTreeRoot;
		}
		        
		else if (inSubtreeParent.right == inGrandParent) //if the right node in subtreeParent is the same as the grandParent
		{
		     inSubtreeParent.right = subTreeRoot; //the node to the right is the new node
		     inParent = inSubtreeParent.right; //update parent of the new node
		}
		        
		else if (inSubtreeParent.left == inGrandParent) //if the left node in subtreeParent is the same as the grandParent
		{
		     inSubtreeParent.left = subTreeRoot; //the node to the left is the new node
		     inParent = inSubtreeParent.left; 
		}
		        
		handleRedBlack(root); //update to make sure root is always black
	}
	
	
	private void applyRightRightCase(Node inSubtreeParent, Node inGrandParent, Node inParent)
	{
		//1. Assign color to template
		boolean grandParentColor = inGrandParent.nodeColourRed; //must be false
		boolean parentColor = inParent.nodeColourRed; //this must be true
		boolean temp = parentColor; //temp saves parentColor
		
		//2. swap parent and grandparent colour (black - red)
		inParent.nodeColourRed = grandParentColor;
		inGrandParent.nodeColourRed = temp;
		
		//3. rotate SubTree (makes 2 the new root and 1/3 the child)
		Node subTreeRoot = rotateSubTreeLeft(inGrandParent);
		
		//4.  update subtree parent
        if (inSubtreeParent == null) //if its null, the new node is the new root
        {
            this.root = subTreeRoot;
        }
        
        else if (inSubtreeParent.right == inGrandParent) //if the right node in subtreeParent is the same as the grandParent
        {
        	inSubtreeParent.right = subTreeRoot; //the node to the right is the new node
        	inParent = inSubtreeParent.right; //update parent of the new node
        }
        
        else if (inSubtreeParent.left == inGrandParent) //if the left node in subtreeParent is the same as the grandParent
        {
        	inSubtreeParent.left = subTreeRoot; //the node to the left is the new node
        	inParent = inSubtreeParent.left; 
        }
        
        handleRedBlack(root); //update to make sure root is always black
	}


    // Node class for Red-Black Tree
	protected class Node {
		public RedBlackTree<T>.Node parent; // Parent node reference
		public boolean nodeColourRed;       // True if node is red, false if black
		public T value;                     // Value stored in the node
		public Node left;                   // Left child
		public Node right;                  // Right child

		public Node(T value) {
			this.value = value;
			this.nodeColourRed = true; // New nodes are red by default
		}

		@Override
		public String toString() {
			return "Node [value=" + value + "]";
		}
	}
}
