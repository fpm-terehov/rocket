package utils;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Body;
import model.Rocket;
import model.StageRocket;

import java.io.*;
import java.nio.file.Files;
import java.util.Vector;

public class fxForm extends Application {

    @Override
    public void start(final Stage myStage) throws Exception {
        myStage.setResizable(false);
        myStage.setTitle("data form");
        final FlowPane rootNode = new FlowPane();
        rootNode.setAlignment(Pos.TOP_LEFT);
        final Scene myScene = new Scene(rootNode,1100,300);
        final TextField apo = new TextField();
        apo.setText("200000");
        final TextField peri = new TextField();
        peri.setText("180000");
        final TextField pms = new TextField();
        pms.setText("3000");
        final TextField stc = new TextField();
        stc.setText("3");

        final TextField st1fuel = new TextField();
        st1fuel.setText("0");

        final TextField st1dry = new TextField();
        st1dry.setText("0");

        final TextField st1delta = new TextField();
        st1delta.setText("0");
        final TextField st1length = new TextField();
        st1length.setText("0");
        final TextField st1force = new TextField();
        st1force.setText("0");

        final TextField st2dry = new TextField();
        st2dry.setText("0");
        final TextField st2fuel = new TextField();
        st2fuel.setText("0");

        final TextField st2delta = new TextField();
        st2delta.setText("0");
        final TextField st2length = new TextField();
        st2length.setText("0");
        final TextField st2force = new TextField();
        st2force.setText("0");

        final TextField st3dry = new TextField();
        st3dry.setText("0");
        final TextField st3fuel = new TextField();
        st3fuel.setText("0");

        final TextField st3delta = new TextField();
        st3delta.setText("0");
        final TextField st3length = new TextField();
        st3length.setText("0");
        final TextField st3force = new TextField();
        st3force.setText("0");



        final ObservableList<String> list = FXCollections.observableArrayList();

        {
            File folder = new File("custom rockets/");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    FileInputStream fis = new FileInputStream("custom rockets/"+file.getName());
                    ObjectInputStream oin = new ObjectInputStream(fis);
                    Rocket r = (Rocket) oin.readObject();
                    list.add(r.name);
                    System.out.println("name="+r.name);
                }
            }
        }
        final ListView<String> lv = new ListView<>(list);
        lv.getSelectionModel().selectFirst();
        lv.setPrefWidth(300);
        lv.setMaxHeight(100);

        final RadioButton n1 = new RadioButton("select from existed");

        final RadioButton n3 = new RadioButton("custom");
        ToggleGroup tg1 = new ToggleGroup();
        n1.setToggleGroup(tg1);
        n3.setToggleGroup(tg1);
        n1.fire();
        FlowPane f1 = new FlowPane();
        f1.getChildren().addAll(n1,n3);

        Label l1 = new Label("stage1");
        Label l2 = new Label("stage2");
        Label l3 = new Label("stage3");
        Label l4 = new Label("stages count");
        Label l5 = new Label("dry mass");
        Label l6 = new Label("fuel mass");
        Label l7 = new Label("orbit settings");
        Label l8 = new Label("apogee");
        Label l9 = new Label("perigee");
        Label l10 = new Label("payload");
        Label l11 = new Label("fuel consumtion");
        Label l12 = new Label("force");
        Label l13 = new Label("length");
        final FlowPane p1 = new FlowPane();

        FlowPane p6 = new FlowPane();
        p6.getChildren().addAll(l5,st1dry);
        FlowPane p7 = new FlowPane();
        p7.getChildren().addAll(l6,st1fuel);
        FlowPane p8 = new FlowPane();
        p8.getChildren().addAll(l11,st1delta);
        FlowPane p9 = new FlowPane();
        p9.getChildren().addAll(l12,st1force);
        FlowPane p10 = new FlowPane();
        p10.getChildren().addAll(l13,st1length);

        FlowPane p62 = new FlowPane();
        p62.getChildren().addAll(st2dry);
        FlowPane p72 = new FlowPane();
        p72.getChildren().addAll(st2fuel);
        FlowPane p82 = new FlowPane();
        p82.getChildren().addAll(st2delta);
        FlowPane p92 = new FlowPane();
        p92.getChildren().addAll(st2force);
        FlowPane p102 = new FlowPane();
        p102.getChildren().addAll(st2length);

        FlowPane p63 = new FlowPane();
        p63.getChildren().addAll(st3dry);
        FlowPane p73 = new FlowPane();
        p73.getChildren().addAll(st3fuel);
        FlowPane p83 = new FlowPane();
        p83.getChildren().addAll(st3delta);
        FlowPane p93 = new FlowPane();
        p93.getChildren().addAll(st3force);
        FlowPane p103 = new FlowPane();
        p103.getChildren().addAll(st3length);

        FlowPane p2 = new FlowPane();
        p2.getChildren().addAll(l8,apo);
        FlowPane p3 = new FlowPane();
        p3.getChildren().addAll(l9,peri);
        FlowPane p4 = new FlowPane();
        p4.getChildren().addAll(l10,pms);
        FlowPane p5 = new FlowPane();
        Label ll = new Label("name:");
        final TextField tf = new TextField();
        p5.getChildren().addAll(l4,stc,ll,tf);
        Button button = new Button("start");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!n1.isSelected())
                    return;
                Main.rocket = new Rocket();
                try {
                    if(lv.getSelectionModel().getSelectedItem()!=null && !n3.isSelected()){
                        FileInputStream fis = new FileInputStream("custom rockets/"
                                +lv.getSelectionModel().getSelectedItem()+".out");
                        ObjectInputStream oin = new ObjectInputStream(fis);
                        try {
                            Main.rocket = (Rocket) oin.readObject();
                            Main.rocket.polmassa = Integer.valueOf(pms.getText());
                            Main.rocket.stages[Main.rocket.stagesCount-1].drymass += Main.rocket.polmassa;
                            Main.rocket.stages[Main.rocket.stagesCount-1].m += Main.rocket.polmassa;
                            Main.rocket.m += Main.rocket.polmassa;
                            Main.rocket.apogee = Double.valueOf(apo.getText());
                            Main.rocket.perigee = Double.valueOf(peri.getText());
                            Main.br = new BufferedWriter(new FileWriter("output.txt"));
                            Main.br1 = new BufferedWriter(new FileWriter("output1.txt"));
                            Main.rocket.velocity.set(0,65.0);
                            Main.realTime = System.currentTimeMillis();
                            game.start();
                            myStage.close();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        VBox f2 = new VBox();
        f2.setPrefWidth(300);
        VBox f4 = new VBox();
        f4.setPrefWidth(300);
        f4.getChildren().addAll(l1,p6,p7,p8,p9,p10);
        final VBox f6 = new VBox();
        f6.setPrefWidth(200);
        f6.getChildren().addAll(l2,p62,p72,p82,p92,p102);
        final VBox f7 = new VBox();
        f7.setPrefWidth(200);
        f7.getChildren().addAll(l3,p63,p73,p83,p93,p103);
        VBox f8 = new VBox();
        FlowPane fp = new FlowPane();
        fp.getChildren().addAll(f4,f6,f7);
        final VBox f5 = new VBox();

        Button button2 = new Button("delete from list");
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lv.getSelectionModel().getSelectedItem()!=null) {
                    File file = new File("custom rockets/"
                            +lv.getSelectionModel().getSelectedItem()+".out");
                    if(file.delete()) {
                        list.remove(lv.getSelectionModel().getSelectedItem());
                        System.out.println("success delete " + file.getName());
                    }
                }
            }
        });
        f2.getChildren().addAll(f1,lv,button2,l7,p2,p3,p4,button);

        Button button3 = new Button("add to list");
        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Main.rocket = new Rocket();
                    Main.rocket.stagesCount = Integer.valueOf(stc.getText());
                    Main.rocket.stages = new StageRocket[3];
                    Main.rocket.stages[0] = new StageRocket();
                    Main.rocket.stages[1] = new StageRocket();
                    Main.rocket.stages[2] = new StageRocket();
                    Main.rocket.stages[0].force = Double.valueOf(st1force.getText());
                    Main.rocket.stages[1].force = Double.valueOf(st2force.getText());
                    Main.rocket.stages[2].force = Double.valueOf(st3force.getText());
                    Main.rocket.stages[0].deltamass = Double.valueOf(st1delta.getText());
                    Main.rocket.stages[1].deltamass = Double.valueOf(st2delta.getText());
                    Main.rocket.stages[2].deltamass = Double.valueOf(st3delta.getText());
                    Main.rocket.stages[0].dlina = Double.valueOf(st1length.getText());
                    Main.rocket.stages[1].dlina = Double.valueOf(st2length.getText());
                    Main.rocket.stages[2].dlina = Double.valueOf(st2length.getText());
                    Main.rocket.stages[0].fuelmass = Double.valueOf(st1fuel.getText());
                    Main.rocket.stages[1].fuelmass = Double.valueOf(st2fuel.getText());
                    Main.rocket.stages[2].fuelmass = Double.valueOf(st3fuel.getText());
                    Main.rocket.stages[0].drymass = Double.valueOf(st1dry.getText());
                    Main.rocket.stages[1].drymass = Double.valueOf(st2dry.getText());
                    Main.rocket.stages[2].drymass = Double.valueOf(st3dry.getText());
                    Main.rocket.name = tf.getText();
                    Main.rocket.init1st();
                    FileOutputStream fos = new FileOutputStream("custom rockets/"+Main.rocket.name+".out");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(Main.rocket);
                    oos.flush();
                    oos.close();
                    list.add(Main.rocket.name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        f5.setPrefWidth(800);
        f5.getChildren().addAll(p5,fp,p1,button3);

        stc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double temp = Double.valueOf(stc.getText());
                if(temp < 3){
                    f7.setVisible(false);
                }
                if(temp < 2){
                    f6.setVisible(false);
                }
            }
        });





        f5.setVisible(false);
        n1.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                f5.setVisible(false);
            }
        });
        n3.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                f5.setVisible(true);
            }
        });

        rootNode.getChildren().addAll(f2,f5);
        myStage.setScene(myScene);
        myStage.show();
        lv.requestFocus();
    }
    public static Main game;
    public static void window(String args[], Main gam){
        game = gam;
        launch(args);
    }
}
