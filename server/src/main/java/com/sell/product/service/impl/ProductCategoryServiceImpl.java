package com.sell.product.service.impl;

import com.sell.common.pojo.ProductCategory;
import com.sell.common.pojo.ProductCategoryExample;
import com.sell.product.mapper.ProductCategoryMapper;
import com.sell.product.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WangWei
 * @Title: ProductCategoryServiceImpl
 * @ProjectName product
 * @date 2018/12/1 18:38
 * @description
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategory> selectByCategoryTypeIn(List<Integer> categoryTypeList) {
        ProductCategoryExample example = new ProductCategoryExample();
        example.createCriteria().andCategoryTypeIn(categoryTypeList);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(example);
        return productCategoryList;
    }
}
