package com.kbstar.item;
import com.kbstar.dto.Item;
import com.kbstar.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UpdateTest {
    @Autowired
    ItemService service;
    @Test
    void contextLoads(){

        Item obj = new Item(104,"야구모자",2,"bbbbb.jpg",null);
        try {
            service.modify(obj);
            service.get(104);
        } catch (Exception e) {
            log.info("에러...");
            e.printStackTrace();
        }
    }
}
