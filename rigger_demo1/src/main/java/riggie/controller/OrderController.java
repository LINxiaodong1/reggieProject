package riggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riggie.common.R;
import riggie.pojo.Orders;
import riggie.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    public R<String> getOrder(Orders order){
        orderService.saveOrderWithDetail(order);
        return R.success("下单支付成功");

    }
}
