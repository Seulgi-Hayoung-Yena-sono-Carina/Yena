package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
// Spring MVC의 HandlerMethodArgumentResolver를 구현하여
// @Login 애노테이션이 붙은 Member 타입 매개변수를 자동으로 주입하는 역할

//Spring MVC에서 컨트롤러의 매개변수를 원하는 방식으로 바꿔주는 인터페이스
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    //컨트롤러의 특정 매개변수를 이 Argument Resolver가 처리할 수 있는지 검사
    public boolean supportsParameter(MethodParameter methodParameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = methodParameter.hasParameterAnnotation(Login.class); //login annotation이 파라미터에 붙어있는가
        boolean hasMemberType = Member.class.isAssignableFrom(methodParameter.getParameterType()); //매개변수 타입이 Member 또는 그 하위 클래스인지
        return hasLoginAnnotation&&hasMemberType;
    }
    //supportsParameter이 true를 반환하면, 이 메서드 실행! (false면 실행 X)
    //현재 요청의 세션(Session)에서 로그인된 사용자(Member 객체)를 가져와 반환
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");
        HttpServletRequest request = (HttpServletRequest)
                webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
