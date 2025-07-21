package hello.login.web.argumentresolver;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//클래스, 필드 등에 적용하려면 ElementType.FIELD, ElementType.TYPE 등을 추가
@Target(ElementType.PARAMETER) //메서드의 매개변수(파라미터)에만 사용 가능하다는 의미



@Retention(RetentionPolicy.RUNTIME) //RetentionPolicy.RUNTIME은 런타임(실행 중)에도 유지됨을 의미.
//즉, 리플렉션(Reflection)을 사용해 런타임에 @Login이 붙은 매개변수를 확인할 수 있습니다.
public @interface Login {
}
