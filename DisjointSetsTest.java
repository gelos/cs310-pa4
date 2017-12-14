import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DisjointSetsTest<Pixel> {

  private DisjointSets<Pixel> classToTest;
  private final int WIDTH = 1000;
  private final int HEIGHT = 1000;

  @Test
  @DisplayName("constructor and getNumSet")
  final void testDisjointSets() {

    ArrayList<Pixel> pixelList = new ArrayList<Pixel>();

    for (int h = 0; h < HEIGHT; h++) {
      for (int w = 0; w < WIDTH; w++) {
        // Pixel myPixel = (Pixel) (new Decomposor.Pixel(rnd.nextInt(), rnd.nextInt()));
        Pixel myPixel = (Pixel) (new Decomposor.Pixel(w, h));
        pixelList.add(myPixel);
      }
    }

    classToTest = new DisjointSets<Pixel>(pixelList);
    assertEquals(WIDTH * HEIGHT, classToTest.getNumSets());

  }

  @Test
  @DisplayName("union, get, find test: random union sets, check roots for all members")
  final void testUnion() {

    // init
    testDisjointSets();
    Random rnd = new Random();

    for (int i = 0; i < WIDTH; i++) {

      // get two roots from random generated points
      int root1 = classToTest.find(rnd.nextInt(HEIGHT * WIDTH));
      int root2 = 0;
      do {
        root2 = classToTest.find(rnd.nextInt(HEIGHT * WIDTH));

      } while (root2 == root1);

      // save sets
      Set<Pixel> set1 = classToTest.get(root1);
      Set<Pixel> set2 = classToTest.get(root2);

      // check root for sets
      for (Iterator<Pixel> itR = set1.iterator(); itR.hasNext();) {
        Decomposor.Pixel curPixel = (Decomposor.Pixel) itR.next();
        int pixelID = WIDTH * curPixel.q + curPixel.p;
        assertEquals(root1, classToTest.find(pixelID));
      }
      for (Iterator<Pixel> itR = set2.iterator(); itR.hasNext();) {
        Decomposor.Pixel curPixel = (Decomposor.Pixel) itR.next();
        int pixelID = WIDTH * curPixel.q + curPixel.p;
        assertEquals(root2, classToTest.find(pixelID));
      }

      // save previous size
      int prevSize = classToTest.getNumSets();

      // union sets with generated indexes
      int root = classToTest.union(root1, root2);

      // check what size of sets decreases
      // assertTrue(root1 == root2 ? true : prevSize - 1 == classToTest.getNumSets());
      assertEquals(prevSize - 1, classToTest.getNumSets());

      // check root for all members of union
      Set<Pixel> setUnion = classToTest.get(root);

      // check size of union set
      assertEquals(setUnion.size(), set1.size() + set2.size());

      // check roots for all members in union set
      for (Iterator<Pixel> itR = setUnion.iterator(); itR.hasNext();) {
        Decomposor.Pixel curPixel = (Decomposor.Pixel) itR.next();
        int pixelID = WIDTH * curPixel.q + curPixel.p;
        assertEquals(root, classToTest.find(pixelID));
      }

    }

  }

  @Test
  @DisplayName("testDisjointSets + check Pixel roots")
  final void testFind() {

    testDisjointSets();

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
      assertEquals(i, classToTest.find(i));
    }
  }

  @Test
  @DisplayName("get, disjoint set with one element - one set")
  final void testGet() {

    // init data structure
    testDisjointSets();

    for (int h = 0; h < HEIGHT; h++) {
      for (int w = 0; w < WIDTH; w++) {

        // iterate through DisjointSet
        int root = h * HEIGHT + w;
        Set<Pixel> mySet = classToTest.get(root);

        // check root for all sets
        for (Iterator<Pixel> itR = mySet.iterator(); itR.hasNext();) {
          Decomposor.Pixel curPixel = (Decomposor.Pixel) itR.next();
          int pixelID = WIDTH * curPixel.q + curPixel.p;
          assertEquals(root, classToTest.find(pixelID));
        }
      }
    }

  }

}
