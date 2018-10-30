package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tino on 10/17/18.
 */

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setId(userId);
        int rowCount = shippingMapper.insert(shipping); // id will add to shipping automatically, check xml file
        if(rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("Create address successfully", result);
        }
        return ServerResponse.createByErrorMessage("Cannot create address");
    }

    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if(resultCount > 0) {
            return ServerResponse.createBySuccess("Delete address successfully");
        }
        return ServerResponse.createBySuccess("Cannot delete address");
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId); // must set current userId to shipping to avoid other people use shipping from other users
        int resultCount = shippingMapper.updateByShipping(shipping);
        if(resultCount > 0) {
            return ServerResponse.createBySuccess("Update address successfully");
        }
        return ServerResponse.createBySuccess("Cannot update address");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if(shipping == null) {
            return ServerResponse.createByErrorMessage("Cannot find the address");
        }
        return ServerResponse.createBySuccess("Find the address successfully", shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> result = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(result);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
