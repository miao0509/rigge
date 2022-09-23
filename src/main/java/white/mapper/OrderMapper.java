package white.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import white.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}