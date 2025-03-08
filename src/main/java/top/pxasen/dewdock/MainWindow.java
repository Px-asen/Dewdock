package top.pxasen.dewdock;



import atlantafx.base.theme.Styles;
import io.vproxy.vfx.control.globalscreen.GlobalScreenUtils;
import io.vproxy.vfx.manager.image.ImageManager;
import io.vproxy.vfx.manager.task.TaskManager;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.button.FusionImageButton;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.scene.VSceneShowMethod;
import io.vproxy.vfx.ui.stage.VStage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import atlantafx.base.controls.ModalPane;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;


public class MainWindow extends Application {
    private VStage stage;
    @Override
    public void start(Stage primaryStage) {

        Theme.setTheme(new CustomTheme());

        var stage = new VStage(primaryStage) {
            @Override
            public void close() {
                super.close();
                TaskManager.get().terminate();
                GlobalScreenUtils.unregister();
            }
        };
        stage.getInitialScene().enableAutoContentWidthHeight();
        stage.setTitle("Dewdock");

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
        menuBtn.getStyleClass().addAll(Styles.BUTTON_ICON);

        var vbox = new VBox();
        stage.getStage().setWidth(1280);
        stage.getStage().setHeight(800);
        stage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
