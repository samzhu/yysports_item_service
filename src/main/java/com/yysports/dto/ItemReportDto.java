package com.yysports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;

/**
 * Created by samchu on 2016/11/14.
 */

@Data
public class ItemReportDto {
    // 品牌名稱
    private String brandName;
    // 業務類型 if(value==0){return '普通商品';}else if(value==1){return '特卖商品';}else if(value==2){return '员购商品';}else if(value==3){return '半马商品'
    private Integer isSpecial;
    private String isSpecialStr;
    // 商品集編碼
    private Long itemGroupID;
    // 商品集名稱
    private String itemGroupName;
    // 商品編號
    private Long itemID;
    // 商品名稱
    private String itemName;
    // 貨號
    private String upcCode;
    // 條形碼
    private String erpCode;
    // 積分價格
    private Integer integral;
    // 庫存
    private Integer stock;
    // 市場價
    private BigDecimal listPrice;
    // 勝道價
    private BigDecimal yyPrice;


}
