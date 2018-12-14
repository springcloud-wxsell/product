package com.sell.product.service;

import com.sell.common.dto.DecreaseStockDTO;
import com.sell.common.pojo.ProductInfo;

import java.util.List;

/**
 * @author WangWei
 * @Title: ProductService
 * @ProjectName product
 * @date 2018/12/1 17:16
 * @description
 */
public interface ProductInfoService {


    /**
     * 查询上架商品
     * @return
     */
    List<ProductInfo> selectUpAll();

    /**
     *  根据productId列表查询商品
     * @param productIdList
     * @return
     */
    List<ProductInfo> selectList(List<String> productIdList);

    /**
     * 扣库存
     * @param decreaseStockDTOList
     */
    void descreaStock(List<DecreaseStockDTO> decreaseStockDTOList);

}
