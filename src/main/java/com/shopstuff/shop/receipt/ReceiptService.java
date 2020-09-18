package com.shopstuff.shop.receipt;

import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.cart.CartItem;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public Receipt purchaseCart(Cart cart, int totalPrice) {
        var receipt = Receipt.builder()
                .user(cart.getUser())
                .totalPrice(totalPrice)
                .build();
        receipt.setReceiptItems(cart.getCartItems()
                .stream()
                .map(ReceiptItem::toReceiptItem)
                .peek(x -> x.setReceipt(receipt))
                .collect(Collectors.toList()));
        return receiptRepository.save(receipt);
    }

    public List<ReceiptDTO> receiptsByUser(User user) {
        return receiptRepository.findByUser(user).stream()
                .map(ReceiptDTO::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Receipt> findById(int id) {
        return receiptRepository.findById(id);
    }


    public Receipt cartToReceipt(Cart cart) {
        return Receipt.builder()
                .user(cart.getUser())
                .receiptItems(cart.getCartItems().stream().map(ReceiptService::toReceiptItem).collect(Collectors.toList()))
                .build();
    }

    private static ReceiptItem toReceiptItem(CartItem cartItem) {
        return ReceiptItem.builder()
                .item(cartItem.getItem())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
