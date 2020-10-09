package com.shopstuff.shop.receipt.report;

import com.shopstuff.shop.item.Item;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportDTOTest {

    @Test
    public void testingMakingReportDTOWithOneReportItem(){
        var reportItem=new ReportItem();
        reportItem.setItem(Item.builder().id(1).price(2000).build());
        reportItem.setQuantity(1);
        var report=Report.builder().reportItemList(List.of(reportItem)).revenue(BigInteger.valueOf(2000)).year(Year.of(2020)).build();
        var reportDTO=ReportDTO.toDTO(report);
        assertEquals(Year.of(2020).getValue(),reportDTO.getYear().getValue());
        assertEquals(2000,report.getRevenue().intValue());
        assertEquals(1,reportDTO.getReportItemDtos().get(0).getItemId());
        assertEquals(1,reportDTO.getReportItemDtos().get(0).getQuantity());
    }

    @Test
    public void testingMakingReportDTOWithNoReportItem(){
        var report=Report.builder().reportItemList(new ArrayList<>()).revenue(BigInteger.valueOf(0)).year(Year.of(2020)).build();
        var reportDTO=ReportDTO.toDTO(report);
        assertEquals(Year.of(2020).getValue(),reportDTO.getYear().getValue());
        assertEquals(0,report.getRevenue().intValue());
        assertTrue(reportDTO.getReportItemDtos().isEmpty());

    }

}
