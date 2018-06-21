package rfinder.Util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import rfinder.Hazeron.*;

import java.lang.System;
import java.util.List;

public class StarMapHandler extends DefaultHandler {
    private StarMap starMap;
    private Galaxy galaxy;
    private Sector sector;
    private rfinder.Hazeron.System system;
    private Planet planet;
    private Star star;
    private String sphere;
    private int zones = 0;
    private boolean parsingStar = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "starmap":
                starMap = new StarMap(attributes.getValue("empire"));
                break;
            case "galaxy":
                String galaxyName = attributes.getValue("name");
                galaxy = new Galaxy(galaxyName, starMap);
                starMap.addGalaxy(galaxy, galaxyName);
                break;
            case "sector":
                String sectorID = attributes.getValue("sectorId");
                sector = new Sector(sectorID, attributes.getValue("name"),
                        Integer.parseInt(attributes.getValue("x")),
                        Integer.parseInt(attributes.getValue("y")),
                        Integer.parseInt(attributes.getValue("z")), galaxy);
                galaxy.addSector(sector, sectorID);
                break;
            case "system":
                String systemID = attributes.getValue("systemId");
                system = new rfinder.Hazeron.System(systemID, attributes.getValue("name"),
                        attributes.getValue("eod"),
                        Double.parseDouble(attributes.getValue("x")),
                        Double.parseDouble(attributes.getValue("y")),
                        Double.parseDouble(attributes.getValue("z")), sector);
                sector.addSystem(system, systemID);
                break;
            case "star":
                sphere = "heliosphere";
                parsingStar = true;
                String starID = attributes.getValue("starId");
                String diameter = attributes.getValue("diameter");
                diameter = diameter.substring(0, diameter.indexOf("au") + 2);
                star = new Star(starID, attributes.getValue("name"),
                        attributes.getValue("orbit"),
                        attributes.getValue("spectralClass"),
                        attributes.getValue("size"),
                        attributes.getValue("hab"),
                        attributes.getValue("shell"),
                        diameter, system);
                system.addStar(star, starID);
                break;
            case "planet":
                parsingStar = false;
                String planetID = attributes.getValue("planetId");
                planet = new Planet(planetID, attributes.getValue("name"),
                        attributes.getValue("bodyType"),
                        attributes.getValue("orbit"),
                        attributes.getValue("zone"), system);
                system.addPlanet(planet, planetID);
                break;
            case "geosphere":
                sphere = "geosphere";
                zones = Integer.parseInt(attributes.getValue("resourceZones"));
                String pDiameter = attributes.getValue("diameter");
                pDiameter = pDiameter.substring(0, pDiameter.indexOf("m") + 1);
                planet.setDiameter(pDiameter);
                break;
            case "hydrosphere":
                sphere = "hydrosphere";
                break;
            case "atmosphere":
                sphere = "atmosphere";
                break;
            case "biosphere":
                sphere = "biosphere";
                break;
            case "resource":
                ResourceType resourceType = ResourceType.getType(attributes.getValue("name"));

                int q1 = attributes.getValue("qualityZone1") != null ?
                        Integer.parseInt(attributes.getValue("qualityZone1")) :
                        (attributes.getValue("quality") != null ? Integer.parseInt(attributes.getValue("quality")) : 0);
                int q2 = attributes.getValue("qualityZone2") != null ?
                        Integer.parseInt(attributes.getValue("qualityZone2")) : 0;
                int q3 = attributes.getValue("qualityZone3") != null ?
                        Integer.parseInt(attributes.getValue("qualityZone3")) : 0;
                int a1 = attributes.getValue("abundanceZone1") != null ?
                        Integer.parseInt(attributes.getValue("abundanceZone1")) :
                        (attributes.getValue("abundance") != null ? Integer.parseInt(attributes.getValue("abundance")) : 0);
                int a2 = attributes.getValue("abundanceZone2") != null ?
                        Integer.parseInt(attributes.getValue("abundanceZone2")) : 0;
                int a3 = attributes.getValue("abundanceZone3") != null ?
                        Integer.parseInt(attributes.getValue("abundanceZone3")) : 0;

                if (parsingStar) {
                    Resource resource = new Resource(resourceType, sphere.equals("geosphere") ? zones : 1, q1, q2, q3, a1,
                            a2, a3, sphere, star);
                    star.addResource(resource, resourceType);
                    starMap.addResource(resource);
                } else {
                    Resource resource = new Resource(resourceType, sphere.equals("geosphere") ? zones : 1, q1, q2, q3, a1,
                            a2, a3, sphere, planet);
                    planet.addResource(resource, resourceType);
                    starMap.addResource(resource);
                }
                break;
        }
    }

    public StarMap getStarMap() {
        return starMap;
    }
}