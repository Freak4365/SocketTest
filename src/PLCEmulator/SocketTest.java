package PLCEmulator;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.*;

public class SocketTest extends JFrame {
    
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of SocketTest */
    public SocketTest() {
        Container cp = getContentPane();
        SocketTestServer server = new SocketTestServer(this);
        cp.add(server);
        createProperties();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch(Exception e) {
            //e.printStackTrace();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception ee) {
                System.out.println("Error setting native LAF: " + ee);
            }
        }      
        
        SocketTest st = new SocketTest();
        st.setTitle("PLC Emulator v 1.0.0");
        st.setSize(600,500);
        Util.centerWindow(st);
        st.setDefaultCloseOperation(EXIT_ON_CLOSE);

        st.setVisible(true);
    }
   
    private void createProperties(){
		Properties prop = new Properties();
		OutputStream output = null;
		
		
		String fileName = System.getProperty("user.dir") + "/config/config.properties";
    	File file = new File(fileName);
    	
    	//if ( !(file.exists())) {
    		//Create properties file
    		
    		File directory = new File(file.getParentFile().getAbsolutePath());
    	   try {
    		   directory.mkdirs();   
    		   file.createNewFile();
    	   } 
    	   catch (IOException e) {
    		   e.printStackTrace();
    	   }
    	
	    	//Write to properties file
			try {
				output = new FileOutputStream("config/config.properties");
		 
				// set the properties value
				prop.setProperty("Sync", "SYES");
				prop.setProperty("Sync_Start", "SYBE");
				prop.setProperty("Sync_End", "SYEN");
				prop.setProperty("Life", "LIFE");
				prop.setProperty("Stat_Req", "SYSR");
				prop.setProperty("Stat_Msg", "STAT");
				prop.setProperty("Handshake_Request", "A");
				prop.setProperty("Handshake_Confirm", "B");
				prop.setProperty("Telegram_length", "128");
		 
				// save properties to project root folder
				prop.store(output, null);
		 
			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		 
			}//end of try
    	//}//end of if
	 }//end of method
    
}//end of class
