package PLCEmulator;

public class TelegrammBuilder {
	
	private String send;
	private String empf;
	private String cp;
	private String hndshk;
	private int numb;
	private String error;
	private String type;
	private String huid;
	private String hutype;
	private String source;
	private String dest;
	private String mfs_error;
	private String cs;
	private String csgr;
	private String rsrc;
	//WTCO: CONV1...EWM.......................A.00000000000000000004....WTCO00000000001000000136E1......CP02..............CP03..............
	
	public TelegrammBuilder(){
		 send = "";
		 empf = "";
		 cp = "";
		 hndshk = "";
		 error = "";
		 type = "";
		 huid = "";
		 hutype = "";
		 source = "";
		 dest = "";
		 mfs_error = "";
		 cs = "";
		 csgr = "";
		 rsrc = "";
	}
	
	public Telegram buildTelegram(){
		if(type.equals(SocketTest.Tele_Stat)||type.equals(SocketTest.Tele_Sysr)){
			Telegram_STAT t = new Telegram_STAT(send, empf, cp, hndshk, numb, error, type, source, dest, mfs_error, cs, csgr, rsrc);
			return t;
		}
		else if(type.equals(SocketTest.Tele_Wt)||type.equals(SocketTest.Tele_Wtco)||type.equals("WTCC")||type.equals("SP")){
			Telegram_WT t = new Telegram_WT(send, empf, cp, hndshk, numb, error, type, huid, hutype, source, dest, mfs_error);
			return t;
		}
		else if(type.equals("")){
			//Throw exception
			return null;
		}
		else{
			Telegram t = new Telegram(send, empf, cp, hndshk, numb, error, type, mfs_error);
			return t;
		}
	}
	
	public TelegrammBuilder sender(String n){
		send = n;
		return this;
	}
	
	public TelegrammBuilder reciever(String n){
		empf = n;
		return this;
	}
	
	public TelegrammBuilder cp(String n){
		cp = n;
		return this;
	}
	
	public TelegrammBuilder hndshk(String n){
		hndshk = n;
		return this;
	}
	
	public TelegrammBuilder numb(int n){
		numb = n;
		return this;
	}
	
	public TelegrammBuilder error(String n){
		error = n;
		return this;
	}
	
	public TelegrammBuilder type(String n){
		type = n;
		return this;
	}
	
	public TelegrammBuilder huid(String n){
		huid = n;
		return this;
	}
	
	public TelegrammBuilder hutype(String n){
		hutype = n;
		return this;
	}
	
	public TelegrammBuilder source(String n){
		source = n;
		return this;
	}
	
	public TelegrammBuilder dest(String n){
		dest = n;
		return this;
	}
	
	public TelegrammBuilder mfs_error(String n){
		mfs_error = n;
		return this;
	}
	
	public TelegrammBuilder cs(String n){
		cs = n;
		return this;
	}
	
	public TelegrammBuilder csgr(String n){
		csgr = n;
		return this;
	}
	
	public TelegrammBuilder rsrc(String n){
		rsrc = n;
		return this;
	}
}
