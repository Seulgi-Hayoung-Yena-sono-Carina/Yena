package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver=new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        //; 부분에서 iter + tab
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = "+messageCode);
        }
        assertThat(messageCodes).containsExactly("required.item","required");
    }

    /**
     *codesResolver.resolveMessageCodes() 호출
     * codesResolver.resolveMessageCodes("required", "item", "itemName", String.class)는 메시지 코드를 생성하는 메서드입니다.
     *
     * 첫 번째 인수 "required"는 메시지의 기본 코드입니다.
     *
     * 두 번째 인수 "item"은 메시지에 대한 추가적인 속성 정보입니다.
     *
     * 세 번째 인수 "itemName"은 또 다른 속성 정보를 나타냅니다.
     *
     * 네 번째 인수 String.class는 이 메시지가 어떤 타입에 해당하는지 나타냅니다 (여기서는 String 타입).
     *
     * 이 메서드는 다양한 조건에 맞는 메시지 코드를 생성하여 반환합니다.
     *
   **/
    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required",
                "item", "itemName", String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
