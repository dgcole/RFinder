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
    private Zone z1, z2, z3, z4;
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
                system = new rfinder.Hazeron.System(attributes.getValue("name"), systemID,
                        Double.parseDouble(attributes.getValue("x")),
                        Double.parseDouble(attributes.getValue("y")),
                        Double.parseDouble(attributes.getValue("z")), sector);
                sector.addSystem(system, systemID);
                starMap.addSystem(system, systemID);
                break;
            case "wormhole":
                double destX = Double.parseDouble(attributes.getValue("destX"));
                double destY = Double.parseDouble(attributes.getValue("destY"));
                double destZ = Double.parseDouble(attributes.getValue("destZ"));
                Wormhole wormhole = new Wormhole(destX, destY, destZ);
                system.addWormhole(wormhole);
                break;
            case "star":
                parsingStar = true;
                String diameter = attributes.getValue("diameter");
                diameter = diameter.substring(0, diameter.indexOf("au") + 2);
                star = new Star(attributes.getValue("name"), diameter, system);
                z1 = new Zone(1, false, star);
                system.addZone(z1);
                starMap.addZone(z1);
                break;
            case "planet":
                parsingStar = false;
                String planetZone = attributes.getValue("zone");
                planetZone = planetZone.substring(0, planetZone.indexOf(" "));
                body = new Body(attributes.getValue("name"), planetZone,
                        BodyType.getType(attributes.getValue("bodyType")), system);
                break;
            case "geosphere":
                String pDiameter = attributes.getValue("diameter");
                pDiameter = pDiameter.substring(0, pDiameter.indexOf("m") + 1);
                body.setDiameter(pDiameter);
                zones = Integer.parseInt(attributes.getValue("resourceZones"));

                switch (zones) {
                    case 1:
                        z1 = new Zone(1, false, body);
                        system.addZone(z1);
                        starMap.addZone(z1);
                        break;
                    case 2:
                        z1 = new Zone(1, true, body);
                        z2 = new Zone(2, true, body);
                        system.addZone(z1);
                        system.addZone(z2);
                        starMap.addZone(z1);
                        starMap.addZone(z2);
                        break;
                    case 3:
                        z1 = new Zone(1, true, body);
                        z2 = new Zone(2, true, body);
                        z3 = new Zone(3, true, body);
                        system.addZone(z1);
                        system.addZone(z2);
                        system.addZone(z3);
                        starMap.addZone(z1);
                        starMap.addZone(z2);
                        starMap.addZone(z3);
                        break;
                    case 4:
                        z1 = new Zone(1, true, body);
                        z2 = new Zone(2, true, body);
                        z3 = new Zone(3, true, body);
                        z4 = new Zone(4, true, body);
                        system.addZone(z1);
                        system.addZone(z2);
                        system.addZone(z3);
                        system.addZone(z4);
                        starMap.addZone(z1);
                        starMap.addZone(z2);
                        starMap.addZone(z3);
                        starMap.addZone(z4);
                    default:
                        break;
                }
                break;
            case "resource":
                ResourceType resourceType = ResourceType.getType(attributes.getValue("name"));

                String qVal = attributes.getValue("quality");
                String q1Val = attributes.getValue("qualityZone1");
                String q2Val = attributes.getValue("qualityZone2");
                String q3Val = attributes.getValue("qualityZone3");
                String q4Val = attributes.getValue("qualityZone4");
                String aVal = attributes.getValue("abundance");
                String a1Val = attributes.getValue("abundanceZone1");
                String a2Val = attributes.getValue("abundanceZone2");
                String a3Val = attributes.getValue("abundanceZone3");
                String a4Val = attributes.getValue("abundanceZone4");

                int q1 = q1Val != null ? Integer.parseInt(q1Val) : (qVal != null ? Integer.parseInt(qVal) : 0);
                int q2 = q2Val != null ? Integer.parseInt(q2Val) : 0;
                int q3 = q3Val != null ? Integer.parseInt(q3Val) : 0;
                int q4 = q4Val != null ? Integer.parseInt(q4Val) : 0;
                int a1 = a1Val != null ? Integer.parseInt(a1Val) : (aVal != null ? Integer.parseInt(aVal) : 0);
                int a2 = a2Val != null ? Integer.parseInt(a2Val) : 0;
                int a3 = a3Val != null ? Integer.parseInt(a3Val) : 0;
                int a4 = a4Val != null ? Integer.parseInt(a4Val) : 0;

                int index = ResourceType.indexOf(resourceType);
                if (parsingStar) {
                    Resource resource = new Resource(resourceType, q1, q2, q3, q4, a1,
                            a2, a3, a4, star);
                    z1.setQuality(index, q1);
                    z1.setAbundance(index, a1);
                    starMap.addResource(resource);
                } else {
                    Resource resource = new Resource(resourceType, q1, q2, q3, q4, a1,
                            a2, a3, a4, body);
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
                        case 4:
                            z1.setQuality(index, q1);
                            z1.setAbundance(index, a1);
                            if (q4 != 0) {
                                z2.setQuality(index, q2);
                                z2.setAbundance(index, a2);
                                z3.setQuality(index, q3);
                                z3.setAbundance(index, a3);
                                z4.setQuality(index, q4);
                                z4.setAbundance(index, a4);
                            } else {
                                z2.setQuality(index, q1);
                                z2.setAbundance(index, a1);
                                z3.setQuality(index, q1);
                                z3.setAbundance(index, a1);
                                z4.setQuality(index, q1);
                                z4.setQuality(index, a1);
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
