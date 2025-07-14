package springbootdeveloper.test_api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitTest {
    @DisplayName("1+3은 4이다.")
    @Test
    public void junitFailedTest(){
        int a=1;
        int b=3;
        int sum=3;
        assertThat(sum).isNotEqualTo(a+b);
    }
}
