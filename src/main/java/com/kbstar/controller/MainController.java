package com.kbstar.controller;

import com.kbstar.dto.Adm;
import com.kbstar.service.AdmService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class MainController {
    @Value("${adminserver}")
    String adminserver;
    // 패스워드 암호화 하기.
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    AdmService admservice;
    // 127.0.0.1

    @RequestMapping("/")
    public String main(Model model){
        model.addAttribute("adminserver",adminserver);
        return "index";
    }

    // 1. 회원가입 페이지 가기
    @RequestMapping("/register") // 127.0.0.1:8080/register
    public String register(Model model){

        model.addAttribute("center",  "register"); // center에는 charts 페이지 뿌려져라.
        model.addAttribute("leftNav", "leftNav");
        return "index";
    }
    // 2. 회원가입 시 > 회원가입impl로
    @RequestMapping("/registerimpl")
    public String registerimpl(Model model, Adm adm, HttpSession session) throws Exception { //사용자 전체정보가 필요한 경우 : CustDTO 이용해보기.
        // 가입완료 정보를 DB에 저장하기
        try {
            adm.setPwd(encoder.encode(adm.getPwd() ));
            //위 코드 설명 . 고객이 입력한 패스워드 -> 가져와서 암호화한 뒤 다시 cust객체로 넣어 db에 집어넣는다.
            admservice.register(adm);
            // 가입완료 즉시, loginadm 에 고객정보를 일시적으로 담아, 로그인까지 바로 되게 하기
            session.setAttribute("loginadm", adm);
        } catch (Exception e) {
            throw new Exception("회원가입에 실패했습니다. ER0005");
        }

        model.addAttribute("radm",adm);
        //adm정보를 radm 에 담아서, 화면에 뿌릴 때 사용하기(사용법 : ${radm.name}. key값(rcust), val값(adm)
        model.addAttribute("center", "registerok"); // center에는 register페이지가 뿌려져라.
        return "index"; // 해석 : index 파일 가운데 부분에 registerok 부분을 넣어서 화면을 만들어라.
    }
    // 3. 로그인 페이지 가기
    @RequestMapping("/login") // 127.0.0.1:8080/login
    public String login(Model model){

        model.addAttribute("center",  "login"); // center에는 login 페이지 뿌려져라.
        model.addAttribute("leftNav", "leftNav");
        return "index";
    }

    @RequestMapping("/logoutimpl") // 127.0.0.1:8080/login
    public String logoutimpl(Model model, HttpSession session){
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }

    // 4. 로그인 시 > loginimpl 로
    // session 추가해서, 로그인 완료 후부턴 adm정보 담고있기(00초 동안 로그인 유지).
    @RequestMapping("/loginimpl")
    public String loginimpl(Model model, String id, String pwd,
                            HttpSession session) throws Exception { // 서버로 넘긴 정보 id, pwd, session
        Adm adm = null; // 받을 준비하기
        String nextPage = "loginfail";
        try {
            adm = admservice.get(id);
            //  get할 id 있고 && adm가 가지고 있는 암호화된 pwd가 일치하면 = 로그인 성공
            if( adm != null && encoder.matches(pwd, adm.getPwd()) ){
                nextPage = "loginok"; // 페이지를 loginok로 교체하기.

                // 성공한 로그인 정보는 session에 loginadm라는 이름으로 adm에 넣어주기.
                // 기능 : session 에 넣어주면, 00초 동안 로그인 유지 가능.**
                // session에 담은 정보도, jsp에서 loginadm라는 이름으로 정보 끄집어내기 가능하다.
                session.setMaxInactiveInterval(100000000);
                session.setAttribute("loginadm", adm);
                log.info(adm.toString());

            }
        } catch (Exception e) {
            throw new Exception("ER0006 : 시스템 장애로 인해 로그인이 실패했습니다. 잠시후 재거래 바랍니다. ");
        }
        model.addAttribute("center", nextPage); // center에는 nextPage 뿌려져라.
        return "index";
    }
    // 5. 본인의 회원정보 상세보기
    @RequestMapping("/adminfo")
    public String custinfo(Model model, String id) throws Exception {
        Adm adm = null; // 받을 준비.
        try {
            adm = admservice.get( id );
        } catch (Exception e) {
            throw new Exception("본인의 정보 상세보기 중 에러발생 ER0007");
        }
        model.addAttribute("adminfo", adm); // DB cust에 담겨있던 id를 adminfo페이지가 페이지로 넘겨주기 그래야 브라우저에서 보이니까.
        model.addAttribute("center", "adminfo"); // center에는 adminfo페이지가 뿌려져라.
        return "index";
    }

    // 6. 본인의 회원정보 수정하기
    @RequestMapping("/adminfoimpl")
    public String adminfoimpl(Model model, Adm adm) throws Exception {

        try {
            log.info("-----------", adm.getPwd() );
            // 1- 수정 전! pwd만 암호화하기.
            adm.setPwd(encoder.encode( adm.getPwd() ));
            // 2- 수정받은 고객정보 id, pwd, name 모두 수정.
            admservice.modify( adm );
        } catch (Exception e) {
            throw new Exception("본인의 회원정보 수정 중 에러발생 ER0008");
        }
        // model 말고, redirect 사용해서 다시 위치해있던 adminfo 페이지 + 해당고객의 adminfo페이지(= adm.getid)로 가기.
        return "redirect:/adminfo?id=" + adm.getId();
    }

    @RequestMapping("/websocket")
    public String websocket(Model model){
        model.addAttribute("adminserver",adminserver);
        model.addAttribute("center",  "websocket");
        return "index";
    }

    @RequestMapping("/callcenter")
    public String callcenter(Model model){
        model.addAttribute("adminserver",adminserver);
        model.addAttribute("center",  "callcenter");
        return "index";
    }
}