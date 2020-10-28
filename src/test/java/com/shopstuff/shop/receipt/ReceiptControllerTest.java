package com.shopstuff.shop.receipt;

import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.Role;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @WithMockUser(username =  "Steve", roles = "CUSTOMER")
    public void testingShowReceiptWithGivenID() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").roles(Set.of(Role.CUSTOMER)).build();
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
    @WithMockUser(username =  "Steve", roles = "CUSTOMER")
    public void testingShowReceiptForUser() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").roles(Set.of(Role.CUSTOMER)).password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        itemRepository.save(item);
        user=userRepository.save(user);
        receiptRepository.save(receipt);
        mockMvc.perform(get("/user/{id}/receipt",user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].items[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].items[0].price").value(1000))
                .andExpect(jsonPath("$[0].items[0].quantity").value(2))
                .andExpect(jsonPath("$[0].purchasedAt").exists())
                .andExpect(jsonPath("$[0].purchasedBy").value(user.getId()));
    }

    @Test
    @WithMockUser(roles= "ADMIN")
    public void testForYearReportJson() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").roles(Set.of(Role.ADMIN)).password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        item=itemRepository.save(item);
        userRepository.save(user);
        receiptRepository.save(receipt);
        mockMvc.perform(get("/report").param("year","2020").header("Accept", MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type","application/json"))
                .andExpect(jsonPath("$.items[0].id").value(item.getId()))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.year").value(2020))
                .andExpect(jsonPath("$.revenue").value(1000*2));
    }
    @Test
    @WithMockUser(roles= "ADMIN")
    public void testForYearReportPdf() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").roles(Set.of(Role.ADMIN)).password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        itemRepository.save(item);
        userRepository.save(user);
        receiptRepository.save(receipt);
        mockMvc.perform(get("/report").param("year","2020").header("Accept", MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-type","application/pdf"));
    }
    @Test
    @WithMockUser(roles= "ADMIN")
    public void testForUserSpecificReceiptPdf() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").roles(Set.of(Role.ADMIN)).password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        itemRepository.save(item);
        userRepository.save(user);
        receipt=receiptRepository.save(receipt);
        mockMvc.perform(get("/receipt/{id}",receipt.getId()).header("Accept", MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-type","application/pdf"));
    }
    @Test
    @WithMockUser(roles= "ADMIN")
    public void testForUserPdfAllReceipts() throws Exception{
        var user=User.builder().name("Steve").email("steve705@yahoo.com").roles(Set.of(Role.ADMIN)).password("4az5j@98gbmawq").build();
        var item= Item.builder().name("Phone").price(1000).build();
        var receiptItem=ReceiptItem.builder().item(item).quantity(2).build();
        var receipt=Receipt.builder().totalPrice(1000*2).user(user).build();
        receipt.addReceiptItem(receiptItem);
        itemRepository.save(item);
        user=userRepository.save(user);
        receiptRepository.save(receipt);
        mockMvc.perform(get("/user/{id}/receipt",user.getId()).header("Accept", MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-type","application/pdf"));
    }
}
