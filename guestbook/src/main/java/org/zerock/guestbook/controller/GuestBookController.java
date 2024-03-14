package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 생성자 주입. allArgsConstructor과 비슷. 단, 반드시 입력이 되는 애들만 받겠다.
public class GuestBookController {
//    private final GuestbookService service; // final은 상수를 뜻함. 즉, 변경 못하게 함

    private final GuestbookService service; //final로 선언

    @GetMapping({"","/"})
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list............." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));

    }

    @GetMapping({"/read","/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model ){
        GuestbookDTO dto=service.read(gno);
        model.addAttribute("dto",dto);
    }


    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){
        service.remove(gno);
        redirectAttributes.addFlashAttribute("delmsg", gno);

        return "redirect:/guestbook/list";

    }


    @PostMapping("/modify")
    public String modify(GuestbookDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){


        log.info("post modify.........................................");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

        redirectAttributes.addAttribute("gno",dto.getGno());


        return "redirect:/guestbook/read";
    }


    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        //새로 추가된 엔티티의 번호
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno); // 글번호를 "msg"에 담아서 전송. (몇번글이 등록되었습니다 이런거 구현하려고...)
        return "redirect:/guestbook/list";
    }
}
