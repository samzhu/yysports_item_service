package com.yysports.service;

import com.yysports.dto.ItemReportDto;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by samchu on 2016/11/16.
 */
@Slf4j
@Service
public class ItemReportService {

    private String itemReportSQL = "select " +
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
            " where item.itemGroupId = igroup.id and item.id = stock.itemId and igroup.id = shopref.itemGroupId " +
            " and item.isDel=0 and igroup.isDel=0 and stock.isDel=0 ";

    @Autowired
    private EntityManager entityManager;

    public ByteArrayOutputStream genExcel(Long id, String upcCode, String itemName, String type, String itemGroupName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder(itemReportSQL);
        if (id != null) {
            sb.append(" and item.id = :id");
        }
        if (StringUtils.hasText(upcCode)) {
            sb.append(" and item.upcCode like :upcCode");
            //sb.append(" and item.upcCode = :upcCode");
        }
        if (StringUtils.hasText(itemName)) {
            sb.append(" and item.itemName like :itemName");
        }
        if (StringUtils.hasText(type)) {
            sb.append(" and item.type = :type");
        }
        if (StringUtils.hasText(itemGroupName)) {
            sb.append(" and item.itemGroupName like :itemGroupName");
        }
        sb.append(" order by item.id desc");
        try {
            TypedQuery<ItemReportDto> typedQuery = entityManager.createQuery(sb.toString(), ItemReportDto.class);
            if (id != null) {
                typedQuery.setParameter("id", id);
            }
            if (StringUtils.hasText(upcCode)) {
                typedQuery.setParameter("upcCode", "%" + upcCode + "%");
            }
            if (StringUtils.hasText(itemName)) {
                typedQuery.setParameter("itemName", "%" + itemName + "%");
            }
            if (StringUtils.hasText(type)) {
                typedQuery.setParameter("type", type);
            }
            if (StringUtils.hasText(itemGroupName)) {
                typedQuery.setParameter("itemGroupName", "%" + itemGroupName + "%");
            }

            List<ItemReportDto> itemReportlist = typedQuery.getResultList();
            System.out.println("get:" + itemReportlist.size());
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
