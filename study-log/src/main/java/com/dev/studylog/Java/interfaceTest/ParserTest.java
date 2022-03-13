package com.dev.studylog.Java.interfaceTest;

public class ParserTest {
    public static void main(String[] args) {
        Parseable parser = ParserManager.getParser("XML");
        parser.parse("document.xml");
        parser = ParserManager.getParser("HTML");
        parser.parse("document2.html");
    }
}

interface Parseable {
    //구문 분석 작업 수행
    public abstract void parse(String fileName);
}

class ParserManager {
    public static Parseable getParser(String type) {
        if(type.equals("XML")){
            return new XMLParser();
        } else {
            Parseable p = new HTMLParser();
            return p;
        }
    }
}

class XMLParser implements Parseable {
    public void parse(String fileName) {
        System.out.println(fileName+"- xml parsing completed.");
    }
}
class HTMLParser implements Parseable{
    public void parse(String fileName) {
        System.out.println(fileName+"- html parsing completed");
    }
}