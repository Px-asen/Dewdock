package top.pxasen.dewdock;


import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import io.vproxy.vfx.control.globalscreen.GlobalScreenUtils;
import io.vproxy.vfx.manager.task.TaskManager;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;


public class DownloadPage extends Application {
    private VStage stage;
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


        //创建菜单Scene
        var menuScene = new VScene(VSceneRole.DRAWER_VERTICAL);
        menuScene.getNode().setPrefWidth(450);
        menuScene.enableAutoContentWidth();
        menuScene.getNode().setBackground(new Background(new BackgroundFill(
                Theme.current().subSceneBackgroundColor(),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));


        //创建菜单按钮
        var menuBtn = new Button(null, new FontIcon(Feather.MENU));
        menuBtn.getStyleClass().add(Styles.FLAT);
        menuBtn.setOnAction(e -> {
            
        });



        stage.getRoot().getContentPane().getChildren().add(menuBtn);
        stage.getStage().setWidth(1100);
        stage.getStage().setHeight(650);
        stage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
