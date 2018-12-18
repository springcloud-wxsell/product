package com.sell.product.service.impl;

import com.sell.common.dto.DecreaseStockDTO;
import com.sell.common.dto.ProductInfoDTO;
import com.sell.common.enums.ProductStatusEnum;
import com.sell.common.enums.ResultEnum;
import com.sell.common.exception.ProductException;
import com.sell.common.pojo.ProductInfo;
import com.sell.common.pojo.ProductInfoExample;
import com.sell.common.utils.JsonUtils;
import com.sell.product.mapper.ProductInfoMapper;
import com.sell.product.service.ProductInfoService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWei
 * @Title: ProductServiceImpl
 * @ProjectName product
 * @date 2018/12/1 17:17
 * @description
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public List<ProductInfo> selectUpAll() {
        ProductInfoExample example = new ProductInfoExample();
        example.createCriteria().andProductStatusEqualTo(ProductStatusEnum.UP.getCode().byteValue());
        List<ProductInfo> productInfoList = productInfoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(productInfoList)) {
            return productInfoList;
        }
        return null;
    }

    @Override
    public List<ProductInfo> selectList(List<String> productIdList) {
        ProductInfoExample example = new ProductInfoExample();
        example.createCriteria().andProductIdIn(productIdList);
        List<ProductInfo> productInfoList = productInfoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(productInfoList)) {
            return productInfoList;
        }
        return null;
    }

    @Override
    public void descreaStock(List<DecreaseStockDTO> decreaseStockDTOList) {
        List<ProductInfo> productInfoList = descreaStockProcess(decreaseStockDTOList);
        List<ProductInfoDTO> productInfoDTOList = productInfoList.stream().map(e -> {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO();
            BeanUtils.copyProperties(e, productInfoDTO);
            return productInfoDTO;
        }).collect(Collectors.toList());
        // 发送mq消息
        amqpTemplate.convertAndSend("productInfo", JsonUtils.toJson(productInfoDTOList));
    }

    @Transactional
    public List<ProductInfo> descreaStockProcess(List<DecreaseStockDTO> decreaseStockDTOList) {
        List<ProductInfo> productInfoList = new ArrayList<>();
        for(DecreaseStockDTO decreaseStockDTO : decreaseStockDTOList) {
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(decreaseStockDTO.getProductId());
            // 判断商品是否存在
            if (StringUtils.isEmpty(productInfo)) {
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            // 判断库存是否足够
            Integer num = productInfo.getProductStock().intValue() - decreaseStockDTO.getProductQuantity().intValue();
            if (num < 0) {
                throw new ProductException(ResultEnum.PRODUCT_STOCK_INSUFFICIENCY);
            }
            productInfo.setProductStock(num);
            productInfoMapper.updateByPrimaryKeySelective(productInfo);

            productInfoList.add(productInfo);
        }
        return productInfoList;
    }
}
