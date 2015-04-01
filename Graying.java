import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

class Graying {
	
	BufferedImage image = null;
	int type;
	static int TYPE_AVERAGING = 1;
	static int TYPE_LUMINANCE = 2;
	static int TYPE_DESATURATE = 3;
	static int TYPE_DECOMPOSE = 4;
	static int TYPE_SINGLE_COLOR = 5;

	

	Graying(BufferedImage image, int type){
		this.image = image;
		this.type = type;
	}

	Graying(BufferedImage image){
		this.image =image;
		this.type = TYPE_AVERAGING;

	}

	BufferedImage compute(){
		BufferedImage output = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		switch(type){
			case 1 : 
				for(int i=0;i<image.getHeight();i++){
					for(int j=0;j<image.getWidth();j++){
						RGBPixel temp = new RGBPixel(image.getRGB(j,i));
						/*int avg = ((temp.red+temp.green+temp.blue)/3)%256;
						avg = temp.getNormalizedPixel(avg);
						output.setRGB(j,i,avg);
						*/

						int out = (int)(temp.red*0.299+temp.green*0.587+temp.blue*0.114);
						output.setRGB(j,i,out);
					}
				}
		}
		return output;
	}



	class RGBPixel{
		int red;
		int blue;
		int green;
		int alpha;

		RGBPixel(int pixel){
			int bitmask = 0xFF000000;
			this.red = pixel & bitmask;
			this.red = this.red >>> 24;

			bitmask = 0x00FF0000;
			this.green = pixel & bitmask;
			this.green = this.green >>> 16;
			
			bitmask = 0x0000FF00;
			this.blue = pixel & bitmask;
			this.blue = this.blue >>>8;
			
			bitmask = 0x000000FF;
			this.alpha = pixel & bitmask;
		}

		int getNormalizedPixel(int val){
			int out = val;

			int temp = val << 24;
			out = out | temp;

			temp = val <<16;
			out = out | temp;

			temp = val << 8;
			out = out|temp;

			out = out|this.alpha;

			return out;
		}

		void display(){
			System.out.println("R :"+this.red+" G:"+this.green+" B:"+this.blue+" A:"+this.alpha);
		}
	}


	public static void main(String args[]) throws Exception{
		BufferedImage img1 = ImageIO.read(new File("img.jpg"));
		Graying gr = new Graying(img1);
		BufferedImage out = gr.compute();
		ImageIO.write(out,"JPG",new File("img-out.jpg"));
	}

}