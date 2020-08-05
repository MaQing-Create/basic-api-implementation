package com.thoughtworks.rslist.tools;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class CommonMethods {

    public static ResponseEntity<List> getList(Integer start, Integer end, List list) {
        if (start == null && end == null) return ResponseEntity.ok().body(list);
        if (start == null) return ResponseEntity.ok().body(list.subList(0, end > list.size() ? list.size() : end));
        if (end == null) return ResponseEntity.ok().body(list.subList(start < 1 ? 0 : start - 1, list.size()));
        return ResponseEntity.ok().body(list.subList(start < 1 ? 0 : start - 1, end > list.size() ? list.size() : end));
    }
}
