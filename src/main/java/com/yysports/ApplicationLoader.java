package com.yysports;

import com.yysports.dto.ItemReportDto;
import com.yysports.repository.ItemRep;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by samchu on 2016/11/15.
 */
//@Component
public class ApplicationLoader implements CommandLineRunner {
    @Autowired
    private ItemRep itemRep;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    private String sql = "select  new com.yysports.dto.ItemReportDto(" +
            " brand,isSpecial,isSpecialStr,groupid,itemGroupName,itemid,itemName,upcCode,erpCode,integral,stock,listPrice,yyPrice)" +
            " from " +
            " ( select igroup.brand, igroup.isSpecial," +
            " CASE igroup.isSpecial WHEN 0 THEN '普通商品' WHEN 1 THEN '特卖商品' WHEN 2 THEN '员购商品' WHEN 3 THEN '半马商品' ELSE '' END isSpecialStr," +
            " igroup.id groupid," +
            " igroup.itemGroupName," +
            " item.id itemid," +
            " item.itemName," +
            " item.upcCode," +
            " item.erpCode," +
            " item.integral," +
            " stock.stock," +
            " item.listPrice," +
            " item.yyPrice ) " +
            " from Item as item, ItemGroup as igroup, ItemStock as stock " +
            " where item.itemGroupId = igroup.id and item.id = stock.itemId " +
            " and item.isDel=0 and igroup.isDel=0 and stock.isDel=0 ) ";

    private String sql2 = "select itemgroup.* from (select igroup.brand brandName, igroup.IS_SPECIAL isSpecial," +
            " CASE igroup.IS_SPECIAL WHEN 0 THEN '普通商品' WHEN 1 THEN '特卖商品' WHEN 2 THEN '员购商品' WHEN 3 THEN '半马商品' ELSE '' END isSpecialStr," +
            " igroup.id itemGroupID," +
            " igroup.ITEM_GROUP_NAME itemGroupName," +
            " item.id itemID," +
            " item.ITEM_NAME itemName," +
            " item.UPC_CODE upcCode," +
            " item.ERP_CODE erpCode," +
            " item.integral integral," +
            " stock.stock stock," +
            " item.LIST_PRICE listPrice," +
            " item.YY_PRICE yyPrice" +
            " from item as item, item_group as igroup, item_stock as stock " +
            " where item.ITEM_GROUP_ID = igroup.id and item.id = stock.ITEM_ID " +
            " and item.IS_DEL=0 and igroup.IS_DEL=0 and stock.IS_DEL=0 ) as itemgroup " +
            " LEFT JOIN item_group_shop_ref as shopref " +
            " ON itemGroupID = shopref.ITEM_GROUP_ID where shopref.IS_DEL=0 ";

    @Override
    public void run(String... args) throws Exception {
        List<ItemReportDto> itemReportlist = new ArrayList();
        Query query = entityManager.createNativeQuery(sql2 + " and itemgroup.itemGroupID = :itemGroupID" );
        query.setParameter("itemGroupID", "1244");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        for (Map<String, Object> map : list) {
            ItemReportDto dto = modelMapper.map(map, ItemReportDto.class);
            System.out.println(dto);

//            dto.setBrandName((String) map.get("brandName"));
//            dto.setIsSpecial((Integer) map.get("isSpecial"));
//            dto.setIsSpecialStr((String) map.get("isSpecialStr"));
//            dto.setItemGroupID((Long) map.get("groupid"));
//            dto.setItemID((Long) map.get("itemid"));
//            dto.setItemName((String) map.get("itemName"));
//            dto.setUpcCode((String) map.get("upcCode"));
//            dto.setErpCode((String) map.get("erpCode"));
//            dto.setIntegral((Integer) map.get("integral"));
//            dto.setStock((Integer) map.get("stock"));
//            dto.setListPrice((BigDecimal) map.get("listPrice"));
//            dto.setYyPrice((BigDecimal) map.get("yyPrice"));
            itemReportlist.add(dto);
        }


        //itemReportlist = entityManager.createNativeQuery(sql2, "ItemReportDtoMapping").getResultList();

        //TypedQuery<ItemReportDto> typedQuery = entityManager.createQuery(sql, ItemReportDto.class);
        //typedQuery.setParameter("","");
        //List<ItemReportDto> itemReportlist = typedQuery.getResultList();
        //List<ItemReportDto> itemReportlist = itemRep.findAllItemReport();


        //InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("report/ItemReport.jasper");

        JasperReport jasperReport = JasperCompileManager.compileReport(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("report/ItemReport.jrxml"));

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemReportlist);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        JRXlsxExporter exporter = new JRXlsxExporter();
//        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
//        config.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
//        config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
//        config.setIgnorePageMargins(Boolean.TRUE);
//        exporter.setConfiguration(config);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("D:/demo.xlsx"));

        exporter.exportReport();

        System.out.println("Done");

        RestTemplate restTemplate = new RestTemplate();

    }
}
