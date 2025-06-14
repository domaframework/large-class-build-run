package com.example.domain;

import java.net.MalformedURLException;
import java.net.URL;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class URLConverter implements DomainConverter<URL, String> {

  @Override
  public String fromDomainToValue(URL url) {
    return url.toExternalForm();
  }

  @Override
  public URL fromValueToDomain(String s) {
    try {
      return new URL(s);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
