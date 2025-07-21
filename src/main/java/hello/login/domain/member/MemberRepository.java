package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository //DAO 클래스(데이터베이스와 직접적으로 연결되는 클래스)**임을 Spring에 알림
public class MemberRepository {
    private static Map<Long, Member> store=new HashMap<>(); //static 사용
    private static long sequence=0L; //static 사용

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(),member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
        /*List<Member> all=findAll();
        for (Member m : all) {
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m);
            }
        }
        return Optional.empty();*/
        //Java 8 람다 문법 사용
        return findAll().stream()
                .filter(m->m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values()); //멤버가(value) 전부 리스트로 변환
    }

    public void clearStore(){
        store.clear();
    }
}
