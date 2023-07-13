package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * 구조를 변경(분리)
 * - 내부 호출 자체가 사라지게 만듬
 * - callService -> internalService 를 호출하는 구조로 변경 => 자연스럽게 AOP 가 적용됨
 *
 * (참고)
 * AOP 는 주로 트랜잭션 적용이나 주요 컴포넌트의 로그 출력 기능에 사용됨
 * 쉽게 이야기해서 인터페이스에 메서드가 나올 정도의 규모에 AOP 를 적용하는 것이 적당함
 * 더 풀어서 이야기하면 AOP 는 public 메서드만 적용함
 * private 메서드처럼 작은 단위에는 AOP 를 적용하지 않음
 * AOP 적용을 위해 private 메서드를 외부 클래스로 변경하고 public 으로 변경하는 일은 없음
 * 그러나 현재 예제와 같이 public 메서드에서 public 메서드를 내부 호출하는 경우에는 문제가 발행함
 * => AOP 가 잘 적용되지 않으면 내부 호출을 의심!!
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

    private final InternalService internalService;

    public void external(){
        log.info("call external");
        internalService.internal(); // 외부 메서드 호출
    }
}
