package white.dto;


import lombok.Data;
import white.entity.Dish;
import white.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
