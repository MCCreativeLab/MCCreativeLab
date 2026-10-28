package de.verdox.mccreativelab;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ChunkSectionCopyTest.class, BehaviourEvaluationTest.class})
@ConfigurationParameter(key = "TestSuite", value = "VanillaFeature")
public class MCCCompatibilityTestSuite {
}
