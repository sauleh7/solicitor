/**
 * SPDX-License-Identifier: Apache-2.0
 */

package com.devonfw.tools.solicitor.reader.maven;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.tools.solicitor.common.FileInputStreamFactory;
import com.devonfw.tools.solicitor.model.ModelFactory;
import com.devonfw.tools.solicitor.model.impl.ModelFactoryImpl;
import com.devonfw.tools.solicitor.model.inventory.ApplicationComponent;
import com.devonfw.tools.solicitor.model.masterdata.Application;
import com.devonfw.tools.solicitor.model.masterdata.UsagePattern;

public class MavenReaderTests {
  private static final Logger LOG = LoggerFactory.getLogger(MavenReaderTests.class);

  @Test
  public void readFileAndCheckSize() {

    ModelFactory modelFactory = new ModelFactoryImpl();

    Application application = modelFactory.newApplication("testApp", "0.0.0.TEST", "1.1.2111", "http://bla.com",
        "Java8");
    MavenReader mr = new MavenReader();
    mr.setModelFactory(modelFactory);
    mr.setInputStreamFactory(new FileInputStreamFactory());
    mr.readInventory("maven", "src/test/resources/licenses_sample.xml", application, UsagePattern.DYNAMIC_LINKING,
        "maven", null);
    LOG.info(application.toString());
    assertEquals(95, application.getApplicationComponents().size());

    boolean found = false;
    for (ApplicationComponent ap : application.getApplicationComponents()) {
      if (ap.getGroupId().equals("org.springframework.boot") && //
          ap.getArtifactId().equals("spring-boot-starter-logging") && //
          ap.getVersion().equals("2.1.4.RELEASE")) {
        found = true;
        assertEquals("pkg:maven/org.springframework.boot/spring-boot-starter-logging@2.1.4.RELEASE",
            ap.getPackageUrl());
        break;
      }
    }
    assertTrue(found);

  }
}
