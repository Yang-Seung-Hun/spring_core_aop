package hello.aop.proxyvs.code;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) //JDK 동적 프록시
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    /**
     * 실행 결과 : BeanNotOfRequiredTypeException: Bean named 'memberServiceImpl' is expected to be of type
     *          'hello.aop.member.MemberServiceImpl' but was actually of type 'com.sun.proxy.$Proxy56'
     * JDK Proxy 는 MemberService 인터페이스를 기반으로 만들어지기 때문에 '@Autowired MemberService memberService' 부분은 문제가 없음
     * 하지만 JDK Proxy 는 MemberServiceImpl 이라는 타입을 전해 알지 못하기 때문에 문제가 생김 (MemberServiceImpl = JDK Proxy)
     *
     * 설정을 바꿔 CGLIB 를 사용하면 위와 같은 오류가 발생하지 않음 (CGLIB 는 구체 클래스를 기반으로 프록시를 생성하니까)
     *
     * 코드 변경 없이 구현 클래스를 변경할 수 있는것이 DI 의 장점이기 때문에 올바르게 설계된 애플리케이션에서는 위와 같은 문제는 발생하지 않음
     * 그럼에도 불구하고 테스트, 또는 여러가지 이유로 AOP 프록시가 적용된 구체 클래스를 직접 의존관계 주입 받아야 하는 경우가 있는데 이때는 CGLIB 를 사용하면 됨
     * 여기까지만 보면 CGLIB 가 만능으로 보이지만, 꼭 그렇지만은 않음
     *
     * CGLIB 구체 크래스 기반 프록시 문제점
     * - 대상 클래스에 기본 생성자 필수
     *      - 자바 언어에서 상속을 받으면 자식 클래스의 생성자를 호출할 때 자식 클래스의 생성자에서 부모 클래스의 생성자도 호출해야함.(생략시 super() 자동으로 들어감)
     *        CGLIB 프록시는 대상 크래스를 상속 받고, 생성자에서 대상 클래스의 기본 생성자를 호출하기 때문에 대상 클래스에는 기본 생성자가 필수로 요구됨
     * - 생성자 2번 호출 문제
     *      - 실제 target 객체를 생성할 때 1번, 프록시 객체를 생성할 때 부모 클래스의 생성자를 호출해 총 2번 호출됨
     * - final 키워드 클래스, 메서드 사용 불가
     *      - final 키워드가 클래스에 있으면 상속이 불가능하고, 메서드에 있으면 오버라이딩이 불가능함. 두 경우 프록시가 생성되지 않거나 정상 동작하지 않음
     *
     */
    @Test
    void go(){
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
