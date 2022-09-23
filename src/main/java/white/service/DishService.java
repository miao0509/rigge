package white.service;

import com.baomidou.mybatisplus.extension.service.IService;
import white.dto.DishDto;
import white.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
