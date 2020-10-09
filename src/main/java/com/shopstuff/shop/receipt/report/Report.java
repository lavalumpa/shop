package com.shopstuff.shop.receipt.report;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.Year;
import java.util.List;

@Data
@Builder
public class Report {
    private Year year;
    private List<ReportItem> reportItemList;
    private BigInteger revenue;
}
