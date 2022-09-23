package white.service;

import com.baomidou.mybatisplus.extension.service.IService;
import white.dto.SetmealDto;
import white.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    void removeWithDish(List<Long> ids);
}
