package cz.leveland.robzone.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.robzone.database.entity.Company;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.Currency;
import cz.leveland.robzone.database.entity.IdHolder;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.Warehouse;

import cz.leveland.robzone.database.impl.CompanyDaoImpl;
import cz.leveland.robzone.database.impl.CountryDaoImpl;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;
import cz.leveland.robzone.database.impl.UserCompanyDaoImpl;
import cz.leveland.robzone.database.impl.WarehouseDaoImpl;
import cz.leveland.robzone.exception.CompanyNotFoundException;
import cz.leveland.robzone.exception.CountryNotFoundException;
import cz.leveland.robzone.modules.CatalogueDataProvider;
import cz.leveland.robzone.modules.UserBusiness;

@Service
public class CCFilter {
	
	@Autowired
	UserBusiness userBusiness;
	
	@Autowired
	UserCompanyDaoImpl userCompanyDao;
	
	@Autowired
	PartnerDaoImpl partnerDao;
	
	@Autowired
	CountryDaoImpl countryDao;
	
	@Autowired
	CompanyDaoImpl companyDao;
	
	@Autowired
	WarehouseDaoImpl warehouseDao;
	
	@Autowired
	CatalogueDataProvider catalogue;
	
	private Map<Integer,Integer> companies = new HashMap<Integer,Integer>();
	private Map<Integer,List<Company>> userCompanies = new HashMap<Integer,List<Company>>();
	
	private Map<String,List<Integer>> countries = new HashMap<String,List<Integer>>();
	private Map<Integer,List<Country>> companyCountries = new HashMap<Integer,List<Country>>();
	
	
	private List<Warehouse> countryWarehouses = new ArrayList<Warehouse>();
	
	private Map<Integer,Currency> currencies = new HashMap<Integer,Currency>();
	
	
	/**
	 * sets current company for user and loads related countries for that company
	 * @param userId
	 * @param companyId
	 * @throws CompanyNotFoundException
	 */
	public Map<String,Object> setCurrentCompany(int userId, int companyId) throws CompanyNotFoundException {
		
		Map<String,Object> companyData = new HashMap<String,Object>();
		synchronized(companies) {
			Company company = companyDao.get(companyId);
			if (company == null)
				throw new CompanyNotFoundException();
			companies.put(userId, companyId);			
			List<Country>countryList = countryDao.getAll(companyId);
			companyCountries.put(companyId, countryList);		
			
			readWarehousesForCompany(companyId);			
			catalogue.readDataForCountry(getCurrentCompany(userId), catalogue.listToIds((List<?>)countryList));
			
			return getCompanyData(userId, companyId);
			
		}
	}
	
	/**
	 * sets current country for user and loads related warehouses
	 * @param userId
	 * @param countryId
	 * @throws CountryNotFoundException 
	 */
	public Map<String,Object> setCountries(int userId, List<Integer> countryList) throws CountryNotFoundException {
		int companyId = getCurrentCompany(userId);
		synchronized(countries) {
			for (Integer cid:countryList) {
				Country country = countryDao.get(cid);
				if (country == null)
					throw new CountryNotFoundException();
			}
			countries.put(ccKey(userId, companyId), countryList);
			readWarehousesForCompany(companyId);
			catalogue.readDataForCountry(getCurrentCompany(userId), countryList);
			
			return getCompanyData(userId, companyId);
			
		}
	}

	public Map<String,Object> getCompanyData(int userId, int companyId) {
		Map<String,Object> companyData = new HashMap<String,Object>();
		List<Country> listOfCountries;
		if (!companyCountries.containsKey(companyId))
			listOfCountries = countryDao.getAll(companyId);
		else
			listOfCountries = companyCountries.get(companyId);
		
		Partner companyPartner = partnerDao.findById("Partner", "companyId", companyId);
		companyData.put("selectedCompany", getCurrentCompany(userId));
		companyData.put("countries", listOfCountries);
		companyData.put("selectedCountries", getCurrentCountries(userId));
		companyData.put("warehouses", countryWarehouses);
		companyData.put("companyPartner", companyPartner);
		
		
		return companyData;
	
	}

	private void readWarehousesForCompany(int companyId) {
		
		countryWarehouses = warehouseDao.getByCompany(companyId);
		
	}

	/**
	 * returns currently set companyId for user
	 * @param userId
	 * @return
	 */
	public Integer getCurrentCompany(int userId) {
		if (!companies.containsKey(userId)) {
			Integer cid = getDefaultUserCompany(userId);			
			try {
				setCurrentCompany(userId, cid);
			} catch (CompanyNotFoundException e) {
				
			}
			return cid;
		} else
			return companies.get(userId);
	}

	/**
	 * returns selected countries for user
	 * if no selection available, makes list with one default country
	 * @param userId
	 * @return
	 */
	public List<Integer> getCurrentCountries(int userId) {
		
		int companyId = getCurrentCompany(userId);
		if (!countries.containsKey(ccKey(userId, companyId))) {
			Integer cid = getDefaultCompanyCountry(userId);
			List<Integer> list = new ArrayList<Integer>();
			list.add(cid);
			try {
				setCountries(userId, list);
			} catch (CountryNotFoundException e) {
				
			}
			return list;
		} else
			return countries.get(ccKey(userId, companyId));
	}
	
	private String ccKey(int userId, int companyId) {
		return userId+"."+companyId;
	}
	
	
	/**
	 * gets selected user country only if it is the only selected one, otherwise return null
	 * @param userId
	 * @return
	 */
	public Integer getOneCurrentCountry(int userId) {
		
		int companyId = getCurrentCompany(userId);
		List<Integer> userCountries = countries.get(ccKey(userId, companyId));
		if (userCountries.size() != 1)
			return null;
		return userCountries.get(0);
	}
	
	/**
	 * gets user one default company preset in the system
	 * @param userId
	 * @return
	 */
	public Integer getDefaultUserCompany(int userId) {
		List<Company> companies = userCompanyDao.getAll(userId);
		userCompanies.put(userId, companies);
		
		for (Company one:companies)			
				return one.getId();
		return null;
	}
	
	/**
	 * returns first country available for company
	 * @param userId
	 * @return
	 */
	
	public Integer getDefaultCompanyCountry(int userId) {
		int companyId = getCurrentCompany(userId);
		List<Country> list = countryDao.getAll(companyId);
		
		return list.get(0).getId();
	}
	
	
	/**
	 * gets system defined companies available to user
	 * @param userId
	 * @return
	 */
	public List<Company> getUserCompanies(int userId) {
		return userCompanies.get(userId);
	}
	
		

}
