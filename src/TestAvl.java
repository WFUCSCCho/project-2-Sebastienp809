
public class TestAvl {
    // Test program
    public static void main( String [ ] args ) {
        AvlTree<Integer> t = new AvlTree<>();
        final int SMALL = 40;
        final int NUMS = 1000000;  // must be even
        final int GAP = 37;

        System.out.println("Checking... (no more output means success)");

        // inserts sequence of numbers "GAP" apart into AVL Tree
        for (int i = GAP; i != 0; i = (i + GAP) % NUMS) {
            //    System.out.println( "INSERT: " + i );
            t.insert(i);
            // checks the balance of the tree after insertion
            if (NUMS < SMALL)
                t.checkBalance();
        }

        // removes odd numbers
        for (int i = 1; i < NUMS; i += 2) {
            //   System.out.println( "REMOVE: " + i );
            t.remove(i);

            if (NUMS < SMALL)
                t.checkBalance();
        }
        // prints the AVL Tree
        if (NUMS < SMALL)
            t.printTree();

        // checks to see that the minimum value is 2 and the maximum value is NUMS - 2
        if (t.findMin() != 2 || t.findMax() != NUMS - 2)
            System.out.println("FindMin or FindMax error!");

        // checks to see if the AVL Tree contains even numbers (it should!)
        for (int i = 2; i < NUMS; i += 2)
            if (!t.contains(i))
                System.out.println("Find error1!");

        // checks to see if the AVL Tree contains odd numbers (it shouldn't!)
        for (int i = 1; i < NUMS; i += 2) {
            if (t.contains(i))
                System.out.println("Find error2!");
        }
    }
}
