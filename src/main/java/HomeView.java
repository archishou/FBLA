import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

public class HomeView extends Home {
    final HorizontalCarousel carousel = new HorizontalCarousel();
    final ThemeResource resource = new ThemeResource("img/books.png");
    public void init() {
        carousel.setArrowKeysMode(ArrowKeysMode.FOCUS);
        carousel.setLoadMode(CarouselLoadMode.LAZY);
        carousel.addComponent(new Image("Books ", resource));
        carousel.setTransitionDuration(500);
    }

}