
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
package org.example.user;

import java.util.Properties;

import org.example.client.CAClient;
import org.example.config.Config;
import org.example.util.Util;

/**
 *
 * @author Balaji Kadambi
 *
 */

public class RegisterEnrollUser {

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

			// Register and Enroll user to Org1MSP
			UserContext userContext = new UserContext();
			String name = "user"+System.currentTimeMillis();
			userContext.setName(name);
			userContext.setAffiliation(Config.ORG1);
			userContext.setMspId(Config.ORG1_MSP);

			String eSecret = caClient.registerUser(name, Config.ORG1);

			userContext = caClient.enrollUser(userContext, eSecret);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
