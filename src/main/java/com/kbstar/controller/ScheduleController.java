package com.kbstar.controller;

import com.kbstar.dto.Cart;
import com.kbstar.dto.MsgAdm;
import com.kbstar.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class ScheduleController {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    CartService cartService;
    @Scheduled(cron = "*/5 * * * * *")
    public void cronJobDailyUpdate() {
//      log.info("----------- Scheduler Start------------");
        Random r = new Random();
        int content1 = r.nextInt(100)+1;
        int content2 = r.nextInt(1000)+1;
        int content3 = r.nextInt(500)+1;
        int content4 = r.nextInt(10)+1;

        MsgAdm msg = new MsgAdm();
        msg.setContent1(content1);
        msg.setContent2(content2);
        msg.setContent3(content3);
        msg.setContent4(content4);
        messagingTemplate.convertAndSend("/sendadm", msg);
    }

    @Scheduled(cron = "*/30000 * * * * *")
    public void cartUpdate() throws Exception {
        List<Cart> sumcart = cartService.sumcart();
        /*cartService.balcart() < 이 값이 배열로 오기때문에 배열의 0번째 값을 가져오기 위해서
        아래와 같이 배열의 0번째 값을 get해옴*/
        sumcart.get(0);
        log.info(sumcart.get(0)+"");
    }
}