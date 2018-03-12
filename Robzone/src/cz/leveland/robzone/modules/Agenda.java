package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import cz.leveland.appbase.database.entity.User;
import cz.leveland.appbase.database.impl.UserDaoImpl;
import cz.leveland.appbase.usermanag.CustomUser;
import cz.leveland.appbase.util.RequestLogger;
import cz.leveland.appbase.util.ResponseData;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.setup.CCFilter;

public abstract class Agenda {
	
	
private static final boolean CONSOLE=true;
	
	protected static final int STATUS_OK = 0;
	protected static final int STATUS_USER_ERROR = 1;
	protected static final int STATUS_FAIL = 500;
	protected static final int STATUS_STOP = 2;
	
	protected static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
	static {
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Autowired	
	protected UserDaoImpl userDao;
	
	@Autowired
	RequestLogger requestLogger;
	
	/*
	@Autowired
	CurrentCompanies companies;
	*/
	
	@Autowired
	CCFilter ccFilter;
	
	
	
	
	public int getCompanyId() {
		return ccFilter.getCurrentCompany(getUserId());
	}
	
	public List<Integer> getCountryIds() {
		return ccFilter.getCurrentCountries(getUserId());
	}
	
	public Integer getUserId() {
		return getUser().getId();
	}
	
	public CustomUser getIdentity () {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUser userDetails = (CustomUser) auth.getPrincipal();
		        return userDetails;
		} else
			return null;
		
	}
	
	public User getUser() {
		
		if (isAuthenticated()) {
			CustomUser cu = getIdentity();
			if (cu==null)
				return null;
			User user=userDao.get(cu.getId());
			//user.setPassword(null);
			return user;
		} else
			return null;
	}
	
	public boolean isAuthenticated() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return false;
		return auth.isAuthenticated();
	}
	
	public void manualLogin(User user) {
		
		CustomUser userDetails = new CustomUser (user, AUTHORITIES);
	    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), AUTHORITIES);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	protected ResponseData responseDataResult(String key, String imageName) {
		
		ResponseData result = new ResponseData();
		result.put(key, imageName);
		return result;
	}
	
	protected ResponseData responseDataResult(String key, Object object) {
		
		ResponseData result = new ResponseData();
		result.put(key, object);
		return result;
	}

	
	protected ResponseObject buildResponse (Object responseData) {
		return buildResponse(responseData, STATUS_OK);
	}
	
	protected ResponseObject buildResponse (Object responseData, int status) {
		
		return new ResponseObject(responseData, status);
	}
	protected ResponseObject buildResponse (String dataName, Object data) {
		
		return new ResponseObject(responseDataResult(dataName, data), STATUS_OK);
	}
	
	protected ResponseObject buildErrorResponse (String message, int status) {
		
		return new ResponseObject(responseDataResult("error", message), status);
	}
	
	protected ResponseObject buildResponseOk () {
		return new ResponseObject(true, STATUS_OK);
	}
	
	protected <T> T getFirstElement(List<T> list) {
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}
	
	protected String bigText(String text) {
		return "<span style=\"font-size:1.5em;\">" + text + "</span>";
	}
	
	protected void console(String msg) {
		
		if (CONSOLE)
			System.out.println(msg);
	}
	
	protected void logAction(User user, Object inObject, Object curObject) {
		
	}
	
	protected void logError(String agenda, String method, String message) {
		requestLogger.addLog(agenda, "E: " + method + ": " + message, getUser(), null);
	}

	protected void logWarning(String agenda, String method, String message) {
		requestLogger.addLog(agenda, "W: " + method + ": " + message, getUser(), null);
	}
	
	protected Map<String,Object> importArgs(String name, Object arg) {
		
		Map<String,Object> res = new HashMap<String,Object>();
		res.put(name, arg);
		return res;
	}

	public double getMapDoubleZero(Map<String,Object> map, String key) {
		if (!map.containsKey(key))
			return 0.0d;
		
		Number val = (Number)map.get(key);
		if (val == null)
			return 0.0d;
		
		return val.doubleValue();
	}

}
