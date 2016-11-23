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

    private String itemReportSQL = "select itemgroup.* from (select igroup.brand brandName, igroup.IS_SPECIAL isSpecial," +
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
            " item.YY_PRICE yyPrice," +
            " item.TYPE type" +
            " from item as item, item_group as igroup, item_stock as stock " +
            " where item.ITEM_GROUP_ID = igroup.id and item.id = stock.ITEM_ID " +
            " and item.IS_DEL=0 and igroup.IS_DEL=0 and stock.IS_DEL=0 ) as itemgroup ";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    public ByteArrayOutputStream genExcel(Long id, String upcCode, String itemName, String type, String itemGroupName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemgroup.itemID = :itemID");
        }
        if (StringUtils.hasText(upcCode)) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemgroup.upcCode like :upcCode");
        }
        if (StringUtils.hasText(itemName)) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemgroup.itemName like :itemName");
        }
        if (StringUtils.hasText(type)) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemgroup.type = :type");
        }
        if (StringUtils.hasText(itemGroupName)) {
            sb.append((sb.length() > 0 ? " and " : "") + " itemgroup.itemGroupName like :itemGroupName");
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
