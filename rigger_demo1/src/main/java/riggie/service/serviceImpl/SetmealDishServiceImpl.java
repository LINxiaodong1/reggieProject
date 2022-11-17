package riggie.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import riggie.mapper.SetmealDishMapper;
import riggie.pojo.SetmealDish;
import riggie.service.SetmealDishService;
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>implements SetmealDishService {
}
