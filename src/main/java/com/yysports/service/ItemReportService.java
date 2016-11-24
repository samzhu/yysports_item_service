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
public class ItemReportService {

    private String itemReportSQL = "select titem.*,tstock.*,tgroup.* from (\n" +
            "select \n" +
            "id itemID, \n" +
            "ITEM_GROUP_ID itemGroupID,\n" +
            "ITEM_NAME itemName, \n" +
            "UPC_CODE upcCode, \n" +
            "ERP_CODE erpCode, \n" +
            "integral integral, \n" +
            "LIST_PRICE listPrice, \n" +
            "YY_PRICE yyPrice, \n" +
            "TYPE type " +
            "from item where IS_DEL = 0 ) as titem \n" +
            "LEFT JOIN (SELECT stock,ITEM_ID FROM (SELECT * FROM item_stock where IS_DEL=0 ORDER BY CREATED_DATE DESC) AS S GROUP BY ITEM_ID) AS tstock ON titem.itemID = tstock.ITEM_ID\n" +
            "LEFT JOIN (SELECT \n" +
            "brand brandName, \n" +
            "IS_SPECIAL isSpecial, \n" +
            "CASE IS_SPECIAL WHEN 0 THEN '普通商品' WHEN 1 THEN '特卖商品' WHEN 2 THEN '员购商品' WHEN 3 THEN '半马商品' ELSE '' END isSpecialStr, \n" +
            "id groupid, \n" +
            "ITEM_GROUP_NAME itemGroupName FROM item_group ) AS tgroup ON titem.itemGroupID = tgroup.groupid ";


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    public ByteArrayOutputStream genExcel(Long id, String upcCode, String itemName, String type, String itemGroupName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemID = :itemID");
        }
        if (StringUtils.hasText(upcCode)) {
            sb.append((sb.length() > 0 ? " and " : "") + " upcCode like :upcCode");
        }
        if (StringUtils.hasText(itemName)) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemName like :itemName");
        }
        if (StringUtils.hasText(type)) {
            sb.append((sb.length() > 0 ? " and " : "") + " type = :type");
        }
        if (StringUtils.hasText(itemGroupName)) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemGroupName like :itemGroupName");
        }
        if (sb.length() > 0) sb.insert(0, " where ");
        sb.insert(0, itemReportSQL);
        sb.append(" order by itemID desc");
        List<ItemReportDto> itemReportlist = new ArrayList();
        try {
            Query query = entityManager.createNativeQuery(sb.toString());
            if (id != null) {
                query.setParameter("itemID", id);
            }
            if (StringUtils.hasText(upcCode)) {
                query.setParameter("upcCode", "%" + upcCode + "%");
            }
            if (StringUtils.hasText(itemName)) {
                query.setParameter("itemName", "%" + itemName + "%");
            }
            if (StringUtils.hasText(type)) {
                query.setParameter("type", type);
            }
            if (StringUtils.hasText(itemGroupName)) {
                query.setParameter("itemGroupName", "%" + itemGroupName + "%");
            }
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> list = query.getResultList();
            for (Map<String, Object> map : list) {
                ItemReportDto dto = modelMapper.map(map, ItemReportDto.class);
                itemReportlist.add(dto);
            }
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
