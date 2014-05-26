package PLCEmulator;

public class handlingUnit {
	
	private String id;
	private String type;
	
	public handlingUnit(String _id, String _type){
		setId(_id);
		setType(_type);
	}
	
	public handlingUnit(){
		setId("");
		setType("");
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
