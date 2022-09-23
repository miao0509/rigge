package white.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import white.entity.OrderDetail;
import white.mapper.OrderDetailMapper;
import white.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}