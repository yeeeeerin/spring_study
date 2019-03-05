package com.example.aopproject;

import java.lang.annotation.*;

/*
* Retention 범위를 class 이상으로 줘야한다
* 이 에노테이션 정보를 얼마나 유지할것인가?
* class일경우 이 에너테이션 정보가 바이트코드까지 남아있는다
* source일 경우는 컴파일하고 나면 사라진다
* 기본값은 class이다
* */
/*
* 이 에너테이션을 사용하면 사용 전 후 로깅이 된닽ㄴ
* */
@Documented //자바독 만들 때 유용
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PerfLogging {



}
