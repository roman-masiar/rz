package cz.leveland.robzone.database.impl;

import java.util.List;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Delivery;
import cz.leveland.robzone.database.entity.DeliveryPackage;
import cz.leveland.robzone.database.entity.dto.DeliveryDto;
import cz.leveland.robzone.database.entity.dto.DeliveryPackageDto;
import cz.leveland.robzone.database.entity.dto.OrderDto;



@Service
@EnableCaching
public class DeliveryPackageDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (DeliveryPackage pack) {
		 dao.saveOrUpdate(pack);
		 
	}

	public DeliveryPackage get(int id) {
		
		return dao.get(DeliveryPackage.class, id);
	}
	

	

	public void delete(int id) {
		
		dao.execute("delete from DeliveryPackage where id=:id", oneParam("id", id));
		
	}


	@SuppressWarnings("unchecked")
	public List<DeliveryPackage> getAll(int companyId) {
				
		return (List<DeliveryPackage>) dao.query(
				"from DeliveryPackage where companyId=:companyId", 
				oneParam("companyId", companyId));
	}


	public DeliveryPackage getByPackageNo(String packageNo) {
		
		return findByColumn("DeliveryPackage", "number", packageNo);
		
	}
	
	public DeliveryPackage getLast(int transporterId, int type) {
		@SuppressWarnings("unchecked")
		List<DeliveryPackage> list = (List<DeliveryPackage>) dao.queryMaxResults(
				"from DeliveryPackage where transporterId=:transporterId and type=:type "
				+ "order by id desc", 
				twoParams("transporterId", transporterId, "type", type), 1);
		return firstListElement(list);
	}

	public List<DeliveryPackage> getUnlinked(String orderNo, int companyId) {
		
		@SuppressWarnings("unchecked")
		List<DeliveryPackage> list = (List<DeliveryPackage>) dao.query("from DeliveryPackage where companyId=:companyId and orderNo=:orderNo and deliveryId=null",
				twoParams("orderNo", orderNo, "companyId", companyId));
		
		return list;
	}

	public List<DeliveryPackageDto> getUnlinkedByTransporter(int companyId, int transporterId) {
		
		@SuppressWarnings("unchecked")
		List<DeliveryPackageDto> list = (List<DeliveryPackageDto>) dao.querySQLTransform(
				"select type as type, packageNumber as packageNumber, orderno as orderNo, name as name, familyname as familyName, "
				+ "street as street, city as city, zip as zip, country as country, phone as phone, "
				+ "paymentTypeId as  paymentTypeId, orderPrice as orderPrice, creditAmount as creditAmount "
				+ "from v_packages_unlinked "
				+ "where companyId=:companyId and transporterId=:transporterId",
				twoParams("companyId", companyId, "transporterId", transporterId), DeliveryPackageDto.class);
		
		return list;
	}

	
	
	

}
