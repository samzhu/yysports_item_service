package com.yysports.repository;

import com.yysports.domain.ItemGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by samchu on 2016/11/10.
 */
@Repository
public interface ItemGroupRep extends PagingAndSortingRepository<ItemGroup, Long> {
}
