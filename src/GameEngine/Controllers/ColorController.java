package GameEngine.Controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ColorController implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED){
            System.out.println("button pressed");
        }
    }
}
