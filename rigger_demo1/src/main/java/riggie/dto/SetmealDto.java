package riggie.dto;


import lombok.Data;
import riggie.pojo.Setmeal;
import riggie.pojo.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
