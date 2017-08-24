package com.purplebeen.springblog.utills;

public class XSSFilter {
    public static String filter(String str) {
        // TODO Auto-generated method stub
        str= str.toLowerCase();
        str = str.replaceAll("javascript", "x-javascript");
        str = str.replaceAll("script", "x-script");
        str = str.replaceAll("iframe", "x-iframe");
        str = str.replaceAll("document", "x-document");
        str = str.replaceAll("vbscript", "x-vbscript");
        str = str.replaceAll("applet", "x-applet");
        str = str.replaceAll("embed", "x-embed");  // embed 태그를 사용하지 않을 경우만
        str = str.replaceAll("object", "x-object");    // object 태그를 사용하지 않을 경우만
        str = str.replaceAll("frame", "x-frame");
        str = str.replaceAll("grameset", "x-grameset");
        str = str.replaceAll("layer", "x-layer");
        str = str.replaceAll("bgsound", "x-bgsound");
        str = str.replaceAll("alert", "x-alert");
        str = str.replaceAll("onblur", "x-onblur");
        str = str.replaceAll("onchange", "x-onchange");
        str = str.replaceAll("onclick", "x-onclick");
        str = str.replaceAll("ondblclick","x-ondblclick");
        str = str.replaceAll("enerror", "x-enerror");
        str = str.replaceAll("onfocus", "x-onfocus");
        str = str.replaceAll("onload", "x-onload");
        str = str.replaceAll("onmouse", "x-onmouse");
        str = str.replaceAll("onscroll", "x-onscroll");
        str = str.replaceAll("onsubmit", "x-onsubmit");
        str = str.replaceAll("onunload", "x-onunload");
        return str;
    }
}
