package com.yysports.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by samchu on 2016/11/14.
 */
@Data
@Entity
@Table(name = "item_stock")
public class ItemStock {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "ITEM_ID")
    private Long itemId;
    @Column(name = "STOCK")
    private Integer stock;
    @Column(name = "LOCK_STOCK")
    private Integer lockStock;
    @Column(name = "IS_DEL")
    private String isDel;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;
}
