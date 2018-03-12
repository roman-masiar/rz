package cz.leveland.robzone.catalogue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.Currency;
import cz.leveland.robzone.database.entity.ProductCategory;
import cz.leveland.robzone.database.entity.VatCategory;
import cz.leveland.robzone.database.entity.Warehouse;
import cz.leveland.robzone.database.entity.dto.ProductDto;
import cz.leveland.robzone.database.entity.dto.ProductTypeDto;

public class CompanyCatalogue {

	
	/* only for one selected country */
	public List<ProductDto> productList;
	public List<ProductDto> transportProductList;
	
	/* for all countries */
	public List<VatCategory> vatCategoryList;
	public List<Country> countryList;
	public Map<Integer,Country> countryMap = new HashMap<Integer,Country>();
	public List<Warehouse> warehouseList;
	public List<ProductTypeDto> productTypeList, nonGoodsTypeList;
	public List<ProductCategory> productCategoryList;
	
	
	
	public void makeMaps() {
		
		for (Country one:countryList) {
			countryMap.put(one.getId(), one);
		}
		
	}
	
	public Map<String,Object> getOrderConversionData (){
		
		Map<String,ProductDto> products = new HashMap<String,ProductDto>();
		Map<String,ProductTypeDto> nonGoodsTypes = new HashMap<String,ProductTypeDto>();
		Map<String,Currency> currencies = new HashMap<String,Currency>();
		Map<String,Country> countries = new HashMap<String,Country>();
		
		/*		
		for (ProductDto one:productList)
			products.put(one.getCode(), one);
		*/
		
		for (ProductTypeDto one:nonGoodsTypeList)
			nonGoodsTypes.put(one.getCode(), one);
				
		
		for (Country one:countryList)
			countries.put(one.getCode(), one);		
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("currencies", currencies);
		result.put("countries", countries);
		result.put("products", products);
		result.put("nonGoodsTypes", nonGoodsTypes);
		
		return result;
	}
	
	public Map<String,Object> getData (){
				
		Map<String,Object> result = new HashMap<String,Object>();
		
		result.put("countries", countryList);
		result.put("products", productList);
		result.put("transportProducts", transportProductList);
		result.put("productTypes", productTypeList);
		result.put("productCategories", productCategoryList);
		result.put("vatCategories", vatCategoryList);
		return result;
	}



	
	public List<Country> getCountryList() {
		return countryList;
	}

	/*
	
	public void makeCodProduct() {
		for (ProductDto one:productList)
			if (one.getProductTypeId() == ProductType.TYPE_PAYMENT_COD)
				codProduct = one.getId();
	}
	
	public int getCodProduct() {		
		return codProduct;
	}
	public List<ProductDto> getProductList() {
		return productList;
	}
	
	public List<VatCategory> getVatCategoryList() {
		return vatCategoryList;
	}
	*/

	public List<ProductTypeDto> getProductTypeList() {
		return productTypeList;
	}

	public List<ProductTypeDto> getNonGoodsTypeList() {
		return nonGoodsTypeList;
	}

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public Map<Integer, Country> getCountryMap() {
		return countryMap;
	}


	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}


	
}
