package white.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import white.entity.ShoppingCart;
import white.mapper.ShoppingCartMapper;
import white.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
