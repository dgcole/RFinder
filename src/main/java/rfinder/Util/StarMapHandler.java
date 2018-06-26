package rfinder.Util;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import rfinder.Hazeron.*;

public class StarMapHandler extends DefaultHandler {
    private StarMap starMap;
    private Galaxy galaxy;
    private Sector sector;
    private rfinder.Hazeron.System system;
    private Body body;
    private Star star;
    private boolean parsingStar = false;
    private Zone z1, z2, z3;
    private int zones;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "starmap":
                starMap = new StarMap();
                break;
            case "galaxy":
                String galaxyName = attributes.getValue("name");
                galaxy = new Galaxy(galaxyName);
                starMap.addGalaxy(galaxy, galaxyName);
                break;
            case "sector":
                String sectorID = attributes.getValue("sectorId");
                sector = new Sector(attributes.getValue("name"),
                        Integer.parseInt(attributes.getValue("x")),
                        Integer.parseInt(attributes.getValue("y")),
                        Integer.parseInt(attributes.getValue("z")), galaxy);
                galaxy.addSector(sector, sectorID);
                break;
            case "system":
                String systemID = attributes.getValue("systemId");
                system = new rfinder.Hazeron.System(attributes.getValue("name"),
                        Double.parseDouble(attributes.getValue("x")),
                        Double.parseDouble(attributes.getValue("y")),
                        Double.parseDouble(attributes.getValue("z")), sector);
                sector.addSystem(system, systemID);
                break;
            case "star":
                parsingStar = true;
                String diameter = attributes.getValue("diameter");
                diameter = diameter.substring(0, diameter.indexOf("au") + 2);
                star = new Star(attributes.getValue("name"), diameter, system);
                z1 = new Zone(1, false, star);
                starMap.addZone(z1);
                break;
            case "planet":
                parsingStar = false;
                String planetZone = attributes.getValue("zone");
                planetZone = planetZone.substring(0, planetZone.indexOf(" "));
                body = new Body(attributes.getValue("name"), planetZone,
                        attributes.getValue("orbit"),
                        BodyType.getType(attributes.getValue("bodyType")), system);
                break;
            case "geosphere":
                zones = Integer.parseInt(attributes.getValue("resourceZones"));
                switch (zones) {
                    case 1:
                        z1 = new Zone(1, false, body);
                        starMap.addZone(z1);
                        break;
                    case 2:
                        z1 = new Zone(1, true, body);
                        z2 = new Zone(2, true, body);
                        starMap.addZone(z1);
                        starMap.addZone(z2);
                        break;
                    case 3:
                        z1 = new Zone(1, true, body);
                        z2 = new Zone(2, true, body);
                        z3 = new Zone(3, true, body);
                        starMap.addZone(z1);
                        starMap.addZone(z2);
                        starMap.addZone(z3);
                        break;
                }
                String pDiameter = attributes.getValue("diameter");
                pDiameter = pDiameter.substring(0, pDiameter.indexOf("m") + 1);
                body.setDiameter(pDiameter);
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

                int index = ResourceType.indexOf(resourceType);
                if (parsingStar) {
                    Resource resource = new Resource(resourceType, q1, q2, q3, a1,
                            a2, a3, star);
                    z1.setQuality(index, q1);
                    z1.setAbundance(index, a1);
                    starMap.addResource(resource);
                } else {
                    Resource resource = new Resource(resourceType, q1, q2, q3, a1,
                            a2, a3, body);
                    starMap.addResource(resource);

                    switch (zones) {
                        case 1:
                            z1.setQuality(index, q1);
                            z1.setAbundance(index, a1);
                            break;
                        case 2:
                            z1.setQuality(index, q1);
                            z1.setAbundance(index, a1);
                            if (q2 != 0) {
                                z2.setQuality(index, q2);
                                z2.setAbundance(index, a2);
                            } else {
                                z2.setQuality(index, q1);
                                z2.setAbundance(index, a1);
                            }
                            break;
                        case 3:
                            z1.setQuality(index, q1);
                            z1.setAbundance(index, a1);
                            if (q3 != 0) {
                                z2.setQuality(index, q2);
                                z2.setAbundance(index, a2);
                                z3.setQuality(index, q3);
                                z3.setAbundance(index, a3);
                            } else {
                                z2.setQuality(index, q1);
                                z2.setAbundance(index, a1);
                                z3.setQuality(index, q1);
                                z3.setAbundance(index, a1);
                            }
                            break;
                    }
                }
                break;
        }
    }

    public StarMap getStarMap() {
        return starMap;
    }
}
