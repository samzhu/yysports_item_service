package com.yysports.repository;

import com.yysports.domain.ItemBrand;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by samchu on 2016/11/10.
 */
public interface ItemBrandRep extends PagingAndSortingRepository<ItemBrand, Long> {
}
