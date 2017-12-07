
//
// !!! Do NOT Change anything in this file
//

import javax.swing.JFrame;

public class ImgSeg
{
    public static void main(String[] args)
    {
    	/*
        if(args.length<2)
        {
            System.err.println("Usage: ImgSeg -k K [-g] image_file\n\tK: number of segments\n\timage_file: *.jpg, *.png, etc");
            return;
        }

        int K=2;
        String img_name="";
        boolean show_img=false;

        for(int i=0;i<args.length;i++)
        {
          if(args[i].toLowerCase().compareTo("-k")==0) K=Integer.parseInt(args[++i]);
          else if(args[i].toLowerCase().compareTo("-g")==0) show_img=true;
          else img_name=args[i];
        }
*/
    	int K = 5;
    	boolean show_img=true;
    	//String img_name = "./image/2-1.png";
    	//String img_name = "./image/eclipse.jpg";
    	String img_name = "./image/Mona-Lisa.jpg";
    	//System.out.println(ImgSeg.class.getClassLoader().getResource("").getPath());
    	
        Decomposor seg = new Decomposor(img_name);
        seg.segment(K);
        seg.outputResults(K);

        //display the image after segmentation
        if(show_img)
        {
          JFrame frame = new JFrame("ImgSeg -k "+K+" "+img_name);
          frame.getContentPane().add(seg);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setSize(seg.image.getWidth(), seg.image.getHeight());
          frame.setVisible(true);
        }
    }
}
