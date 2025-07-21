package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SessionManagerTest {

    SessionManager sessionManager=new SessionManager();

    @Test
    void sessionTest(){

        //스프링이 response, request 테스트할 수 있도록 만든 것 (MockHtpServletResponse, Request)
        MockHttpServletResponse response=new MockHttpServletResponse();

        //서버에서 세션 생성헤 request에 담음
        Member member=new Member();
        sessionManager.createSession(member,response);

        //웹브루저가 쿠키를 만들어 서버에 전달, 요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //mySessionId=~~~

        //클라이언트에서 서버로 넘어온 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
}
