package cz.leveland.robzone.modules;



import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import cz.leveland.appbase.cors.TokenUtils;
import cz.leveland.appbase.database.entity.User;
import cz.leveland.appbase.database.exceptions.UserAlreadyExistsException;
import cz.leveland.appbase.database.exceptions.UserNotFoundException;
import cz.leveland.appbase.database.exceptions.WrongAmountException;
import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.database.exceptions.WrongPasswordException;
import cz.leveland.appbase.database.impl.resultbeans.UserDTO;
import cz.leveland.appbase.managers.SettingsManager;
import cz.leveland.appbase.modules.AppModule;
import cz.leveland.appbase.usermanag.UserInfo;
import cz.leveland.appbase.usermanag.UserManager;
import cz.leveland.appbase.util.InputSanitizer;
import cz.leveland.appbase.util.RequestLogger;
import cz.leveland.appbase.util.ResponseData;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.UserCompanyDaoImpl;
import cz.leveland.robzone.exception.CompanyNotFoundException;
import cz.leveland.robzone.exception.CountryNotFoundException;
import cz.leveland.robzone.pusher.PushManager;
import cz.leveland.robzone.setup.CCFilter;


// github


@Component
@EnableCaching
public class UserBusiness extends Agenda {

	private static final Log logger = LogFactory
			.getLog(UserBusiness.class);

	private static final String[] MAIL_CHECKLIST_SIGNUP = {"email"};
	
	/*
    @Autowired 
    CurrentCompanies currentCompanies;
    */
	
	@Autowired
	UserManager userManager;

	
	@Autowired
	RequestLogger requestLogger;

	@Autowired
	SettingsManager settingsManager;
	
	@Autowired
	ProductDaoImpl productDao;

	@Autowired
	UserCompanyDaoImpl userCompanyDao;
		
	
	@Autowired
	PushManager pushManager;
	
	@Autowired
	TradeAgenda tradeAgenda;
	
	@Autowired
	CCFilter ccFilter;
	
	@Autowired
	CatalogueDataProvider catalogue;

	Random random = new Random();
	//List<UserCompany> usersCompanies = new ArrayList<UserCompany>();
	
	public UserBusiness() throws Exception {
	}

	@PostConstruct
	public void setup() {
		//setupUserCompanies();
	}

	@Transactional(rollbackFor = Exception.class)
	public ResponseObject signUp(Map<String, Object> userData, String remoteAddr) throws UserAlreadyExistsException, Exception {

		String result = InputSanitizer.isMapPlainText(userData, MAIL_CHECKLIST_SIGNUP);
		if (result.length()>0)
			return buildErrorResponse(result, STATUS_USER_ERROR); 

		String name="", email = (String)userData.get("email");
		if (userData.containsKey("name")) {
			name = (String)userData.get("name");
		} else {
			String[] mailName = email.split("@");
			String mn = mailName[0];
			String[] firstName = mn.split("\\.");
			name = firstName[0];
		}
		if (name.length()>8)
			name = name.substring(0, 8);

		UserInfo userInfo = new UserInfo(userData);
		userInfo.setName(name);
		//userInfo.setMarketCode((String)userData.get("marketCode"));
		//userInfo.setPictureUrl(getAvatarUrl((String)userData.get("picture")));
		
		if (!userInfo.isSaveable())
			return buildResponse(responseDataResult("error", "credentials not complete or wrong"), STATUS_OK);


		int status=STATUS_OK;
		User user = userManager.addNewUser(userInfo);
		/*
		rewardManager.collectExisting(user);
		rewardManager.rewardInviter(user);
		*/

		manualLogin(user);
		//sendActivationEmail(user, remoteAddr);
		

		requestLogger.addLog("Register", userData.get("email") + "/" + userData.get("role"), getUser(), remoteAddr);
		//user.setPassword(null);

		return buildResponse(userAndToken(user), status);

	}
	

	public ResponseObject login (String credentials, String remoteAddr) {

		try {
			String decoded = new String(Base64.getDecoder().decode(credentials), StandardCharsets.UTF_8); 
			String[] split = decoded.split(":");
			if (split.length != 2)
				throw new WrongInputException("Incomplete credentials");

			String email = split[0];
			String password = split[1];

			if (!InputSanitizer.isPlainText(email) || !InputSanitizer.isPlainText(password))
				throw new WrongInputException("Incomplete credentials");

			User user = userManager.login(email, password);
			
			if (user == null)
				return buildErrorResponse("User not found", STATUS_USER_ERROR);

			requestLogger.addLog("Login", user.getEmail(), user, remoteAddr);
			/*
			Activity activity = activityManager.updateLogin(user, true);
			user.setActivityId(activity.getId());
			userDao.saveOrUpdate(user);
			ResponseData loginData = pageData("login", user);
			 */

			ResponseData loginData = userAndToken(user);
			loginData.put("profile", System.getProperty("PROFILE"));

			/* set current company for logged user to DEFAULT COMPANY */
			/* TODO */
			int companyId = ccFilter.getCurrentCompany(user.getId());
			loginData.copyDataFrom(ccFilter.getCompanyData(user.getId(), companyId));
			loginData.put("userCompanies", userCompanyDao.getAll(user.getId()));
			
			catalogue.readData(companyId);

			return buildResponse(loginData, STATUS_OK);

		} catch (UserNotFoundException e) {
			return buildErrorResponse("Login failed", STATUS_USER_ERROR);
		} catch (WrongPasswordException e) {
			return buildErrorResponse("Login failed", STATUS_USER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Login error, " + e.getMessage());
			return buildResponse("Login error", STATUS_FAIL);
		}
	}

	private ResponseData userAndToken(User user) throws WrongAmountException {

		ResponseData loginData = responseDataResult("user", new UserDTO(user));
		loginData.put("authToken", 
				TokenUtils.generateToken(user));
		
		return loginData;

	}


	/*
	public void setupUserCompanies() {
		
		usersCompanies = userCompanyDao.getAll();
	}
	*/
	
	

	public ResponseObject changePassword(Map<String, String> password) {

		String result = InputSanitizer.isMapPlainText(password);
		if (result.length()>0)
			return buildErrorResponse(result, STATUS_USER_ERROR); 

		try {
			User user = getUser();
			userManager.changePassword(user.getId(), password);
			return buildResponse(userAndToken(user), STATUS_OK);
		} catch (WrongPasswordException e) {
			return buildErrorResponse("wrong_password", STATUS_USER_ERROR);
		} catch (WrongInputException e) {
			return buildErrorResponse("pass_notmatch", STATUS_USER_ERROR);
		} catch (Exception e) {
			return buildErrorResponse("Changing of password failed", STATUS_FAIL);
		}
	}


	public ResponseObject basicInfo(String countryCode) {

		try {
			if (countryCode.length()>8)
				throw new Exception("Wrong market code");

			ResponseData pubInfo = new ResponseData();
			
			return buildResponse(pubInfo, STATUS_OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return buildErrorResponse("Error reading basic info", STATUS_OK);
		}
	}


	public ResponseObject getProfileData() throws WrongAmountException {

		User user = getUser();
		return buildResponse(userAndToken(user), STATUS_OK);
	}

	public ResponseObject ccFilterCompany(Map<String, Object> ccData) throws CompanyNotFoundException {
		
		Integer companyId = (Integer)ccData.get("companyId");
		Integer countryId = (Integer)ccData.get("countryId");
		Map<String,Object> companyData = null;
		
		companyData = ccFilter.setCurrentCompany(getUser().getId(), companyId);		
		return buildResponse(companyData, STATUS_OK);
	}

	public ResponseObject ccFilterCountry(Map<Integer, Boolean> ccData) throws CompanyNotFoundException, CountryNotFoundException {
		
		List<Integer>countryList = new ArrayList<Integer>();
		for (Integer cid:ccData.keySet())
			if (ccData.get(cid))
				countryList.add(cid);
		
		Map<String,Object> companyData = ccFilter.setCountries(getUserId(), countryList);		
		return buildResponse(companyData, STATUS_OK);
	}
	
	/*
	public List<UserCompany> getUsersCompanies() {
		return usersCompanies;
	}

	public void setUsersCompanies(List<UserCompany> usersCompanies) {
		this.usersCompanies = usersCompanies;
	}
	*/

	
	

	
	
	}
