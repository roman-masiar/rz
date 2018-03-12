package cz.leveland.robzone.functionblocks.filters;

import java.util.List;

import cz.leveland.robzone.database.entity.dto.OrderDto;

public interface OrderFilter {
	
	public static final int TYPE_STATUS = 1;
	
	public List<OrderDto> filter(List<OrderDto> orders);

}
