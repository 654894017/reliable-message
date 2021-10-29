package com.damon.rmq.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 页面控制器
 * 
 * @author xianping_lu
 *
 */
@Controller
@RequestMapping(value = "page")
public class PageController {

    @RequestMapping(value = "{model}/{name}", method = RequestMethod.GET)
    public String page(@PathVariable("model") String model, @PathVariable("name") String name) {
        return model + "/" + name;
    }
}
