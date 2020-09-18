package com.shopstuff.shop.receipt;


import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    private final UserService userService;

    @GetMapping("user/{id}/receipt")
    public List<ReceiptDTO> getUserReceipts(@PathVariable int id){
        var user=userService.findById(id).orElseThrow(NotFoundException::new);
        return receiptService.receiptsByUser(user);
    }

    @GetMapping("receipt/{id}")
    public ReceiptDTO getReceipt(@PathVariable int id){
        var receipt=receiptService.findById(id).orElseThrow(NotFoundException::new);
        return ReceiptDTO.toDTO(receipt);
    }
}
