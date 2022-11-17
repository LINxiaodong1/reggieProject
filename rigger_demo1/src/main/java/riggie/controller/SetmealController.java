package riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import riggie.common.R;
import riggie.dto.SetmealDto;
import riggie.pojo.Category;
import riggie.pojo.Setmeal;
import riggie.pojo.SetmealDish;
import riggie.service.CategoryService;
import riggie.service.SetMealService;
import riggie.service.SetmealDishService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("启用saveSetmeal方法");
        setMealService.saveSetmeal(setmealDto);
        return R.success("添加套餐成功");

    }
    @GetMapping("/page")
    public R<Page> getPage(int page,int pageSize,String name){
        Page pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        setMealService.page(pageInfo,queryWrapper);
        Page setmealDtoPage=new Page<>();
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> setmealList=pageInfo.getRecords();
        List<SetmealDto> setmealDtoList=null;
        setmealDtoList=setmealList.stream().map((item->{
           Long categoryId= item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            String categoryName=category.getName();
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        })).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }
    @DeleteMapping
    public R<String> deletesetMealwithDish(Long[] ids){
        for(Long id:ids){
            setMealService.deleteSetmealWithDish(id);
        }
        return R.success("删除套餐成功！");
    }
   /* @GetMapping("/{id}")
    public R<SetmealDto> getSetmealDto(@PathVariable String id){
        Setmeal setmeal=setMealService.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList=setmealDishService.list(queryWrapper);
        Category category=categoryService.getById(setmeal.getCategoryId());
        String categoryName=category.getName();
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);
        setmealDto.setCategoryName(categoryName);
        return R.success(setmealDto);
    }*/
    @GetMapping("/list")
    public R<List<Setmeal>> getSetmealList( Setmeal setmeal){
        String key=setmeal.getCategoryId()+"_"+setmeal.getStatus();
        List<Setmeal> setmealList=(List<Setmeal>) redisTemplate.opsForValue().get(key);
        if(setmealList!=null){
            return R.success(setmealList);
        }
        Long cateGoryId= setmeal.getCategoryId();
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,cateGoryId)
                .eq(Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> setmealList1=setMealService.list(queryWrapper);
        redisTemplate.opsForValue().set(key,setmealList1);
        return R.success(setmealList1);

    }

    @PutMapping
    public R<String> updateSetmealWithDish(@RequestBody SetmealDto setmealDto){
        setMealService.updateSetmealWithDish(setmealDto);
        return R.success("修改成功！");
    }
    @PostMapping("/status/{status}")
    public R<String> updateSetmealStatus(@PathVariable int status,Long[] ids){
        for(Long id:ids){
            Setmeal setMeal=new Setmeal();
            setMeal.setId(id);
            setMeal.setStatus(status);
            setMealService.updateById(setMeal);
        }
       return R.success("修改成功！");


    }



}
