package riggie.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riggie.common.CustomerException;
import riggie.mapper.CategoryMapper;
import riggie.pojo.Category;
import riggie.pojo.Dish;
import riggie.pojo.Setmeal;
import riggie.service.CategoryService;
import riggie.service.DishService;
import riggie.service.SetMealService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;
    @Override
    public boolean removeCategory(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int dishCounts=dishService.count(queryWrapper);
        if(dishCounts>0){
            throw new CustomerException("当前目录下关联菜品，无法删除");

        }
        LambdaQueryWrapper<Setmeal> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
        int setmealCount=setMealService.count(queryWrapper1);
        if(setmealCount>0){
            throw new CustomerException("当前目录下关联菜品，无法删除");
        }
       return super.removeById(id);
    }
}
