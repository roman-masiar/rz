package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.api.impl.OrderApiConnector;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.StockSet;
import cz.leveland.robzone.database.entity.StockSetItem;
import cz.leveland.robzone.database.entity.UnknownProduct;
import cz.leveland.robzone.database.entity.dto.OrderDto;
import cz.leveland.robzone.database.entity.dto.ProductSetItemDto;
import cz.leveland.robzone.database.entity.dto.Progress;
import cz.leveland.robzone.database.entity.interfaces.ProcessableOrder;
import cz.leveland.robzone.database.impl.InstockDaoImpl;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.OrderItemDaoImpl;
import cz.leveland.robzone.database.impl.ProductSetItemDaoImpl;
import cz.leveland.robzone.database.impl.StockItemDaoImpl;
import cz.leveland.robzone.database.impl.StockSetDaoImpl;
import cz.leveland.robzone.database.impl.StockSetItemDaoImpl;
import cz.leveland.robzone.exception.OrderNotFoundException;
import cz.leveland.robzone.exception.OrderProcessingException;
import cz.leveland.robzone.exception.OverAllocationException;
import cz.leveland.robzone.exception.StockAllocationException;
import cz.leveland.robzone.exception.StockSetCreationException;
import cz.leveland.robzone.exception.WrongDataException;
import cz.leveland.robzone.functionblocks.filters.StatusOrderFilter;
import cz.leveland.robzone.pusher.PushManager;
import cz.leveland.robzone.stock.Allocation;
import cz.leveland.robzone.stock.FreeStockAllocator;

@Service
public class TransactionComponents extends Agenda {
	
	private static final boolean PROCESS_ORDER_UPON_CREATION = false;

	private static final String AGENDA_NAME = "trans-comp";

	@Autowired
	OrderDaoImpl orderDao;

	@Autowired
	OrderItemDaoImpl orderItemDao;
	
	@Autowired
	PartnerAgenda partnerAgenda;
	
	
	@Autowired
	OrderApiConnector orderApiConnector;
	
	@Autowired
	StatusOrderFilter orderFilter;
	
	@Autowired
	StockItemDaoImpl stockItemDao;
	
	
	@Autowired
	StockAgenda stockAgenda;
	
	@Autowired
	StockSetDaoImpl stockSetDao;
	
	@Autowired
	StockSetItemDaoImpl stockSetItemDao;
	
	@Autowired
	ProductSetItemDaoImpl productSetItemDao;
	
	@Autowired
	InstockDaoImpl instockDao;
	
	@Autowired
	FreeStockAllocator freeStockAllocator;
	
	@Autowired
	PushManager pushManager;
	
	
	List<UnknownProduct> unkownProducts = new ArrayList<UnknownProduct>();
	int orderFlushCnt = 0, orderItemFlushCnt = 0;

	/**
	 * called from tradeAgenda - importOrders
	 * saves new order and makes all related work
	 *  	create partner for new order, 
	 *  	adds new order with its items 
	 *  	links order with pending credit record
	 *  	creates pick slips if goods is available
	 * @param orderData, containing partner / order information 
	 * @return new order
	 * @throws WrongInputException 
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 * @throws StockAllocationException 
	 * @throws OverAllocationException 
	 */
	@SuppressWarnings("unchecked")
	//@Transactional(rollbackFor = Exception.class)
	public ActionResult addNewOrder(Map<String, Object> orderData, int warehouseId) throws WrongInputException, OrderNotFoundException, StockAllocationException, OverAllocationException {
	
		
		
		Order order = (Order)orderData.get("order");
				
		/* adding partner */
		Partner partner = (Partner) orderData.get("partner");
		
		partnerAgenda.addPartner(partner);
		
		/* adding order */		
		order.setPartnerId(partner.getId());
		order.setPayerId(partner.getId());
		try {			
			orderDao.saveOrUpdate(order);
			if (orderFlushCnt++ % DaoImpl.FLUSH_BATCH_SIZE == 0)
				orderDao.flushAndClear();
			System.out.println("Saving order " + order.getOrderNo());			
		} catch (Exception e) {
			throw new WrongInputException();
		}
		
		/* adding order items */
		List<OrderItem> orderItems = (List<OrderItem>)orderData.get("orderItems");
		for (OrderItem one:orderItems) {
			one.setOrderId(order.getId());
			orderItemDao.saveOrUpdate(one);
				if (orderItemFlushCnt++ % DaoImpl.FLUSH_BATCH_SIZE == 0)
					orderItemDao.flushAndClear();
		}

		/* creates pick slips from stock */		
		if (PROCESS_ORDER_UPON_CREATION)
			processNewOrder(order, orderItems, warehouseId);
			
		return null;
		
	}
	
	




	/**
	 * called by add new order - simpler check if order may proceed without check of finance
	 * allocates order to instock records, creates outstock records (pick slips)
	 * condition for processing is simple - COD payment
	 * @param order
	 * @param orderItems
	 * @param warehouseId
	 * @throws StockAllocationException
	 * @throws OrderNotFoundException
	 */
	public void processNewOrder(ProcessableOrder order, List<OrderItem> orderItems, int warehouseId) throws StockAllocationException, OrderNotFoundException {
		
		if (order.getPaymentTypeId() == ProductType.TYPE_PAYMENT_COD)
			stockAgenda.allocateOrder(order, orderItems, warehouseId);
		
	}
	
	/**
	 * 
	 * allocates order to instock records
	 * @param order
	 * @param orderItems
	 * @throws OrderProcessingException 
	 * @throws WrongDataException 
	 * @throws Exception
	 */
	
	public void processOrder(OrderDto order, List<OrderItem> orderItems, int warehouseId) throws StockAllocationException  {
		
		
		int newStatus = orderFilter.getOrderStatus(order);
		
		if (newStatus == Order.STATUS_PROCESSED || newStatus == Order.STATUS_MISSING) 
			stockAgenda.allocateOrder(order, orderItems, warehouseId);
		
	}


	/**
	 * creates required qty of stock sets and allocates them to available stock
	 * check is done before creating if enough stock is available
	 * returns map with stockItem IDs and missing QTYs
	 * @param productId
	 * @param qty
	 * @param warehouseId
	 * @return
	 * @throws OverAllocationException
	 * @throws StockAllocationException
	 * @throws StockSetCreationException
	 */
	
	@Transactional
	public Map<Integer,Integer> stockSetFromProductSet(int productId, int qty, int warehouseId) throws OverAllocationException, StockAllocationException, StockSetCreationException {
		
		freeStockAllocator.init(warehouseId);
		
		List<ProductSetItemDto> ssItems = productSetItemDao.getByProductId(productId);
		
		/* check if there are enough available free instocks to cover the sets in desired qty */
		Map<Integer,Integer> missing = freeStockAllocator.checkFreeAvailability(ssItems, qty);
		if (!missing.isEmpty()) {
			return missing;
		}
		
		/* stock is enough, create sets and allocate them */
		for (int i=0; i<qty; i++) {
			
			StockSet stockSet = new StockSet(warehouseId, productId);
			stockSetDao.saveOrUpdate(stockSet);
			
			for (ProductSetItemDto psi:ssItems) {
				if (psi.isHasSerialNo()) {
				
					/* item has a serial number, only 1 pc will be allocated with no link to instock
					 * instead stockitemId will be supplied and instockId will be set when the set is physically picked and real serial no is known
					 * */
					StockSetItem item = new StockSetItem(stockSet.getId(), null, psi.getStockItemId(), 1);
					stockSetItemDao.saveOrUpdate(item);
				} else {
					
					/* no serial number, allocate to instocks directly */
					List<Allocation> allocations = freeStockAllocator.allocateInStockNonSerial(psi.getQty(), psi.getStockItemId());
					
					/* optional double-check of allocated amount, now turned on */
					checkAllocatedAmount(allocations, psi.getQty());
					
					/* create allocations for nonserial items */
					for (Allocation oneAlloc:allocations) {
						StockSetItem item = new StockSetItem(stockSet.getId(), oneAlloc.getSourceId(), null, oneAlloc.getQty());								
						stockSetItemDao.saveOrUpdate(item);
					}
				}				
			}
			
			/* report progress */
			pushManager.reportStockSetProgress(new Progress(qty, i+1, i+1, 0), getCompanyId());
		}
		return null;
	}



	/**
	 * double check if allocated amount is equal to total amount
	 * @param allocations
	 * @param qty
	 * @throws StockSetCreationException
	 */
	private void checkAllocatedAmount(List<Allocation> allocations, int qty) throws StockSetCreationException {
		
		int sum = 0;
		for (Allocation one:allocations)
			sum += one.getQty();
		if (sum < qty)
			throw new StockSetCreationException();		
	}
	
	
}
