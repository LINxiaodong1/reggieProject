package riggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import riggie.common.BaseContext;
import riggie.common.R;
import riggie.pojo.ShoppingCart;
import riggie.service.ShoppingCartService;

import javax.security.auth.callback.LanguageCallback;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
   @Autowired
   private ShoppingCartService shoppingCartService;
   @PostMapping("/add")
   public R<ShoppingCart> addCart(@RequestBody ShoppingCart cart){
       Long userId= BaseContext.getCurrentId();
       cart.setUserId(userId);
       Long dishId= cart.getDishId();
       LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.eq(ShoppingCart::getUserId,userId);
       if(dishId==null){
          queryWrapper.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
       }else{
           queryWrapper.eq(ShoppingCart::getDishId,cart.getDishId());
       }
       ShoppingCart shoppingCart=shoppingCartService.getOne(queryWrapper);
       if(shoppingCart==null){
           shoppingCartService.save(cart);
           return R.success(cart);
       }else{
           shoppingCart.setNumber(shoppingCart.getNumber()+1);
           shoppingCartService.updateById(shoppingCart);
           return R.success(shoppingCart);

       }
   }

   @GetMapping("/list")
    public R<List<ShoppingCart>> getCartList(){
       Long userId=BaseContext.getCurrentId();
       LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.eq(ShoppingCart::getUserId,userId);
       List<ShoppingCart> list=shoppingCartService.list(queryWrapper);
       return R.success(list);
   }
   @PostMapping("/sub")
    public R deleteCart(@RequestBody ShoppingCart cart){
       Long userId=BaseContext.getCurrentId();
       LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.eq(ShoppingCart::getUserId,userId);
       if(cart.getDishId()==null){
           queryWrapper.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
       }else{
           queryWrapper.eq(ShoppingCart::getDishId,cart.getDishId());

       }
       ShoppingCart shoppingCart=shoppingCartService.getOne(queryWrapper);
       if(shoppingCart.getNumber()==1){
           shoppingCartService.remove(queryWrapper);
           return R.success("");
       }else{
        shoppingCart.setNumber(shoppingCart.getNumber()-1);
        shoppingCartService.updateById(shoppingCart);
           return R.success(shoppingCart);
       }

   }
   @DeleteMapping("/clean")
    public R<String> cleaCart(){
       Long userId=BaseContext.getCurrentId();
       LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.eq(ShoppingCart::getUserId,userId);
       shoppingCartService.remove(queryWrapper);
       return R.success("购物车已经清空");
   }



}
