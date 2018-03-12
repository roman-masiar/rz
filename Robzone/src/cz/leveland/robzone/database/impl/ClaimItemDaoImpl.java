package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.ClaimItem;
import cz.leveland.robzone.database.entity.dto.ClaimItemDto;



@Service
@EnableCaching
public class ClaimItemDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (ClaimItem claimItem) {
		 dao.saveOrUpdate(claimItem);		 
	}
	

	public ClaimItem get(int id) {
		
		return dao.get(ClaimItem.class, id);
	}
	
	public List<ClaimItem> getByClaimId(int claimId) {
		return findById("ClaimItem", "claimId", claimId);
	}

	@SuppressWarnings("unchecked")
	public List<ClaimItemDto> getByClaimIdDto(int claimId) {
		return (List<ClaimItemDto>) dao.queryTransform("select "
				+ "ci.instockId as instockId, si.name as name, inst.serialNo as serialNo, ci.qty as qty, ci.reason as reason, ci.action as action "
				+ "from ClaimItem ci, InStock inst, StockItem si "
				+ "where ci.instockId=inst.id and inst.stockItemId=si.id "
				+ "and claimId=:claimId", 
				oneParam("claimId", claimId), ClaimItemDto.class);
	}
	
	

}
