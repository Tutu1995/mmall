package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IStatisticService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tino on 12/1/18.
 */

@Controller
@RequestMapping("/manage/statistic/")
public class StatisticManageController {

    @Autowired
    private IStatisticService iStatisticService;

    @RequestMapping(value="base_count.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse baseCount(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("Need to login");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if(user != null){
            return iStatisticService.baseCount();
        }
        return ServerResponse.createByErrorMessage("Need to login.");

    }
}
