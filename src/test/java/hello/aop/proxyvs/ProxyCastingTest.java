package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JDK 동적 프록시는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 없음
 * CGLIB 프록시는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 있음
 * => 근데 어쩌라고 생각 할 수 있지만 이것 때문에 의존관계 주입시 문제가 발생할 수 있음
 */
@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy(){
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); //JDK 동적 프록시

        //프록시를 인터페이스로 캐스팅 성공 -> 프록시 생성할 때 타겟의 인터페이스를 구현해서 만드니까!!
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //JDK 동적 프록시를 구현 클래스로 캐스팅 시도 실패, ClassCastException 예외 발생 -> 동적 프록시는 타겟의 인터페이스를 구현해서 만들었지만 타겟의 구현체는 전혀 알지 못함!!
        assertThrows(ClassCastException.class, ()-> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });

    }

    @Test
    void cglibProxy(){
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); //CGLIB 프록시

        //프록시를 인터페이스로 캐스팅 성공 -> 프록시 생성할 때 구체클래스를 기반으로 만드니까!!
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //CGLIB 프록시를 구현 클래스로 캐스팅 시도 성공 -> 프록시 생성할 때 구체클래스를 기반으로 만드니까!!
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;

    }
}
