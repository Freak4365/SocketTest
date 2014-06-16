package PLCEmulator;

import java.awt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Properties;

import javax.swing.*;

public class SocketTest extends JFrame {
    
	private static final long serialVersionUID = 1L;
	public static String Tele_Sync;
	public static String Tele_Sync_start;
	public static String Tele_Sync_end;
	public static String Tele_Life;
	public static String Tele_Sysr;
	public static String Tele_Stat;
	public static String Tele_Wt;
	public static String Tele_Wtco;
	public static String Tele_Wtcc;
	public static String Tele_Sp;
	public static String Tele_hndshk_Req;
	public static String Tele_hndshk_Conf;
	public static String Tele_length;
	public static String Name_EWM;
	public static String Name_PLC;

	/** Creates a new instance of SocketTest */
    public SocketTest() {
        Container cp = getContentPane();
        SocketTestServer server = new SocketTestServer(this);
        cp.add(server);
        createProperties();
        loadProperties();
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
        st.setSize(1000,700);
        Util.centerWindow(st);
        st.setDefaultCloseOperation(EXIT_ON_CLOSE);

        st.setVisible(true);
    }
   
    /**
     * Create properties file
     */
    private void createProperties(){
		Properties prop = new Properties();
		OutputStream output = null;
		
		
		String fileName = System.getProperty("user.dir") + "/config/config.properties";
    	File file = new File(fileName);
    	
    	if ( !(file.exists())) {
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
				prop.setProperty("Tele_Warehouse_task", "WT");
				prop.setProperty("Tele_Confirm_warehouse_task", "WTCO");
				prop.setProperty("Name_EWM", "EWM");
				prop.setProperty("Name_PLC", "CONV1");
				prop.setProperty("Tele_Wtcc", "WTCC");
				prop.setProperty("Tele_Sp", "SP");
		 
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
    	}//end of if
	 }//end of method
    
    /**
	 * Load properties from properties file
	 */
	private void loadProperties(){
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("config/config.properties");
			
			prop.load(input);

			Tele_Sync = (prop.getProperty("Sync"));
			Tele_Sync_start = (prop.getProperty("Sync_Start"));
			Tele_Sync_end = (prop.getProperty("Sync_End"));
			Tele_Life = (prop.getProperty("Life"));
			Tele_Sysr = (prop.getProperty("Stat_Req"));
			Tele_Stat = (prop.getProperty("Stat_Msg"));
			Tele_hndshk_Req = (prop.getProperty("Handshake_Request"));
			Tele_hndshk_Conf = (prop.getProperty("Handshake_Confirm"));
			Tele_length = (prop.getProperty("Telegram_length"));
			Tele_Wt = (prop.getProperty("Tele_Warehouse_task"));
			Tele_Wtco = (prop.getProperty("Tele_Confirm_warehouse_task"));
			Name_EWM = (prop.getProperty("Name_EWM"));
			Name_PLC = (prop.getProperty("Name_PLC"));
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}//end of class
