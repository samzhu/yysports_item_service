package com.yysports.domain;

import com.yysports.dto.ItemReportDto;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by samchu on 2016/11/10.
 */
@Data
@Entity
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "UPC_CODE")
    private String upcCode; // 貨號
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "ABBR")
    private String abbr;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "LIST_PRICE")
    private BigDecimal listPrice;
    @Column(name = "PROTECTION_PRICE")
    private BigDecimal protectionPrice;
    @Column(name = "YY_PRICE")
    private BigDecimal yyPrice;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "INTEGRAL")
    private Integer integral;
    @Column(name = "ITEM_GROUP_ID")
    private Long itemGroupId;
    @Column(name = "IS_SUBSCRIBE")
    private String isSubscribe;
    @Column(name = "THUMB_IMG")
    private String thumbImg;
    @Column(name = "ERP_WAREHOUSE_NO")
    private String erpWarehouseNo;
    @Column(name = "IS_DEL")
    private String isDel;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
    @Column(name = "ERP_CODE")
    private String erpCode;
    @Column(name = "ITEM_GROUP_NAME")
    private String itemGroupName;
    @Column(name = "DISCOUNT_PERCENT")
    private BigDecimal discountPercent;
    @Column(name = "SOURCE")
    private String source;
    @Column(name = "IS_DEFAULT")
    private String isDefault;
    @Column(name = "DESC_LEVEL")
    private BigDecimal descLevel;
    @Column(name = "SERVICE_LEVEL")
    private BigDecimal serviceLevel;
    @Column(name = "LOGISTICS_LEVEL")
    private BigDecimal logisticsLevel;
    @Column(name = "START_DATE")
    private Timestamp startDate;
    @Column(name = "END_DATE")
    private Timestamp endDate;
    @Column(name = "QRCODE_URL")
    private String qrcodeUrl;
    @Column(name = "FREE_SHIPPING")
    private String freeShipping;
    @Column(name = "IS_TIME_LIMIT")
    private String isTimeLimit;
}
