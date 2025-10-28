// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 */
public class AvlTree<AnyType extends Comparable<? super AnyType>> {

    /** The tree root. */
    private AvlNode<AnyType> root;

    /** Construct the tree. */
    public AvlTree() {
        root = null;
    }

    /** Insert into the tree; duplicates are ignored. */
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    /** Remove from the tree. Nothing is done if x is not found. */
    public void remove(AnyType x) {
        root = remove(x, root);
    }

    /** Internal method to remove from a subtree. */
    private AvlNode<AnyType> remove(AnyType x, AvlNode<AnyType> t) {
        if (t == null) return null;

        int cmp = x.compareTo(t.element);
        if (cmp < 0) {
            t.left = remove(x, t.left);
        } else if (cmp > 0) {
            t.right = remove(x, t.right);
        } else {
            // Found node to remove
            if (t.left != null && t.right != null) {
                // Two children: replace with min of right subtree
                AvlNode<AnyType> min = findMin(t.right);
                t.element = min.element;
                t.right = remove(t.element, t.right);
            } else {
                // One or zero child
                t = (t.left != null) ? t.left : t.right;
            }
        }
        return balance(t);
    }

    /** Find the smallest item in the tree. */
    public AnyType findMin() {
        if (isEmpty())
            throw new UnderflowException();
        return findMin(root).element;
    }

    /** Find the largest item in the tree. */
    public AnyType findMax() {
        if (isEmpty())
            throw new UnderflowException();
        return findMax(root).element;
    }

    /** Find an item in the tree. */
    public boolean contains(AnyType x) {
        return contains(x, root);
    }

    /** Make the tree logically empty. */
    public void makeEmpty() {
        root = null;
    }

    /** Test if the tree is logically empty. */
    public boolean isEmpty() {
        return root == null;
    }

    /** Print the tree contents in sorted order. */
    public void printTree() {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced
    private AvlNode<AnyType> balance(AvlNode<AnyType> t) {
        if (t == null) return null;

        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE) {
            if (height(t.left.left) >= height(t.left.right))
                t = rotateWithLeftChild(t);      // LL
            else
                t = doubleWithLeftChild(t);      // LR
        } else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE) {
            if (height(t.right.right) >= height(t.right.left))
                t = rotateWithRightChild(t);     // RR
            else
                t = doubleWithRightChild(t);     // RL
        }

        // Update height before returning
        if (t != null)
            t.height = Math.max(height(t.left), height(t.right)) + 1;

        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> t) {
        if (t == null)
            return -1;

        int hl = checkBalance(t.left);
        int hr = checkBalance(t.right);
        if (Math.abs(height(t.left) - height(t.right)) > 1 ||
                height(t.left) != hl || height(t.right) != hr)
            System.out.println("OOPS!!");

        return height(t);
    }

    /** Internal method to insert into a subtree. */
    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t) {
        if (t == null) return new AvlNode<>(x);

        int cmp = x.compareTo(t.element);
        if (cmp < 0) {
            t.left = insert(x, t.left);
        } else if (cmp > 0) {
            t.right = insert(x, t.right);
        } else {
            // Duplicate; do nothing
            return t;
        }
        return balance(t);
    }

    /** Internal method to find the smallest item in a subtree. */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t) {
        if (t == null) return null;
        while (t.left != null) t = t.left;
        return t;
    }

    /** Internal method to find the largest item in a subtree. */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t) {
        if (t == null) return null;
        while (t.right != null) t = t.right;
        return t;
    }

    /** Internal method to find an item in a subtree. */
    private boolean contains(AnyType x, AvlNode<AnyType> t) {
        if (t == null) return false;
        int cmp = x.compareTo(t.element);
        if (cmp < 0) return contains(x, t.left);
        if (cmp > 0) return contains(x, t.right);
        return true;
    }

    /** Internal method to print a subtree in (sorted) order. */
    private void printTree(AvlNode<AnyType> t) {
        if (t == null) return;
        printTree(t.left);
        System.out.println(t.element);
        printTree(t.right);
    }

    /** Return the height of node t, or -1, if null. */
    private int height(AvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    /** Single rotation with left child (LL). */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2) {
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        // Update heights: child first, then parent
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        return k1;
    }

    /** Single rotation with right child (RR). */
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k1) {
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        // Update heights
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        return k2;
    }

    /** Double rotation (LR): left child with its right, then node with new left. */
    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /** Double rotation (RL): right child with its left, then node with new right. */
    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    /** AVL node. */
    private static class AvlNode<AnyType> {
        AnyType element;
        AvlNode<AnyType> left;
        AvlNode<AnyType> right;
        int height;

        AvlNode(AnyType theElement) {
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
            height = 0; // with height(null) = -1, a leaf is height 0
        }
    }
}
