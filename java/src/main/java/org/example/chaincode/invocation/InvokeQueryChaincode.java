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
package org.example.chaincode.invocation;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.example.client.CAClient;
import org.example.client.ChannelClient;
import org.example.client.FabricClient;
import org.example.config.Config;
import org.example.user.UserContext;
import org.example.util.Util;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;

/**
 *
 * @author Balaji Kadambi
 *
 */

public class InvokeQueryChaincode {

	private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
	private static final String EXPECTED_EVENT_NAME = "event";

	public static void main(String args[]) {
		try {
            Util.cleanUp();
		  String caUrl = Config.CA_ORG1_URL;
	  	String TLS_FILE = Config.CA_ORG1_PEMFILE;
		  Properties properties = new Properties();
		  properties.put("pemFile", TLS_FILE);

		  CAClient caClient = new CAClient(caUrl, properties);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG1);
			adminUserContext.setMspId(Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

			FabricClient fabClient = new FabricClient(adminUserContext);

			ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			// Set Peer Variables
			String peer_name = Config.ORG1_PEER_0;
			String peer_url = Config.ORG1_PEER_0_URL;
			String peer_TLS = Config.TLSCA_ORG1_PEMFILE;
			String event_url = "grpcs://peer0.org1.example.com:7053";

			Properties peer_properties = new Properties();
			peer_properties.put("pemFile", peer_TLS);
			peer_properties.setProperty("sslProvider", "openSSL");
			peer_properties.setProperty("negotiationType", "TLS");

			Peer peer = fabClient.getInstance().newPeer(peer_name, peer_url, peer_properties);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", event_url, peer_properties);

			// Set Orderer Variables
			String orderer_name = Config.ORDERER_NAME;
			String orderer_url = Config.ORDERER_URL;
			String orderer_TLS = Config.ORDERER_TLSCA_PEMFILE;

			Properties orderer_properties = new Properties();
			orderer_properties.put("pemFile", orderer_TLS);
			orderer_properties.setProperty("sslProvider", "openSSL");
      orderer_properties.setProperty("negotiationType", "TLS");
			Orderer orderer = fabClient.getInstance().newOrderer(orderer_name, orderer_url, orderer_properties);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
			ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
			request.setChaincodeID(ccid);
			request.setFcn("createCar");
			String[] arguments = { "CAR1", "Chevy", "Volt", "Red", "Nick" };
			request.setArgs(arguments);
			request.setProposalWaitTime(1000);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); // Just some extra junk
																								// in transient map
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); // ditto
			tm2.put("result", ":)".getBytes(UTF_8)); // This should be returned see chaincode why.
			tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA); // This should trigger an event see chaincode why.
			request.setTransientMap(tm2);
			Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);

			Thread.sleep(10000);

			Collection<ProposalResponse>  responsesQuery = channelClient.queryByChainCode("fabcar", "queryAllCars", null);
			for (ProposalResponse pres : responsesQuery) {
				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
				System.out.println(stringResponse);
			}

			Thread.sleep(10000);
			String[] args1 = {"CAR1"};
			Collection<ProposalResponse>  responses1Query = channelClient.queryByChainCode("fabcar", "queryCar", args1);
			for (ProposalResponse pres : responses1Query) {
				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
				System.out.println(stringResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
