/******************************************************
 *  Copyright 2018 IBM Corporation
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.example.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

import org.example.client.ChannelClient;
import org.example.client.FabricClient;
import org.example.config.Config;
import org.example.user.UserContext;
import org.example.util.Util;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

/**
 *
 * @author Balaji Kadambi
 *
 */

public class DeployInstantiateChaincode {

	public static void main(String[] args) {
		try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();

			UserContext org1Admin = new UserContext();
			File pkFolder1 = new File(Config.ORG1_USR_ADMIN_PK);
			File[] pkFiles1 = pkFolder1.listFiles();
			File certFolder = new File(Config.ORG1_USR_ADMIN_CERT);
			File[] certFiles = certFolder.listFiles();
			Enrollment enrollOrg1Admin = Util.getEnrollment(Config.ORG1_USR_ADMIN_PK, pkFiles1[0].getName(),
					Config.ORG1_USR_ADMIN_CERT, certFiles[0].getName());
			org1Admin.setEnrollment(enrollOrg1Admin);
			org1Admin.setMspId("Org1MSP");
			org1Admin.setName("admin");

			UserContext org2Admin = new UserContext();
			File pkFolder2 = new File(Config.ORG2_USR_ADMIN_PK);
			File[] pkFiles2 = pkFolder2.listFiles();
			File certFolder2 = new File(Config.ORG2_USR_ADMIN_CERT);
			File[] certFiles2 = certFolder2.listFiles();
			Enrollment enrollOrg2Admin = Util.getEnrollment(Config.ORG2_USR_ADMIN_PK, pkFiles2[0].getName(),
					Config.ORG2_USR_ADMIN_CERT, certFiles2[0].getName());
			org2Admin.setEnrollment(enrollOrg2Admin);
			org2Admin.setMspId(Config.ORG2_MSP);
			org2Admin.setName(Config.ADMIN);

			FabricClient fabClient = new FabricClient(org1Admin);

			Channel mychannel = fabClient.getInstance().newChannel(Config.CHANNEL_NAME);

			// Set peer0_org1 Variables
			String peer0_org1_name = Config.ORG1_PEER_0;
			String peer0_org1_url = Config.ORG1_PEER_0_URL;
			String peer0_org1_TLS = Config.TLSCA_ORG1_PEMFILE;

			Properties peer0_org1_properties = new Properties();
			peer0_org1_properties.put("pemFile", peer0_org1_TLS);
			peer0_org1_properties.setProperty("sslProvider", "openSSL");
			peer0_org1_properties.setProperty("negotiationType", "TLS");

			// Set peer1_org1 Variables
			String peer1_org1_name = Config.ORG1_PEER_1;
			String peer1_org1_url = Config.ORG1_PEER_1_URL;
			String peer1_org1_TLS = Config.TLSCA_ORG1_PEMFILE;

			Properties peer1_org1_properties = new Properties();
			peer1_org1_properties.put("pemFile", peer1_org1_TLS);
			peer1_org1_properties.setProperty("sslProvider", "openSSL");
			peer1_org1_properties.setProperty("negotiationType", "TLS");

			// Set peer0_org2 Variables
			String peer0_org2_name = Config.ORG2_PEER_0;
			String peer0_org2_url = Config.ORG2_PEER_0_URL;
			String peer0_org2_TLS = Config.TLSCA_ORG2_PEMFILE;

			Properties peer0_org2_properties = new Properties();
			peer0_org2_properties.put("pemFile", peer0_org2_TLS);
			peer0_org2_properties.setProperty("sslProvider", "openSSL");
			peer0_org2_properties.setProperty("negotiationType", "TLS");

			// Set peer1_org2 Variables
			String peer1_org2_name = Config.ORG2_PEER_1;
			String peer1_org2_url = Config.ORG2_PEER_1_URL;
			String peer1_org2_TLS = Config.TLSCA_ORG2_PEMFILE;

			Properties peer1_org2_properties = new Properties();
			peer1_org2_properties.put("pemFile", peer1_org2_TLS);
			peer1_org2_properties.setProperty("sslProvider", "openSSL");
			peer1_org2_properties.setProperty("negotiationType", "TLS");

			// Set Orderer Variables
			String orderer_name = Config.ORDERER_NAME;
			String orderer_url = Config.ORDERER_URL;
			String orderer_TLS = Config.ORDERER_TLSCA_PEMFILE;

			Properties orderer_properties = new Properties();
			orderer_properties.put("pemFile", orderer_TLS);
			orderer_properties.setProperty("sslProvider", "openSSL");
			orderer_properties.setProperty("negotiationType", "TLS");

			Orderer orderer = fabClient.getInstance().newOrderer(orderer_name, orderer_url, orderer_properties);
			Peer peer0_org1 = fabClient.getInstance().newPeer(peer0_org1_name, peer0_org1_url, peer0_org1_properties);
			Peer peer1_org1 = fabClient.getInstance().newPeer(peer1_org1_name, peer1_org1_url, peer1_org1_properties);
			Peer peer0_org2 = fabClient.getInstance().newPeer(peer0_org2_name, peer0_org2_url, peer0_org2_properties);
			Peer peer1_org2 = fabClient.getInstance().newPeer(peer1_org2_name, peer1_org2_url, peer1_org2_properties);

			mychannel.addOrderer(orderer);
			mychannel.addPeer(peer0_org1);
			mychannel.addPeer(peer1_org1);
			mychannel.addPeer(peer0_org2);
			mychannel.addPeer(peer1_org2);
			mychannel.initialize();

			List<Peer> org1Peers = new ArrayList<Peer>();
			org1Peers.add(peer0_org1);
			org1Peers.add(peer1_org1);

			List<Peer> org2Peers = new ArrayList<Peer>();
			org2Peers.add(peer0_org2);
			org2Peers.add(peer1_org2);

			Collection<ProposalResponse> response = fabClient.deployChainCode(Config.CHAINCODE_1_NAME,
					Config.CHAINCODE_1_PATH, Config.CHAINCODE_ROOT_DIR, Type.GO_LANG.toString(),
					Config.CHAINCODE_1_VERSION, org1Peers);


			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						Config.CHAINCODE_1_NAME + "- Chain code deployment " + res.getStatus());
			}

			fabClient.getInstance().setUserContext(org2Admin);

			response = fabClient.deployChainCode(Config.CHAINCODE_1_NAME,
					Config.CHAINCODE_1_PATH, Config.CHAINCODE_ROOT_DIR, Type.GO_LANG.toString(),
					Config.CHAINCODE_1_VERSION, org2Peers);


			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						Config.CHAINCODE_1_NAME + "- Chain code deployment " + res.getStatus());
			}

			ChannelClient channelClient = new ChannelClient(mychannel.getName(), mychannel, fabClient);

			String[] arguments = { "" };
			response = channelClient.instantiateChainCode(Config.CHAINCODE_1_NAME, Config.CHAINCODE_1_VERSION,
					Config.CHAINCODE_1_PATH, Type.GO_LANG.toString(), "init", arguments, null);

			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						Config.CHAINCODE_1_NAME + "- Chain code instantiation " + res.getStatus());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
