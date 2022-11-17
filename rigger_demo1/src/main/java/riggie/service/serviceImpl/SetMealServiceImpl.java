package riggie.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import riggie.dto.SetmealDto;
import riggie.mapper.SetMealMapper;
import riggie.pojo.Setmeal;
import riggie.pojo.SetmealDish;
import riggie.service.SetMealService;
import riggie.service.SetmealDishService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveSetmeal(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId=setmealDto.getId();
        List<SetmealDish> setmealDishList=setmealDto.getSetmealDishes();
        setmealDishList=setmealDishList.stream().map((item->{
            item.setSetmealId(setmealId);
            return item;
        })).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }
    @Transactional
    @Override
    public void deleteSetmealWithDish(Long id) {
    this.removeById(id);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getDishId,id);

        setmealDishService.remove(lambdaQueryWrapper);
    }
    @Transactional
    @Override
    public void updateSetmealWithDish(SetmealDto setmealDto) {
        this.update(setmealDto,null);
        Long setmealId=setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishList=setmealDto.getSetmealDishes();
        for(SetmealDish setmealDish:setmealDishList){
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(setmealDishList);
    }
}
