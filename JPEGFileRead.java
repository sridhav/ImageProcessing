import java.io.*;

class JPEGFileRead{
	
	File inpFile = null;

	JPEGFileRead(File file){
		this.inpFile = file;
	}

	void readBinaryFile() throws Exception{
		InputStream input = new FileInputStream(inpFile);
		int total = 0;
		int a = 0;
        boolean ff = false;
        int count = 0;
        while(a!=-1){
			a = input.read();
            if(a == 0xFF){
                ff = true;
                
            }
            
            if(ff && a==0xd8){
                ff = false;
                //System.out.println("Start of image");
            }
            
            if(ff && a==0xe0){
                ff= false;
                handleAPP0(input);
            }
            
            if(ff && a==0xe1){
                ff= false;
                System.out.println("AAP1");
                handleAPP1(input);
            }
            
            while(ff && a==0xc0){
                if(a == 0xFF){
                    ff = true;
                    
                }
                ff=false;
                //System.out.println("SOS %%%%%%%%%%%%%%"+count);
                handleSOF(input);
            }
            
            
        }
	}
    
    
    void handleSOF(InputStream input) throws Exception{
        System.out.println("Image Length"+combineBytes(input.read(),input.read()));
    }
    
    void handleAPP1(InputStream input) throws Exception{
        int x=combineBytes(input.read(),input.read());
        //System.out.println("Exif Lenght:"+x);
        int count = 2;
        int arr[] = {input.read(),input.read(),input.read(),input.read(),input.read(),input.read()};
        if(arr[0]==0x45 && arr[1]==0x78 && arr[2]==0x69 && arr[3] == 0x66 && arr[4]==0x00 && arr[5] == 0x00){
            //System.out.println("Exif Identifier");
            count = count+6;
            boolean flag = false;
            
            while(count!=x){
                count ++;
                int val = input.read();
                
                if(flag) {
                    flag =false;
                   // System.out.println(val+"FOUNF");
                }
                if(val == 0xff)
                    flag=true;
            }
            
        }
        System.out.println("HANDLED AAP1");
        return;
        

    }
    
    void handleAPP0(InputStream input) throws Exception{
        combineBytes(input.read(),input.read());
        
        int arr[] = {input.read(),input.read(),input.read(),input.read(),input.read()};
        
        if(arr[0]==0x4A && arr[1]==0x46 && arr[2]==0x49 && arr[3] == 0x46 && arr[4]==0x00){
            System.out.println("JFIF");
            System.out.println("Version :"+ input.read()+"."+input.read());
            System.out.println("Units :"+ input.read());
            System.out.println("XDensity:"+combineBytes(input.read(),input.read()));
            System.out.println("YDensity:"+combineBytes(input.read(),input.read()));
            int XThumbnail = input.read();
            int YThumbnail = input.read();
            System.out.println("XThumbnail"+XThumbnail);
            System.out.println("YThumbnail"+YThumbnail);
            if(XThumbnail == 0 || YThumbnail == 0){
                return;
            }
            else{
                //Handle the Thumbnail part;
            }
            
        } else if (arr[0]==0x4A && arr[1]==0x46 && arr[2]==0x58 && arr[3] == 0x58 && arr[4]==0x00){
            //JIFF EXTENSION
            
        }
        
    }
    
    int combineBytes(int one, int two){
        int temp = (one&0xff) << 8 | (two&0xff);
        return temp;
    }
    
	public static void main(String args[]) throws Exception{
		JPEGFileRead jp = new JPEGFileRead(new File("img.jpg"));
		jp.readBinaryFile();
	}

}