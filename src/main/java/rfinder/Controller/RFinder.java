package rfinder.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;
import rfinder.Hazeron.*;
import rfinder.Hazeron.System;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.System;
import java.util.Objects;

public class RFinder extends Application {
    private static Stage mainStage;

    @FXML
    private AnchorPane topPane;

    @Override
    public void start(Stage mainStage) throws Exception {
        RFinder.mainStage = mainStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rfinder.fxml")));

        Scene rootScene = new Scene(root);

        mainStage.setTitle("RFinder");
        mainStage.setScene(rootScene);
        mainStage.setMinWidth(960);
        mainStage.setMinHeight(540);
        mainStage.show();
    }

    public void exit(ActionEvent actionEvent) {
        mainStage.close();
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 0.1.0");
        alert.setContentText("RFinder Copyright © 2018 expert700, all right reserved.\n" +
                "Icons Copyright © 1999-2018 Software Engineering, Inc.");

        alert.show();
    }

    public void importStarmap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open StarMap");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("StarMap (*.xml)", "*.xml"));
        fileChooser.setInitialDirectory(new File(java.lang.System.getProperty("user.home") + "/Shores of Hazeron/"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(selectedFile);
                Element root = document.getDocumentElement();

                NodeList galaxyNodes = root.getChildNodes();
                StarMap starMap = new StarMap(root.getAttribute("empire"));
                for (int i = 0; i < galaxyNodes.getLength(); i++) {
                    Node galaxyNode = galaxyNodes.item(i);
                    if (!galaxyNode.getNodeName().equals("galaxy")) continue;
                    Galaxy galaxy = new Galaxy(galaxyNode.getAttributes().getNamedItem("name").getNodeValue());
                    NodeList sectorNodes = galaxyNode.getChildNodes();
                    for (int j = 0; j < sectorNodes.getLength(); j++) {
                        Node sectorNode = sectorNodes.item(j);
                        if (!sectorNode.getNodeName().equals("sector")) continue;
                        NamedNodeMap sectorAttributes = sectorNode.getAttributes();
                        Sector sector = new Sector(sectorAttributes.getNamedItem("sectorId").getNodeValue(),
                                sectorAttributes.getNamedItem("name").getNodeValue(),
                                Integer.parseInt(sectorAttributes.getNamedItem("x").getNodeValue()),
                                Integer.parseInt(sectorAttributes.getNamedItem("y").getNodeValue()),
                                Integer.parseInt(sectorAttributes.getNamedItem("z").getNodeValue()));
                        NodeList systemNodes = sectorNode.getChildNodes();
                        for (int k = 0; k < systemNodes.getLength(); k++) {
                            Node systemNode = systemNodes.item(k);
                            if (!systemNode.getNodeName().equals("system")) continue;
                            NamedNodeMap systemAttributes = systemNode.getAttributes();
                            rfinder.Hazeron.System system = new rfinder.Hazeron.System(systemAttributes.getNamedItem("systemId").getNodeValue(),
                                    systemAttributes.getNamedItem("name").getNodeValue(),
                                    systemAttributes.getNamedItem("eod").getNodeValue(),
                                    Double.parseDouble(systemAttributes.getNamedItem("x").getNodeValue()),
                                    Double.parseDouble(systemAttributes.getNamedItem("y").getNodeValue()),
                                    Double.parseDouble(systemAttributes.getNamedItem("z").getNodeValue()));
                            NodeList celestialBodies = systemNode.getChildNodes();
                            for (int l = 0; l < celestialBodies.getLength(); l++) {
                                Node body = celestialBodies.item(l);
                                if (body.getNodeName().equals("star")) {
                                    NamedNodeMap starAttributes = body.getAttributes();
                                    Star star = new Star(starAttributes.getNamedItem("starId").getNodeValue(),
                                            starAttributes.getNamedItem("name").getNodeValue(),
                                            starAttributes.getNamedItem("orbit").getNodeValue(),
                                            starAttributes.getNamedItem("spectralClass").getNodeValue(),
                                            starAttributes.getNamedItem("size").getNodeValue(),
                                            starAttributes.getNamedItem("hab").getNodeValue(),
                                            starAttributes.getNamedItem("shell").getNodeValue(),
                                            starAttributes.getNamedItem("diameter").getNodeValue());
                                    NodeList resourceNodes = body.getChildNodes();
                                    for (int m = 0; m < resourceNodes.getLength(); m++) {
                                        Node resourceNode = resourceNodes.item(m);
                                        if (!resourceNode.getNodeName().equals("resource")) continue;
                                        NamedNodeMap resourceAttributes = resourceNode.getAttributes();
                                        //TODO; Add preons.
                                    }
                                } else if (body.getNodeName().equals("planet")) {

                                }
                            }
                            sector.addSystem(system, system.getID());
                        }
                        galaxy.addSector(sector, sector.getID());
                    }
                    starMap.addGalaxy(galaxy, galaxy.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
