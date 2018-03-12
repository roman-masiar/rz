package cz.leveland.robzone.modules;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.InputSanitizer;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.api.impl.OrderApiConnector;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.ProductSetItem;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.OrderItemDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.ProductSetItemDaoImpl;
import cz.leveland.robzone.database.impl.ProductTypeDaoImpl;
import cz.leveland.robzone.functionblocks.filters.StatusOrderFilter;
import cz.leveland.robzone.setup.CCFilter;

@Service
public class ProductAgenda extends Agenda{

	
	@Autowired
	OrderDaoImpl orderDao;

	@Autowired
	OrderItemDaoImpl orderItemDao;
	
	@Autowired
	StatusOrderFilter orderFilter;
	
	@Autowired
	StockAgenda stockAgenda;

	@Autowired
	PartnerAgenda partnerAgenda;
	
	@Autowired
	FinanceAgenda financeAgenda;
	
	@Autowired
	APIAgenda apiAgenda;
	
	@Autowired
	OrderApiConnector orderApiConnector;
	
	@Autowired
	TransactionComponents transactionComponents;
	
	/*
	@Autowired
	CurrentCompanies companies;
	*/
	
	@Autowired
	ProductDaoImpl productDao;

	@Autowired
	ProductSetItemDaoImpl productSetItemDao;
	
	@Autowired
	ProductTypeDaoImpl productTypeItemDao;
	
	
	@Autowired
	CatalogueDataProvider catalogueDataProvider;
	
	
	@Autowired
	CCFilter ccFilter;
	
	
	
	public static final boolean PROCESS_ORDER_UPON_CREATION = true;

	private static final String AGENDA_NAME = "trade";
	
	
	@PostConstruct
	public void setup() {
		
	}
	
	public ResponseObject getProduct(int productId) {
		
		return buildResponse("product", productDao.get(productId));
	}

	public ResponseObject getProducts() {
		
		return buildResponse("products", productDao.getAll(getCompanyId(), getCountryIds()));
	}
	
	public ResponseObject getProductSetItems(int productId) {		
		return buildResponse("productSetItems", productSetItemDao.getByProductId(productId));
	}

	public ResponseObject getNonGoodsProducts() {		
		return buildResponse("productTypes", productTypeItemDao.getAll(getCompanyId(), true));
	}
	
	public ResponseObject getSetProducts() {		
		
		return buildResponse("setProducts", productDao.getSetProducts(getCountryIds()));
	}
	

	/**
	 * called by client
	 * @param itemData inserted order item
	 * @return new list of order items
	 * @throws WrongInputException
	 */
	public ResponseObject addProduct(Map<String, Object> productData) throws WrongInputException {
		
		try {
			String code = (String)productData.get("code");
			String name = (String)productData.get("name");	
			
			if (!InputSanitizer.isPlainText(code) || !InputSanitizer.isPlainText(name))
				throw new WrongInputException();
			
			int countryId = (int)productData.get("countryId");
			boolean isSetType = (Boolean)productData.get("setProduct");
			int vatCategoryId = (Integer)productData.get("vatCategoryId");
			int productTypeId = (Integer)productData.get("productTypeId");
			int productCategoryId = (Integer)productData.get("productCategoryId");
			Integer stockItemId = (Integer)productData.get("stockItemId");
			if (isSetType)
				stockItemId = null;
			
			
			double price = ((Number)productData.get("price")).doubleValue();					
			int companyId = getCompanyId();
			
			Product product = new Product(companyId, countryId, vatCategoryId, productTypeId, productCategoryId, isSetType, code, name, price, stockItemId);			
			productDao.saveOrUpdate(product);
			return buildResponse("product", product);

		} catch (Exception e) {
			e.printStackTrace();
			logError(AGENDA_NAME, "addProduct", "product couldn't be saved " );
			throw new WrongInputException();
		}
		
	}

	
	/**
	 * called by client
	 * @param itemData inserted order item
	 * @return new list of order items
	 * @throws WrongInputException
	 */
	public ResponseObject updateProduct(Map<String, Object> productData) throws WrongInputException {
		
		Product product = null;
		try {
			Integer productId = (Integer)productData.get("id");
			String code = (String)productData.get("code");
			String name = (String)productData.get("name");	
			Number price = (Number)productData.get("price");
			
			if (productId == null || !InputSanitizer.isPlainText(code) || !InputSanitizer.isPlainText(name))
				throw new WrongInputException();
			
			product = productDao.get(productId);
			if (product == null)
				throw new WrongInputException();
			
			
			boolean isSetType = (Boolean)productData.get("setProduct");
			int vatCategoryId = (Integer)productData.get("vatCategoryId");
			int productTypeId = (Integer)productData.get("productTypeId");
			int productCategoryId = (Integer)productData.get("productCategoryId");
			Integer stockItemId = (Integer)productData.get("stockItemId");
			if (isSetType)
				stockItemId = null;

			product.setSetProduct(isSetType);
			product.setName(name);
			product.setCode(code);
			product.setPrice(price);
			product.setVatCategoryId(vatCategoryId);
			product.setProductTypeId(productTypeId);
			product.setProductCategoryId(productCategoryId);
			product.setStockItemId(stockItemId);
			product.validate();
			
			
			productDao.saveOrUpdate(product);
		} catch (Exception e) {
			e.printStackTrace();
			logError(AGENDA_NAME, "addProduct", "product couldn't be saved " );
			throw new WrongInputException();
		}
		return buildResponse("product", product);
	}
	
	
	
	
	
	/**
	 * deletes product
	 * @param productData
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject removeProduct(Map<String, Object> productData) {
		
		Integer productId = (Integer)productData.get("productId");
		productSetItemDao.deleteByProduct(productId);
		productDao.delete(productId);
		return getProducts();
	}
	

	/**
	 * called by client
	 * adds an item to a product set
	 * @param setItemData
	 * @return list of product set items
	 * @throws WrongInputException 
	 */
	public ResponseObject addSetItem(Map<String, Object> setItemData) throws WrongInputException {

		Integer productId = (Integer)setItemData.get("productId");
		Integer stockItemId = (Integer)setItemData.get("stockItemId");			
		Integer qty = ((Number)setItemData.get("qty")).intValue();
		
		if (productId == null || productId <= 0 || stockItemId == null || stockItemId == 0 || qty == null || qty <= 0)
			throw new WrongInputException();
		
		ProductSetItem setItem = new ProductSetItem(productId, stockItemId, qty);
		try {
			productSetItemDao.saveOrUpdate(setItem);
		} catch (Exception e) {
			logError(AGENDA_NAME, "addProduct", "product set item couldn't be saved, stockItemId:" + stockItemId + ", sitem: " + stockItemId);
			throw new WrongInputException();
		}
		return getProductSetItems(productId);
	}

	public ResponseObject deleteSetItem(Map<String, Object> setItemData) throws WrongInputException {
		Integer productSetItemId = (Integer)setItemData.get("productSetItemId");
		Integer productId = (Integer)setItemData.get("productId");
		
		if (productSetItemId == null || productSetItemId <= 0 || productId == null || productId <= 0)
			throw new WrongInputException();
		
		
		try {
			ProductSetItem item = productSetItemDao.get(productSetItemId);
			if (item.getProductId() != productId)
				throw new WrongInputException();

			productSetItemDao.delete(productSetItemId);
			
		} catch (Exception e) {
			logError(AGENDA_NAME, "addProduct", "product set item couldn't be deleted, productSetItemId:" + productSetItemId);
			throw new WrongInputException();
		}
		return getProductSetItems(productId);
	}

	
}
