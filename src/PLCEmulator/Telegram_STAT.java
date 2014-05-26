package PLCEmulator;

/*
 * Status telegrams, like STAT 
 * Subclass of Telegram
 */
public class Telegram_STAT extends Telegram {	
	
	private String _source;
	private String _dest;
	private String _mfs_error;
	private String _cs;
	private String _csgr;
	private String _rsrc;

	public Telegram_STAT(String _send, String _empf, String _cp,
			String _hndshk, int _numb, String _error, String _type, String _source, 
			String _dest, String _mfs_error, String _cs, String _csgr, String _rsrc) 
	{
		super(_send, _empf, _cp, _hndshk, _numb, _error, _type);
		setsource(_source);
		setdest(_dest);
		setmfs_error(_mfs_error);
		setcs(_cs);
		setcsgr(_csgr);
		setrsrc(_rsrc);
	}

	public Telegram_STAT(String in) {
		super(in);
		//Telegram structure
		_source = getFromTele(in, 64, 82);
		_dest = getFromTele(in, 82, 100);
		_mfs_error = getFromTele(in, 100, 104);
		_cs = getFromTele(in, 104, 114);
		_rsrc = getFromTele(in, 114, 128);
		//_csgr = getFromTele(in, 128, 138);
	}
	
	public String toString(){
		String source,dest,mfs_error,cs,rsrc; //,csgr
		
		String out = super.toStringShort();
		source = addDots(_source, 18);
		dest = addDots(_dest, 18);
		mfs_error = addDots(_mfs_error, 4);
		cs = addDots(_cs, 10);
		//csgr = addDots(_csgr, 10);
		rsrc = addDots(_rsrc, 14);
		
		out = out+source+dest+mfs_error+cs+rsrc;
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
	
	public String getmfs_error() {
		return _mfs_error;
	}

	public void setmfs_error(String _mfs_error) {
		this._mfs_error = _mfs_error;
	}

	public String getcs() {
		return _cs;
	}

	public void setcs(String _cs) {
		this._cs = _cs;
	}

	public String getcsgr() {
		return _csgr;
	}

	public void setcsgr(String _csgr) {
		this._csgr = _csgr;
	}

	public String getrsrc() {
		return _rsrc;
	}

	public void setrsrc(String _rsrc) {
		this._rsrc = _rsrc;
	}

}
