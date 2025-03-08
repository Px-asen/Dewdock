package top.pxasen.dewdock;

import io.vproxy.vfx.theme.impl.DarkTheme;
import javafx.scene.paint.Color;

public class CustomTheme extends DarkTheme {
    // 基础主色调（示例使用蓝灰色系）
    private final Color MAIN = Color.web("#5E81AC");
    private final Color SECONDARY = MAIN.deriveColor(0, 1, 0.9, 1);
    private final Color ACCENT = Color.web("#88C0D0");

    @Override
    public Color sceneBackgroundColor() {
        return Color.web("#CECECE"); // 改为浅灰背景
    }

    @Override
    public Color normalTextColor() {
        return Color.web("#2E3440"); // 深灰文字提高可读性
    }

    // 新增关键配置 ↓
    public Color mainColor() {
        return Color.web("#5E81AC"); // 示例颜色
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



}







