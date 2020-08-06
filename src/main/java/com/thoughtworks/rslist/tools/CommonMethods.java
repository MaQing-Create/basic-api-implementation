package com.thoughtworks.rslist.tools;

import com.thoughtworks.rslist.exception.InvalidIndexException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CommonMethods {

    public static ResponseEntity<List> getList(Integer start, Integer end, List list) throws InvalidIndexException, Exception {
        if (start == null && end == null) return ResponseEntity.ok().body(list);
        if (start != null) checkIsInputIndexOutOfRange(start, list, "invalid request param");
        if (end != null) checkIsInputIndexOutOfRange(end, list, "invalid request param");
        if (start == null) return ResponseEntity.ok().body(list.subList(0, end));
        if (end == null) return ResponseEntity.ok().body(list.subList(start - 1, list.size()));
        return ResponseEntity.ok().body(list.subList(start - 1, end));
    }

    public static void checkIsInputIndexOutOfRange(int index, List list, String errorMessage) throws Exception,
            InvalidIndexException {
        if (index < 1 || index > list.size()) {
            throw new InvalidIndexException(errorMessage);
        }
    }
}
