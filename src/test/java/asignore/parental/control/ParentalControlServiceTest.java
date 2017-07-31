package asignore.parental.control;

import asignore.parental.control.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ParentalControlServiceTest {

    @Mock
    private
    MovieService movieService;

    @Before
    public void init() throws TechnicalFailureException, TitleNotFoundException {

        //set up the mock MovieService
        when(movieService.getParentalControlLevel("U")).thenReturn("U");
        when(movieService.getParentalControlLevel("PG")).thenReturn("PG");
        when(movieService.getParentalControlLevel("12")).thenReturn("12");
        when(movieService.getParentalControlLevel("15")).thenReturn("15");
        when(movieService.getParentalControlLevel("18")).thenReturn("18");

        when(movieService.getParentalControlLevel("not existent")).thenThrow(new TitleNotFoundException("Title not found"));
        when(movieService.getParentalControlLevel("bad thing happened")).thenThrow(new TechnicalFailureException("Technical failure"));
    }

    @Test
    public void testTheStringsExpectedByTheMovieServiceAreAssociatedCorrectlyWithOurEnums() throws Exception {
        Assert.assertEquals(RatingEnum.U, RatingEnum.getByString("U"));
        Assert.assertEquals(RatingEnum.PG, RatingEnum.getByString("PG"));
        Assert.assertEquals(RatingEnum.VM_12, RatingEnum.getByString("12"));
        Assert.assertEquals(RatingEnum.VM_15, RatingEnum.getByString("15"));
        Assert.assertEquals(RatingEnum.VM_18, RatingEnum.getByString("18"));
    }

    @Test
    public void testNotExistentRatingStringReturnsNull() throws Exception {
        Assert.assertNull(RatingEnum.getByString("not existent"));
    }

    @Test
    public void testTheIntegerAssociatedToTheEnumAreCorrect() throws Exception {
        Assert.assertEquals(0, RatingEnum.U.getValue());
        Assert.assertEquals(1, RatingEnum.PG.getValue());
        Assert.assertEquals(2, RatingEnum.VM_12.getValue());
        Assert.assertEquals(3, RatingEnum.VM_15.getValue());
        Assert.assertEquals(4, RatingEnum.VM_18.getValue());
    }

    @Test
    public void testAdultCanSeeAdultMovies() throws TechnicalFailureException, TitleNotFoundException {
        ParentalControlService service = new ParentalControlService(movieService);
        Assert.assertTrue(service.isAllowedToWatch("18", "18"));

    }

    @Test
    public void testUnderageCannotWatchAdultMovie() throws TechnicalFailureException, TitleNotFoundException {
        ParentalControlService service = new ParentalControlService(movieService);
        Assert.assertFalse(service.isAllowedToWatch("18", "15"));
        Assert.assertFalse(service.isAllowedToWatch("18", "12"));
        Assert.assertFalse(service.isAllowedToWatch("18", "PG"));
        Assert.assertFalse(service.isAllowedToWatch("18", "U"));
    }

    @Test
    public void testThatUnderageCannotWatchAdultMovie() throws TechnicalFailureException, TitleNotFoundException {
        ParentalControlService service = new ParentalControlService(movieService);
        Assert.assertTrue(service.isAllowedToWatch("PG", "18"));
        Assert.assertTrue(service.isAllowedToWatch("PG", "15"));
        Assert.assertTrue(service.isAllowedToWatch("PG", "12"));
        Assert.assertTrue(service.isAllowedToWatch("PG", "PG"));
        Assert.assertFalse(service.isAllowedToWatch("PG", "U"));
    }

    @Test(expected = TitleNotFoundException.class)
    public void shouldReturnFalseAndIfTitleNotFoundFeedback() throws TechnicalFailureException, TitleNotFoundException {

        ParentalControlService service = new ParentalControlService(movieService);
        Assert.assertFalse(service.isAllowedToWatch("not existent", "18"));

    }

    @Test(expected = TechnicalFailureException.class)
    public void shouldDetectTechnicalFailureException() throws TechnicalFailureException, TitleNotFoundException {

        ParentalControlService service = new ParentalControlService(movieService);

        Assert.assertFalse(service.isAllowedToWatch("bad thing happened", "18"));

    }


    @Test(expected = TechnicalFailureException.class)
    public void shouldNotBeAllowedToWatch() throws TechnicalFailureException, TitleNotFoundException {

        ParentalControlService service = new ParentalControlService(movieService);

        Assert.assertFalse(service.isAllowedToWatch("bad thing happened", "42"));

    }

    @Test(expected = TechnicalFailureException.class)
    public void shouldReturnFalseAndInvalidRatingFeedback() throws TechnicalFailureException, TitleNotFoundException {

        when(movieService.getParentalControlLevel("18")).thenReturn("19");

        ParentalControlService service = new ParentalControlService(movieService);
        Assert.assertFalse(service.isAllowedToWatch("18", "U"));


    }

}