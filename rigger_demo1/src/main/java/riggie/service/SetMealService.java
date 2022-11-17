package riggie.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import riggie.dto.SetmealDto;
import riggie.pojo.Setmeal;

public interface SetMealService extends IService<Setmeal> {
    public void saveSetmeal(SetmealDto setmealDto);
    public void deleteSetmealWithDish(Long id);
    public void updateSetmealWithDish(SetmealDto setmealDto);
}
