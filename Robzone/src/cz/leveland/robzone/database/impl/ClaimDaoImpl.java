package cz.leveland.robzone.database.impl;

import java.util.List;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Claim;
import cz.leveland.robzone.database.entity.ClaimItem;
import cz.leveland.robzone.database.entity.dto.ClaimDto;



@Service
@EnableCaching
public class ClaimDaoImpl extends DaoImpl {
	
	private static final String QUERY_STR = "select id as id, claimNo as claimNo, delivered as delivered, userId as userId, " +
	"solverId as solverId, deliveryId as deliveryId, orderId as orderId, orderNo as orderNo, claimOrderNo as claimOrderNo, customer as customer, street as street, city as city, " +
	"message as message, created as created, finished as finished, solverName as solverName ";
	
	private static final int NEXT_REPAIR_BATCH_LENGTH = 10;
	

	public void saveOrUpdate (Claim claim) {
		 dao.saveOrUpdate(claim);
		 
	}
	
	public Claim get(int id) {
		
		return dao.get(Claim.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ClaimDto> getAll(int companyId) {
		
		return (List<ClaimDto>) dao.querySQLTransform(
				QUERY_STR
				+ "from v_claims where companyId=:companyId", 
				oneParam("companyId", companyId), ClaimDto.class);
	}

	@SuppressWarnings("unchecked")
	public List<ClaimDto> getRepairs(Integer solverId) {

		return (List<ClaimDto>) dao.querySQLTransform(
				QUERY_STR
				+ "from v_claims where solverId=:solverId", 
				oneParam("solverId", solverId), ClaimDto.class);
	}

	public int getUnfinished(int solverId) {
		
		@SuppressWarnings("unchecked")
		List<Claim> list = (List<Claim>) dao.query(
				"from Claim where solverId=:solverId and finished=false", 
				oneParam("solverId", solverId));
		return list.size();
		
	}

	public Claim getNextForRepair(int companyId) {
		

		
		@SuppressWarnings("unchecked")
		List<Claim> list = (List<Claim>) dao.queryMaxResults(
				"select distinct claim from Claim claim, ClaimItem ci where "
				+ "claim.id=ci.claimId and ci.action=:action and companyId=:companyId and "
				+ "finished=false order by created", 
				twoParams("action", ClaimItem.ACTION_REPAIR, "companyId", companyId), 
				NEXT_REPAIR_BATCH_LENGTH);
		return firstListElement(list);
		
	}

	public Object getRepairDetails(int claimId) {
		@SuppressWarnings("unchecked")
		List<Claim> list = (List<Claim>) dao.queryMaxResults(
				"select claim.id, claim.claimno, "
				+ "from Claim claim, ClaimItem ci where "
				+ "ci.claimId=claim.id and claim.id=:claimId", oneParam("claimId", claimId), 1);
		return firstListElement(list);
	}
	

	public List<Claim> getByDeliveryId(int deliveryId) {
		
		@SuppressWarnings("unchecked")
		List<Claim> list = (List<Claim>) dao.query(
				"from Claim where deliveryId=:deliveryId and finished=false", 
				oneParam("deliveryId", deliveryId));
		return list;
	}

	
	
	

}
