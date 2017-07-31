package asignore.parental.control.service.mock;

import asignore.parental.control.service.MovieService;
import asignore.parental.control.service.TechnicalFailureException;
import asignore.parental.control.service.TitleNotFoundException;

public class MockMovieService implements MovieService {

    @Override
    public String getParentalControlLevel(String movieId) throws TitleNotFoundException, TechnicalFailureException {
        switch (movieId) {
            case "Movie U":
                return "U";
            case "Movie PG":
                return "18";
            case "Movie VM_12":
                return "12";
            case "Movie VM_15":
                return "15";
            case "Movie VM_18":
                return "18";

            case "crash":
                throw new TechnicalFailureException("Sorry, we have experience a technical failure.\n");

            default:
                throw new TitleNotFoundException("Sorry, we could not find the movie you are looking for.\n");
        }
    }
}
