package io.oth.xdsgenerator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(Config.class)
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties("xdsgenerator")
class Config {

  String getCvr() { return cvr; }

  String getSystemName() { return systemName; }

  String getStsUrl() { return stsUrl; }

  void setCvr(String cvr) { this.cvr = cvr; }

  void setSystemName(String systemName) { this.systemName = systemName; }

  void setStsUrl(String stsUrl) { this.stsUrl = stsUrl; }

  void setFederationType(String federationType) {
    this.federationType = federationType;
  }

  void setVaultType(String vaultType) { this.vaultType = vaultType; }

  void setVaultPassword(String vaultPassword) {
    this.vaultPassword = vaultPassword;
  }

  void setVaultPath(String vaultPath) { this.vaultPath = vaultPath; }

  String getFederationType() { return federationType; }

  String getVaultType() { return vaultType; }

  String getVaultPassword() { return vaultPassword; }

  String getVaultPath() { return vaultPath; }
  private String cvr;
  private String systemName;
  private String stsUrl;
  private String federationType;
  private String vaultType;
  private String vaultPassword;
  private String vaultPath;
}
