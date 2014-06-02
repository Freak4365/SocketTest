package PLCEmulator;

public class warehouseTask {
	private String source;
	private String dest;
	private handlingUnit hu;
	private String confCp;
	
	public warehouseTask(String _hu, String _hutype, String _source, String _dest){
		setSource(_source);
		setDest(_dest);
		setHu(new handlingUnit(_hu, _hutype));
	}
	
	public warehouseTask(){
		setSource("");
		setDest("");
		setConfCp("");
	}
	
	/**
	 * @param in Telegram_WT
	 */
	public warehouseTask(Telegram_WT in){
		dest = in.getdest();
		source = in.getsource();
		hu = new handlingUnit(in.gethuid(), in.gethutype());
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the destination
	 */
	public String getDest() {
		return dest;
	}

	/**
	 * @param dest the destination to set
	 */
	public void setDest(String dest) {
		this.dest = dest;
	}

	/**
	 * @return the Handling Unit
	 */
	public handlingUnit getHu() {
		return hu;
	}

	/**
	 * @param hu the Handling Unit to set
	 */
	public void setHu(handlingUnit hu) {
		this.hu = hu;
	}
	
	/**
	 * @return the Handling Unit Type
	 */
	public String getHuType(){
		return hu.getType();
	}
	
	/**
	 * @return the Handling Unit ID
	 */
	public String getHuId(){
		return hu.getId();
	}

	/**
	 * @return the confCp
	 */
	public String getConfCp() {
		return confCp;
	}

	/**
	 * @param confCp the confCp to set
	 */
	public void setConfCp(String confCp) {
		this.confCp = confCp;
	}

}
