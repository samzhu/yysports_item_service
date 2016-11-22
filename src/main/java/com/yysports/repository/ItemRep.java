package com.yysports.repository;

import com.yysports.domain.Item;
import com.yysports.dto.ItemReportDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by samchu on 2016/11/10.
 */

@Repository
public interface ItemRep extends PagingAndSortingRepository<Item, Long>{
    String findAllItemReportSQL = "select " +
            " new com.yysports.dto.ItemReportDto(igroup.brand, igroup.isSpecial," +
            " CASE igroup.isSpecial WHEN 0 THEN '普通商品' WHEN 1 THEN '特卖商品' WHEN 2 THEN '员购商品' WHEN 3 THEN '半马商品' ELSE '' END ," +
            " igroup.id," +
            " igroup.itemGroupName," +
            " item.id," +
            " item.itemName," +
            " item.upcCode," +
            " item.erpCode," +
            " item.integral," +
            " stock.stock," +
            " item.listPrice," +
            " item.yyPrice ) " +
            " from Item as item, ItemGroup as igroup, ItemStock as stock, ItemGroupShopRef as shopref " +
            " where item.itemGroupId = igroup.id and item.id = stock.itemId and igroup.id = shopref.itemGroupId";

    List<Item> findByItemGroupIdIn(List<Long> itemGroupIDs);

    //@Query(value = findAllItemReportSQL)
    //List<ItemReportDto> findAllItemReport();
}
