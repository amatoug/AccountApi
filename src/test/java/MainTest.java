import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

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
        PrintStream mockOut = mock(PrintStream.class);

        try (MockedStatic<System> systemMock = Mockito.mockStatic(System.class)) {
            systemMock.when(() -> System.out).thenReturn(mockOut);

            Main.main(new String[]{});

            verify(mockOut, never()).println(anyString());
        }
    }

    @Test
    void should_not_write_to_stderr() {
        PrintStream mockErr = mock(PrintStream.class);

        try (MockedStatic<System> systemMock = Mockito.mockStatic(System.class)) {
            systemMock.when(() -> System.err).thenReturn(mockErr);

            Main.main(new String[]{});

            verify(mockErr, never()).println(anyString());
        }
    }

    @Test
    void should_not_call_system_exit() {
        try (MockedStatic<System> systemMock = Mockito.mockStatic(System.class)) {
            Main.main(new String[]{});

            systemMock.verify(() -> System.exit(anyInt()), never());
        }
    }
}
