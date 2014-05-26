package PLCEmulator;

public class warehouseTask {
	//private handlingUnit hu;	
	private String source;
	private String dest;
	
	public warehouseTask(String _source, String _dest){
		setSource(_source);
		setDest(_dest);
	}
	
	public warehouseTask(){
		setSource("");
		setDest("");
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

}
