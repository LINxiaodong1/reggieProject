package riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.deploy.ui.DialogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import riggie.common.R;
import riggie.dto.DishDto;
import riggie.pojo.Category;
import riggie.pojo.Dish;
import riggie.pojo.DishFlavor;
import riggie.service.CategoryService;
import riggie.service.DishFlavorService;
import riggie.service.DishService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
@PostMapping
    public  R<String> saveDish(@RequestBody DishDto dishDto){

    log.info(dishDto.toString());

    dishService.saveDish(dishDto);

    return R.success("新增菜品成功");
}
@GetMapping("/page")
    public R<Page> getPage(int page,int pageSize,String name){
    Page<Dish> pageInfo=new Page<>(page,pageSize);
    Page<DishDto> dishDtoPage=new Page<>();

    LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
    queryWrapper.orderByAsc(Dish::getUpdateTime);
    dishService.page(pageInfo,queryWrapper);
    BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
    List<Dish> records=pageInfo.getRecords();
    List<DishDto> list=records.stream().map((item)->{
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(item,dishDto);
        Long categoryId=item.getCategoryId();
        Category category=categoryService.getById(categoryId);
        if(category!=null){
            String categoryName=category.getName();
            dishDto.setCategoryName(categoryName);

        }
        return dishDto;
    }).collect(Collectors.toList());
    dishDtoPage.setRecords(list);


    return R.success(dishDtoPage);
}
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id){
    Dish dish=dishService.getById(id);
    DishDto dishDto=new DishDto();
    BeanUtils.copyProperties(dish,dishDto);
    LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(DishFlavor::getDishId,id);
    DishFlavor dishFlavor=dishFlavorService.getOne(queryWrapper);
    List<DishFlavor> flavors=new ArrayList<>();
    flavors.add(dishFlavor);
    dishDto.setFlavors(flavors);
    return R.success(dishDto);
}


    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto){
    dishService.updateDish(dishDto);
    return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<DishDto>> getDishListByCategoryId(  String categoryId){
    LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(Dish::getCategoryId,categoryId );
    List<Dish> dishList=dishService.list(queryWrapper);

        List<DishDto> dishDtoList=  dishList.stream().map((item->{
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(item,dishDto);
        Long dishId=item.getId();
        LambdaQueryWrapper<DishFlavor> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(DishFlavor::getDishId,dishId);
       List<DishFlavor>dishFlavors=dishFlavorService.list(queryWrapper1);
        Category category=categoryService.getById(item.getCategoryId());
        dishDto.setFlavors(dishFlavors);
        dishDto.setCategoryName(category.getName());
        return dishDto;
    })).collect(Collectors.toList());
    return R.success(dishDtoList);
    }
    @DeleteMapping
    public R<String> deleteDishWithFlavor(Long ids){
    dishService.deleteDishWithFlavor(ids);
    return R.success("删除菜品成功！");
    }








}
