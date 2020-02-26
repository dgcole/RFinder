package rfinder.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import rfinder.Hazeron.Sector;
import rfinder.Hazeron.Wormhole;

import java.util.ArrayList;

public class MapController {
    @FXML
    SubScene subScene;

    private PerspectiveCamera camera;
    private Group root;
    private Translate pivot;
    private static MapController instance;

    @FXML
    public void initialize() {
        instance = this;

        Sphere core = new Sphere(5);
        core.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        core.setDrawMode(DrawMode.FILL);

        Rotate yRot = new Rotate(0, Rotate.Y_AXIS);
        pivot = new Translate();
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.01);
        camera.setFarClip(500);
        camera.setFieldOfView(75);
        camera.getTransforms().addAll (pivot, yRot, new Rotate(-20, Rotate.X_AXIS), new Translate(0, 0, -10));

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(yRot.angleProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(15),
                        new KeyValue(yRot.angleProperty(), 360)
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(core);

        subScene.setRoot(root);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);


    }

    void setup() {
        ArrayList<Sector> sectors = MainController.getInstance().getStarMap().getGalaxies().get(4).getSectors();
        for (Sector sector : sectors) {
            Box sectorBox = new Box(1, 1, 1);

            sectorBox.setMaterial(new PhongMaterial(Color.GREY));
            sectorBox.setDrawMode(DrawMode.LINE);
            sectorBox.setTranslateX(sector.getX());
            sectorBox.setTranslateY(sector.getY());
            sectorBox.setTranslateZ(sector.getZ());

            root.getChildren().add(sectorBox);
            for (rfinder.Hazeron.System system : sector.getSystems()) {
                Sphere systemSphere = new Sphere(0.05);

                systemSphere.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
                systemSphere.setDrawMode(DrawMode.LINE);
                systemSphere.setTranslateX(system.getX() / 10);
                systemSphere.setTranslateY(system.getY() / 10);
                systemSphere.setTranslateZ(system.getZ() / 10);

                root.getChildren().add(systemSphere);

                for (Wormhole wormhole : system.getWormholes()) {
                    Point3D origin = new Point3D(system.getX() / 10, system.getY() / 10, system.getZ() / 10);
                    Point3D target = new Point3D(wormhole.getDestX() / 10, wormhole.getDestY() / 10,
                            wormhole.getDestZ() / 10);

                    Point3D diff = target.subtract(origin);
                    double height = diff.magnitude();

                    Point3D mid = target.midpoint(origin);
                    Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

                    Point3D axisOfRotation = diff.crossProduct(Rotate.Y_AXIS);
                    double angle = Math.acos(diff.normalize().dotProduct(Rotate.Y_AXIS));
                    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

                    Cylinder line = new Cylinder(0.01, height);
                    line.setMaterial(new PhongMaterial());
                    line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

                    root.getChildren().add(line);
                }
            }
        }
        pivot.setX(170);
    }

    static MapController getInstance() {
        return instance;
    }
}
