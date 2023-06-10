package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest
/**
 * 1.프록시 생성을 JDK 동적 프록시로 생성할 경우 (spring.aop.proxy-target-class=false)
 * (결과) memberService Proxy=class com.sun.proxy.$Proxy54
 *       [target-impl] String hello.aop.member.MemberService.hello(String)
 *       [target-interface] String hello.aop.member.MemberService.hello(String)
 *       [this-interface] String hello.aop.member.MemberService.hello(String)
 * 2.프록시 생성을 CGLIB 으로 생성할 경우 (spring.aop.proxy-target-class=true)
 * (결과) memberService Proxy=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$4b16d377
 *       [target-impl] String hello.aop.member.MemberServiceImpl.hello(String)
 *       [target-interface] String hello.aop.member.MemberServiceImpl.hello(String)
 *       [this-impl] String hello.aop.member.MemberServiceImpl.hello(String)
 *       [this-interface] String hello.aop.member.MemberServiceImpl.hello(String)
 * => 1번 결과로 [this-Impl] 이 호출 되지 않은 이유
 *    - this 는 프록시 객체를 가져옴
 *    - JDK 동적 프록시로 프록시를 생성할 때 인터페이스를 기반으로 프록시를 생성함
 *    - this 는 프록시를 가져오는데 그 프록시가 인터페이스 기반이니까 당연히 구현체에는 인터페이스를 담을 수 없으니까 호출이 되지 않음
 */
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success(){
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect{

        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }


        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
