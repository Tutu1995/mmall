package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IStatisticService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
    public ServerResponse baseCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return iStatisticService.baseCount();
        }
        return ServerResponse.createByErrorMessage("Need to login.");

    }
}
