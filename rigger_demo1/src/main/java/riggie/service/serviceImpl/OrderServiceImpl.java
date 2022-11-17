package riggie.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import riggie.common.BaseContext;
import riggie.common.CustomerException;
import riggie.mapper.OrderMapper;
import riggie.pojo.*;
import riggie.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Orders> implements OrderService {
    @Autowired
    private OrdersDetailService ordersDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public void saveOrderWithDetail(Orders order) {
        Long userId= BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList=shoppingCartService.list(queryWrapper);
        if(shoppingCartList==null||shoppingCartList.size()==0){

            throw new CustomerException("购物车为空，不能下单");

        }
        User user=userService.getById(userId);
        AddressBook addressBook=addressBookService.getById(order.getAddressBookId());
        if(addressBook==null){
            throw new CustomerException("输入的地址有误不能下单");
        }
        Long orderId=IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetailList=shoppingCartList.stream().map((item->{
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrderId(orderId);
            if(item.getDishId()==null){
                orderDetail.setSetmealId(item.getSetmealId());
            }else{
                orderDetail.setDishId(item.getDishId());
                orderDetail.setDishFlavor(item.getDishFlavor());
            }
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        })).collect(Collectors.toList());
        order.setUserId(userId);
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setAmount(new BigDecimal(amount.get()));
        order.setAddress((addressBook.getProvinceCode()==null?"":addressBook.getProvinceCode())
                +(addressBook.getCityName()==null?"":addressBook.getCityName()
                +(addressBook.getDistrictName()==null?"":addressBook.getDistrictName())
                +(addressBook.getDetail()==null?"":addressBook.getDetail())));
        order.setConsignee(addressBook.getConsignee());
        order.setPhone(user.getPhone());
        order.setStatus(2);
        order.setUserName(user.getName());
        this.save(order);
        ordersDetailService.saveBatch(orderDetailList);
        shoppingCartService.remove(queryWrapper);
    }
}
