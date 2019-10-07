package org.example.config;

import java.io.File;

public class Config {

	public static final String ORG1_MSP = "Org1MSP";

	public static final String ORG1 = "org1";

	public static final String ORG2_MSP = "Org2MSP";

	public static final String ORG2 = "org2";

	public static final String ADMIN = "admin";

	public static final String ADMIN_PASSWORD = "adminpw";

	public static final String CHANNEL_CONFIG_PATH = "config/channel.tx";

	public static final String ORG1_USR_BASE_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org1.example.com" + File.separator + "users" + File.separator + "Admin@org1.example.com"
			+ File.separator + "msp";

	public static final String ORG2_USR_BASE_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org2.example.com" + File.separator + "users" + File.separator + "Admin@org2.example.com"
			+ File.separator + "msp";

	public static final String ORG1_USR_ADMIN_PK = ORG1_USR_BASE_PATH + File.separator + "keystore";
	public static final String ORG1_USR_ADMIN_CERT = ORG1_USR_BASE_PATH + File.separator + "admincerts";

	public static final String ORG2_USR_ADMIN_PK = ORG2_USR_BASE_PATH + File.separator + "keystore";
	public static final String ORG2_USR_ADMIN_CERT = ORG2_USR_BASE_PATH + File.separator + "admincerts";

	public static final String CA_ORG1_URL = "https://ca.org1.example.com:7054";
	//public static final String CA_ORG1_URL = "https://localhost:7054";

	public static final String TLSCA_ORG1_PEMFILE = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org1.example.com" + File.separator + "tlsca" + File.separator + "tlsca.org1.example.com-cert.pem";

	public static final String CA_ORG1_PEMFILE = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org1.example.com" + File.separator + "ca" + File.separator + "ca.org1.example.com-cert.pem";

	public static final String CA_ORG2_URL = "https://ca.org2.example.com:8054";
	//public static final String CA_ORG2_URL = "https://localhost:8054";

	public static final String TLSCA_ORG2_PEMFILE = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org2.example.com" + File.separator + "tlsca" + File.separator + "tlsca.org2.example.com-cert.pem";

	public static final String CA_ORG2_PEMFILE = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org2.example.com" + File.separator + "ca" + File.separator + "ca.org2.example.com-cert.pem";

	public static final String ORDERER_URL = "grpcs://orderer.example.com:7050";
	//public static final String ORDERER_URL = "grpcs://localhost:7050";

	public static final String ORDERER_NAME = "orderer.example.com";

	public static final String CHANNEL_NAME = "mychannel";

	public static final String ORDERER_TLSCA_PEMFILE = "crypto-config" + File.separator + "ordererOrganizations" + File.separator + "example.com" + File.separator + "orderers" + File.separator + "orderer.example.com" + File.separator + "tls" + File.separator + "ca.crt";

	public static final String ORG1_PEER_0 = "peer0.org1.example.com";

	public static final String ORG1_PEER_0_URL = "grpcs://peer0.org1.example.com:7051";
	//public static final String ORG1_PEER_0_URL = "grpcs://localhost:7051";

	public static final String ORG1_PEER_1 = "peer1.org1.example.com";

	public static final String ORG1_PEER_1_URL = "grpcs://peer1.org1.example.com:7056";
	//public static final String ORG1_PEER_1_URL = "grpcs://localhost:7056";

  public static final String ORG2_PEER_0 = "peer0.org2.example.com";

	public static final String ORG2_PEER_0_URL = "grpcs://peer0.org2.example.com:8051";
	//public static final String ORG2_PEER_0_URL = "grpcs://localhost:8051";

	public static final String ORG2_PEER_1 = "peer1.org2.example.com";

	public static final String ORG2_PEER_1_URL = "grpcs://peer1.org2.example.com:8056";
	//public static final String ORG2_PEER_1_URL = "grpcs://localhost:8056";

	public static final String CHAINCODE_ROOT_DIR = "chaincode";

	public static final String CHAINCODE_1_NAME = "fabcar";

	public static final String CHAINCODE_1_PATH = "github.com/fabcar";

	public static final String CHAINCODE_1_VERSION = "1";


}
