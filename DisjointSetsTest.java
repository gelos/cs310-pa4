import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DisjointSetsTest<Pixel> {

  private DisjointSets<Pixel> classToTest;
  private final int WIDTH = 100;
  private final int HEIGHT = 100;

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

      // random generate two root
      int root1 = rnd.nextInt(HEIGHT * WIDTH);
      int root2 = 0;
      do {
        root2 = rnd.nextInt(HEIGHT * WIDTH);
      } while (root2 == root1);

      // save sets
      Set<Pixel> set1 = classToTest.get(root1);
      Set<Pixel> set2 = classToTest.get(root2);
      
      // save previous size
      int prevSize = classToTest.getNumSets();
      
      // union sets with generated indexes
      int root = classToTest.union(root1, root2);
      
      // check to current size
      assertTrue(root1==root2?true:prevSize-1==classToTest.getNumSets());
      
      // check root for all members of union
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
  final void testGet() {
    fail("Not yet implemented"); // TODO
  }



}
