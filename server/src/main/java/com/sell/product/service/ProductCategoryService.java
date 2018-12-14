package com.sell.product.service;

import com.sell.common.pojo.ProductCategory;

import java.util.List;

/**
 * @author WangWei
 * @Title: ProductCategoryService
 * @ProjectName product
 * @date 2018/12/1 18:35
 * @description
 */
public interface ProductCategoryService {

    /**
     * 查询商品类目
     * @param categoryType
     * @return
     */
    List<ProductCategory> selectByCategoryTypeIn(List<Integer> categoryType);
}
