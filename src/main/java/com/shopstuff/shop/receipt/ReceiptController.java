package com.shopstuff.shop.receipt;


import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.receipt.report.ReportDTO;
import com.shopstuff.shop.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.PastOrPresent;
import javax.websocket.server.PathParam;
import java.time.Year;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ReceiptController {

    private final ReceiptService receiptService;
    private final UserService userService;

    @GetMapping("user/{id}/receipt")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @userService.correctUser(principal.username,#id))")
    public List<ReceiptDTO> getUserReceipts(@PathVariable int id) {
        var user = userService.findById(id).orElseThrow(NotFoundException::new);
        var receipts= receiptService.receiptByUser(user);
        return ReceiptDTO.listToDTO(receipts);
    }

    @GetMapping(value = "user/{id}/receipt", headers = "accept=application/pdf", produces = {"application/pdf"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @userService.correctUser(principal.username,#id))")
    public ResponseEntity<byte[]> getUserReceiptsPDF(@PathVariable int id) {
        var user = userService.findById(id).orElseThrow(NotFoundException::new);
        var receipts = receiptService.receiptByUser(user);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(receiptService.receiptListToPdf(receipts));
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @receiptService.correctUser(principal.username,#id))")
    @GetMapping("receipt/{id}")
    public ReceiptDTO getReceipt(@PathVariable int id) {
        var receipt = receiptService.findById(id).orElseThrow(NotFoundException::new);
        return ReceiptDTO.toDTO(receipt);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @receiptService.correctUser(principal.username,#id))")
    @GetMapping(value = "receipt/{id}", headers = {"accept=application/pdf"},produces = {"application/pdf"} )
    public ResponseEntity<byte[]> getReceiptPDF(@PathVariable int id) {
        var receipt = receiptService.findById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .body(receiptService.receiptToPdf(receipt));
    }


    @GetMapping(value = "/report", headers = {"accept=application/json"}, produces = {"application/json"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> getReportJSON(@PathParam("year") @PastOrPresent(message = "Year has to be present or past") Year year) {
        var report = receiptService.yearlyReport(year);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ReportDTO.toDTO(report));
    }

    @GetMapping(value = "/report", headers = {"accept=application/pdf"}, produces = {"application/pdf"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getReportPDF(@PathParam("year") @PastOrPresent(message = "Year has to be present or past") Year year) {
        var report = receiptService.yearlyReport(year);
        var pdf = receiptService.reportToPdf(report);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}