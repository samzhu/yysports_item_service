package com.yysports;

import com.yysports.dto.ItemReportDto;
import com.yysports.repository.ItemRep;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samchu on 2016/11/15.
 */
//@Component
public class ApplicationLoader implements CommandLineRunner {
    @Autowired
    private ItemRep itemRep;

    @Autowired
    private EntityManager entityManager;

    private String sql = "select " +
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

    @Override
    public void run(String... args) throws Exception {


        //QItem QItem = QItem.item;

        TypedQuery<ItemReportDto> typedQuery = entityManager.createQuery(sql + " and shopref.shopId=1024612", ItemReportDto.class);
        //typedQuery.setParameter("","");
        List<ItemReportDto> itemReportlist = typedQuery.getResultList();
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
