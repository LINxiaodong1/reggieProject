package riggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import riggie.dto.OrdersDto;
import riggie.pojo.Orders;
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}
