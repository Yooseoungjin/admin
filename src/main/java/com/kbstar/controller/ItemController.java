package com.kbstar.controller;

import com.kbstar.dto.Item;
import com.kbstar.dto.ItemSearch;
import com.kbstar.service.ItemService;
import com.kbstar.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j /*로그 찍는 골뱅이 이거 넣으면 log.info 사용가능 */
@Controller
@RequestMapping("/item") // /cust를 넣음으으로 기본적으로 주소에 /cust가 셋팅됨
public class ItemController {
    @Autowired
    ItemService itemService;
    String dir = "item/";
    @Value("${uploadimgdir}") /*이미지 저장경로*/
    String imgdir;
    @RequestMapping("/add")
    public String add(Model model){
        model.addAttribute("center",dir+"add");
        return "index";
    }
    @RequestMapping("/addimpl")
    public String addimpl(Model model, Item item) throws Exception {
        MultipartFile mf = item.getImg();
        String imgname = mf.getOriginalFilename();
//        log.info("---------------------------------------------------------------------");
//        log.info(imgname);
//        log.info("---------------------------------------------------------------------"); 확인용 로그.인포
        item.setImgname(imgname);
        itemService.register(item);
        FileUploadUtil.saveFile(mf,imgdir);
        return "redirect:/item/all";
    }

    @RequestMapping("/all")
    public String all(Model model) throws Exception {
        List<Item> list = null;
        list = itemService.get();
        model.addAttribute("ilist",list);
        model.addAttribute("center",dir+"all");
        return "index";
    }

    @RequestMapping("/detail")
    public String detail(Model model, int id) throws Exception {
        Item item = null;
        item = itemService.get(id);
        model.addAttribute("gitem",item);
        model.addAttribute("center",dir+"detail");
        return "index";
    }

    @RequestMapping("/updateimpl")
    public String updateimpl(Model model, Item item) throws Exception {
        MultipartFile mf = item.getImg();
        String new_imgname = mf.getOriginalFilename();

      if(new_imgname.equals("") || new_imgname == null){
           itemService.modify(item);
        }else{
            item.setImgname(new_imgname);
            itemService.modify(item);
            FileUploadUtil.saveFile(mf,imgdir);
        }
/*        if(!imgname.equals("") || imgname != null){
            item.setImgname(imgname);
            FileUploadUtil.saveFile(mf,imgdir);
        }
        itemService.modify(item);*/
        return "redirect:/item/detail?id="+item.getId();
    }
    @RequestMapping("/deleteimpl")
    public String delete(Model model,int id) throws Exception {
        itemService.remove(id);
//        model.addAttribute("center",dir+"detail");
        return "redirect:/item/all";
    }

    @RequestMapping("/search")
    public String search(Model model, ItemSearch ms) throws Exception {
//        log.info("---------------------------------------");
//        log.info(ms.getStartdate()); 어떻게 입력되는지 확인후 마이바티스 sql변경할것
//        log.info(ms.getEnddate());    어떻게 입력되는지 확인후 마이바티스 sql변경할것
//        log.info("---------------------------------------");
        List<Item> list = null;
        list = itemService.search(ms);
        model.addAttribute("ms",ms);
        model.addAttribute("ilist",list);
        model.addAttribute("center",dir+"all");
        return "index";
    }
}