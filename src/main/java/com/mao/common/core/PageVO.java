package com.mao.common.core;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author myseital
 * @date 2022/8/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> implements Serializable {
    private List<T> rows;
    private Long total;

    public void empty() {
        this.rows = new ArrayList<>();
        this.total = 0L;
    }

    public static <T> PageVO<T> returnEmpty() {
        return new PageVO<T>(Lists.newArrayList(), 0L);
    }
}
