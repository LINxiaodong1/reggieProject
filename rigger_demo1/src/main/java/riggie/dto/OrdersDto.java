package riggie.dto;

import lombok.Data;
import riggie.pojo.OrderDetail;
import riggie.pojo.Orders;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
