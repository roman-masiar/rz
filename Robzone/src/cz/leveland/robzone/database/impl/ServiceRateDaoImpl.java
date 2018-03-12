package cz.leveland.robzone.database.impl;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Claim;
import cz.leveland.robzone.database.entity.ServiceRate;
import cz.leveland.robzone.database.entity.dto.ClaimDto;
import cz.leveland.robzone.database.entity.dto.LaborItemDto;
import cz.leveland.robzone.database.entity.dto.ServiceRateDto;



@Service
@EnableCaching
public class ServiceRateDaoImpl extends DaoImpl {
	
	private static final String QUERY_STR = "select id as id, claimNo as claimNo, userId as userId, type as type, " +
	"solverId as solverId, deliveryId as deliveryId, customer as customer, street as street, city as city, " +
	"message as message, created as created, finished as finished, solverName as solverName ";
	

	public void saveOrUpdate (ServiceRate serviceRate) {
		 dao.saveOrUpdate(serviceRate);
		 
	}
	
	public ServiceRate get(int id) {
		
		return dao.get(ServiceRate.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ServiceRateDto> getAll(int companyId) {
		
		return (List<ServiceRateDto>) dao.querySQLTransform(
				"select id as id, stockItemId as stockItemId, modelId as modelId, name as name, rate as rate, itemName as itemName, modelName as modelName "
				+ "from v_servicerates ", 
				null, ServiceRateDto.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<ServiceRate> getByModelId(int modelId) {
		
		return (List<ServiceRate>) dao.query(
				"from ServiceRate where stockItemId=:stockItemId", 
				oneParam("stockItemId", modelId));
	}
	
	@SuppressWarnings("unchecked")
	public List<LaborItemDto> getByClaimId(int claimId) {
		
		return (List<LaborItemDto>) dao.querySQLTransform(
				"select modelId as modelId, orderId as orderId, name as name, rate as rate "
				+ "from v_claim_servicerates where claimId=:claimId", 
				oneParam("claimId", claimId), LaborItemDto.class);
	}
	
	
}
