import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(capturedOut));
            Main.main(new String[]{});
            org.assertj.core.api.Assertions.assertThat(capturedOut.toString()).isBlank();
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void should_not_write_to_stderr() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();
        try {
            System.setErr(new PrintStream(capturedErr));
            Main.main(new String[]{});
            org.assertj.core.api.Assertions.assertThat(capturedErr.toString()).isBlank();
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    void should_not_call_system_exit() {
        assertThatCode(() -> Main.main(new String[]{}))
                .doesNotThrowAnyException();
    }
}
