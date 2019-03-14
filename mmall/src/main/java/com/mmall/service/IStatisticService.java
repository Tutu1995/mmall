package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.StatisticVo;

/**
 * Created by tino on 12/1/18.
 */
public interface IStatisticService {
    ServerResponse<StatisticVo> baseCount();
}
