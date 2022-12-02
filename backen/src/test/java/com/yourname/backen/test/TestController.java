package com.yourname.backen.test;

import com.yourname.backen.common.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping(value = "/test")
    @ResponseBody
    public R test(){
        return R.success();
    }
}
