package budgetbuddy.ui.tab;

import budgetbuddy.commons.util.AppUtil;
import budgetbuddy.ui.panel.DisplayPanel;
import javafx.scene.control.Tab;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

/**
 * Represents an abstract tab component that displays its corresponding panel when selected.
 */
public abstract class PanelTab extends Tab {


    public PanelTab(DisplayPanel panel, String imageFileName) {
        setClosable(false);
        setContent(panel.getRoot());

        // Create image
        ImageView currImage = new ImageView(AppUtil.getImage(imageFileName));
        ColorAdjust desaturateEffect = new ColorAdjust(0, -1, 0, 0);
        ColorAdjust saturateEffect = new ColorAdjust(0, 0, 0, 0);
        currImage.setEffect(desaturateEffect);
        setGraphic(currImage);

        // Change effect depending on state
        setOnSelectionChanged(event -> {
            if (isSelected()) {
                currImage.setEffect(saturateEffect);
            } else {
                currImage.setEffect(desaturateEffect);
            }
        });
    }
}