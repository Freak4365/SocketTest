package PLCEmulator;

/**
 * Parent class for all telegrams
 * @author André Kukla
 */
public class Telegram {
	
	private String _send;
	private String _empf;
	private String _cp;
	private String _hndshk;
	private int _numb;
	private String _mfs_error;
	private String _error;
	private String _type;
	
	
	public Telegram(String _send, String _empf, String _cp, String _hndshk, int _numb,
			String _error, String _type, String _mfserror)
	{
		setSend(_send);
		setEmpf(_empf);
		setCp(_cp);
		setHndshk(_hndshk);
		setNumb(_numb);
		setError(_error);
		setType(_type);
		setMfserror(_mfserror);
	}
	
	public Telegram(String in){
		//Telegram structure, change here and in subclasses.
		_send = getFromTele(in, 0, 8);
		_empf = getFromTele(in, 8, 16);
		_cp = getFromTele(in, 16, 34);
		_hndshk = getFromTele(in, 34, 36);
		_numb = stringToInt(getFromTele(in, 36, 56));
		_error = getFromTele(in, 56, 60);
		_type = getFromTele(in, 60, 64);
		_mfs_error = "";
	}
	
	/**
	 * Converts telegram to String, spaces are filled up with filler
	 * @return Telegram string
	 */
	public String toString(){
		String send,empf,cp,hndshk,numb,error,type, end, mfserror;
		
		send = addDots(_send, 8);
		empf = addDots(_empf, 8);
		cp = addDots(_cp, 18);
		hndshk = addDots(_hndshk, 2);
		numb = addDots(intToString(_numb), 20);
		error = addDots(_error, 4);
		type = addDots(_type, 4);
		end = addDots("", 60);
		mfserror = addDots(_mfs_error, 4);

		String out = send+empf+cp+hndshk+numb+error+type+end+mfserror;
		return out;
	}

	/**
	 * Converts Telegram to String, the end is not filled up with filler
	 * @return Telegram string
	 */
	public String toStringShort(){
		String send,empf,cp,hndshk,numb,error,type;
		
		send = addDots(_send, 8);
		empf = addDots(_empf, 8);
		cp = addDots(_cp, 18);
		hndshk = addDots(_hndshk, 2);
		numb = addDots(intToString(_numb), 20);
		error = addDots(_error, 4);
		type = addDots(_type, 4);
		
		String out = send+empf+cp+hndshk+numb+error+type;
		return out;
	}
	
	//fill string with dots to the given length
	protected String addDots(String in, int length){
		String out = in;
		while(out.length()<length){
			out = out + ".";
		}
		return out;
	}

	/**
	 * Returns String from start to end and removes all dots
	 * @param in input String
	 * @param start number of start position
	 * @param end number of end position
	 * @return output String
	 */
	protected String getFromTele(String in, int start, int end){
		String out = null;
		out = in.substring(start,end);
		out = removeDots(out);
		return out;
	}
		
	//Removes dots from String
	protected String removeDots(String in){
		String out = null;
		String[] split = in.split("\\.");
		if(split.length>0){
			out = split[0];
		}
		else{
			out = "";
		}
		return out;
	}
		
	//converts integer in to String
	protected String intToString(int in){
		String out = new Integer(in).toString();
		while(out.length()<20){
			out = "0"+out;
		}
		addDots(out, 20);
		return out;
	}
	
	protected int stringToInt(String in){
		int out = Integer.parseInt(in);
		return out;
	}
	
	/**
	 * Switch sender and receiver
	 */
	public void switchSendRec(){
		String temp = _send;
		_send = _empf;
		_empf = temp;
	}
	
	public void changeHndshk(){
		if(_hndshk.equals(SocketTest.Tele_hndshk_Req)){
			_hndshk = SocketTest.Tele_hndshk_Conf;
		}
		else{
			_hndshk = SocketTest.Tele_hndshk_Req;
		}
	}
	//getters and setters
	
	public String getSend() {
		return _send;
	}

	public void setSend(String send) {
		this._send = send;
	}

	public String getEmpf() {
		return _empf;
	}

	public void setEmpf(String empf) {
		this._empf = empf;
	}

	public String getCp() {
		return _cp;
	}

	public void setCp(String cp) {
		this._cp = cp;
	}

	public String getHndshk() {
		return _hndshk;
	}

	public void setHndshk(String hndshk) {
		this._hndshk = hndshk;
	}
	
	public int getNumb() {
		return _numb;
	}
	
	public void setNumb(int numb) {
		this._numb = numb;
	}

	public String getError() {
		return _error;
	}

	public void setError(String error) {
		this._error = error;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	/**
	 * @return the _mfserror
	 */
	public String getMfserror() {
		return _mfs_error;
	}

	/**
	 * @param _mfserror the _mfserror to set
	 */
	public void setMfserror(String _mfserror) {
		this._mfs_error = _mfserror;
	}

}