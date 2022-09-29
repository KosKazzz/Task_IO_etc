import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Loadconf {

    private boolean loadEnabled;
    private String loadFileName;
    private String loadFormat;

    private boolean saveEnabled;
    private String saveFileName;
    private String saveFormat;

    private boolean logEnabled;
    private String logFileName;
    private String logFormat;

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public String getLoadFileName() {
        return loadFileName;
    }

    public String getLoadFormat() {
        return loadFormat;
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public String getSaveFormat() {
        return saveFormat;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public String getLogFormat() {
        return logFormat;
    }

    public Loadconf(String confFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document domConf = builder.parse(new File(confFile));
            Node root = domConf.getDocumentElement();
            NodeList rootChildList = root.getChildNodes();
            for (int i = 0; i < rootChildList.getLength(); i++) {
                Node rootChildNode = rootChildList.item(i);
                if (rootChildNode.ELEMENT_NODE == rootChildNode.getNodeType()) {
                    NodeList rootChildNodeProp = rootChildNode.getChildNodes();
                    for (int a = 0; a < rootChildNodeProp.getLength(); a++) {
                        Node prop = rootChildNodeProp.item(a);
                        if (prop.getNodeType() != Node.TEXT_NODE) {
                            if (rootChildNode.getNodeName().equals("load")) {
                                switch (prop.getNodeName()) {
                                    case ("enabled"):
                                        loadEnabled = isBoolean(isBoolString(prop.getTextContent()));
                                        break;
                                    case ("fileName"):
                                        loadFileName = prop.getTextContent();
                                        break;
                                    case ("format"):
                                        loadFormat = prop.getTextContent();
                                        break;
                                }
                            }
                            if (rootChildNode.getNodeName().equals("save")) {
                                switch (prop.getNodeName()) {
                                    case ("enabled"):
                                        saveEnabled = isBoolean(isBoolString(prop.getTextContent()));
                                        break;
                                    case ("fileName"):
                                        saveFileName = prop.getTextContent();
                                        break;
                                    case ("format"):
                                        saveFormat = prop.getTextContent();
                                        break;
                                }
                            }
                            if (rootChildNode.getNodeName().equals("log")) {
                                switch (prop.getNodeName()) {
                                    case ("enabled"):
                                        logEnabled = isBoolean(isBoolString(prop.getTextContent()));
                                        break;
                                    case ("fileName"):
                                        logFileName = prop.getTextContent();
                                        break;
                                    case ("format"):
                                        logFormat = prop.getTextContent();
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public String isBoolString(String str) {
        if (str.equals("true") || str.equals("false")) {
            return str;
        }
        System.err.println(str + " - Это не верный параметр в файле конфигурации!");
        System.exit(0);
        return "Error!!!";
    }

    public boolean isBoolean(String srt) {
        return srt.equals("true");
    }
}
