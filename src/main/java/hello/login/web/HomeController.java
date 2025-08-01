package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    private final SessionManager sessionManager;
    //@GetMapping("/") ->사용자가 루트 경로로 요청을 보내면 실행되는 메서드
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLoginV1(@CookieValue(name="memberId",required = false) Long memberId, Model model){
        if(memberId==null){
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember==null){
            return "home"; //쿠키가 너무 옛날에 만들어졌거나 다양한 이유로 null일 수 있음
        }
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request,Model model){

        //세션 관리자에 저장된 회원 정보 조회
        Member member= (Member)sessionManager.getSession(request);

        //로그인
        if(member==null){
            return "home";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request,Model model){

        HttpSession session = request.getSession(false); //로그인한 사용자가 있는지 확인하는 용도

        if(session==null){
            return "home";
        }
        Member loginmember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home으로 (ex: 로그아웃했거나, 세션이 만료되었을 때 등)
        if(loginmember==null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginmember);
        return "loginHome";
    }
    //@GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name=SessionConst.LOGIN_MEMBER,required = false) Member loginMember, Model model){

        /*HttpSession session = request.getSession(false); //로그인한 사용자가 있는지 확인하는 용도

        if(session==null){
            return "home";
        }
        Member loginmember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);*/

        //세션에 회원 데이터가 없으면 home으로 (ex: 로그아웃했거나, 세션이 만료되었을 때 등)
        if(loginMember==null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){

        //세션에 회원 데이터가 없으면 home으로 (ex: 로그아웃했거나, 세션이 만료되었을 때 등)
        if(loginMember==null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

}