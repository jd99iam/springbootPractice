package com.example.firstproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    //특정 어노테이션을 대상 지정
    @Pointcut("@annotation(com.example.firstproject.annotation.RunningTime)")
    private void enalbeRunningTime() {}

    //기본 패키지의 모든 메소드 대상
    @Pointcut("execution(* com.example.firstproject..*.*(..))")
    private void cut(){}

    // 실행 시점 설정 : 두 조건을 모두 만족하는 대상을 전후로 부가기능을 삽입
    @Around("cut() && enableRunningTime()")
    public void loggingRunningTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //메소드 수행 전, 측정 시작
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //메소드 수행
        Object returningObj = joinPoint.proceed();

        // 메소드명
        String methodName = joinPoint.getSignature().getName();

        //메소드 수행 후, 측정 종료, 로깅
        stopWatch.stop();
        log.info("{}의 총 수행 시간 => {} sec", methodName, stopWatch.getTotalTimeSeconds());
    }


}
