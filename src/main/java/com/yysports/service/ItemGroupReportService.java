package com.yysports.service;

import com.yysports.dto.ItemReportDto;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by samchu on 2016/11/16.
 */
@Slf4j
@Service
public class ItemGroupReportService {
    private String itemGroupReportSQL = "select itemgroup.* from (select igroup.brand brandName, igroup.IS_SPECIAL isSpecial," +
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
            " LEFT JOIN item_group_shop_ref as shopref" +
            " ON itemGroupID = shopref.ITEM_GROUP_ID where shopref.IS_DEL=0 ";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    public ByteArrayOutputStream genExcel(Long id, String itemGroupName, List<Long> shopIdPmt, String brand, Integer isSpecial) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder(itemGroupReportSQL);
        if (id != null) {
            sb.append(" and itemgroup.itemGroupID = :itemGroupID");
        }
        if (StringUtils.hasText(itemGroupName)) {
            sb.append(" and itemgroup.itemGroupName like :itemGroupName");
        }
        if (shopIdPmt != null) {
            sb.append(" and shopref.SHOP_ID in :shopIdPmt");
        }
        if (StringUtils.hasText(brand)) {
            sb.append(" and itemgroup.brandName like :brandName");
        }
        if (isSpecial != null) {
            sb.append(" and itemgroup.isSpecial = :isSpecial");
        }
        sb.append(" order by itemGroupID desc");
        List<ItemReportDto> itemReportlist = new ArrayList();
        try {
            Query query = entityManager.createNativeQuery(sb.toString());
            if (id != null) {
                query.setParameter("itemGroupID", id);
            }
            if (StringUtils.hasText(itemGroupName)) {
                query.setParameter("itemGroupName", "%" + itemGroupName + "%");
            }
            if (shopIdPmt != null) {
                query.setParameter("shopIdPmt", shopIdPmt);
            }
            if (StringUtils.hasText(brand)) {
                query.setParameter("brandName", "%" + brand + "%");
            }
            if (isSpecial != null) {
                query.setParameter("isSpecial", isSpecial);
            }
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> list = query.getResultList();
            for (Map<String, Object> map : list) {
                ItemReportDto dto = modelMapper.map(map, ItemReportDto.class);
                itemReportlist.add(dto);
            }

//
//            List<ItemReportDto> itemReportlist = typedQuery.getResultList();
//            System.out.println("get:" + itemReportlist.size());
//            for (ItemReportDto dto : itemReportlist) {
//                System.out.println("dto:" + dto);
//            }

            JasperReport jasperReport = JasperCompileManager.compileReport(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("report/ItemReport.jrxml"));
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemReportlist);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            //exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("D:/demo.xlsx"));
            exporter.exportReport();
        } catch (JRException e) {
            log.error("", e);
            e.printStackTrace();
            throw e;
        }
        return baos;
    }
}
