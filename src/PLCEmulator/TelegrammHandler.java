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
	}
	
	/**
	 * @param inStr The String received from TCP-/IP-Connection
	 */
	@SuppressWarnings("unchecked")
	public void handle(String inStr){
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
								.mfs_error("0000")
										.buildTelegram()
											.toString();
			send(stat);
		}
		//Confirm warehouse task telegram
		else if(type.equals(Tele_Wt)&&hndshk.equals(Tele_hndshk_Req)){
			/*
			//Confirm WT request telegram
			Telegram_WT _in = new Telegram_WT(inStr);
			Telegram_WT conf = _in;
			conf.setEmpf(send);
			conf.setSend(empf);
			conf.setHndshk(Tele_hndshk_Conf);
			send(conf);
			
			//Confirm WT
			Telegram_WT wt_conf = _in;
			wt_conf.setEmpf(send);
			wt_conf.setSend(empf);
			wt_conf.setHndshk(Tele_hndshk_Req);
			wt_conf.setNumb(count_out++);
			wt_conf.setType(Tele_Wtco);
			send(wt_conf);
			*/
			Telegram_WT _in = new Telegram_WT(inStr);
			confirmTelegram(_in);
			
			warehouseTask wt = new warehouseTask(_in);
			wt_list.add(wt);
			
			int length = wt_list.size();
			String[] list = new String[length];
	    	for(int i=0;i<length;i++) {
				list[i]=wt_list.get(i).getHuId().toString();
			}
	    	server.setWtListData(list);
		}
	}
	
	/**
	 * Sends a telegram to the TCP-/IP-Connection
	 * @param tele Telegram as String to send.
	 */
	private void send(String tele){
		server.sendMessage(tele);
	}
	
	/**
	 * Sends a telegram to the TCP-/IP-Connection
	 * @param tele Telegram as object to send.
	 */
	private void send(Telegram tele){
		String out = tele.toString();
		server.sendMessage(out);
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
		String tele = out.toString();
		send(tele);
	}
	
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
	
	public void confirmWt(String hu, String hutype, String source, String dest, String mfs_error){
		String wtco = new TelegrammBuilder()
							.sender(SocketTest.Name_PLC)
							.reciever(SocketTest.Name_EWM)
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
	
	public void confirmWt(int index){
		warehouseTask wt = wt_list.get(index);
		confirmWt(wt);
	}
	
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
	
}
