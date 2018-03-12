package cz.leveland.robzone.modules;

/**
 * result of action on one of items from set
 * when set is processed and error happenes, it is returned as and instance of this class and the processing continues
 * @author Roman
 *
 */

public class ActionResult {

	public static final int TYPE_SUCCESS = 0;
	public static final int TYPE_ERROR = 1;
	
	int type;
	Object itemId;
	String message;
	
	public ActionResult(int type, Object itemId, String message) {
		super();
		this.type = type;
		this.itemId = itemId;
		this.message = message;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getItemId() {
		return itemId;
	}
	public void setItemId(Object itemId) {
		this.itemId = itemId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isOk() {
		return type == TYPE_SUCCESS;
	}
	
	public boolean isError() {
		return type != TYPE_SUCCESS;
	}
	
	
	
}
