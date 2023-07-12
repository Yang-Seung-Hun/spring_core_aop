package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired CallServiceV0 callServiceV0;

    /**
     * 실행 결과
     * hello.aop.internalcall.aop.CallLogAspect     : aop=void hello.aop.internalcall.CallServiceV0.external()
     * hello.aop.internalcall.CallServiceV0     : call external
     * hello.aop.internalcall.CallServiceV0     : call internal
     * => external 내부에 있는 internal 에 AOP 적용되지 않음
     *    (WHY) 자바 언어에서 메서드 앞에 별도의 참조가 없으면 'this' 라는 뜻으로 자기 자신의 인스턴스를 가리킴.
     *          결과적으로 자기 자신의 내부 메서드를 호출하는 this.internal()이 되는데, 여기서 'this' 는 실제 대상 객체(target)의 인스턴스를 가리킴
     *          결과적으로 이러한 내부 호출은 프록시를 거치지 않고, 따라서 어드바이스도 적용할 수 없음
     */
    @Test
    void external(){
        callServiceV0.external();
    }

    @Test
    void internal(){
        callServiceV0.internal();
    }

}