package riggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import riggie.mapper.OrderMapper;
import riggie.pojo.Orders;

@Service
public interface OrderService extends IService<Orders> {
    public void saveOrderWithDetail(Orders order);
}
