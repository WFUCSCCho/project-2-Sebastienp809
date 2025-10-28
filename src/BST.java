/*******************************************************
 * @file: BST.java
 * @description: Minimal generic Binary Search Tree used
 *               to time against AVL in Project 2.
 *               Ops: insert (ignore dups), search,
 *               contains, size, clear, and a simple
 *               in-order iterator for debugging.
 * @author: Sebastien Pierre
 * @date: October 21, 2025
 *******************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<E extends Comparable<? super E>> implements Iterable<E> {

    //**  node holder */
    private static class Node<T> {
        T elem;
        Node<T> left, right;
        Node(T e) { elem = e; }
    }

    private Node<E> root;
    private int nodecount;

    //** Start empty */
    public BST() { root = null; nodecount = 0; }

    //** Drop all nodes */
    public void clear() { root = null; nodecount = 0; }

    //** Number of elements */
    public int size() { return nodecount; }

    //** Insert; ignore duplicates */
    public void insert(E x) {
        Box b = new Box();
        root = insertRec(root, x, b);
        if (b.added) nodecount++;
    }

    //** Return matching element or null */
    public E search(E key) {
        Node<E> cur = root;
        while (cur != null) {
            int c = key.compareTo(cur.elem);
            if (c == 0) return cur.elem;
            cur = (c < 0) ? cur.left : cur.right;
        }
        return null;
    }

    /** True if key is present */
    public boolean contains(E key) { return search(key) != null; }

    // ---------- internals ----------

    /** Tracks if an insert actually added a node */
    private static class Box { boolean added = false; }

    private Node<E> insertRec(Node<E> t, E x, Box b) {
        if (t == null) { b.added = true; return new Node<>(x); }
        int c = x.compareTo(t.elem);
        if (c < 0)      t.left  = insertRec(t.left,  x, b);
        else if (c > 0) t.right = insertRec(t.right, x, b);
        // equal: do nothing
        return t;
    }

    // ---------- optional: inorder iterator (for quick checks) ----------

    @Override
    public Iterator<E> iterator() { return new InIt(root); }

    private class InIt implements Iterator<E> {
        private final Stack<Node<E>> st = new Stack<>();
        InIt(Node<E> n) { pushLeft(n); }

        private void pushLeft(Node<E> n) {
            while (n != null) { st.push(n); n = n.left; }
        }

        public boolean hasNext() { return !st.isEmpty(); }

        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<E> n = st.pop();
            if (n.right != null) pushLeft(n.right);
            return n.elem;
        }
    }
}
