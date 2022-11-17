package riggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import riggie.pojo.Category;

public interface CategoryService extends IService<Category> {
    public boolean removeCategory(Long id);

}
