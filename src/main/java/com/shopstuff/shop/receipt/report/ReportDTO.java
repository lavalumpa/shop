package com.shopstuff.shop.receipt.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.Year;
import java.util.List;

import java.util.stream.Collectors;

@Data
@Builder
public class ReportDTO {
    @JsonProperty("items")
    private List<ReportItemDto> reportItemDtos;
    private BigInteger revenue;
    private Year year;

    public static ReportDTO toDTO(Report report){
        var reportItems=report.getReportItemList()
                .stream()
                .map(ReportItemDto::toDto)
                .collect(Collectors.toList());
        return ReportDTO.builder().year(report.getYear()).revenue(report.getRevenue())
                .reportItemDtos(reportItems).build();
    }
}
