package com.sell.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWei
 * @Title: ServerController
 * @ProjectName product
 * @date 2018/12/4 20:40
 * @description:
 */
@RestController
public class ServerController {
    @GetMapping("/msg")
    public String meg() {
        return "this is product msg 2";
    }
}
