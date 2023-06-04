package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    /**
     * - @Pointcut 에 표인트컷 표현식을 사용함
     * - 메서드 이름과 파라미터를 합쳐서 포인트컷 시그니처라고 함
     * - 메서드의 반환 타입은 void 여야 함
     * - 코드 내용은 비워둠
     * - @Around 어드바이스에서는 포인트컷을 직접 지정해도 되지만, 포인터컷 시그니처를 사용해 됨
     * - private, public 같은 접근 제어자는 내부에서만 사용하면 private 을 사용해도 되지만, 다른 애스팩트에서 참고하려며 public 을 사용해야 함
     */
    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){} // pointcut signature

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
