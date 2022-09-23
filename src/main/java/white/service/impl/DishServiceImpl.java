package white.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import white.dto.DishDto;
import white.entity.Dish;
import white.entity.DishFlavor;
import white.mapper.DishMapper;
import white.service.DishFlavorService;
import white.service.DishService;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        DishDto dishDto = new DishDto();
        dishDto.setFlavors(list);
        BeanUtils.copyProperties(dish,dishDto);
        return dishDto;
    }

    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(wrapper);
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }
}
