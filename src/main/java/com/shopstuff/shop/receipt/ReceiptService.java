package com.shopstuff.shop.receipt;

import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.cart.CartItem;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;



    public List<ReceiptDTO> receiptsByUser(User user) {
        return receiptRepository.findByUser(user).stream()
                .map(ReceiptDTO::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Receipt> findById(int id) {
        return receiptRepository.findById(id);
    }


    public Receipt createReceipt(Cart cart) {
        var receipt= Receipt.builder()
                .user(cart.getUser())
                .build();
        receipt.setReceiptItems(cart.getCartItems().stream()
                .map(ReceiptService::toReceiptItem)
                .peek(x->x.setReceipt(receipt))
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
    public int receiptTotalPrice(Receipt receipt){
        return receipt.getReceiptItems()
                .stream()
                .mapToInt(x->x.getQuantity()*x.getItem().getPrice())
                .sum();
    }
}
