package cz.leveland.robzone.database.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.ProductNature;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.dto.ProductDto;



@Service
@EnableCaching
public class ProductDaoImpl extends DaoImpl {
	
	
	private static final String QUERY_PRODUCT = "select prod.id as id, prod.companyid as companyId, prod.countryid as countryId, "
			+ "prod.productnatureid as productNatureId, prod.productTypeId as productTypeId, prod.productcategoryid as productCategoryId, prod.providerId as providerId, prod.productCategoryName as productCategoryName, prod.price as price, " 
			+ "prod.setProduct as setProduct, prod.vatName as vatName, prod.active as active, prod.hasserialno as hasSerialNo, prod.stockItemId as stockItemId, " 
			+ "prod.name as name, prod.code as code, prod.image as image ";


	public void flushAndClear() {
		dao.flushAndClear();
	}
	
	
	
	public void saveOrUpdate (Product product) {
		 dao.saveOrUpdate(product);
		 
	}
	
	public Product get(int id) {
		return dao.get(Product.class, id); 
	}
	
	/**
	 * 
	 * finds stockItem for orderItem
	 * used to verify if outstock is allocated to instock holding correct product
	 * @param orderItemId
	 * @return
	 */
	public Product findForOrderItem(int orderItemId) {
		@SuppressWarnings("unchecked")
		List<Product> list = (List<Product>) dao.query("select prod from "
				+ "Product prod, OrderItem oi "
				+ "where oi.id=:orderItemId and oi.productId=prod.id", oneParam("orderItemId", orderItemId));
		return firstListElement(list);
	}
	

	
	

	@SuppressWarnings("unchecked")
	public ProductDto getdTO(int id) {
		
		List<ProductDto> list = (List<ProductDto>) dao.querySQLTransform(

				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where id=:id "				
				, oneParam("id", id), ProductDto.class);
		return firstListElement(list);
	}


	@SuppressWarnings("unchecked")
	public List<ProductDto> getAll(int companyId, int countryId) {
		
		return (List<ProductDto>) dao.querySQLTransform(
				
				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where companyId=:companyId and countryId=:countryId order by prod.id"				
				, twoParams("companyId", companyId, "countryId", countryId), ProductDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductDto> getTransportProducts(int countryId) {
		
		int productNatureId = ProductNature.TYPE_TRANSPORT;
		
		return (List<ProductDto>) dao.querySQLTransform(
				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where countryId=:countryId and productNatureId=:productNatureId order by prod.id"				
				, twoParams("countryId", countryId, "productNatureId", productNatureId), 
				ProductDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public ProductDto getCodProduct(int companyId) {
//TODO - but, country also needed
		
		List<ProductDto> list = (List<ProductDto>) dao.querySQLTransform(
				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where companyId=:companyId and productTypeId=:productTypeId order by prod.id"				
				, twoParams("companyId", companyId, "productTypeId", ProductType.TYPE_PAYMENT_COD), 
				ProductDto.class);
		
		return firstListElement(list);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductDto> getTransportProducts(List<Integer> countries) {

		int productNatureId = ProductNature.TYPE_TRANSPORT;
		String inStr = createFilterList(countries);
		
		return (List<ProductDto>) dao.querySQLTransform(
				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where countryId in " + inStr + " and productNatureId=:productNatureId "
				+ "and countryId " 
				+ "order by prod.id"				
				, oneParam("productNatureId", productNatureId), 
				ProductDto.class);
	}
	
	

	public void delete(int productId) {
		
		dao.execute("delete from Product where id=:id", oneParam("id", productId));
		
	}

	public void remove(int companyId, int productId) {
		
		dao.execute("update Product set removed=true where companyId=:companyId and id=:id", twoParams("companyId", companyId, "id", productId));
		
	}



	@Cacheable("productnature")
	public ProductDto getProductNature(int productId) {
		
		System.out.println("Product dao: getProductNature " + productId);
		@SuppressWarnings("unchecked")
		List<ProductDto> list = (List<ProductDto>) dao.querySQLTransform(
				"select pn.id as id, pn.producttypeid as productTypeId, pn.productnatureid as productNatureId, pn.setProduct as setProduct, pn.active as active, pn.stockItemId as stockItemId "
				+ "from v_product_nature pn where pn.id=:productId", oneParam("productId", productId), ProductDto.class);
		return firstListElement(list);
	}
	
	
	public List<Product> getSetProducts(int countryId) {
		
		
		@SuppressWarnings("unchecked")
		List<Product> list = (List<Product>) dao.query(
				"from Product where countryId=:countryId and setProduct=true", oneParam("countryId", countryId));
		return list;
	}

	
	public Object getSetProducts(List<Integer> countryIds) {
		
		String inStr = createFilterList(countryIds);
		@SuppressWarnings("unchecked")
		List<Product> list = (List<Product>) dao.query(
				"from Product where countryId in " + inStr + " and setProduct=true", null);
		return list;
	}
	
	



	public Map<String, ProductDto> getProductMap(int companyId, int countryId) {
		List<ProductDto> list = getAll(companyId, countryId);
		Map<String, ProductDto> map = new HashMap<String, ProductDto>();
		for (ProductDto one:list)
			map.put(one.getCode(), one);
		return map;
	}



	@SuppressWarnings("unchecked")
	public List<ProductDto> getAll(int companyId, List<Integer> currentCountries) {
			
		String inStr = createFilterList(currentCountries);
		return (List<ProductDto>) dao.querySQLTransform(
				
				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where companyId=:companyId and countryId in " + inStr				
				, oneParam("companyId", companyId), ProductDto.class);
	}


	@SuppressWarnings("unchecked")
	public List<ProductDto> getServiceProducts(int companyId, List<Integer> currentCountries) {
		
		String inStr = createFilterList(currentCountries);
		int productTypeId = ProductType.TYPE_PAYMENT_PARTS;
		return (List<ProductDto>) dao.querySQLTransform(
				
				QUERY_PRODUCT
				+ "from v_products prod "
				+ "where prod.productTypeId=:productTypeId and companyId=:companyId and countryId in " + inStr				
				, twoParams("companyId", companyId, "productTypeId", productTypeId), ProductDto.class);
	}
	
	

	public Product getByStockItemId(Integer stockItemId) {
		
		return findById("Product", "stockItemId", stockItemId);
	}



	@SuppressWarnings("unchecked")
	public Product getServiceLabour(Integer userId) {
		
		List<Product> list = (List<Product>) dao.query("from Product "
				+ "where productTypeId=:productTypeId and providerId=:providerId",
				twoParams("productTypeId", ProductType.TYPE_PAYMENT_LABOR, "providerId", userId));
		return firstListElement(list);
	}



	




}
