package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.OrderMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.dao.UserMapper;
import com.mmall.service.IStatisticService;
import com.mmall.vo.StatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tino on 12/1/18.
 */

@Service("iStatisticService")
public class StatisticServiceIml implements IStatisticService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ServerResponse<StatisticVo> baseCount() {
        StatisticVo statisticVo = new StatisticVo();
        int userCount = userMapper.checkUserNumber();
        int productCount = productMapper.checkProductNumber();
        int orderCount = orderMapper.checkOrderNumber();
        statisticVo.setUserCount(userCount);
        statisticVo.setProductCount(productCount);
        statisticVo.setOrderCount(orderCount);

        return ServerResponse.createBySuccess(statisticVo);
    }
}
