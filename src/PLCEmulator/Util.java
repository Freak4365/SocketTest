package PLCEmulator;


import java.net.*;
import java.io.*;
import java.awt.*;

public class Util {
    
    public static void centerWindow(Window win) {
        Dimension dim = win.getToolkit().getScreenSize();
        win.setLocation(dim.width/2 - win.getWidth()/2,
                dim.height/2 - win.getHeight()/2);
    }
    
    public static boolean checkHost(String host) {
        try {
            InetAddress.getByName(host);
            return(true);
        } catch(UnknownHostException uhe) {
            return(false);
        }
    }
    
    public static void writeFile(String fileName, String text)
    throws IOException {
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(fileName)));
        out.print(text);
        out.close();
    }
    
    public static String readFile(String fileName, Object parent)
    throws IOException {
        StringBuffer sb = new StringBuffer();
        InputStream is = parent.getClass().getResourceAsStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String s;
        while((s = in.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        in.close();
        return sb.toString();
    }
    
    public static String checkTraceDatei() {
    	String TraceDateiName = System.getProperty("user.dir") + "/trace/SocketTest.txt";
    	File file = new File(TraceDateiName);
    	File directory = new File(file.getParentFile().getAbsolutePath());
    	if ( !(file.exists())) {
    	   try {
    		directory.mkdirs();   
			file.createNewFile();
    	   } 
    	   catch (IOException e) {
			e.printStackTrace();
    	   }
    	}
    	return TraceDateiName;
    }
    
    public static void writeTrace(String DataIn){
    	String TraceDatei = checkTraceDatei();
    	try{
            BufferedWriter output = new BufferedWriter(new FileWriter(TraceDatei, true));
            output.append(DataIn);
            output.newLine();
            output.newLine();
            output.close();    
        }
        catch (IOException e) { 
            System.out.println("Datei konnte nicht erstellt werden!");
            e.printStackTrace();
        }
    }
}

