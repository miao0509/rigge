package white.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import white.common.CustomException;
import white.dto.SetmealDto;
import white.entity.Setmeal;
import white.entity.SetmealDish;
import white.mapper.SetMealMapper;
import white.service.SetMealService;
import white.service.SetmealDishService;

import java.util.List;
import java.util.Set;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        for (SetmealDish dish : setmealDto.getSetmealDishes()) {
            dish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);
        int count = this.count(wrapper);
        if (count >0){
            throw  new CustomException("套餐正在售卖中,无法删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
