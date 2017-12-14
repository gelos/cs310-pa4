import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Collections;
import java.util.AbstractCollection;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.JPanel;

public class Decomposor extends JPanel {
  //
  // TODO: remove Task 2: Get a set of neighboring regions (10%)
  /**
   * Given a disjoint set and a region (defined by its root id), return a list of adjacent regions
   * (again, represented by their root ids) which are DIFFERENT from the parameter region.
   * 
   * @param ds the disjoint set
   * @param root the id of region to find neighbors
   * @return set of adjacent regions (represented by their root ids) sorted by natural order
   */
  private TreeSet<Integer> getNeighborSets(DisjointSets<Pixel> ds, int root) {

    // create the returned data structure with sorting in natural order
    TreeSet<Integer> result = new TreeSet<Integer>();

    // get target set by its root
    Set<Pixel> targetSet = ds.get(root);

    // iterate through targetSet to find all neighbors
    for (Iterator<Pixel> itTS = targetSet.iterator(); itTS.hasNext();) {

      // compute current Pixel and its ID
      Pixel curPixel = itTS.next();

      // Get array of neighbors of current Pixel
      ArrayList<Pixel> arrNeighbors = getNeighbors(curPixel);

      // Iterate thought all neighbors and add their ID to result
      // Ignore neighbors in same root as parameter region root
      for (Pixel neighPixel : arrNeighbors) {
        int neighID = getID(neighPixel);
        int neighRoot = ds.find(neighID);
        if (neighRoot != root) {
          result.add(neighRoot);
        }
      }
    }

    return result;
  }

  /**
   * Compute the similarity between two given regions R1 and R2. Compute the average color, C, of
   * the union of these two regions and compute the sum of the color differences between C and all
   * pixels in R1 and R2.
   * 
   * @param ds the set of Pixel
   * @param root1 the Pixel of root id first region R1
   * @param root2 the Pixel of root id second region R2
   * @return Similarity class
   * @see Decomposor.Similarity
   * @see Decomposor.Pixel
   */
  private Similarity getSimilarity(DisjointSets<Pixel> ds, int root1, int root2) {

    // Get sets by root ID
    Set<Pixel> R1 = ds.get(root1);
    Set<Pixel> R2 = ds.get(root2);

    // Compute average colors for sets
    Color C1 = computeAverageColor(R1);
    Color C2 = computeAverageColor(R2);

    // System.out.println("C1 " + C1 + " C2 " + C2);

    // Compute size
    int sizeR1 = R1.size();
    int sizeR2 = R2.size();
    int sizeSumR1R2 = sizeR1 + sizeR2;

    // Compute average color of R1, R2 union
    int redC = ((C1.getRed() * sizeR1) + (C2.getRed() * sizeR2)) / (sizeSumR1R2);
    int greenC = ((C1.getGreen() * sizeR1) + (C2.getGreen() * sizeR2)) / (sizeSumR1R2);
    int blueC = ((C1.getBlue() * sizeR1) + (C2.getBlue() * sizeR2)) / (sizeSumR1R2);
    Color averageC = new Color(redC, greenC, blueC);

    // Init color distance sum
    int sumSimilarity = 0;

    // Iterate R1 and R2 and compute sum of similarity
    for (Iterator<Pixel> itR1 = R1.iterator(); itR1.hasNext();) {
      Pixel curPixel = itR1.next();
      Color curColor = getColor(curPixel);
      sumSimilarity += getDifference(averageC, curColor);
    }

    for (Iterator<Pixel> itR2 = R2.iterator(); itR2.hasNext();) {
      Pixel curPixel = itR2.next();
      Color curColor = getColor(curPixel);
      sumSimilarity += getDifference(averageC, curColor);
    }

    return new Similarity(sumSimilarity, getPixel(root1), getPixel(root2));
    // TODO: Check This!!!
    //return new Similarity(sumSimilarity / sizeSumR1R2, getPixel(root1), getPixel(root2));
  }

  /**
   * Iteratively merging two adjacent regions with most similar colors until the number of regions
   * is K.
   * 
   * @param K is the number of desired segments
   */
  public void segment(int K) {
    if (K < 2) {
      throw new IllegalArgumentException(
          new String("! Error: K should be greater than 1, current K=" + K));
    }

    int width = this.image.getWidth();
    int height = this.image.getHeight();

    System.out.println("w " + width + " h " + height + " size " + (width * height));

    // Init and fill disjoint set with image Pixels
    ArrayList<Pixel> data = new ArrayList<Pixel>();
    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        data.add(new Pixel(w, h));
      }
    }
    this.ds = new DisjointSets(data);

    System.out.println("ds.size " + ds.getNumSets());

    // Create and fill priority queue
    PriorityQueue<Similarity> priorityQueue = new PriorityQueue<Similarity>();

    // Iterate all pixels in data to get all pars of adjacent pixels
    for (Pixel pixel : data) {

      // Get root of current pixel
      // int pixelRoot = ds.find(getID(pixel));
      int pixelRoot = getID(pixel);

      // Get ordered set of neighbors of region of the pixel
      TreeSet<Integer> neighborsRoot = getNeighborSets(this.ds, pixelRoot);

      // Iterate thought neighbors
      for (Integer neighborRoot : neighborsRoot) {

        // Fill similarity with root values
        priorityQueue.add(getSimilarity(this.ds, pixelRoot, neighborRoot));

      }
    }

    //System.out.println("priorityQueue.size " + priorityQueue.size());

    // loop while number of regions not reduced to K
    //while (ds.getNumSets() >= K) {
    for(;;) {

      // Get smallest Similarity
      Similarity minSim = priorityQueue.remove();
      
      System.out.println("Disjoint set size " + ds.getNumSets() + " priorityQueue.size " + priorityQueue.size());
      
      // Get color distance
      int colDistance = minSim.distance;
      
      // Get root IDs for adjacent Pixels
      int pixelID1 = getID(minSim.pixels.p);
      int pixelID2 = getID(minSim.pixels.q);
      int root1 = ds.find(pixelID1);
      int root2 = ds.find(pixelID2);
      // int root1 = getID(minSim.pixels.p);
      // int root2 = getID(minSim.pixels.q);

      System.out.println("root1 " + root1 + " pixelID1 " + pixelID1 + " root2 " + root2 + 
           " pixelID2 " + pixelID2 + " dist " + colDistance);
      
      /*
       * if (colDistance != 0) { System.out.println("distance " + colDistance + " root1 " + root1 +
       * " root2 " + root2); }
       */

      // Check if its in same region ignore this pair
      if (root1 == root2) {
        // priorityQueue.remove();
        continue;
      }

      // Check if pixelID1 or pixelID2 not equals to their root IDs
      if ((pixelID1 != root1) || (pixelID2 != root2)) {

        if (colDistance > 0) { // Regions are not identical

          // Add back to the priorityQueue with their root IDs
          priorityQueue.add(new Similarity(colDistance, getPixel(root1), getPixel(root2)));

        } /*else { // Regions are identical

          // Union two regions with identical similarity
          ds.union(root1, root2);

        } */
      } else { // pixelID1 = root1 && pixel2ID2 = root2

        // Compute actual similarity
        Similarity actualSim = getSimilarity(this.ds, root1, root2);

        // Gets actual color distance
        int colDistanceAct = actualSim.distance;

        System.out.println("colDistance " + colDistance + " colDistanceAct " + colDistanceAct);
                
        // Compare actual and queue color distances, if its not equal ignore pair
        if (colDistance != colDistanceAct) {
          continue;
        }

        // exit from while if number of segments = K
        if (ds.getNumSets() == K) {
          break;
        }
        
        // Union two regions with minimal similarity
        int pixelRoot = ds.union(root1, root2);

        // If current distance not zero
       // if (colDistanceAct != 0) {

          // Add to queue similarity for all neighbors of new region root

          /*
           * // Get new region Set<Pixel> R = ds.get(root);
           * 
           * // Iterate region for (Iterator<Pixel> it = R.iterator(); it.hasNext(); ) {
           * 
           * Pixel pixel = it.next();
           * 
           * // Get root of current pixel int pixelRoot = root;
           * 
           */
          // Get ordered set of neighbors of region of the pixel
          TreeSet<Integer> neighborsRoot = getNeighborSets(this.ds, pixelRoot);

          // Iterate thought neighbors
          for (Integer neighborRoot : neighborsRoot) {

            // Fill similarity with root values
            priorityQueue.add(getSimilarity(this.ds, pixelRoot, neighborRoot));

          }
          // }

        //}

      }

    }

    // Todo: Your code here (remove this line)

    // Hint: the algorithm is not fast and you are processing many pixels
    // (e.g., 10,000 pixel for a small 100 by 100 image)
    // output a "." every 100 unions so you get some progress updates.

  }

  // Task 5: Output results (10%)
  // Recolor all pixels with the average color and save output image
  /**
   * Recolor all pixels with the average color and save output image
   * 
   * @param K is the number of desired segments
   */
  public void outputResults(int K) {
    // collect all sets
    int region_counter = 1;

    // create and fill list to store region parameters pairs <size, root id>
    ArrayList<Pair<Integer>> sorted_regions = new ArrayList<Pair<Integer>>();

    int sum = 0;

    int width = this.image.getWidth();
    int height = this.image.getHeight();
    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        int id = getID(new Pixel(w, h));
        int parentID = ds.find(id);
        if (id != parentID) { // ignore non root elements
          continue;
        }
        sorted_regions.add(new Pair<Integer>(ds.get(parentID).size(), parentID));
        sum += ds.get(parentID).size();
      } // end for w
    } // end for h

    System.out.println("Item count " + sum);

    // sort the regions first by size if size equal sort by rootid
    Collections.sort(sorted_regions, new Comparator<Pair<Integer>>() {
      @Override
      public int compare(Pair<Integer> a, Pair<Integer> b) {
        if (a.p != b.p)
          return b.p - a.p;
        else
          return b.q - a.q;
      }
    });

    // Recolors and output region info

    // TODO: Your code here (remove this line)
    // Hint: Use image.setRGB(x,y,c.getRGB()) to change the color of a pixel (x,y) to the given
    // color "c"
    for (Iterator<Pair<Integer>> it = sorted_regions.iterator(); it.hasNext();) {

      // get current region size, average color and root ID
      Pair<Integer> curPair = it.next();
      int curRoot = curPair.q;
      int curSize = curPair.p;
      Set<Pixel> curRegion = ds.get(curRoot);
      Color avgColor = computeAverageColor(curRegion);

      // output region information
      System.out.println(
          "region " + region_counter + " size = " + curSize + " color = " + avgColor.toString());
      region_counter++;

      // recolor image in current region
      for (Iterator<Pixel> itR = curRegion.iterator(); itR.hasNext();) {
        Pixel curPixel = itR.next();
        this.image.setRGB(curPixel.p, curPixel.q, avgColor.getRGB());
      }

    }

    // save output image
    String out_filename = img_filename + "_seg_" + K + ".png";
    try {
      File ouptut = new File(out_filename);
      ImageIO.write(this.image, "png", ouptut);
      System.err.println("- Saved result to " + out_filename);
    } catch (Exception e) {
      System.err.println("! Error: Failed to save image to " + out_filename);
    }
  }

  // -----------------------------------------------------------------------
  //
  //
  // Todo: Read and provide comments, but do not change the following code
  //
  //
  // -----------------------------------------------------------------------

  //
  // Data
  //
  public BufferedImage image; // this is the 2D array of RGB pixels
  private String img_filename; // input image filename without .jpg or .png
  private DisjointSets<Pixel> ds; // the disjoint set

  /**
   * Constructor, read image from the file
   * 
   * @param imgfile the path to the file
   */
  public Decomposor(String imgfile) {
    File imageFile = new File(imgfile);
    try {
      this.image = ImageIO.read(imageFile);
    } catch (IOException e) {
      System.err.println("! Error: Failed to read " + imgfile + ", error msg: " + e);
      return;
    }
    this.img_filename = imgfile.substring(0, imgfile.lastIndexOf('.')); // remember the filename
  }


  //
  // Private classes below
  //

  /**
   * Base class to store a couple of objects
   */
  private static class Pair<T> {
    public Pair(T p_, T q_) {
      this.p = p_;
      this.q = q_;
    }

    T p, q;
  }

  /**
   * Class to store 2D coordinate (w,h) in an image
   */
  public static class Pixel extends Pair<Integer> {
    public Pixel(int w, int h) {
      super(w, h);
    }

  }

  /**
   * This class represents the similarity between the colors of two adjacent pixels or regions
   *
   */
  private class Similarity implements Comparable<Similarity> {
    public Similarity(int d, Pixel p, Pixel q) {
      this.distance = d;
      this.pixels = new Pair<Pixel>(p, q);
    }

    public int compareTo(Similarity other) {
      return this.distance - other.distance;
    }

    // a pair of adjacent pixels or regions (represented by the "root" pixels)
    public Pair<Pixel> pixels;

    // distance between the color of two pixels or two regions,
    // smaller distance indicates higher similarity
    public int distance;
  }

  //
  // Helper functions
  //

  /**
   * Convert a Pixel to an ID
   * 
   * @param pixel the Pixel object
   * @return pixel id
   */
  private int getID(Pixel pixel) {
    return this.image.getWidth() * pixel.q + pixel.p;
  }

  // convert ID back to pixel
  /**
   * Convert ID back to pixel
   * 
   * @param id the Pixel ID
   * @return Pixel object
   * @see Pixel
   */
  private Pixel getPixel(int id) {
    int h = id / this.image.getWidth();
    int w = id - this.image.getWidth() * h;

    if (h < 0 || h >= this.image.getHeight() || w < 0 || w >= this.image.getWidth())
      throw new ArrayIndexOutOfBoundsException();

    return new Pixel(w, h);
  }

  /**
   * Return color for given pixel
   * 
   * @param p the Pixel object
   * @return Color object
   * @see Pixel
   * @see Color
   */
  private Color getColor(Pixel p) {
    return new Color(image.getRGB(p.p, p.q));
  }

  /**
   * Compute the average color of a collection of pixels
   * 
   * @param pixels the pixels collection
   * @return average color
   */
  private Color computeAverageColor(AbstractCollection<Pixel> pixels) {
    int r = 0, g = 0, b = 0;
    for (Pixel p : pixels) {
      Color c = new Color(image.getRGB(p.p, p.q));
      r += c.getRed();
      g += c.getGreen();
      b += c.getBlue();
    }
    return new Color(r / pixels.size(), g / pixels.size(), b / pixels.size());
  }

  /**
   * Compute difference between two given colors. Difference compute as scalar multiplication of
   * vector of color differences
   * 
   * @param c1 the color1
   * @param c2 the color2
   * @return color difference
   */
  private int getDifference(Color c1, Color c2) {
    int r = (int) (c1.getRed() - c2.getRed());
    int g = (int) (c1.getGreen() - c2.getGreen());
    int b = (int) (c1.getBlue() - c2.getBlue());

    return r * r + g * g + b * b;
  }

  /**
   * Return array of 8-neighbors of a given pixel
   * 
   * @param pixel the Pixel object
   * @return arraylist of neighbors as Pixel objects
   */
  private ArrayList<Pixel> getNeighbors(Pixel pixel) {
    ArrayList<Pixel> neighbors = new ArrayList<Pixel>();

    for (int i = -1; i <= 1; i++) {
      int n_w = pixel.p + i;
      if (n_w < 0 || n_w == this.image.getWidth())
        continue;
      for (int j = -1; j <= 1; j++) {
        int n_h = pixel.q + j;
        if (n_h < 0 || n_h == this.image.getHeight())
          continue;
        if (i == 0 && j == 0)
          continue;
        neighbors.add(new Pixel(n_w, n_h));
      } // end for j
    } // end for i

    return neighbors;
  }

  //
  // JPanel function
  //
  public void paint(Graphics g) {
    g.drawImage(this.image, 0, 0, this);
  }
}
