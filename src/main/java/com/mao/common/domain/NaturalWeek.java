package com.mao.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 自然周
 *
 * @author myseital
 * @date 2022/12/6
 */
@Data
public class NaturalWeek implements Serializable {
    /**
     * 日期(2022-12-01——2022-12-30)
     */
    private String day;
    /**
     * 开始日期
     */
    private LocalDate startDate;
    /**
     * 结束日期
     */
    private LocalDate endDate;

    public NaturalWeek(LocalDate startDate, LocalDate endDate, String separator) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("自然周数据异常");
        }
        if (separator == null || "".equals(separator)) {
            separator = "——";
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.day = startDate + separator + endDate;
    }
}
