import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

public class HomeView extends Home {
    final HorizontalCarousel carousel = new HorizontalCarousel();

    public void init() {
        carousel.setArrowKeysMode(ArrowKeysMode.FOCUS);
        carousel.setLoadMode(CarouselLoadMode.LAZY);
        carousel.setTransitionDuration(500);
        addComponent(carousel);
    }
}

