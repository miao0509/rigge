package white.dto;


import lombok.Data;
import white.entity.Setmeal;
import white.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
