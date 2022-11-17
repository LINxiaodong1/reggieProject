package riggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import riggie.dto.DishDto;
import riggie.pojo.Dish;

public interface DishService extends IService<Dish> {
    public void saveDish(DishDto dishDto);
    public void updateDish(DishDto dishDto);
    public void deleteDishWithFlavor(Long id);
}