package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ServerResponse baseCount(){
        return iStatisticService.baseCount();
    }
}
