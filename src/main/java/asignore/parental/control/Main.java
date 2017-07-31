package asignore.parental.control;

import asignore.parental.control.cli.MovieCLI;
import asignore.parental.control.service.ParentalControlService;
import asignore.parental.control.service.mock.MockMovieService;

public class Main {
    public static void main(String[] args) {

        final ParentalControlService parentalControlService = new ParentalControlService(new MockMovieService());

        new MovieCLI(System.out
                , System.in, parentalControlService)
                .start();
    }
}
