package com.github.nitram509.geodatabase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Country {

  public long ipstart;
  public long ipend;
  public String name;
  public String code;

  public String remoteHost;

}
