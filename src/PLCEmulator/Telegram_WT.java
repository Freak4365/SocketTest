package PLCEmulator;

/*
 * Warehouse task telegrams, like WT, WTCO and WTCC. 
 * Subclass of Telegram
 */
public class Telegram_WT extends Telegram {
	
	private String _huid;
	private String _hutype;
	private String _source;
	private String _dest;
	private String _mfs_error;

	public Telegram_WT(String _send, String _empf, String _cp, String _hndshk,
			int _numb, String _error, String _type, String _huid,
			String _hutype, String _source, String _dest, String _mfs_error) {
		
		super(_send, _empf, _cp, _hndshk, _numb, _error, _type);
		sethuid(_huid);
		sethutype(_hutype);
		setsource(_source);
		setdest(_dest);
		setmfs_error(_mfs_error);
	}

	public Telegram_WT(String in) {
		super(in);
		//Telegram structure
		_huid = getFromTele(in, 64, 84);
		_hutype = getFromTele(in, 84, 88);
		_source = getFromTele(in, 88, 106);
		_dest = getFromTele(in, 106, 124);
		_mfs_error = getFromTele(in, 124, 128);
		System.out.println("HU: "+_huid+"HU-Typ: "+_hutype+"Quelle: "+_source+"Ziel: "+_dest+"Fehler: "+_mfs_error);
	}
	
	public String toString(){
		String huid,hutype,source,dest,mfs_error;
		
		String out = super.toStringShort();
		huid = addDots(_huid, 20);
		hutype = addDots(_hutype, 4);
		source = addDots(_source, 18);
		dest = addDots(_dest, 18);
		mfs_error = addDots(_mfs_error, 4);
		
		out = out+huid+hutype+source+dest+mfs_error;
		return out;
	}
	
	public String getsource() {
		return _source;
	}

	public void setsource(String _source) {
		this._source = _source;
	}

	public String getdest() {
		return _dest;
	}

	public void setdest(String _dest) {
		this._dest = _dest;
	}
	
	public String gethuid() {
		return _huid;
	}

	public void sethuid(String _huid) {
		this._huid = _huid;
	}

	public String gethutype() {
		return _hutype;
	}

	public void sethutype(String _hutype) {
		this._hutype = _hutype;
	}

	public String getmfs_error() {
		return _mfs_error;
	}

	public void setmfs_error(String _mfs_error) {
		this._mfs_error = _mfs_error;
	}

}
