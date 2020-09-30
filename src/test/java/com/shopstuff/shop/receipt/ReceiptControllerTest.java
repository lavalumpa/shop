package com.shopstuff.shop.receipt;

import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ReceiptControllerTest {

    private final MockMvc mockMvc;
    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    public void testingShowReceiptWithGivenID() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        itemRepository.save(item);
        user=userRepository.save(user);
        receipt=receiptRepository.save(receipt);
        mockMvc.perform(get("/receipt/{id}",receipt.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(receipt.getId()))
                .andExpect(jsonPath("$.items[0].id").value(item.getId()))
                .andExpect(jsonPath("$.items[0].price").value(1000))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.purchasedAt").exists())
                .andExpect(jsonPath("$.purchasedBy").value(user.getId()));
    }

    @Test
    public void testingShowReceiptForUser() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        itemRepository.save(item);
        user=userRepository.save(user);
        receiptRepository.save(receipt);
        mockMvc.perform(get("/user/{id}/receipt",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].items[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].items[0].price").value(1000))
                .andExpect(jsonPath("$[0].items[0].quantity").value(2))
                .andExpect(jsonPath("$[0].purchasedAt").exists())
                .andExpect(jsonPath("$[0].purchasedBy").value(user.getId()));
    }
}
