import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class MainTest {

    @Test
    void should_run_without_throwing_exception() {
        assertThatCode(() -> Main.main(new String[]{}))
                .doesNotThrowAnyException();
    }

    @Test
    void should_run_with_null_args_without_throwing_exception() {
        assertThatCode(() -> Main.main(null))
                .doesNotThrowAnyException();
    }

    @Test
    void should_run_with_multiple_args_without_throwing_exception() {
        assertThatCode(() -> Main.main(new String[]{"arg1", "arg2"}))
                .doesNotThrowAnyException();
    }

    @Test
    void should_not_write_to_stdout() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        try {
            Main.main(new String[]{});
        } finally {
            System.setOut(originalOut);
        }

        org.assertj.core.api.Assertions.assertThat(output.toString(StandardCharsets.UTF_8)).isEmpty();
    }

    @Test
    void should_not_write_to_stderr() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setErr(new PrintStream(output));

        try {
            Main.main(new String[]{});
        } finally {
            System.setErr(originalErr);
        }

        org.assertj.core.api.Assertions.assertThat(output.toString(StandardCharsets.UTF_8)).isEmpty();
    }

}
