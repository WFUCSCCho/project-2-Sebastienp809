/*******************************************************
 * @file: Proj2.java
 * @description: Reads first N rows from a CSV dataset
 *               into ArrayList <Movie>, builds sorted and
 *               shuffled lists, times insert/search on
 *               BST and AVL, prints summary, appends CSV
 *               to output.txt.
 * @author: Sebastien Pierre
 * @date: October 21, 2025
 *******************************************************/
import java.io.*;
import java.util.*;

public class Proj2 {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java Proj2 <dataset-file> <number-of-lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        List<Movie> base = readMovies(inputFileName, numLines);
        if (base.isEmpty()) {
            System.err.println("No rows read. Check file path and N.");
            System.exit(1);
        }

        List<Movie> sorted = new ArrayList<>(base);
        Collections.sort(sorted);
        List<Movie> random = new ArrayList<>(base);
        Collections.shuffle(random);

        // Trees
        BST<Movie> bstSorted = new BST<>();
        BST<Movie> bstRandom = new BST<>();
        AvlTree<Movie> avlSorted = new AvlTree<>();
        AvlTree<Movie> avlRandom = new AvlTree<>();

        // Insert timings
        long bstInsSorted = timeInsertBST(bstSorted, sorted);
        long bstInsRandom = timeInsertBST(bstRandom, random);
        long avlInsSorted = timeInsertAVL(avlSorted, sorted);
        long avlInsRandom = timeInsertAVL(avlRandom, random);

        // Search timings (search using original order)
        long bstFindSorted = timeSearchBST(bstSorted, base);
        long bstFindRandom = timeSearchBST(bstRandom, base);
        long avlFindSorted = timeSearchAVL(avlSorted, base);
        long avlFindRandom = timeSearchAVL(avlRandom, base);

        // Human-readable summary
        printSummary(numLines, bstInsSorted, bstInsRandom, avlInsSorted, avlInsRandom,
                bstFindSorted, bstFindRandom, avlFindSorted, avlFindRandom);

        // CSV append
        appendCsv("output.txt", numLines,
                bstInsSorted, bstInsRandom, avlInsSorted, avlInsRandom,
                bstFindSorted, bstFindRandom, avlFindSorted, avlFindRandom);
    }

    // ---------- dataset reading ----------
    private static List<Movie> readMovies(String path, int limit) {
        List<Movie> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null && list.size() < limit) {
                if (first && looksLikeHeader(line)) { first = false; continue; }
                first = false;
                String[] p = line.split(",");
                if (p.length < 4) continue; // expect: title,year,rating,votes
                String title = p[0].trim();
                int year = safeInt(p[1]);
                double rating = safeDouble(p[2]);
                int votes = safeInt(p[3]);
                list.add(new Movie(title, year, rating, votes));
            }
        } catch (IOException e) {
            System.err.println("Read error: " + e.getMessage());
        }
        return list;
    }
    private static boolean looksLikeHeader(String s) {
        s = s.toLowerCase();
        return s.contains("title") && s.contains("year");
    }
    private static int safeInt(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e){ return 0; } }
    private static double safeDouble(String s){ try { return Double.parseDouble(s.trim()); } catch (Exception e){ return 0.0; } }

    // ---------- timing helpers ----------
    private static long timeInsertBST(BST<Movie> t, List<Movie> data) {
        long start = System.nanoTime();
        for (Movie m : data) t.insert(m);
        return System.nanoTime() - start;
    }
    private static long timeInsertAVL(AvlTree<Movie> t, List<Movie> data) {
        long start = System.nanoTime();
        for (Movie m : data) t.insert(m);
        return System.nanoTime() - start;
    }
    private static long timeSearchBST(BST<Movie> t, List<Movie> data) {
        long start = System.nanoTime();
        int hits = 0;
        for (Movie m : data) if (t.search(m) != null) hits++;
        return System.nanoTime() - start;
    }
    private static long timeSearchAVL(AvlTree<Movie> t, List<Movie> data) {
        long start = System.nanoTime();
        int hits = 0;
        for (Movie m : data) if (t.contains(m)) hits++;
        return System.nanoTime() - start;
    }

    // ---------- output ----------
    private static void printSummary(
            int n,
            long bstInsS, long bstInsR, long avlInsS, long avlInsR,
            long bstFindS, long bstFindR, long avlFindS, long avlFindR) {

        System.out.println("================================");
        System.out.println("N = " + n);
        System.out.println("Insert (nanoseconds):");
        System.out.printf("  BST sorted   : %d%n", bstInsS);
        System.out.printf("  BST shuffled : %d%n", bstInsR);
        System.out.printf("  AVL sorted   : %d%n", avlInsS);
        System.out.printf("  AVL shuffled : %d%n", avlInsR);
        System.out.println("Search (nanoseconds):");
        System.out.printf("  BST (sorted tree)   : %d%n", bstFindS);
        System.out.printf("  BST (shuffled tree) : %d%n", bstFindR);
        System.out.printf("  AVL (sorted tree)   : %d%n", avlFindS);
        System.out.printf("  AVL (shuffled tree) : %d%n", avlFindR);
        System.out.println("================================");
    }

    private static void appendCsv(String file, int n,
                                  long bstInsS, long bstInsR, long avlInsS, long avlInsR,
                                  long bstFindS, long bstFindR, long avlFindS, long avlFindR) {
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            // N,bst_ins_sorted,bst_ins_rand,avl_ins_sorted,avl_ins_rand,bst_find_sorted,bst_find_rand,avl_find_sorted,avl_find_rand
            out.printf(Locale.US, "%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                    n, bstInsS, bstInsR, avlInsS, avlInsR, bstFindS, bstFindR, avlFindS, avlFindR);
        } catch (IOException e) {
            System.err.println("Write error: " + e.getMessage());
        }
    }
}
