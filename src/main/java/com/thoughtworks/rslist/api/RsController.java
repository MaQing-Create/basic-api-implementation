package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<String> rsList = new ArrayList<>(Arrays.asList("第一条事件", "第二条事件", "第三条事件"));

    @GetMapping("/rs/{index}")
    String getOneRs(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("/rs/list")
    String getRsBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null && end == null) return rsList.toString();
        if (start == null) return rsList.subList(0, end).toString();
        if (end == null) return rsList.subList(start - 1, rsList.size()).toString();
        return rsList.subList(start - 1, end).toString();
    }
}
