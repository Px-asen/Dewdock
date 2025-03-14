package top.pxasen.dewdock;


import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import io.vproxy.vfx.control.globalscreen.GlobalScreenUtils;
import io.vproxy.vfx.manager.task.TaskManager;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneHideMethod;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.scene.VSceneShowMethod;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import atlantafx.base.util.Animations;



public class DownloadPage extends Application {
    private VStage stage;
    private StackPane menuContainer;
    @Override
    public void start(Stage primaryStage) {
        //设置主题 （Atlantafx和VFX的主题）
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Theme.setTheme(new CustomTheme());


        var stage = new VStage(primaryStage, new VStageInitParams()       
                .setMaximizeAndResetButton(false) // 隐藏最大化按钮
        ) {
            @Override
            public void close() {
                super.close();
                TaskManager.get().terminate();
                GlobalScreenUtils.unregister();
            }
        };
        stage.getInitialScene().enableAutoContentWidthHeight();
        stage.setTitle("");

    //创建菜单
        menuContainer = new StackPane();
        menuContainer.setPrefWidth(450);
        // 使用 CustomTheme 中定义的颜色
        menuContainer.setBackground(new Background(new BackgroundFill(
            ((CustomTheme)Theme.current()).mainColor(), // 使用 mainColor
            CornerRadii.EMPTY,
            Insets.EMPTY
        )));
        
        //创建菜单Scene
        Scene menuScene = new Scene(menuContainer);
        menuScene.setFill(Color.TRANSPARENT);
        
        //创建菜单舞台
        Stage menuStage = new Stage();
        menuStage.setScene(menuScene);
        menuStage.initOwner(stage.getStage());
        menuStage.setX(stage.getStage().getX());
        menuStage.setY(stage.getStage().getY());
        menuStage.setHeight(stage.getStage().getHeight());
        menuStage.setWidth(450);
        menuStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        menuStage.setOpacity(0);
        menuStage.hide();

        // 为主窗口添加点击事件，点击时隐藏菜单
        stage.getStage().getScene().setOnMouseClicked(event -> {
            if (menuStage.isShowing()) {
                var animation = Animations.fadeOutLeft(menuContainer, Duration.millis(400));
                animation.setOnFinished(e -> menuStage.hide());
                animation.play();
            }
        });

        //创建菜单按钮
        var menuBtn = new Button(null, new FontIcon(Feather.MENU));
        menuBtn.getStyleClass().add(Styles.FLAT);
        menuBtn.setOnAction(e -> {
            // 阻止事件传播到主窗口
            e.consume();
            
            // 检查菜单是否已显示
            if (!menuStage.isShowing()) {
                // 更新菜单位置，确保跟随主窗口
                menuStage.setX(stage.getStage().getX());
                menuStage.setY(stage.getStage().getY());
                menuStage.setHeight(stage.getStage().getHeight());

                // 显示菜单
                menuStage.setWidth(300);
                menuStage.show();

                // 淡入动画
                menuStage.setOpacity(1);
                var animation = Animations.fadeInLeft(menuContainer, Duration.millis(500));
                animation.play();
            } else {

                //淡出动画
                var animation = Animations.fadeOutLeft(menuContainer, Duration.millis(400));
                animation.setOnFinished(event -> menuStage.hide());
                animation.play();
            }
        });


        //创建控件布局
        var DownloadBtn = new Button("新建任务",new FontIcon(Feather.PLUS));
        DownloadBtn.getStyleClass().addAll(Styles.BUTTON_OUTLINED,Styles.ACCENT);



        stage.getRoot().getContentPane().getChildren().addAll(menuBtn, DownloadBtn);
        stage.getStage().setWidth(1100);
        stage.getStage().setHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
