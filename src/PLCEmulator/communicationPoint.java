package PLCEmulator;

/**
 * @author André Kukla
 *
 */
public class communicationPoint {
	
	private String name;
	private int pos_x;
	private int pos_y;
	private String next;
	private String prev;
	private handlingUnit hu;
	private String error;
	
	public communicationPoint(String _name, int _x, int _y, String _prev, String _next, handlingUnit _hu, String _error){
		name = _name;
		pos_x = _x;
		pos_y = _y;
		prev = _prev;
		next = _next;
		hu = _hu;
		setError(_error);
	}
	
	public communicationPoint(){
		name = null;
		pos_x = -1;
		pos_y = -1;
		prev = null;
		next = null;
		hu = null;
		setError(null);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the pos_x
	 */
	public int getPos_x() {
		return pos_x;
	}
	/**
	 * @param pos_x the pos_x to set
	 */
	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}
	/**
	 * @return the pos_y
	 */
	public int getPos_y() {
		return pos_y;
	}
	/**
	 * @param pos_y the pos_y to set
	 */
	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
	/**
	 * @return the next
	 */
	public String getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(String next) {
		this.next = next;
	}
	/**
	 * @return the previous
	 */
	public String getPrev() {
		return prev;
	}
	/**
	 * @param prev the previous to set
	 */
	public void setPrev(String prev) {
		this.prev = prev;
	}
	/**
	 * @return the handling unit
	 */
	public handlingUnit getHu() {
		return hu;
	}
	/**
	 * @param hu the handling unit to set
	 */
	public void setHu(handlingUnit hu) {
		this.hu = hu;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
}
