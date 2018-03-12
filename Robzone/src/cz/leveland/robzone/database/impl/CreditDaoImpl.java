package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Credit;
import cz.leveland.robzone.database.entity.dto.CreditDto;



@Service
@EnableCaching
public class CreditDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (Credit payment) {
		 dao.saveOrUpdate(payment);
	}
	
	public Credit get(int id) {
		
		return dao.get(Credit.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<CreditDto> getAll(int companyId) {
		return (List<CreditDto>) dao.querySQLTransform(
				"select cre.id as id, cre.refNo as refNo, cre.creditno as creditNo, cre.orderId as orderId, cre.partnerid as partnerId, "
				+ "cre.created as created, cre.amount as amount, cre.approved as approved "
				+ "from v_credits cre "
				+ "where companyid=:companyId", 
				oneParam("companyId", companyId), CreditDto.class);
	}
	
	

	public void delete(int creditId) {
		
		dao.execute("delete from Credit where id=:id", oneParam("id", creditId));		
	}


	public Credit getByRefNo(String refNo) {
		return findByColumn(Credit.class.getName(), "refNo", refNo);
		
	}


	public Credit getByCreditNo(String creditNo) {
		return findByColumn(Credit.class.getName(), "creditNo", creditNo);		
	}
	
	


	
	
	

}
