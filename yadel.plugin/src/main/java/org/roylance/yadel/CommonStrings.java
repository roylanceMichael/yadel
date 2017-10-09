package org.roylance.yadel;

public class CommonStrings {
  public static final String DebianBuild = CommonStringsHelper.DebianBuild;
  public static final String DebianBuildBuild = CommonStringsHelper.DebianBuildBuild;
  public static final String BuildInstallPath = CommonStringsHelper.BuildInstallPath;
  public static final String SBinPath = CommonStringsHelper.SBinPath;

  public static final String AWSDesiredCapacityName = CommonStringsHelper.AWSDesiredCapacityName;
  public static final String CreateAndInstallPackageName = CommonStringsHelper.CreateAndInstallPackageName;
  public static final String AutoScalingName = CommonStringsHelper.AutoScalingName;
  public static final String ServerTarName = CommonStringsHelper.ServerTarName;
  public static final String StartupName = CommonStringsHelper.StartupName;

  public static String buildProjectStartServerScriptLocation(String projectName) {
    return CommonStringsHelper.INSTANCE.buildProjectStartServerScriptLocation(projectName);
  }
  public static String buildProjectStartActorScriptLocation(String projectName) {
    return CommonStringsHelper.INSTANCE.buildProjectStartActorScriptLocation(projectName);
  }
  public static String buildSystemStartServerScriptLocation(String projectName) {
    return CommonStringsHelper.INSTANCE.buildSystemStartServerScriptLocation(projectName);
  }
  public static String buildSystemStartActorScriptLocation(String projectName) {
    return CommonStringsHelper.INSTANCE.buildSystemStartActorScriptLocation(projectName);
  }
  public static String buildSystemStopScriptLocation(String projectName) {
    return CommonStringsHelper.INSTANCE.buildSystemStopScriptLocation(projectName);
  }
  public static String buildProjectStopScriptLocation(String projectName) {
    return CommonStringsHelper.INSTANCE.buildProjectStopScriptLocation(projectName);
  }
  public static String buildInstallPath(String projectName, String fileName) {
    return CommonStringsHelper.INSTANCE.buildInstallPath(projectName, fileName);
  }
  public static String buildStartActorName(String projectName) {
    return CommonStringsHelper.INSTANCE.buildStartActorName(projectName);
  }

  public static String buildStartServerName(String projectName) {
    return CommonStringsHelper.INSTANCE.buildStartServerName(projectName);
  }
  public static String buildStopName(String projectName) {
    return CommonStringsHelper.INSTANCE.buildStopName(projectName);
  }
}
