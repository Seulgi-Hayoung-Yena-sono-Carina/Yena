package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //Spring에서 비즈니스 로직을 담당하는 서비스 계층을 나타내는 애노테이션, 자동 스프링 Bean 등록
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password){
        /*Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get();
        if(member.getPassword().equals(password)){
            return member;
        } else {
            return null;
        }*/

        /*Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
        byLoginId.filter(m->m.getPassword().equals(password))
                .orElse(null);*/

        //위의 3줄 Ctrl+Alt+n으로 코드 합칠 수 있음
        return memberRepository.findByLoginId(loginId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }
}
