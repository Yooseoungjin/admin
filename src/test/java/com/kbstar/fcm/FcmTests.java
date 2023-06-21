package com.kbstar.fcm;

import com.kbstar.util.PushNotificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@Slf4j
@SpringBootTest
class FcmTests {

    @Autowired
    private PushNotificationUtil pushNotificationUtil;
    //특정 유저에게만 보내려면 userToken을 사용한다
    String userToken = "cZf7Vmh9TqORFYMTFmiLRh:APA91bHyhyvqqPT-Jcby0fMp5HZgKV1Mq_aqIQ2bCPnh4r0Kk2Aw5GxQv2vpx_ZeIKADs0e1BLXIj-CiQ_INNAVxBFsZst1YhZfgVskaUZgqFKxg2rUmrCujqbJH_Y3hPFm0DwOJKPtZ";
    String imgUrl = "https://www.w3schools.com/css/img_5terre.jpg";
    @Test
    void contextLoads() throws IOException {

        pushNotificationUtil.sendCommonMessage("춘향에 오신것을 환영", "Good", "/register", imgUrl);

//        pushNotificationUtil.sendTargetMessage("SPRING title4tti", "hello", "/register", userToken);

    }

}