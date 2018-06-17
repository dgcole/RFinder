package rfinder;

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
                galaxy = new Galaxy(galaxyName);
                starMap.addGalaxy(galaxy, galaxyName);
                break;
            case "sector":
                String sectorID = attributes.getValue("sectorId");
                sector = new Sector(sectorID, attributes.getValue("name"),
                        Integer.parseInt(attributes.getValue("x")),
                        Integer.parseInt(attributes.getValue("y")),
                        Integer.parseInt(attributes.getValue("z")));
                galaxy.addSector(sector, sectorID);
                break;
            case "system":
                String systemID = attributes.getValue("systemId");
                system = new rfinder.Hazeron.System(systemID, attributes.getValue("name"),
                        attributes.getValue("eod"),
                        Double.parseDouble(attributes.getValue("x")),
                        Double.parseDouble(attributes.getValue("y")),
                        Double.parseDouble(attributes.getValue("z")));
                sector.addSystem(system, systemID);
                break;
            case "star":
                sphere = "heliosphere";
                parsingStar = true;
                String starID = attributes.getValue("starId");
                star = new Star(starID, attributes.getValue("name"),
                        attributes.getValue("orbit"),
                        attributes.getValue("spectralClass"),
                        attributes.getValue("size"),
                        attributes.getValue("hab"),
                        attributes.getValue("shell"),
                        attributes.getValue("diameter"));
                system.addStar(star, starID);
                break;
            case "planet":
                parsingStar = false;
                String planetID = attributes.getValue("planetId");
                planet = new Planet(planetID, attributes.getValue("name"),
                        attributes.getValue("bodyType"),
                        attributes.getValue("orbit"),
                        attributes.getValue("zone"));
                system.addPlanet(planet, planetID);
                break;
            case "geosphere":
                sphere = "geosphere";
                zones = Integer.parseInt(attributes.getValue("resourceZones"));
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
                        Integer.parseInt(attributes.getValue("qualityZone1")) : 0;
                int q2 = attributes.getValue("qualityZone2") != null ?
                        Integer.parseInt(attributes.getValue("qualityZone2")) : 0;
                int q3 = attributes.getValue("qualityZone3") != null ?
                        Integer.parseInt(attributes.getValue("qualityZone3")) : 0;
                int a1 = attributes.getValue("abundanceZone1") != null ?
                        Integer.parseInt(attributes.getValue("abundanceZone1")) : 0;
                int a2 = attributes.getValue("abundanceZone2") != null ?
                        Integer.parseInt(attributes.getValue("abundanceZone2")) : 0;
                int a3 = attributes.getValue("abundanceZone3") != null ?
                        Integer.parseInt(attributes.getValue("abundanceZone3")) : 0;

                Resource resource = new Resource(resourceType, sphere.equals("geosphere") ? zones : 1, q1, q2, q3, a1,
                        a2, a3, sphere);

                if (parsingStar) star.addResource(resource, resourceType);
                else planet.addResource(resource, resourceType);
                break;
        }
    }

    public StarMap getStarMap() {
        return starMap;
    }
}
