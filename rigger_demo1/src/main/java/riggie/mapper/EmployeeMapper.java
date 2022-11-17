package riggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import riggie.pojo.Employee;
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
