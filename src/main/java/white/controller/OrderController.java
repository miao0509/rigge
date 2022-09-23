package white.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;
import white.common.BaseUtils;
import white.common.R;
import white.dto.OrdersDto;
import white.entity.OrderDetail;
import white.entity.Orders;
import white.entity.User;
import white.service.OrderDetailService;
import white.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import white.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize){
        //查询订单信息
        //查询用户信息
        //OrderDetail
        Long userId = BaseUtils.getCurrentThreadId();
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,userId);
        orderService.page(pageInfo,wrapper);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        User user = userService.getById(userId);
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        for (Orders record : pageInfo.getRecords()) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(record,ordersDto);
            ordersDto.setUserName(user.getName());
            ordersDto.setPhone(user.getPhone());
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId,record.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            ordersDtoList.add(ordersDto);
        }
        dtoPage.setRecords(ordersDtoList);
        return  R.success(dtoPage);
    }
}