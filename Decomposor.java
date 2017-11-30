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

public class Decomposor extends JPanel
{
    //
    // Task 2: Get a set of neighboring regions  (10%)
    //
    // Given a disjoint set and a region (defined by its root id),
    // return a list of adjacent regions (again, represented by their root ids)
    //
    private TreeSet<Integer> getNeightborSets(DisjointSets<Pixel> ds, int root)
    {
      return null; //TODO: remove and replace this line
    }

    //
    // Task 3: Compute region to region similarity (10%)
    //
    // Given two regions R1 and R2, compute the similarity between these two regions
    // You will need to compute the average color, C, of the union of these two regions
    // And compute the sum of the color differences between C and all pixels in R1 and R2
    //
    private Similarity getSimilarity(DisjointSets<Pixel> ds, int root1, int root2)
    {
      
      Color C1 = computeAverageColor(ds.get(root1));
      Color C2 = computeAverageColor(ds.get(root2));
      
      int sizeR1 = ds.get(root1).size();
      int sizeR2 = ds.get(root1).size();
      int sizeSumR1R2 = sizeR1+sizeR2;
      
      int redC = ((C1.getRed() * sizeR1)+(C2.getRed() * sizeR2))/(sizeSumR1R2);
      int greenC = ((C1.getGreen() * sizeR1)+(C2.getGreen() * sizeR2))/(sizeSumR1R2);
      int blueC = ((C1.getBlue() * sizeR1)+(C2.getBlue() * sizeR2))/(sizeSumR1R2);

      Color C = new Color(redC, greenC, blueC);
      
      int sumSimilarity = 0;
      
      Set<Pixel> R1 = ds.get(root1);
      Set<Pixel> R2 = ds.get(root1);
      Iterator<Pixel> itR1 = R1.iterator();
      Iterator<Pixel> itR2 = R2.iterator();
      
      for(itR1.hasNext())
      
      
      return null; //TODO: remove and replace this line
    }

    //
    // Task 4. Imeplement the decomposor (50%)
    //
    // High-level idea
    //
    // - Iteratively merging two adjacent regions with most similar colors until the number of regions is K.
    //
    public void segment(int K) //K is the number of desired segments
    {
      if(K<2)
      {
          throw new IllegalArgumentException(new String("! Error: K should be greater than 1, current K="+K));
      }

      int width = this.image.getWidth();
      int height = this.image.getHeight();

      //Todo: Your code here (remove this line)
      
      //Hint: the algorithm is not fast and you are processing many pixels 
      //      (e.g., 10,000 pixel for a small 100 by 100 image)
      //      output a "." every 100 unions so you get some progress updates.
    }

    //Task 5: Output results (10%)
    //Recolor all pixels with the average color and save output image
    public void outputResults(int K)
    {
        //collect all sets
        int region_counter=1;
        ArrayList<Pair<Integer>> sorted_regions = new ArrayList<Pair<Integer>>();

        int width = this.image.getWidth();
        int height = this.image.getHeight();
        for(int h=0; h<height; h++){
          for(int w=0; w<width; w++){
              int id=getID(new Pixel(w,h));
              int setid=ds.find(id);
              if(id!=setid) continue;
              sorted_regions.add(new Pair<Integer>(ds.get(setid).size(),setid));
          }//end for w
        }//end for h

        //sort the regions
        Collections.sort(sorted_regions, new Comparator<Pair<Integer>>(){
          @Override
          public int compare(Pair<Integer> a, Pair<Integer> b) {
              if(a.p!=b.p) return b.p-a.p;
              else return b.q-a.q;
          }
        });

        //Recolors and output region info
		
      //TODO: Your code here (remove this line)
      //Hint: Use image.setRGB(x,y,c.getRGB()) to change the color of a pixel (x,y) to the given color "c"
        for (int h = 0; h < height; h++) {
          for (int w = 0; w < width; w++) {
            Color c = new Color(255, 255, 255);
            this.image.setRGB(w, h, c.getRGB());
          }
        }
        
      //save output image
      String out_filename = img_filename+"_seg_"+K+".png";
      try
      {
        File ouptut = new File(out_filename);
        ImageIO.write(this.image, "png", ouptut);
        System.err.println("- Saved result to "+out_filename);
      }
      catch (Exception e) {
        System.err.println("! Error: Failed to save image to "+out_filename);
      }
    }

    //-----------------------------------------------------------------------
    //
    //
    // Todo: Read and provide comments, but do not change the following code
    //
    //
    //-----------------------------------------------------------------------

    //
    //Data
    //
    public BufferedImage image;       //this is the 2D array of RGB pixels
    private String img_filename;      //input image filename without .jpg or .png
    private DisjointSets<Pixel> ds;   //the disjoint set

    //
    // constructor, read image from file
    //
    public Decomposor(String imgfile)
    {
      File imageFile = new File(imgfile);
      try
      {
        this.image = ImageIO.read(imageFile);
      }
      catch(IOException e)
      {
        System.err.println("! Error: Failed to read "+imgfile+", error msg: "+e);
        return;
      }
      this.img_filename=imgfile.substring(0, imgfile.lastIndexOf('.')); //remember the filename
    }


    //
    // 3 private classes below
    //
    private class Pair<T>
    {
      public Pair(T p_, T q_){this.p=p_;this.q=q_;}
      T p, q;
    }

    //a pixel is a 2D coordinate (w,h) in an image
    private class Pixel extends Pair<Integer>{ public Pixel(int w, int h){ super(w,h); } } //aliasing Pixel

    //this class represents the similarity between the colors of two adjacent pixels or regions
    private class Similarity implements Comparable<Similarity>
    {
      public Similarity(int d, Pixel p, Pixel q)
      {
        this.distance=d;
        this.pixels=new Pair<Pixel>(p,q);
      }

      public int compareTo( Similarity other )
      {
        return this.distance - other.distance;
      }

      //a pair of ajacent pixels or regions (represented by the "root" pixels)
      public Pair<Pixel> pixels;

      //distance between the color of two pixels or two regions,
      //smaller distance indicates higher similarity
      public int distance;
    }

    //
    // helper functions
    //

    //convert a pixel to an ID
    private int getID(Pixel pixel)
    {
      return this.image.getWidth()*pixel.q+pixel.p;
    }

    //convert ID back to pixel
    private Pixel getPixel(int id)
    {
      int h= id/this.image.getWidth();
      int w= id-this.image.getWidth()*h;

      if(h<0 || h>=this.image.getHeight() || w<0 || w>=this.image.getWidth())
        throw new ArrayIndexOutOfBoundsException();

      return new Pixel(w,h);
    }
	
	private Color getColor(Pixel p) {
		return new Color(image.getRGB(p.p, p.q));
	}

    //compute the average color pf a collection of pixels
    private Color computeAverageColor(AbstractCollection<Pixel> pixels)
    {
      int r=0, g=0, b=0;
      for(Pixel p : pixels)
      {
        Color c = new Color(image.getRGB(p.p, p.q));
        r+=c.getRed();
        g+=c.getGreen();
        b+=c.getBlue();
      }
      return new Color(r/pixels.size(),g/pixels.size(),b/pixels.size());
    }

    private int getDifference(Color c1, Color c2)
    {
      int r = (int)(c1.getRed()-c2.getRed());
      int g = (int)(c1.getGreen()-c2.getGreen());
      int b = (int)(c1.getBlue()-c2.getBlue());

      return r*r+g*g+b*b;
    }

    //8-neighbors of a given pixel
    private ArrayList<Pixel> getNeightbors(Pixel pixel)
    {
      ArrayList<Pixel> neighbors = new ArrayList<Pixel>();

      for(int i=-1;i<=1;i++)
      {
        int n_w=pixel.p+i;
        if(n_w<0 || n_w==this.image.getWidth()) continue;
        for(int j=-1;j<=1;j++)
        {
          int n_h=pixel.q+j;
          if(n_h<0 || n_h==this.image.getHeight()) continue;
          if(i==0 && j==0) continue;
          neighbors.add( new Pixel(n_w, n_h) );
        }//end for j
      }//end for i

      return neighbors;
    }

    //
    // JPanel function
    //
    public void paint(Graphics g)
    {
      g.drawImage(this.image, 0, 0,this);
    }
}
