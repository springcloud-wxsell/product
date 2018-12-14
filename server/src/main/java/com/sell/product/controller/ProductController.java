package com.sell.product.controller;

import com.sell.common.dto.DecreaseStockDTO;
import com.sell.common.pojo.ProductCategory;
import com.sell.common.pojo.ProductInfo;
import com.sell.common.utils.ResultVOUtils;
import com.sell.common.vo.ProductInfoVO;
import com.sell.common.vo.ProductVO;
import com.sell.common.vo.ResultVO;
import com.sell.product.service.ProductCategoryService;
import com.sell.product.service.ProductInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWei
 * @Title: ProductController
 * @ProjectName product
 * @date 2018/12/1 16:58
 * @description
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ResultVO<ProductVO> list() {
        // 1.查询所有在架的商品
        List<ProductInfo> productInfoList = productInfoService.selectUpAll();
        // 2.获取类目type列表
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
        // 3.从数据库查询类目
        List<ProductCategory> productCategoryList = productCategoryService.selectByCategoryTypeIn(categoryTypeList);
        // 4. 构造数据
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType().intValue());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtils.success(productVOList);
    }

    /**
     * 获取商品列表（给订单服务使用）
     * @param productIdList
     * @return
     */
    @PostMapping("/listForOrder")
    public List<ProductInfo> listForOrder(@RequestBody List<String> productIdList) {
        List<ProductInfo> productInfoList = productInfoService.selectList(productIdList);
        return productInfoList;
    }

    /**
     * 扣库存（给订单服务使用）
     * @param decreaseStockDTOList
     */
    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<DecreaseStockDTO> decreaseStockDTOList) {
        productInfoService.descreaStock(decreaseStockDTOList);
    }

}
