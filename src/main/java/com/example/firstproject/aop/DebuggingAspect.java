package com.example.firstproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect // AOP 클래스 선언 : 이 클래스는 부가 기능을 주입하는 클래스
@Component // IOC 컨테이너가 해당 객체를 생성 및 관리하도록 한다
@Slf4j
public class DebuggingAspect {

    //대상 메소드 선택 : api 패키지의 모든 메소드
    @Pointcut("execution(* com.example.firstproject.api.*.*(..))")
    private void cut(){ }

    //실행 시점 설정 : cut()의 대상이 수행되기 이전
    @Before("cut()")
    public void loggingArgs(JoinPoint joinPoint) { //cut의 대상 메소드
        // 입력값 가져오기
        Object[] args = joinPoint.getArgs();


        // 클래스명
        String className = joinPoint.getTarget().getClass().getSimpleName();


        // 메소드명
        String methodName = joinPoint.getSignature().getName();


        // 입력값 로깅하기
        // CommentService()#create()의 입력값 = > 5 이런식으로. 클래스명과 입력값 필요

        for (Object obj : args){ //foreach 문
            log.info("{}#{}의 입력값 => {}",className,methodName,obj);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void loggingReturnValue(JoinPoint joinPoint,
                                   Object returnObj) {
        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();
        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();
        // 반환값 로깅
        log.info("{}#{}의 반환값 => {}", className, methodName, returnObj);
    }

}
