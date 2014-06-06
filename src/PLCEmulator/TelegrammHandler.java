package PLCEmulator;

import java.util.ArrayList;

/**
 * @author André Kukla
 *
 */
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
	private String Tele_Wtco = "WTCO";
	private String Tele_hndshk_Req;
	private String Tele_hndshk_Conf;
	private String Tele_length;
	private ArrayList<warehouseTask> wt_list; 
	private ArrayList<communicationPoint> cp_list; 
	private String last_in;
	private String last_out;
	
	public TelegrammHandler(SocketTestServer s){
		server = s;
		setCount_in(0);
		count_out = 0;
		Tele_Sync = SocketTest.Tele_Sync;
		Tele_Sync_start = SocketTest.Tele_Sync_start;
		Tele_Sync_end  = SocketTest.Tele_Sync_end;
		Tele_Life = SocketTest.Tele_Life;
		Tele_Sysr = SocketTest.Tele_Sysr;
		Tele_Stat = SocketTest.Tele_Stat;
		Tele_Wt = SocketTest.Tele_Wt;
		Tele_Wtco = SocketTest.Tele_Wtco;
		Tele_hndshk_Req = SocketTest.Tele_hndshk_Req;
		Tele_hndshk_Conf = SocketTest.Tele_hndshk_Conf;
		Tele_length = SocketTest.Tele_length;
		wt_list = new ArrayList<warehouseTask>();
		cp_list = new ArrayList<communicationPoint>();
		cp_list.add(new communicationPoint("CP01", 1, 1, "CP02", "", new handlingUnit("123456789", "E1"), ""));
		cp_list.add(new communicationPoint("CP02", 2, 2, "CP03", "", null, ""));
		cp_list.add(new communicationPoint("CP03", 2, 2, "CP04", "", null, ""));
		setCpListData();
	}
	
	/**
	 * Automatically react to incoming telegram 
	 * @param inStr The String received from TCP-/IP-Connection
	 */
	public void handle(String inStr){
		//repeat last telegram if error occurs
		if(inStr.equals(last_in)){
			send(last_out);
			return;
		}
		last_in = inStr;
		setCount_in(getCount_in() + 1);
		Telegram in = new Telegram(inStr);

		String send = in.getSend();
		String empf = in.getEmpf();
		String cp = in.getCp();
		String hndshk = in.getHndshk();
		//int numb = in.getNumb();
		//String error = in.getError();
		String type = in.getType();
		
		//Confirm Sync request, send start of sync
		if(type.equals(Tele_Sync)&&hndshk.equals(Tele_hndshk_Req)){
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
								.mfs_error("")
										.buildTelegram()
											.toString();
			send(stat);
			
		}
		
		//Confirm warehouse task telegram
		else if(type.equals(Tele_Wt)&&hndshk.equals(Tele_hndshk_Req)){
			Telegram_WT _in = new Telegram_WT(inStr);
			confirmTelegram(_in);
			
			warehouseTask wt = new warehouseTask(_in);
			wt_list.add(wt);
			
			setWtListData();
		}
	}
	
	/**
	 * Convert list of warehouse tasks to String array and send it to GUI
	 */
	private void setWtListData(){
		int length = wt_list.size();
		String[] list = new String[length];
    	for(int i=0;i<length;i++) {
			list[i]=wt_list.get(i).getHuId().toString();
		}
    	server.setWtListData(list);
	}
	
	/**
	 * Convert list of communication points to String array and send it to GUI
	 */
	private void setCpListData(){
		int length = cp_list.size();
		String[] list = new String[length];
    	for(int i=0;i<length;i++) {
			list[i]=cp_list.get(i).getName().toString();
		}
		server.setCpListData(list);
	}
	
	/**
	 * Sends a telegram to the TCP-/IP-Connection
	 * @param tele Telegram as String to send.
	 */
	private void send(String tele){
		last_out = tele;
		server.sendMessage(tele);
	}
	
	/**
	 * Sends a telegram to the TCP-/IP-Connection
	 * @param tele Telegram as object to send.
	 */
	private void send(Telegram tele){
		String out = tele.toString();
		send(out);
	}
		
	/**
	 * Build a telegram confirmation and send it.
	 * Sender and receiver are exchanged, handshake is changed.
	 * @param in telegram to confirm
	 */
	private void confirmTelegram(Telegram in){
		Telegram out = in;
		out.switchSendRec();
		out.changeHndshk();
		send(out);
	}
	
	public void sendSPMsg(String hu, String hutype, String cp){
		String sp = new TelegrammBuilder()
							.sender(SocketTest.Name_PLC)
							.reciever(SocketTest.Name_EWM)
							.cp(cp)
							.hndshk(Tele_hndshk_Req)
							.numb(count_out++)
							.type("SP")
							.huid(hu)
							.hutype(hutype)
									.buildTelegram()
										.toString();
		send(sp);
	}
	
	/**
	 * Set an error code for a given CP
	 * @param i index
	 * @param error error code
	 */
	public void setCpError(int i, String error){
		communicationPoint cp = cp_list.get(i);
		cp.setError(error);
		cp_list.set(i, cp);
		sendStateMsg(cp.getName(), error);
	}
	
	/**
	 * Send a state message with given CP and error code
	 * @param cp control point
	 * @param error error code
	 */
	public void sendStateMsg(String cp, String error){
		String stat = new TelegrammBuilder()
								.sender(SocketTest.Name_PLC)
								.reciever(SocketTest.Name_EWM)
								.cp(cp)
								.hndshk(Tele_hndshk_Req)
								.numb(count_out++)
								.type(Tele_Stat)
								.mfs_error(error)
										.buildTelegram()
											.toString();
		send(stat);
	}
	
	/**
	 * Confirm a warehouse task with given parameters
	 * @param hu Handling unit
	 * @param hutype Handling unit type
	 * @param source source
	 * @param dest destination
	 * @param mfs_error error of PLC
	 */
	public void confirmWt(String hu, String hutype, String source, String dest, String mfs_error){
		String wtco = new TelegrammBuilder()
							.sender(SocketTest.Name_PLC)
							.reciever(SocketTest.Name_EWM)
							//.cp(n)
							.hndshk(Tele_hndshk_Req)
							.numb(count_out++)
							.type(Tele_Wtco)
							.huid(hu)
							.hutype(hutype)
							.source(source)
							.dest(dest)
							.mfs_error(mfs_error)
									.buildTelegram()
										.toString();
		send(wtco);
	}
	
	/**
	 * Confirm a warehouse task by index
	 * @param index of warehouse task to confirm
	 */
	public void confirmWt(int index){
		warehouseTask wt = wt_list.get(index);
		confirmWt(wt);
		wt_list.remove(index);
		setWtListData();
	}
	
	/**
	 * Confirm the given warehouse task
	 * @param _wt warehousetask to confirm
	 */
	public void confirmWt(warehouseTask _wt){
		confirmWt(_wt.getHuId(), _wt.getHuType(), _wt.getSource(), _wt.getDest(),"");
	}

	/**
	 * @return Length of a telegram from properties file
	 */
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
	
	/**
	 * @param i index
	 * @return String array, [0]:HU-ID, [1]:HU type, [2]:Source, [3]:Destination
	 */
	public String[] getWtInfo(int i){
		warehouseTask wt = wt_list.get(i);
		if(wt != null){
			String[] s = {wt.getHuId(), wt.getHuType(), wt.getSource(), wt.getDest()};
			return s;
		}
		else{
			return null;
		}
	}
	
	/**
	 * @param i index
	 * @return String array, [0]:Name, [1]:Error, [2]:HU-ID
	 */
	public String[] getCpInfo(int i){
		communicationPoint cp = cp_list.get(i);
		if(cp != null){
			handlingUnit hu = cp.getHu();
			String huid = "";
			if(hu != null){
				huid = hu.getId();
			}
			
			String[] s = {cp.getName(), cp.getError(), huid};
			return s;
		}
		else{
			return null;
		}
	}
	
}
