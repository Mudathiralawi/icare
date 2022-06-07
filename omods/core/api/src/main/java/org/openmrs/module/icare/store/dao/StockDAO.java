package org.openmrs.module.icare.store.dao;

// Generated Oct 7, 2020 12:49:21 PM by Hibernate Tools 5.2.10.Final

import org.hibernate.Query;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.module.icare.core.Item;
import org.openmrs.module.icare.core.dao.BaseDAO;
import org.openmrs.module.icare.store.models.OrderStatus;
import org.openmrs.module.icare.store.models.Stock;

import java.util.*;

/**
 * Home object for domain model class StStock.
 * 
 * @author Hibernate Tools
 * @see org.openmrs.module.icare.store.models.Stock
 */
public class StockDAO extends BaseDAO<Stock> {
	
	public List<Stock> getStockByItemId(String itemUuid) {
		
		DbSession session = this.getSession();
		String queryStr = "SELECT st \n" + "FROM Stock st \n"
		        + "WHERE st.item = (SELECT im FROM Item im WHERE im.uuid = :itemUuid AND im.stockable = true) \n";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("itemUuid", itemUuid);
		
		return query.list();
	}
	
	public List<Stock> getStockByDrugId(String drugUuid) {
		
		DbSession session = this.getSession();
		String queryStr = "SELECT st \n"
		        + "FROM Stock st \n"
		        + "WHERE st.item = (SELECT im FROM Item im WHERE im.drug = (SELECT d FROM Drug d WHERE d.uuid= :drugUuid) AND im.stockable = true) \n";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("drugUuid", drugUuid);
		
		return query.list();
	}
	
	public List<Stock> getStockByItemAndLocation(String itemUuid, String locationUuid) {
		DbSession session = this.getSession();
		String queryStr = "SELECT st \n" + "FROM Stock st \n"
		        + "WHERE st.item = (SELECT it FROM Item it WHERE it.uuid = :itemUuid) "
		        + "AND st.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)\n"
		        + "AND st.item.stockable = true ORDER BY st.expiryDate DESC";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("itemUuid", itemUuid);
		query.setParameter("locationUuid", locationUuid);
		
		return query.list();
	}
	
	public List<Stock> getStockByItemAndLocations(String itemUuid, List<String> locationUuids) {
		/*DbSession session = this.getSession();
		String locations = "";
		for(String location: locationUuids){
			if(!locations.equals("")){
				locations += ",";
			}
			locations += "'" +location +"'";
		}
		String queryStr = "SELECT st \n" + "FROM Stock st \n"
				+ "WHERE st.item = (SELECT it FROM Item it WHERE it.uuid = :itemUuid) "
				+ "AND st.location = (SELECT l FROM Location l WHERE l.uuid IN (:locationUuids))\n"
				+ "AND st.item.stockable = true";

		Query query = session.createQuery(queryStr);
		query.setParameter("itemUuid", itemUuid);
		query.setParameter("locationUuids", locations);*/

        List<Stock> stockList = new ArrayList<>();

        for (String location : locationUuids) {
            stockList.addAll(getStockByItemAndLocation(itemUuid, location));
        }
        return stockList;
    }
	
	public List<Stock> getStockByDrugAndLocation(String drugUuid, String locationUuid) {
		DbSession session = this.getSession();
		String queryStr = "SELECT st FROM Stock st \n"
		        + "WHERE st.item = (SELECT it FROM Item it WHERE it.drug = (SELECT d FROM drug d WHERE d.uuid=:drugUuid)) "
		        + "AND st.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)\n"
		        + "AND st.item.stockable = true";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("drugUuid", drugUuid);
		query.setParameter("locationUuid", locationUuid);
		
		return query.list();
	}
	
	public List<Stock> getStockByItemLocation(String itemUuid, String locationUuid) {
		
		DbSession session = this.getSession();
		Date expireDate = new Date();
		System.out.println("dates expiry");
		System.out.println(expireDate);
		
		String queryStr = "SELECT st \n" + "FROM Stock st \n"
		        + "WHERE st.item = (SELECT it FROM Item it WHERE it.uuid = :itemUuid) "
		        + "AND st.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)\n"
		        + "AND st.item.stockable = true AND st.expiryDate >= :expireDate";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("itemUuid", itemUuid);
		query.setParameter("locationUuid", locationUuid);
		query.setParameter("expireDate", expireDate);
		
		return (List<Stock>) query.list();
		
	}
	
	public Stock getStockByItemBatchExpDateLocation(String itemUuid, String batch, Date expireDate, String locationUuid) {
		DbSession session = this.getSession();
		
		if (batch == null && expireDate == null) {
			
			expireDate = new Date();
			System.out.println("dates expiry");
			System.out.println(expireDate);
			
			String queryStr = "SELECT st \n" + "FROM Stock st \n"
			        + "WHERE st.item = (SELECT it FROM Item it WHERE it.uuid = :itemUuid) "
			        + "AND st.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)\n"
			        + "AND st.item.stockable = true AND st.expiryDate <= :expireDate";
			
			Query query = session.createQuery(queryStr);
			query.setParameter("itemUuid", itemUuid);
			query.setParameter("locationUuid", locationUuid);
			query.setParameter("expireDate", expireDate);
			
			System.out.println(query.getQueryString());
			
			return (Stock) query.uniqueResult();
			
		} else {
			
			String queryStr = "SELECT st \n" + "FROM Stock st \n"
			        + "WHERE st.item = (SELECT it FROM Item it WHERE it.uuid = :itemUuid) "
			        + "AND st.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)\n"
			        + "AND st.item.stockable = true AND st.batch = :batch " + "AND st.expiryDate = :expireDate";
			
			Query query = session.createQuery(queryStr);
			query.setParameter("itemUuid", itemUuid);
			query.setParameter("locationUuid", locationUuid);
			query.setParameter("batch", batch);
			query.setParameter("expireDate", expireDate);
			
			System.out.println(query.getQueryString());
			
			return (Stock) query.uniqueResult();
			
		}
		
	}
	
	public List<Stock> getStockByLocation(String locationUuid) {
		
		DbSession session = this.getSession();
		String queryStr = "SELECT st \n" + "FROM Stock st \n"
		        + "WHERE st.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("locationUuid", locationUuid);
		
		return query.list();
		
	}
	
	public List<Item> getStockedOut() {
		
		DbSession session = this.getSession();
		String queryStr = "SELECT item FROM Item item \n"
		        + "WHERE item.stockable = true AND item NOT IN(SELECT stock.item FROM Stock stock)";
		
		Query query = session.createQuery(queryStr);
		
		return query.list();
	}
	
	//TODO fix getting by location query
	public List<Item> getStockedOutByLocation(String locationUuid) {
		DbSession session = this.getSession();
		//String queryStr = "SELECT item FROM Item item \n"
		//        + "WHERE item.stockable = true AND item.uuid NOT IN(SELECT stock.item.uuid FROM Stock stock WHERE stock.location.uuid =:locationUuid)";
		//String queryStr = "SELECT item FROM Item item, Stock stock WHERE item.stockable = true AND stock.item=item AND stock.location.uuid =:locationUuid";
		String queryStr = "SELECT item FROM Item item \n"
		        + "WHERE item.stockable = true AND item NOT IN(SELECT stock.item FROM Stock stock WHERE stock.location.uuid =:locationUuid)";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}
	
	public Map<String, Object> getStockMetricsByLocation(String locationUuid) {
		
		//		{
		//			nearly stocked out;
		//			stocked out;
		//			expired;
		//			nearly expired;
		//		}
		
		/* ------------------------
		-----------------------
		query for nearly out of stock
		------------------------
		------------------------- */
		
		DbSession session = this.getSession();
		
		String queryStr = "SELECT stc.item,SUM(stc.quantity) FROM Stock stc \n"
		
		+ "WHERE stc.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid) \n" + "GROUP BY stc.item \n"
		        + "HAVING SUM(stc.quantity) <=  (SELECT rol.quantity FROM ReorderLevel rol \n"
		        + "WHERE rol.id.location = (SELECT loc FROM Location loc WHERE loc.uuid = :locationUuid))";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("locationUuid", locationUuid);
		
		Map<String, Object> metricsMap = new HashMap<String, Object>();
		
		metricsMap.put("nearlyStockedOut", query.list().size());
		
		/* ------------------------
		-----------------------
		query for nearly expired
		------------------------
		------------------------- */
		String nearlyExpired = "SELECT stc,(stc.expiryDate - current_date) FROM Stock stc WHERE stc.expiryDate <= current_date + 30 AND stc.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)";
		
		Query queryNearlyExpired = session.createQuery(nearlyExpired);
		
		queryNearlyExpired.setParameter("locationUuid", locationUuid);
		
		Integer nearExpiredCountlist = queryNearlyExpired.list().size();
		
		metricsMap.put("nearlyExpired", nearExpiredCountlist);
		
		/* ------------------------
		-----------------------
		query for out of stock
		------------------------
		------------------------- */
		metricsMap.put("stockedOut", this.getStockedOutByLocation(locationUuid).size());
		
		/* ------------------------
		-----------------------
		query for expired stock
		------------------------
		------------------------- */
		String expiredQueryString = "SELECT stc FROM Stock stc WHERE stc.expiryDate <= current_date AND stc.location = (SELECT l FROM Location l WHERE l.uuid = :locationUuid)";
		
		Query queryExpired = session.createQuery(expiredQueryString);
		
		queryExpired.setParameter("locationUuid", locationUuid);
		
		metricsMap.put("expired", queryExpired.list().size());
		
		return metricsMap;
		
	}
	
	public List<OrderStatus> getOrderStatusByVisit(String visitUuid) {
		DbSession session = this.getSession();
		//String queryStr = "SELECT item FROM Item item \n"
		//        + "WHERE item.stockable = true AND item.uuid NOT IN(SELECT stock.item.uuid FROM Stock stock WHERE stock.location.uuid =:locationUuid)";
		//String queryStr = "SELECT item FROM Item item, Stock stock WHERE item.stockable = true AND stock.item=item AND stock.location.uuid =:locationUuid";
		String queryStr = "SELECT os FROM OrderStatus os\n" + "INNER JOIN os.order o\n" + "INNER JOIN o.encounter e\n"
		        + "INNER JOIN e.visit v\n" + "WHERE v.uuid=:visitUuid";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("visitUuid", visitUuid);
		return query.list();
	}
	
	public List<OrderStatus> getOrderStatusByOrderUuid(String orderUuid) {
		DbSession session = this.getSession();
		//String queryStr = "SELECT item FROM Item item \n"
		//        + "WHERE item.stockable = true AND item.uuid NOT IN(SELECT stock.item.uuid FROM Stock stock WHERE stock.location.uuid =:locationUuid)";
		//String queryStr = "SELECT item FROM Item item, Stock stock WHERE item.stockable = true AND stock.item=item AND stock.location.uuid =:locationUuid";
		String queryStr = "SELECT os FROM OrderStatus os\n" + "INNER JOIN os.order o\n" + "WHERE o.uuid=:orderUuid";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("orderUuid", orderUuid);
		return query.list();
	}
	
	public OrderStatus saveOrderStatus(OrderStatus entity) {
		DbSession session = getSession();
		session.saveOrUpdate("OrderStatus", entity);
		session.flush();
		return entity;
	}
}