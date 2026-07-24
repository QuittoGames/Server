package com.quitto.server;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Coffee Server - All Tests")
@SelectPackages("com.quitto.server")
@IncludeEngines("junit-jupiter")
public class TestSuite {
}