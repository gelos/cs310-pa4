import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)

class SetTest<Pixel> {

  private Set<Pixel> setTest;
  private Set<Pixel> classToTesting;
  
  @BeforeEach
  void setUp() throws Exception {
    setTest = new Set<Pixel>();
  }

  @AfterEach
  void tearDown() throws Exception {
    setTest = null;
    
  }

  @Test
  void testSize() {
    assertNotNull(setTest);
    assertEquals(0,setTest.size());
    setTest.add((Pixel)(new Decomposor.Pixel(0,0)));
    assertEquals(1,setTest.size());
    setTest.clear();
    assertEquals(0,setTest.size());
  }

  @Test
  void testClear() {
    assertNotNull(setTest);
    for (int i = 0; i < 1000; i++) {
      setTest.add((Pixel)(new Decomposor.Pixel(0,0)));  
    }
    assertEquals(1000,setTest.size());
    setTest.clear();
    assertEquals(0,setTest.size());
  }

  @Test
  void testSetT() {
    Set<Pixel> mySet = new Set<Pixel>((Pixel) new Decomposor.Pixel(0,0));
    assertNotNull(mySet);
    assertEquals(1, mySet.size());
  }

  @Test
  void testSet() {
    Set<Pixel> mySet = new Set<Pixel>();
    assertNotNull(mySet);
    assertEquals(0, mySet.size());
  }

  @Test
  void testAddT() {
    assertNotNull(setTest);
    for (int i = 0; i < 10000; i++) {
      assertTrue(setTest.add((Pixel)(new Decomposor.Pixel(0,0))));  
    }
    assertEquals(10000,setTest.size());
  }

  @Test
  void testAddAllSetOfT() {
    assertNotNull(setTest);
    
    Set<Pixel> mySet = new Set<Pixel>();
    
    Random rnd = new Random();
    LinkedList<Pixel> pixelList = new LinkedList<Pixel>();   
    
    for (int i = 0; i < 10000; i++) {
      Pixel myPixel = (Pixel)(new Decomposor.Pixel(rnd.nextInt(),rnd.nextInt()));
      assertTrue(setTest.add(myPixel));
      pixelList.add(myPixel);
    }
    assertEquals(10000,setTest.size());
        
    assertTrue(mySet.addAll(setTest));
    int i = 0;
    
    for (Iterator<Pixel> itP = mySet.iterator(); itP.hasNext();) {
      Decomposor.Pixel curPixel = (Decomposor.Pixel) itP.next();
      Decomposor.Pixel listPixel = (Decomposor.Pixel) pixelList.get(i++);
      boolean result = ((curPixel.p == listPixel.p) && (curPixel.q == listPixel.q));
      assertTrue(result);
    }
    
  }

}
