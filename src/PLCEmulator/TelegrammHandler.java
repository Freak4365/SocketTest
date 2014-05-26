package PLCEmulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TelegrammHandler {
	private SocketTestServer server;
	private int count_in;
	private int count_out;
	private String Tele_Sync;
	private String Tele_Sync_start;
	private String Tele_Sync_end;
	private String Tele_Life;
	private String Tele_Sysr;
	private String Tele_Stat;
	private String Tele_Wt = "WT";
	//private String Tele_Wtco = "WTCO";
	private String Tele_hndshk_Req;
	private String Tele_hndshk_Conf;
	private String Tele_length;
	
	
	public TelegrammHandler(SocketTestServer s){
		server = s;
		loadProperties();
		setCount_in(0);
		count_out = 0;
	}
	
	public void handle(String inStr){
		setCount_in(getCount_in() + 1);
		Telegram in = new Telegram(inStr);
		String 	send = null, 
				empf = null, 
				cp = null, 
				hndshk = null,  
				//error = null, 
				type = null;
		//int numb;
	
		send = in.getSend();
		empf = in.getEmpf();
		cp = in.getCp();
		hndshk = in.getHndshk();
		//numb = in.getNumb();
		//error = in.getError();
		type = in.getType();
		//System.out.println("send: "+send+" empf: "+empf+" hndshk: "+hndshk+" numb: "+numb+" type: "+type);
		
		//Confirm Sync request, send start of sync
		if(type.equals(Tele_Sync)){
			confirmTelegram(in);
			String sync_start = new TelegrammBuilder()	.sender(empf)
														.reciever(send)
														.hndshk(Tele_hndshk_Req)
														.numb(count_out++)
														.type(Tele_Sync_start)
														.buildTelegram()
															.toString();
			send(sync_start);
		}
		//Start of sync confirmed, finish sync
		else if(type.equals(Tele_Sync_start) && hndshk.equals(Tele_hndshk_Conf)){
			String sync_end = new TelegrammBuilder().sender(empf)
													.reciever(send)
													.hndshk(Tele_hndshk_Req)
													.numb(count_out++)
													.type(Tele_Sync_end)
													.buildTelegram()
														.toString();
			send(sync_end);
		}
		//Confirm Life telegram
		else if(type.equals(Tele_Life)){
			confirmTelegram(in);
		}
		//Confirm status telegram, send status message
		else if(type.equals(Tele_Sysr)){
			confirmTelegram(in);
			String stat = new TelegrammBuilder()
								.sender(empf)
								.reciever(send)
								.cp(cp)
								.hndshk(Tele_hndshk_Req)
								.numb(count_out++)
								.type(Tele_Stat)
								.mfs_error("1234")
										.buildTelegram()
											.toString();
			send(stat);
		}
		//Confirm warehouse task telegram
		else if(type.equals(Tele_Wt)){
			confirmTelegram(in);
		}
	}
	
	private void send(String tele){
		server.sendMessage(tele);
	}
	
	private void confirmTelegram(Telegram in){
		Telegram out = in;
		
		//change sender and receiver
		String send = in.getSend();
		String empf = in.getEmpf();
		out.setEmpf(send);
		out.setSend(empf);
		
		//change handshake
		String hndshk = in.getHndshk();
		if(hndshk.equals(Tele_hndshk_Req)){
			out.setHndshk(Tele_hndshk_Conf);
		}
		else{
			out.setHndshk(Tele_hndshk_Req);
		}
		
		//make telegram to String and return String
		String tele = out.toString();
		send(tele);
	}

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

	public int getTeleLenght(){
		int len = Integer.parseInt(Tele_length);
		return len;
	}
	
	public int getCount_in() {
		return count_in;
	}
	
	public void setCount_in(int count_in) {
		this.count_in = count_in;
	}
	
}
