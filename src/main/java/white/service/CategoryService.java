package white.service;

import com.baomidou.mybatisplus.extension.service.IService;
import white.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
