package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    /**
     * - @Around 애노테이션의 값인 execution(* hello.aop.order..*(..)) 는 포인트컷이 됨
     * - @Around 애노테이션의 메서드인 doLog 는 어드바이스가 됨
     * - execution(* hello.aop.order..*(..))는 hello.aop.order 패키지와 그 하위 패키지(..)를 지정하는 AspectJ 포인트컷 표현식임
     * (참고) 스프링 AOP 는 AspectJ 문법을 차용하고, 프록시 방식의 AOP 를 제공함. AspectJ 를 직접 사용하는 것으 아님
     *       스프링에서는 AspectJ가 제공하는 애노테이션이나 관련 인터페이스만 사용하는 것이고, 실제 AspectJ 가 제공하는 컴파일, 로드타임 위버 등을 사용하는 것은 아님
     */
    @Around("execution(* hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
