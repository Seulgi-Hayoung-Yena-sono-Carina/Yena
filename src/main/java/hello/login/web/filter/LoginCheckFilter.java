package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList={"/","/members/add","/login","/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI=httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 피러 시작{}",requestURI);
            if (isLoginCheckPatch(requestURI)){
                log.info("인증 체크 로직 실행 {}",requestURI);
                HttpSession session=httpRequest.getSession(false);
                if (session==null||session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
                    log.info("미인증 사용자 요청 {}",requestURI);
                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI); //로그인하고 기존 페이지로 자동 다시 넘어옴
                    return; //필터를 더는 진행하지 않고 서브릿, 컨트롤러도 물론 더는 호출되지 않음
                }
            }
            filterChain.doFilter(request,response);
        } catch(Exception e){
            throw e; //예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야 함
        } finally{
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트인 경우 인증 체크 X
     */
    private boolean isLoginCheckPatch(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI); //둘의 패턴이 매칭되는가?
        //whiteList에 안 들면 false
    }
}
