package asignore.parental.control.service;


public class ParentalControlService {

    private MovieService movieService;

    public ParentalControlService(MovieService movieService) {
        this.movieService = movieService;
    }

    public boolean isAllowedToWatch(String movieId, String preferredLevel) throws TechnicalFailureException, TitleNotFoundException {

        if (!RatingEnum.ratingMap.containsKey(preferredLevel))
            throw new TechnicalFailureException("Sorry, you have entered non existent rating preferred level");

        String parentalControlLevel = movieService.getParentalControlLevel(movieId);

        if (!RatingEnum.ratingMap.containsKey(parentalControlLevel))
            throw new TechnicalFailureException("Sorry, Movie service provided a rating non existent");

        return RatingEnum.getByString(parentalControlLevel).getValue() <= RatingEnum.getByString(preferredLevel).getValue();

    }
}
