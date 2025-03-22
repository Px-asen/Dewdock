package top.pxasen.dewdock;

import io.vproxy.vfx.manager.image.ImageManager;
import io.vproxy.vfx.theme.impl.DarkTheme;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class CustomTheme extends DarkTheme {
    private final Color MAIN = Color.web("#FFFFFF");
    private final Color SECONDARY = MAIN.deriveColor(0, 1, 0.9, 1);
    private final Color ACCENT = Color.web("#88C0D0");

    @Override
    public Color sceneBackgroundColor() {
        return Color.web("#FFFFFF"); // 改为浅灰背景
    }

    @Override
    public Color normalTextColor() {
        return Color.web("#737780"); // 深灰文字提高可读性
    }

    public Color mainColor() {
        return Color.web("#181A1F"); // 示例颜色
    }

    @Override
    public Color fusionButtonHoverBackgroundColor() {
        return SECONDARY;
    }

    @Override
    public Color fusionButtonDownBackgroundColor() {
        return MAIN.darker();
    }

    @Override
    public Color fusionButtonAnimatingBorderLightColor() {
        return ACCENT;
    }

    @Override
    public Color scrollBarColor() {
        return MAIN;
    }

    @Override
    public Color progressBarProgressColor() {
        return ACCENT;
    }

    @Override
    public Color tableCellSelectedBackgroundColor() {
        return SECONDARY.deriveColor(0, 1, 1, 0.3);
    }

    @Override
    public Color fusionButtonNormalBackgroundColor() {
        return Color.TRANSPARENT; 
    }

    @Override
    public Image windowCloseButtonNormalImage() {
        return ImageManager.get().load("close.png");
    }

    @Override
    public Image windowCloseButtonHoverImage() {
        return ImageManager.get().load("close.png");
    }

    @Override
    public Image windowIconifyButtonNormalImage() {
        return ImageManager.get().load("iconify.png");
    }

    @Override
    public Image windowIconifyButtonHoverImage() {
        return ImageManager.get().load("iconify.png");
    }

}
    


    











