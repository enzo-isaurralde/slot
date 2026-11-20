package com.casino.slot.api;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SpinResponse {
    private String spinId;
    private List<List<String>> grid;
    private List<WinLine> wins;
    private BigDecimal totalWin;
    private Long balance;
    private Boolean isBonus;

    @Data
    public static class WinLine {
        private int line;
        private String symbol;
        private int count;
        private BigDecimal payout;
    }
}