package com.jial.advice;

import com.alibaba.fastjson.JSON;
import com.jial.pojo.VO;
import org.junit.jupiter.api.Test;

class ExceptionAdviceTest {

    @Test
    void handleException() {
        VO vo = new VO();
        vo.setCode(1);
        vo.setObj("服务器异常!");
        System.out.println(JSON.toJSONString(vo));
    }
}