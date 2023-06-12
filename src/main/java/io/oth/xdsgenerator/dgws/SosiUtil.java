package io.oth.xdsgenerator.dgws;

import dk.sosi.seal.model.AuthenticationLevel;
import dk.sosi.seal.model.CareProvider;
import dk.sosi.seal.model.IDCard;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.interceptor.Fault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;

import dk.sosi.seal.SOSIFactory;
import dk.sosi.seal.model.ModelException;
import dk.sosi.seal.model.Request;
import dk.sosi.seal.model.SecurityTokenRequest;
import dk.sosi.seal.model.SecurityTokenResponse;
import dk.sosi.seal.model.SystemIDCard;
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues;
import dk.sosi.seal.pki.SOSIFederation;
import dk.sosi.seal.pki.SOSITestFederation;
import dk.sosi.seal.vault.CredentialPair;
import dk.sosi.seal.vault.CredentialVault;
import dk.sosi.seal.vault.CredentialVaultException;
import dk.sosi.seal.vault.FileBasedCredentialVault;
import dk.sosi.seal.xml.XmlUtil;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Handles the SOSI stuff towards seal
 */
@Component
public class SosiUtil {
	private static Logger log = LoggerFactory.getLogger(SosiUtil.class);
	private IDCard idCard = null;

	private static Object mutex = new Object();

	protected SOSIFactory sosiFactory;

	protected CredentialVault credentialVault;

	@Autowired
	protected STSRequestHelper requestHelper;

	@Value("${dgws.keystore.type}")
	private String keystoreType;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${dgws.certificate.itsystem}")
	private String itsystem;

	@Value("${dgws.certificate.cvr}")
	private String cvr;

	@Value("${dgws.certificate.orgname}")
	private String orgname;

	@Value("${dgws.keystore.alias}")
	private String keystoreAlias;

	@Value("${dgws.keystore.filename}")
	private String keystoreFilename;

	@Value("${dgws.keystore.password}")
	private String keystorePassword;


	@PostConstruct
	public void init() throws ModelException, CredentialVaultException, KeyStoreException, IOException {
		log.info("Initializing the sosi utility - setting up Credentials etc.");
		synchronized (mutex) {
			Properties props = new Properties(System.getProperties());
			props.setProperty(SOSIFactory.PROPERTYNAME_SOSI_VALIDATE, Boolean.toString(true));
			this.credentialVault = createCredentialVault();
			if (keystoreType.equalsIgnoreCase("production")) {
				log.info("Creating SOSI production federation");
				this.sosiFactory = new SOSIFactory(new SOSIFederation(new Properties()), getVault(), props);
			} else {
				log.info("Creating SOSI test federation");
				this.sosiFactory = new SOSIFactory(new SOSITestFederation(new Properties()), getVault(), props);

			}
		}
	}

	protected Document generateTokenRequest(CredentialVault vault, SOSIFactory sosiFactory) {
		X509Certificate cert = vault.getSystemCredentialPair().getCertificate();
		CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, getCvr(), getOrgName());
		SystemIDCard selfSignedSystemIdCard = sosiFactory.createNewSystemIDCard(getItSystem(), careProvider,
				AuthenticationLevel.VOCES_TRUSTED_SYSTEM, null, null, cert, null);

		SecurityTokenRequest securityTokenRequest = sosiFactory.createNewSecurityTokenRequest();
		securityTokenRequest.setIDCard(selfSignedSystemIdCard);
		Document doc = securityTokenRequest.serialize2DOMDocument();
		return doc;
	}

	public static CredentialVault getVaultFromFile(Resource r, String password, String alias) throws IOException {
		return getVaultFromFile(r, password, alias, password);
	}

	public static CredentialVault getVaultFromFile(Resource r, String keyStorePassword, String alias,
			String keyPassword) throws IOException {
		InputStream in = r.getInputStream();
		return getVaultFromInputStream(in, keyStorePassword, alias, keyPassword);
	}

	public static CredentialVault getVaultFromInputStream(InputStream in, String keyStorePassword, String alias,
			String keyPassword) throws IOException {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(in, keyStorePassword.toCharArray());
			X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());
			if (certificate != null && privateKey != null) {
				return new CredentialVaultWrapper(certificate, privateKey);
			}
			return null;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
			throw new IOException("Fejl i indlæsning af den private nøgle", e);
		}
	}

	public CredentialVault getVault() throws CredentialVaultException, IOException, KeyStoreException {
		CredentialVault vault = credentialVault;
		CredentialPair pair = vault.getSystemCredentialPair();
		if (pair == null || pair.getCertificate() == null) {
			throw new IOException("Could not load certificate from vault: "
					+ vault.getKeyStore().getProvider().getName() + " with alias: " + CredentialVault.KEY_ALIAS_SYSTEM);
		} else {
			log.info("CredentialValue loaded correctly");
			return vault;
		}
	}

	public SOSIFactory getSOSIFactory() {
		return this.sosiFactory;
	}

	private IDCard getTokenFromSTS() throws IOException,Fault {
		Document doc = generateTokenRequest(credentialVault, sosiFactory);

		String requestXml = XmlUtil.node2String(doc, false, true);
		String responseXml = requestHelper.sendRequest(requestXml);
		SecurityTokenResponse securityTokenResponse = sosiFactory.deserializeSecurityTokenResponse(responseXml);

		if (securityTokenResponse.isFault() || securityTokenResponse.getIDCard() == null) {
			log.error("Sts says "+securityTokenResponse.getFaultString());
			throw new Fault(new RuntimeException(securityTokenResponse.getFaultString()));
		} else {
			SystemIDCard stsSignedIdCard = (SystemIDCard) securityTokenResponse.getIDCard();
			return stsSignedIdCard;
		}
	}

	public CredentialVault createCredentialVault() {
		try {
			System.setProperty("dk.sosi.seal.vault.CredentialVault#Alias", keystoreAlias);
			Resource resource = resourceLoader.getResource(keystoreFilename);
			FileBasedCredentialVault vault = new FileBasedCredentialVault(new Properties(), resource.getFile(),
					keystorePassword);
			return vault;
		} catch (CredentialVaultException | IOException e) {
			log.error("Error creating credentialvault", e);
			throw new RuntimeException(e);
		}
	}

	public String getOrgName() {
		return orgname;
	}

	public String getCvr() {
		return cvr;
	}

	public String getItSystem() {
		return itsystem;
	}

	/**
	 * Creates document with sosi headers. Reuses id card from cache if still valid.
	 */
	public Document getSosiDocument() throws IOException {
		Request request = sosiFactory.createNewRequest(false, null);
		request.setIDCard(getTokenFromCache());
		return request.serialize2DOMDocument();
	}

	private IDCard cachedIDCard = null;

	/**
	 * Returns cached id card or retrieves a new one from STS if needed.
	 */
	private synchronized IDCard getTokenFromCache() throws IOException, Fault {
		long start = System.currentTimeMillis();
		if (cachedIDCard == null || !cachedIDCard.isValidInTime()) {

			cachedIDCard = getTokenFromSTS();
		}
		log.debug("IDCard " + cachedIDCard.getUsername() + " " + cachedIDCard.isValidInTime() + " "
				+ cachedIDCard.getIDCardID());
		log.info("action=idcard tt=" + (System.currentTimeMillis() - start) + " ms");
		return cachedIDCard;
	}

}
