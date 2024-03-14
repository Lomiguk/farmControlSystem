package ru.skibin.farmsystem.util;

import org.springframework.stereotype.Component;

@Component
public class LimitOffsetTransformer {
    public Integer getLimit(Integer limit) {
        return limit == null ? Integer.MAX_VALUE : limit;
    }

    public Integer getOffset(Integer offset) {
        return offset == null ? 0 : offset;
    }
}
