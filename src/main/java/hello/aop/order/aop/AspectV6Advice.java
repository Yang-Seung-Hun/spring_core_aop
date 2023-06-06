package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @Around : 메서드 호출 전후에 수행, 가장 강력한 어드바이스, 조인 포인트 실행 여부 실행 선택, 반환 값 변환, 예외 변환 등이 가능
 * @Before : 조인 포인트 실행 이전에 실행
 * @AfterReturning : 조인 포인트가 정상 완료후 실행
 * @AfterThrowing : 메서드가 예외를 던지는 경우 실행
 * @After : 조인 포인트가 정상 또는 예외에 관계없이 실행 (finally)
 *
 * 참고 정보 획득
 * -모든 어드바이스는 org.aspectj.lang.JoinPoint 를 첫번째 파라미터에 사용할 수 있음 (생략도 가능)
 * -단 @Around 는 ProceedingJoinPoint 를 사용해야 함 (ProceedingJoinPoint 는 org.aspectj.lan.JoinPoint 의 하위 타입임
 *
 * JoinPoint 인터페이스의 주요 기능
 *      - getArgs() : 메서드 인수를 반환
 *      - getThis() : 프록시 객체를 반환
 *      - getTarget() : 대상 객체를 반환
 *      - getSignature() : 조언되는 메서드에 대한 설명을 반환
 *      - toString() : 조언되는 방법에 대한 유용한 설명을 인쇄
 *
 * ProceedingJoinPoint 인터페이스의 주요 기능
 *      - proceed() : 다음 어드바이스나 타겟을 호출
 *
 * 순서
 * - 실행 순서 : @Around @Before @After @AfterReturning @AfterThrowing
 *
 * 여러 어드바이스가 존재하는 이유
 * - @Around 하나만 있어도 모든 기능을 수행할 수 있지만 다른 어드바이스들이 존재하는 이유는 무엇일까?
 *      -> 개발자가 실수할 요소가를 줄임 (개발자가 실수로 타겟을 호출하지 않을 수 있음)
 *      -> 코드를 작성한 의도가 명확해짐 (@Before 를 보는 순간 아! 이 코드는 타겟 실행 전에 한정해서 어떤 일을 하는 구나 라는 것이 들어남)
 */
@Slf4j
@Aspect
public class AspectV6Advice {

//    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
//    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable{
//        try {
//            //@Before
//            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
//
//            Object result = joinPoint.proceed();
//
//            //@AfterReturning
//            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
//            return result;
//        } catch (Exception e){
//            //@AfterThrowing
//            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
//            throw e;
//        } finally {
//            //@After
//            log.info("[트랜잭션 릴리즈] {}", joinPoint.getSignature());
//        }
//    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint){
        log.info("[before] {}", joinPoint.getSignature());
    }

    /**
     * returning 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 함
     * returning 절에 지정된 타입의 값을 반환하는 메서드만 대상으로 실행함 (부모 타입을 지정하면 모든 자식 타입은 인정)
     */
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result){
        log.info("[return] {} return ={}", joinPoint.getSignature(), result);
    }

    /**
     * throwing 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 함
     * throwing 절에 지정된 타입과 맞은 예외를 대상으로 실행함 (부모 타입을 지정하면 모든 자식 타입은 인정)
     */
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrow(JoinPoint joinPoint, Exception ex){
        log.info("[ex] {} message ={}", ex);
    }

    @After("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[after] {}", joinPoint.getSignature());
    }
}
