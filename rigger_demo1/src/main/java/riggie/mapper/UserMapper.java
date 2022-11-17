package riggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import riggie.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
