package white.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import white.common.CustomException;
import white.entity.Category;
import white.entity.Dish;
import white.entity.Setmeal;
import white.mapper.CategoryMapper;
import white.service.CategoryService;
import white.service.DishService;
import white.service.SetMealService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    public DishService dishService;
    @Autowired
    public SetMealService setMealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(Dish::getCategoryId,id);

        int countDish = dishService.count(dishWrapper);
        if (countDish> 0 ){
            throw new CustomException("已经关联菜品，无法删除");
        }
        LambdaQueryWrapper<Setmeal> setMealWrapper = new LambdaQueryWrapper<>();
        setMealWrapper.eq(Setmeal::getCategoryId,id);
        int countSetMeal = setMealService.count(setMealWrapper);
        if (countSetMeal >0 ){
            throw new CustomException("已经关联菜单，无法删除");
        }
        super.removeById(id);
    }
}
