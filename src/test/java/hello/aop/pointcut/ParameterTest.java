package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * 포인트컷 표현식을 사용해 어드바이스에 매개변수를 전달할 수 있음
 * - 포인트컷의 이름과 매개변수의 이름을 맞춰야 함
 * - 타입이 메서드에 지정한 타입으로 제한됨
 */
@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success(){
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect{
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember(){}

        /**
         * jointPoint.getArgs()[] 와 같이 매개변수를 전달 받음
         */
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        /**
         * args(arg,..) 와 같이 매개변수를 전달 받음
         */
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        /**
         * @Before 를 사용한 축약 버전
         * 타입을 String 으로 제한함
         */
        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg){
            log.info("[logArgs3] arg={}", arg);
        }

        /**
         * 스프링 컨테이너에 등록 된 프록시 객체를 전달 받음
         * (ex)obj=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$15329053
         */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj){
            log.info("[this]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * 실제 대상 객체를 전달 받음
         * (ex)obj=class hello.aop.member.MemberServiceImpl
         */
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj){
            log.info("[target]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * 타입의 애노테이션을 전달 받음
         */
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation){
            log.info("[@target]{}, obj={}", joinPoint.getSignature(), annotation);
        }

        /**
         * 타입의 애노테이션을 전달 받음
         */
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation){
            log.info("[@within]{}, obj={}", joinPoint.getSignature(), annotation);
        }

        /**
         * 메서드의 애노테이션을 전달 받음
         * annotation.value() 로 해당 애노테이션의 값을 출력하는 것도 가능
         */
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation){
            log.info("[@annotation]{}, annotationValue={}", joinPoint.getSignature(), annotation.value());
        }
    }
}
