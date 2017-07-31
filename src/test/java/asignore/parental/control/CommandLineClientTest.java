package asignore.parental.control;

import asignore.parental.control.cli.MovieCLI;
import asignore.parental.control.service.ParentalControlService;
import asignore.parental.control.service.TechnicalFailureException;
import asignore.parental.control.service.TitleNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandLineClientTest {

    @Mock
    private PrintStream out;

    @Mock
    private ParentalControlService service;

    private InputStream in;
    private MovieCLI client;

    @Before
    public void init() throws TechnicalFailureException, TitleNotFoundException {
        when(service.isAllowedToWatch(anyString(), any())).thenReturn(true);
    }

    @Test
    public void shouldPrintLevelRequestNotExistentIfInputIsCarriageReturn() throws Exception {

        // just hit carriage return at terminal
        in = inputString(("\n").getBytes());

        new MovieCLI(out, in, service).start();

        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
        inOrder.verify(out).print("You must to enter one of these values strings as preferred level:  U, PG, 12, 15, 18\n");
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void shouldPrintLevelRequestNotExistentIfInputIsNotExistingLevel() throws Exception {

        // type a non existent level
        in = inputString(("55\n").getBytes());
        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any())).thenReturn(true);

        client.start();

        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
        inOrder.verify(out).print("Level requested not existent\n");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDetectMovieNotExistentIfAndInputIs18AllowedLevel() throws Exception {
        in = inputString(("18\n" + "blah\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenThrow(new TitleNotFoundException("Sorry, we could not find the movie you are looking for.\n"));

        client.start();

        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
        inOrder.verify(out).print("Sorry, we could not find the movie you are looking for.\n");
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void shouldDetectTechnicalFailureIfAndInputIs18AllowedLevel() throws Exception {
        in = inputString(("18\n" + "crash\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenThrow(new TechnicalFailureException("Sorry, we have experience a technical failure.\n"));

        client.start();

        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
        inOrder.verify(out).print("Sorry, we have experience a technical failure.\n");
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void shouldDisplayApprovalIfInputIs18AllowedLevel() throws Exception {
        in = inputString(("18\n" + "Movie VM_15\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(true);

        client.start();

        verifyIsAllowedOrder();
    }


    @Test
    public void shouldDisplayApprovalIfInputIs15AllowedLevel() throws Exception {
        in = inputString(("15\n" + "Movie VM_15\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(true);

        client.start();

        verifyIsAllowedOrder();

    }

    private void verifyIsAllowedOrder() {
        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
        inOrder.verify(out).print("Please enter the movie id you would like to view :");
        inOrder.verify(out).print("You are allowed to watch this movie\n");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDisplayApprovalIfInputIs12AllowedLevel() throws Exception {
        in = inputString(("12\n" + "Movie VM_15\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(true);

        client.start();

        verifyIsAllowedOrder();

    }

    @Test
    public void shouldDisplayApprovalIfInputIsPGAllowedLevel() throws Exception {
        in = inputString(("PG\n" + "Movie VM_15\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(true);

        client.start();

        verifyIsAllowedOrder();

    }

    @Test
    public void shouldDisplayApprovalIfInputIsUAllowedLevel() throws Exception {
        in = inputString(("U\n" + "Movie VM_15\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(true);

        client.start();

        verifyIsAllowedOrder();

    }

    @Test
    public void startWithWatchAllowed_shouldDisplayNonApproval() throws Exception {
        in = inputString(("15\n" + "Movie VM_18\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(false);

        client.start();

        verifyIsNotAllowedOrder();

    }

    private void verifyIsNotAllowedOrder() {
        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
        inOrder.verify(out).print("Please enter the movie id you would like to view :");
        inOrder.verify(out).print("You are not allowed to watch this movie\n");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDisplayNonApproval() throws Exception {
        in = inputString(("15\n" + "not existent\n").getBytes());

        client = new MovieCLI(out, in, service);

        when(service.isAllowedToWatch(anyString(), any()))
                .thenReturn(false);

        client.start();

        verifyIsNotAllowedOrder();

    }


    @Test
    public void testClientDetectsIOExceptionAndDisplayTheErrorMessage() throws Exception {
        in = mock(InputStream.class);
        client = new MovieCLI(out, in, service);

        when(in.read(any())).thenThrow(new IOException("something went wrong"));

        client.start();

        verify(out).println("An error occurred. Please start over.");
    }

    private ByteArrayInputStream inputString(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

}