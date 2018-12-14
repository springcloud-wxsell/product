package com.sell.product.client;

import com.sell.common.dto.DecreaseStockDTO;
import com.sell.common.dto.ProductInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WangWei
 * @Title: ProductClient
 * @ProjectName product
 * @date 2018/12/6 13:58
 * @description:
 */
@FeignClient("product")
public interface ProductClient {

    @PostMapping("/product/listForOrder")
    List<ProductInfoDTO> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void descreaStock(@RequestBody List<DecreaseStockDTO> decreaseStockDTOList);
}
