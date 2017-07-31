package asignore.parental.control.cli;

import asignore.parental.control.service.ParentalControlService;
import asignore.parental.control.service.RatingEnum;
import asignore.parental.control.service.TechnicalFailureException;
import asignore.parental.control.service.TitleNotFoundException;

import java.io.*;

public class MovieCLI {

    private PrintStream out;
    private BufferedReader in;
    private ParentalControlService parentalControlService;

    public MovieCLI(PrintStream out, InputStream in, ParentalControlService parentalControlService) {
        this.out = out;
        this.in = new BufferedReader(new InputStreamReader(in));
        this.parentalControlService = parentalControlService;
    }

    public void start() {
        try {

            out.print("Enter your Parental Control Level preference (U, PG, 12, 15, 18) :");
            final String preferredLevel = in.readLine();

            if (preferredLevel == null || preferredLevel.length() == 0)
                out.print("You must to enter one of these values strings as preferred level:  U, PG, 12, 15, 18\n");
            else if (RatingEnum.getByString(preferredLevel) == null)
                out.print("Level requested not existent\n");
            else {
                out.print("Please enter the movie id you would like to view :");
                final String movieId = in.readLine();

                boolean allowed;
                try {
                    allowed = parentalControlService.isAllowedToWatch(movieId, preferredLevel);
                    out.print(allowed ?
                            "You are allowed to watch this movie\n" :
                            "You are not allowed to watch this movie\n");
                } catch (TechnicalFailureException | TitleNotFoundException e) {
                    out.print(e.getMessage());
                }
            }
        } catch (IOException e) {
            out.println("An error occurred. Please start over.");
        }
    }
}
