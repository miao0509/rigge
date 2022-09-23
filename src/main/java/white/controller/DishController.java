package white.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.common.R;
import white.dto.DishDto;
import white.entity.Category;
import white.entity.Dish;
import white.entity.DishFlavor;
import white.service.CategoryService;
import white.service.DishFlavorService;
import white.service.DishService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }
    @GetMapping("page")
    public R<Page<DishDto>> page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, wrapper);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Category> list = categoryService.list();
        Map<Long, String> idToName = list.stream().collect(Collectors.toMap(Category::getId, Category::getName));
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dish : pageInfo.getRecords()) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setCategoryName(idToName.get(dish.getCategoryId()));
            dishDtoList.add(dishDto);
        }
        dtoPage.setRecords(dishDtoList);
        return R.success(dtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("更新成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getCategoryId());
        wrapper.eq(Dish::getStatus,1);
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(wrapper);
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dish1 : dishList) {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,dish1.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            dishDto.setFlavors(dishFlavorList);
            dishDtoList.add(dishDto);
        }

        return R.success(dishDtoList);
    }

}
