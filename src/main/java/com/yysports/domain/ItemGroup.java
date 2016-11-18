package com.yysports.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by samchu on 2016/11/10.
 */
@Data
@Entity
@Table(name = "item_group")
public class ItemGroup {
    @Id
    @Column(name = "ID")
    private long id;
    @Column(name = "ITEM_GROUP_NAME")
    private String itemGroupName;
    @Column(name = "GROSS_WEIGHT")
    private BigDecimal grossWeight;
    @Column(name = "ITEM_ID")
    private Long itemId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "IS_SPECIAL")
    private Integer isSpecial;
    @Column(name = "LIST_PRICE")
    private BigDecimal listPrice;
    @Column(name = "YY_PRICE")
    private BigDecimal yyPrice;
    @Column(name = "PROTECTION_PRICE")
    private BigDecimal protectionPrice;
    @Column(name = "INTEGRAL")
    private Integer integral;
    @Column(name = "NET_WEIGHT")
    private BigDecimal netWeight;
    @Column(name = "ITEM_CATEGORY_ID")
    private Long itemCategoryId;
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
    @Column(name = "UNIT")
    private Integer unit;
    @Column(name = "MANUFACTURER_NAME")
    private String manufacturerName;
    @Column(name = "SUPPLIER_NO")
    private String supplierNo;
    @Column(name = "ORIGIN")
    private String origin;
    @Column(name = "LENGTH")
    private BigDecimal length;
    @Column(name = "WIDTH")
    private BigDecimal width;
    @Column(name = "HEIGH")
    private BigDecimal heigh;
    @Column(name = "WRAP")
    private String wrap;
    @Column(name = "BRAND_ID")
    private Long brandId;
    @Column(name = "BRAND")
    private String brand;
    @Column(name = "BULK")
    private BigDecimal bulk;
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Column(name = "IS_DEL")
    private String isDel;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "THUMB_IMG")
    private String thumbImg;
    @Column(name = "DISCOUNT_PERCENT")
    private BigDecimal discountPercent;
}
