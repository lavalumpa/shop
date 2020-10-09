package com.shopstuff.shop.receipt.report;

import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.receipt.ReceiptItem;
import lombok.Data;

@Data
public class ReportItem {
    private Item item;
    private int quantity;

    public static ReportItem toReportItem(ReceiptItem receiptItem){
        var report=new ReportItem();
        report.item=receiptItem.getItem();
        report.quantity=receiptItem.getQuantity();
        return report;
    }

}
