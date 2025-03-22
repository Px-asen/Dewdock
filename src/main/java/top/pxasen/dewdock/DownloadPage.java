package top.pxasen.dewdock;


import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import io.vproxy.vfx.control.globalscreen.GlobalScreenUtils;
import io.vproxy.vfx.manager.task.TaskManager;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import atlantafx.base.util.Animations;



public class DownloadPage extends Application {
    private StackPane menuContainer;

    private Pane createPane(String text, Orientation orientation) {
        Pane pane = new Pane();
        pane.setPrefSize(200, 200);
        pane.getChildren().addAll();
        return pane;
    }

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


        // 监听主窗口位置变化
        stage.getStage().xProperty().addListener((obs, oldVal, newVal) -> {
            if (menuStage.isShowing()) {
                menuStage.setX(newVal.doubleValue());
            }
        });
        
        stage.getStage().yProperty().addListener((obs, oldVal, newVal) -> {
            if (menuStage.isShowing()) {
                menuStage.setY(newVal.doubleValue());
            }
        });
        
        // 监听主窗口大小变化
        stage.getStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            if (menuStage.isShowing()) {
                menuStage.setHeight(newVal.doubleValue());
            }
        });

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
        // 确保按钮可以接收鼠标事件
        menuBtn.setPickOnBounds(true);
        menuBtn.toFront(); // 将按钮置于前景
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

        var menuseparator = new Separator(Orientation.VERTICAL);
        menuseparator.setPrefWidth(1);
        menuseparator.setPrefHeight(1);
        menuseparator.setLayoutX(50); 
        menuseparator.setLayoutY(10); 
        menuseparator.setPadding(new Insets(0, 0, 0, 0));
        HBox headerBox = new HBox(10); 
        headerBox.setPadding(new Insets(10));
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().addAll(menuBtn, menuseparator);
        
        // 在创建menuBtn后添加
        menuBtn.setStyle("-fx-cursor: hand;"); // 设置鼠标悬停时的光标样式
        menuBtn.setViewOrder(-100); // 设置视图顺序，确保按钮在最上层
        headerBox.setViewOrder(-100);
        headerBox.setMouseTransparent(false);
        headerBox.setPickOnBounds(false); // 只有子元素接收事件


        
        Pane mainPane = new Pane();
        mainPane.setPrefSize(1000, 600);
    

        Pane leftPane = createPane("左侧", Orientation.HORIZONTAL);
        var DownloadingBtn = new Button("正在下载",new FontIcon(Feather.DOWNLOAD)){{
            setPrefWidth(140);
            setPrefHeight(25);
            setLayoutX(0);
            setLayoutY(215);
            getStyleClass().addAll(Styles.FLAT);
        }};

        var CompletedBtn = new Button("下载完成", new FontIcon(Feather.CHECK_CIRCLE)){{
            setPrefWidth(140);
            setPrefHeight(25);
            setLayoutX(0);
            setLayoutY(265); 
            getStyleClass().addAll(Styles.FLAT);
        }};

        var AllTasksBtn = new Button("全部任务", new FontIcon(Feather.LIST)){{
            setPrefWidth(140);
            setPrefHeight(25);
            setLayoutX(0);
            setLayoutY(315); 
            getStyleClass().addAll(Styles.FLAT);
        }};

        leftPane.setLayoutX(10);
        leftPane.setLayoutY(50);
        leftPane.getChildren().addAll(DownloadingBtn, CompletedBtn, AllTasksBtn);
    
 
        Separator verticalSep = new Separator(Orientation.VERTICAL);
        verticalSep.setPrefWidth(1);
        verticalSep.setLayoutX(150);
        verticalSep.setLayoutY(180);
        verticalSep.setPrefHeight(400);
        
        // 使用内联CSS样式
        verticalSep.setStyle("-fx-background-color: transparent; -fx-opacity: 1; -fx-border-width: 0;");
        // 添加自定义样式类
        verticalSep.getStyleClass().add("custom-separator");
        
        // 在场景初始化时添加样式
        stage.getStage().getScene().getStylesheets().add("data:text/css," + 
            ".custom-separator .line { -fx-border-width: 0; -fx-background-color: #181A1F; }");
        verticalSep.setPrefWidth(1);
        verticalSep.setLayoutX(150);
        verticalSep.setLayoutY(180);
        verticalSep.setPrefHeight(400);
    
  
        Pane rightPane = createPane("右侧", Orientation.HORIZONTAL);

        var tab1 = new Tab();
        tab1.setGraphic(new HBox(5, new FontIcon(Feather.PLAY), new javafx.scene.text.Text("全部开始")));
        var tab2 = new Tab();
        tab2.setGraphic(new HBox(5, new FontIcon(Feather.PAUSE), new javafx.scene.text.Text("全部暂停")));
        var tab3 = new Tab();
        tab3.setGraphic(new HBox(5, new FontIcon(Feather.PLAY_CIRCLE), new javafx.scene.text.Text("开始")));
        var tab4 = new Tab();
        tab4.setGraphic(new HBox(5, new FontIcon(Feather.PAUSE_CIRCLE), new javafx.scene.text.Text("暂停")));
        var tab5 = new Tab();
        tab5.setGraphic(new HBox(5, new FontIcon(Feather.TRASH_2), new javafx.scene.text.Text("清除任务")));

        var tabPane = new TabPane(){{
            getTabs().addAll(tab1, tab2, tab3, tab4, tab5);
            setPrefSize(700, 500);
            setLayoutX(0);
            setLayoutY(80);
            setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        }};
        

        rightPane.getChildren().add(tabPane);
        rightPane.setLayoutX(240);
        rightPane.setLayoutY(50);
    

        
        stage.getStage().getScene().getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm()); 
        mainPane.getChildren().addAll(leftPane, verticalSep, rightPane);
    








        stage.getRoot().getContentPane().getChildren().addAll(headerBox,mainPane);
        stage.getStage().setWidth(1100);
        stage.getStage().setHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
