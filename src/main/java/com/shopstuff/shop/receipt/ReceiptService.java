package com.shopstuff.shop.receipt;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.cart.CartItem;

import com.shopstuff.shop.receipt.report.Report;
import com.shopstuff.shop.receipt.report.ReportItem;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;


    public Report yearlyReport(Year year) {
        var payments = receiptRepository.findAllByCreatedAtBetween(
                LocalDateTime.of(year.getValue(), Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(year.getValue(), Month.DECEMBER, 31, 23, 59));
        var items = payments.stream()
                .flatMap(x -> x.getReceiptItems().stream())
                .map(ReportItem::toReportItem)
                .collect(Collectors.toMap((x -> x.getItem().getId()), (x -> x)
                        , (x, y) -> {
                            x.setQuantity(x.getQuantity() + y.getQuantity());
                            return x;
                        }));
        var revenue = payments.stream().map(Receipt::getTotalPrice).map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.valueOf(0));
        return Report.builder()
                .reportItemList(new ArrayList<>(items.values()))
                .year(year).revenue(revenue).build();
    }

    @SneakyThrows(DocumentException.class)
    public byte[] reportToPdf(Report report) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph items=new Paragraph("Items sold:", font);
        PdfPTable table = new PdfPTable(4);
        setItemTableHeaders(table);
        addItemsToTable(table, report);
        items.add(table);
        items.add(new Paragraph("Revenue: " + report.getRevenue().toString() , font));
        items.add(new Paragraph("Year: " + report.getYear().toString(), font));
        document.add(items);
        document.close();
        return baos.toByteArray();
    }


    private void setItemTableHeaders(PdfPTable table) {
        var headerNames = new ArrayList<String>();
        headerNames.add("item id");
        headerNames.add("item name");
        headerNames.add("item price");
        headerNames.add("quantity bought");
        for (String s : headerNames) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(s));
            table.addCell(header);
        }
    }

    private void addItemsToTable(PdfPTable pdfPTable, Report report) {
        for (ReportItem r : report.getReportItemList()) {
            pdfPTable.addCell(String.valueOf(r.getItem().getId()));
            pdfPTable.addCell(r.getItem().getName());
            pdfPTable.addCell(String.valueOf(r.getItem().getPrice()));
            pdfPTable.addCell(String.valueOf(r.getQuantity()));
        }
    }


    public List<ReceiptDTO> receiptsByUser(User user) {
        return receiptRepository.findByUser(user).stream()
                .map(ReceiptDTO::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Receipt> findById(int id) {
        return receiptRepository.findById(id);
    }


    public Receipt createReceipt(Cart cart) {
        var receipt = Receipt.builder()
                .user(cart.getUser())
                .build();
        receipt.setReceiptItems(cart.getCartItems().stream()
                .map(ReceiptService::toReceiptItem)
                .peek(x -> x.setReceipt(receipt))
                .collect(Collectors.toList()));
        receipt.setTotalPrice(receiptTotalPrice(receipt));
        return receiptRepository.save(receipt);
    }

    private static ReceiptItem toReceiptItem(CartItem cartItem) {
        return ReceiptItem.builder()
                .item(cartItem.getItem())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public int receiptTotalPrice(Receipt receipt) {
        return receipt.getReceiptItems()
                .stream()
                .mapToInt(x -> x.getQuantity() * x.getItem().getPrice())
                .sum();
    }

    public boolean correctUser(String username, int id){
        return receiptRepository.findById(id)
                .map(Receipt::getUser)
                .map(User::getName)
                .filter(x->x.equals(username))
                .isPresent();
    }
}
