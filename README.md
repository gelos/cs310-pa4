# A Tiny Image Segmentation 

- CS 310 Programming Assignment 4 Due: **Dec 10th** (Sunday) 11:59pm, 2017

## Assignment Objective
- Write a program to segment an image into _K_>1 regions, where _K_ is a user parameter.
- Learn to use _priority queue_ (binary heap) and _disjoint sets_ (union-find data structure)
- Image segmentation is one of the most fundamental operation in computer vision and image processing and has many many applications; [Learn more about image segmentation from Wikipedia](https://en.wikipedia.org/wiki/Image_segmentation)

## Table of Contents
1. [Input/Output](#input-output)
2. [Examples](#examples)
4. [Definitions](#definitions)
5. [Tasks](#tasks)
6. [Rules](#rules)
7. [Submission Instructions](#submission-instructions)
8. [Grading Rubric](#grading-rubric)
9. [Useful Links](#external-links)

## Input Output

### Input
- A single bitmap RGB image in jpg or png formats. 
- A positive integer _K_ > 1, i.e., the number of final regions

### Output

Your program should output: (1) region information and (2) re-colored image. 

- A list of regions ordered from large to small (pixel count). Each line of your terminal output (System.out) will contain 
  1. size of the region, this is the number of pixels in the region
  2. color of the region, this color is the average color of all pixels in the region
- An image file, in which, all pixels in the same region are assigned the average color of the region. 
  - The image filename must be named **ABCD_seg_K.png** if your input file is "ABCD.xyz" where xyz is jpg or png and _K_ is the input argument _K_
- See [example output](#examples) below; your program must reproduce the same output.

## Examples

1. Segment into two regions

```
> java  -k 2 image/2-1.png
region 1 size= 8313 color=java.awt.Color[r=250,g=250,b=252]
region 2 size= 3087 color=java.awt.Color[r=61,g=86,b=144]
- Saved result to image/2-1_seg_2.png
```

![input](https://github.com/jmlien/cs310-pa4/blob/master/image/2-1.png "input")|![output](https://github.com/jmlien/cs310-pa4/blob/master/output/2-1_seg_2.png "output")
:---: | :---: 
input|output

2. Segment into 3 regions

```
> java  -k 2 image/3.png
region 1 size= 4853 color=java.awt.Color[r=252,g=251,b=252]
region 2 size= 5247 color=java.awt.Color[r=143,g=77,b=143]
- Saved result to image/3_seg_2.png

> java  -k 3 image/3.png
region 1 size= 4853 color=java.awt.Color[r=252,g=251,b=252]
region 2 size= 4236 color=java.awt.Color[r=145,g=63,b=145]
region 3 size= 1011 color=java.awt.Color[r=136,g=136,b=136]
- Saved result to image/3_seg_3.png
```
![input](https://github.com/jmlien/cs310-pa4/blob/master/image/3.png "input")|![output](https://github.com/jmlien/cs310-pa4/blob/master/output/3_seg_2.png "output")|![output](https://github.com/jmlien/cs310-pa4/blob/master/output/3_seg_3.png "output")
:---: | :---: | :---:
input|_K_=2|_K_=3

3. Segment into K>3 regions

```
> java  -k 10 image/Leiadeathstar.jpg
region 1 size= 13761 color=java.awt.Color[r=34,g=37,b=44]
region 2 size= 7883 color=java.awt.Color[r=158,g=171,b=191]
region 3 size= 7807 color=java.awt.Color[r=35,g=37,b=45]
region 4 size= 7593 color=java.awt.Color[r=191,g=189,b=194]
region 5 size= 7580 color=java.awt.Color[r=186,g=188,b=196]
region 6 size= 4717 color=java.awt.Color[r=54,g=58,b=69]
region 7 size= 1936 color=java.awt.Color[r=123,g=85,b=79]
region 8 size= 1064 color=java.awt.Color[r=160,g=105,b=92]
region 9 size= 963 color=java.awt.Color[r=141,g=130,b=128]
region 10 size= 696 color=java.awt.Color[r=168,g=109,b=99]
- Saved result to image/Leiadeathstar_seg_10.png

> java  -k 100 image/Leiadeathstar.jpg
region 1 size= 4346 color=java.awt.Color[r=16,g=20,b=29]
region 2 size= 3685 color=java.awt.Color[r=40,g=52,b=76]
region 3 size= 3588 color=java.awt.Color[r=114,g=147,b=186]
region 4 size= 2902 color=java.awt.Color[r=191,g=197,b=209]
[snipped...]
region 100 size= 5 color=java.awt.Color[r=183,g=140,b=140]
- Saved result to image/Leiadeathstar_seg_100.png
```

![input](https://github.com/jmlien/cs310-pa4/blob/master/image/Leiadeathstar.jpg "input")|![output](https://github.com/jmlien/cs310-pa4/blob/master/output/Leiadeathstar_seg_10.png "output")|![output](https://github.com/jmlien/cs310-pa4/blob/master/output/Leiadeathstar_seg_100.png "output")
:---: | :---: | :---:
input|_K_=10|_K_=100

4. Need more examples? Look under [output folder](https://github.com/jmlien/cs310-pa4/blob/master/output)

## Definitions

Please familiarize yourself with these terms and their definitions before you move on.

### Pixel

- In this assignment, a pixel is a 2D coorindate of an image. 
- You can use a pixel to retrieve its color from an image. 
- A pixel _(w,h)_ can also be represented as a unique id _(h*W+w)_, where _W_ is the width of the image

### Region

- A region is a set of contiguous pixels. 
- A pixel can only belong to one region. 
- Consequently, a region is a set in the disjoint sets data structure.
- The color of a region is the average color of all pixels in the region.
- A region is identified by one of its pixels that is the root in the disjoint sets data structure. 
- Two regions are neighbors if some of their pixels are adjacent. For example, region _E_ is neighboring to regions _A_, _C_, _G_, but not regions _B_, _D_, _F_ in the image below.

![regions](https://github.com/jmlien/cs310-pa4/blob/master/image/regions.png "regions")

### Similarity

- Similarity is the color distance (a.k.a. color difference) between a pair of adjacent regions.
- Smaller distance indicates stronger similarity.
- The similarity between a pair of non-adjacent regions is undefined and should never be created.

## Tasks

There are **5** tasks in this assignment. It is recommended that you implement these tasks in the given order. 

### Task 0: Read the given code (0%)

Read and familiarize yourself with the code, in particular the following classes and methods in Decomposor.java

This will save you a lot of time later.

```java

private class Pair<T>{...}

private class Pixel extends Pair<Integer>{...}

private class Similarity implements Comparable<Similarity>{}

//Convert a pixel to its ID
private int getID(Pixel pixel);
    
//Convex ID back to a pixel
private Pixel getPixel(int id);
	
//returns the color of a given pixel
private Color getColor(Pixel p)

//given a collection of pixels, determines the average color
private Color computeAverageColor(AbstractCollection<Pixel> pixels);

//computes the difference (as an integer) between two colors
private int getDifference(Color c1, Color c2);
	
//returns the pixels adjacent to the pixel
private ArrayList<Pixel> getNeightbors(Pixel pixel);
```

### Task 1: Implement Disjoint Sets (the union-find data structure) (20%)

- Implement Set<T> in Set.java **(5%)**
- Implement DisjointSets<T> in DisjointSets.java **(15%)**

_Hints_
- Each Set represents a region of contiguous pixels
- Initially every pixel is its own region
- You can use/modify the DisjointSets code from your textbook

```java
//in Set.java
public class Set<T>{...} //must provide add, addAll, clear, and iterator
```

```java
//in DisjointSets.java
public class DisjointSets<T>
{
    //Constructor
    public DisjointSets( ArrayList<T> data  );

    //Compute the union of two sets using union by size
    //returns the new root of the unioned set
    //Must be O(1) time
    public int union( int root1, int root2 );

    //Find and return the root
    //Must implement path compression
    public int find( int x );
    
    // get all the data in the same set
    // must be O(1) time
    public Set<T> get( int root );
    
    // return the number of disjoint sets remaining
    // must be O(1) time
    public int getNumSets(  );

    private int [ ] s;
    private ArrayList<Set<T>> sets; //actual data
}
```

### Task 2: Get Neighboring Sets (10%)

Implement getNeightborSets function in Decomposor.java

Given a disjoint-sets data structure and a region (defined by its root id), return a list of adjacent regions (again, represented by their root ids) which are DIFFERENT from the parameter region. 

```java
private TreeSet<Integer> getNeightborSets(DisjointSets<Pixel> ds, int root);    
```

_Hints_: Use TreeSet<T> and the following private method.

```java
private ArrayList<Pixel> getNeightbors(Pixel pixel)
```

#### Task 3: Compute region to region similarity (10%)     

Given two regions _R1_ and _R2_, implement a function to compute the similarity between these two regions.
A region is considered similar to another region based on the distance from the weighted root colors _C_. _C_ can be computed with the following formula:
- C-red = ((R1-red * pixels-in-R1)+(R2-red * pixels-in-R2))/(total-number-of-pixels-in-R1-and-R2)
- C-green = ((R1-green * pixels-in-R1)+(R2-green * pixels-in-R2))/(total-number-of-pixels-in-R1-and-R2)
- C-blue = ((R1-blue * pixels-in-R1)+(R2-blue * pixels-in-R2))/(total-number-of-pixels-in-R1-and-R2)

To compute the distance, you'll need to compute **the sum of all color differences** between _C_ and all pixels in _R1_ and _R2_. 

Hint: Iterate through all pixels and sum the result returned by the getDifference() helper function.

Return a new ```Similarity``` where distance is the sum computed above, and the two pixels are the pixels of root1 and root2.
    
```java
private Similarity getSimilarity(DisjointSets<Pixel> ds, int R1, int R2);
```

### Task 4: Implement the decomposor (50%)

Implement the following method

```java
public void segment(int K) //K is the number of desired segments
```

**High-level idea**

- Iteratively merging two adjacent regions with most similar colors until the number of regions is _K_.

**Here is what you should do** in this function:

1. Create ```DisjointSets<Pixel>``` and populate it with all pixels
2. Create ```PriorityQueue<Similarity>```, which should contain all possible pairs of neighboring pixels and their similarity values. Use your ```getSimilarity``` (from task 3) to compute the similarity. 
3. Loop until the number of regions equals _K_; in each iteration,
  - You must use ```PriotityQueue<Similarity>``` to find a pair of most similar regions 
  - If the regions are _not_ disjoint, ignore the pair
  - Otherwise
    1. If the pixels are no longer roots of their own sets (this may have happened due to an earlier union):
	  - if the similarity distance is greater than 0 (in other words, the two pixel regions were not identical), add the roots of both pairs back into the queue (there may be higher priority things than the union of the two roots).
	  - if the similarity distance is 0 (in otherwords, the two regions were identical), union the roots (you can't be any more similar than a distance of 0).
	2. If the pixels are both roots of their own sets:
      - compute the union of the pair (we'll call this new region _R_)
      - measure ```Similarity``` between all pairs of _R_ and _R_'s neighboring regions using ```getNeightborSets``` and ```getSimilarity```
	  - add each new similarity to your priority queue


### Task 5: Output (10%)

Recolor all pixels with the average color and save output image.
      
```java
      public void outputResults(int K)
```    

_Hints_
Use ```image.setRGB(x,y,c.getRGB())``` to change the color of a pixel (x,y) to the given color "c"

## Rules

### You must
1. Fill out readme.txt **(NOT README.md, which is this document)** with your information (goes in your user folder)
2. Have a style (indentation, good variable names, etc.)
3. Comment your code well in JavaDoc style (no need to overdo it, just do it well)
4. Have code that compiles with the command: javac *.java in your user directory
5. **Print regions to terminal by size, large to small**

### You may 

1. Import the following libraries
```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
```

2. Use code from your text book _Data Structures and Problem Solving Using Java_, 4th Edition by _Mark A. Weiss_

### You cannot 
1. Make your program part of a package.
2. Import any additional libraries/packages

## Submission Instructions
- Use the cloud or some other server to backup your code!
- Remove all test files, jar files, class files, etc.
- You should just submit your java files and your readme.txt
- Zip your user folder (not just the files) and name the zip “username-p4.zip” (no other type of archive) where “username” is your username.
- Submit to blackboard.

## Grading Rubric
[back to top](#table-of-contents)

### No Credit
- Non submitted assignments
- Late assignments (**late tokens not allowed** for this project)
- Non compiling assignments
- Non-independent work
- "Hard coded" solutions
- Code that would win an obfuscated code competition with the rest of CS310 students.

### How will my assignment be graded?
- Grading will be divided into two portions:
  1. Manual/Automatic Testing (100%): To assess the correctness of programs.
  2. Manual Inspection (10% off the top points): [A checklist](#manual-code-inspection-rubric-10-off-the-top-points) of features your programs should exhibit. These comprise things that cannot be easily checked via unit tests such as good variable name selection, proper decomposition of a problem into multiple functions or cooperating objects, overall design elegance, and proper asymptotic complexity. These features will be checked by graders and assigned credit based on level of compliance. See the remainder of this document for more information.
- You CANNOT get points (even style/manual-inspection points) for code that doesn't compile or for submitting just the files given to you with the assignment. You CAN get manual inspection points for code that (a) compiles and (b) is an "honest attempt" at the assignment, but does not pass any unit tests.

#### Manual/Automatic Testing (100%)
- Your output images will be compared with our output image using [ImageMagick](https://www.imagemagick.org/script/index.php) via the following command
```
magick compare  -metric RMSE image/your-output.png image/my-ouput.png diff.png
```
The output should be "0 (0)" when these two images are identical. 

#### Manual Code Inspection Rubric (10% "off the top" points)
These are all "off the top" points (i.e. items that will lose you points rather than earn you points):

Inspection Point | Points | High (all points) | Med (1/2 points) | Low (no points)
:---: | :---: | :--- | :--- | :--- 
Submission Format (Folder Structure) |  2 |  Code is in a folder which in turn is in a zip file. Folder is correctly named. | Code is not directly in user folder, but in a sub-folder. Folder name is correct or close to correct. | Code is directly in the zip file (no folder) and/or folder name is incorrect.
Code Formatting | 2 | Code has a set indentation and formatting style which is kept consistent throughout and code looks "well laid out".| Code has a mostly consistent indentation and formatting style, but one or more parts do not match.|Code indentation and formatting style changes throughout the code and/or the code looks "messy".
JavaDocs | 3 | The entire code base is well documented with meaningful comments in JavaDoc format. Each class, method, and field has a comment describing its purpose. Occasional in-method comments used for clarity. | The code base has some comments, but is lacking comments on some classes/methods/fields or the comments given are mostly "translating" the code. | The only documentation is what was in the template and/or documentation is missing from the code (e.g. taken out).
Coding conventions | 3 | Code has good, meaningful variable, method, and class names. All (or almost all) added fields and methods are properly encapsulated. For variables, only class constants are public. | Names are mostly meaningful, but a few are unclear or ambiguous (to a human reader) [and/or] Not all fields and methods are properly encapsulated. |  Names often have single letter identifiers and/or incorrect/meaningless identifiers. [Note: i/j/k acceptable for indexes.] [and/or] Many or all fields and methods are public or package default.

### External Links
- [What is digital image, a.k.a raster graphics](https://wiki.scratch.mit.edu/wiki/Raster_Graphics)
- [Wikipedia page on raster graphics](https://en.wikipedia.org/wiki/Raster_graphics)
- [Wikipedia page on pixel](https://en.wikipedia.org/wiki/Pixel)
- [Java official tutorial on image](https://docs.oracle.com/javase/tutorial/2d/overview/images.html)
- [Java official tutorial on loading and saving image](https://docs.oracle.com/javase/tutorial/2d/images/index.html)
