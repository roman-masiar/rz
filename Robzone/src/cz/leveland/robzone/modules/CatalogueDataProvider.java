package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.robzone.catalogue.CompanyCatalogue;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.Currency;
import cz.leveland.robzone.database.entity.IdHolder;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.dto.ProductDto;
import cz.leveland.robzone.database.impl.CountryDaoImpl;
import cz.leveland.robzone.database.impl.CurrencyDaoImpl;
import cz.leveland.robzone.database.impl.ProductCategoryDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.ProductTypeDaoImpl;
import cz.leveland.robzone.database.impl.VatCategoryDaoImpl;
import cz.leveland.robzone.database.impl.WarehouseDaoImpl;
import cz.leveland.robzone.exception.ProductNotFoundException;

@Service
public class CatalogueDataProvider {

	@Autowired
	CountryDaoImpl countryDao;

	@Autowired
	WarehouseDaoImpl warehouseDao;
	
	@Autowired
	CurrencyDaoImpl currencyDao;
	
	@Autowired
	ProductDaoImpl productDao;
	
	@Autowired
	ProductTypeDaoImpl productTypeDao;
	
	@Autowired
	ProductCategoryDaoImpl productCategoryDao;
	
	@Autowired
	VatCategoryDaoImpl vatCategoryDao;
	

	Map<Integer,CompanyCatalogue> companyCatalogues = new HashMap<Integer,CompanyCatalogue>();
	public List<Currency> currencyList;
	public Map<String,Currency> currencyMap = new HashMap<String,Currency>();
	public Map<Integer,Currency> currencyMapId = new HashMap<Integer,Currency>();
	

	@PostConstruct
	public void read() {
		currencyList = currencyDao.getAll();
		for (Currency one:currencyList) {
			currencyMap.put(one.getCode(), one);
			currencyMapId.put(one.getId(), one);
		}
	}
	
	public CompanyCatalogue get(int companyId) {
		return companyCatalogues.get(companyId);
	}
	

	public synchronized void readDataForCountry(int companyId, List<Integer> countries) {
		
		if (!companyCatalogues.containsKey(companyId)) {
			companyCatalogues.put(companyId, new CompanyCatalogue());
		}
		
		CompanyCatalogue catalogue = companyCatalogues.get(companyId);
		
		catalogue.productList = productDao.getAll(companyId, countries);
		catalogue.transportProductList = productDao.getTransportProducts(countries);
	}		

	
	public synchronized void readData(int companyId) {
		
		if (!companyCatalogues.containsKey(companyId)) {
			companyCatalogues.put(companyId, new CompanyCatalogue());
		}
		
		CompanyCatalogue catalogue = companyCatalogues.get(companyId);
		
		//catalogue.productList = productDao.getAll(companyId);
		//catalogue.transportProductList = productDao.getTransportProducts(companyId);
		//catalogue.makeCodProduct();
		
		catalogue.vatCategoryList = vatCategoryDao.getAll(companyId);		
		catalogue.countryList = countryDao.getAll(companyId);
		catalogue.warehouseList = warehouseDao.getByCompany(companyId);
		catalogue.productTypeList = productTypeDao.getAll(companyId);
		catalogue.nonGoodsTypeList = productTypeDao.getAll(companyId, true);
		catalogue.productCategoryList = productCategoryDao.getAll(companyId);
		catalogue.makeMaps();
		
	}
	
	public Map<String,Object> getOrderConversionData (int companyId){
		
		CompanyCatalogue catalogue = companyCatalogues.get(companyId);
		
		if (catalogue == null || catalogue.countryList == null)
			readData(companyId);
		
		/* add currency for any company */
		Map<String,Object>cData = catalogue.getOrderConversionData();
		cData.put("currencies", currencyMap);
		return cData;

	}
	
	public Map<String,Object> getData (int companyId){
		
		
		CompanyCatalogue catalogue = companyCatalogues.get(companyId);
		
		if (catalogue == null || catalogue.countryList == null)
			readData(companyId);
				
		
		Map<String,Object> result = catalogue.getData();
		result.put("currencies", currencyList);
		return result;
	}
	

	public int getCodProductId(int companyId) throws ProductNotFoundException {
		ProductDto product = (ProductDto) productDao.getCodProduct(companyId);
		if (product == null)
			throw new ProductNotFoundException();
		return product.getId();
	}

	public Currency getCurrency(int countryId, int companyId) {
		
		CompanyCatalogue catalogue = get(companyId);
		return currencyMapId.get(catalogue.getCountryMap().get(countryId).getCurrencyId());
	}

	public int getCurrencyId(int companyId, int countryId) {
		
		CompanyCatalogue catalogue = get(companyId);
		return currencyMapId.get(catalogue.getCountryMap().get(countryId).getCurrencyId()).getId();
	}
	
	public List<Integer> listToIds(List<?> list) {
		List<Integer>ids = new ArrayList<Integer>();
		for (Object one:list) {
			IdHolder idHolder = (IdHolder)one;
			ids.add(idHolder.getId());
		}
		return ids;

	}
	

}
