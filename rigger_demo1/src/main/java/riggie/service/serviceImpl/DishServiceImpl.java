package riggie.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import riggie.dto.DishDto;
import riggie.mapper.DishMapper;
import riggie.pojo.Dish;
import riggie.pojo.DishFlavor;
import riggie.service.DishFlavorService;
import riggie.service.DishService;

import java.util.List;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishMapper dishMapper;
    @Transactional
    @Override
    public void saveDish(DishDto dishDto) {
        log.info("进入到service中");


        this.save(dishDto);

        Long dishId=dishDto.getId();
        List<DishFlavor> flavors=dishDto.getFlavors();
        for (DishFlavor dishFlavor:flavors){
            dishFlavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }
    @Transactional
    @Override
    public void updateDish(DishDto dishDto) {
        Long dishId=dishDto.getId();
        this.removeById(dishId);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(queryWrapper);
        this.save(dishDto);
        List<DishFlavor> flavors=dishDto.getFlavors();
        for (DishFlavor dishFlavor:flavors){
            dishFlavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public void deleteDishWithFlavor(Long id) {
        this.removeById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(queryWrapper);

    }
}
