package com.yysports.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by samchu on 2016/11/10.
 */
@Data
@Entity
@Table(name = "item_brand")
public class ItemBrand {
    @Id
    @Column(name = "ID")
    private long id;
    @Column(name = "BRAND_NAME")
    private String brandName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "LOGO")
    private String logo;
    @Column(name = "ERP_CODE")
    private String erpCode;
    @Column(name = "ERP_BRAND_ID")
    private String erpBrandId;
    @Column(name = "PID")
    private Long pid;
    @Column(name = "IS_DEL")
    private String isDel;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
}
