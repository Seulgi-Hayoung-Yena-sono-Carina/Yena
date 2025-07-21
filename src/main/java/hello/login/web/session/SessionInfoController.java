package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        //세션에 저장된 모든 속성(attribute)들의 이름을 가져옴
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }
        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}",
                        name, session.getAttribute(name)));
        log.info("sessionId={}", session.getId()); //세션 ID
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval()); //세션 유효 시간
        log.info("creationTime={}", new Date(session.getCreationTime())); //세션 생성일시
        log.info("lastAccessedTime={}", new //세션과 연결된 사용자가 최근에 서버에 접근한 시간
                Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());//새로 생성된 세션인지 여부
        return "세션 출력";
    }
}
