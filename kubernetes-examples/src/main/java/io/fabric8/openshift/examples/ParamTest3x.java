package io.fabric8.openshift.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ParamTest3x {

	private static final Logger logger = LoggerFactory.getLogger(ParamTest3x.class);

	public static void main(String[] args) throws Exception {
		Option1();
		Option2();
	}

	/*
	SERVER_OUTPUT:
	
	req.URL.Path=/api/v1/namespaces/ns;type=che;space=997f146d-b0f4-4a97-ab20-6414878d9508;w=true/pods
	req.URL.RawQuery=
	query_params 0
	headers 3
	key=Connection, value=[Keep-Alive]
	key=Accept-Encoding, value=[gzip]
	key=User-Agent, value=[okhttp/3.9.1]
	 */
	private static void Option1() {
		ConfigBuilder configBuilder = new ConfigBuilder() //
				.withMasterUrl("http://localhost:8080") //
				.withNamespace("ns;type=che;space=997f146d-b0f4-4a97-ab20-6414878d9508;w=true") //
				.withTrustCerts(true) //
				.withOauthToken("token1111"); //

		Config config = configBuilder.build();
		try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient(config)) {
			List<Pod> items = openShiftClient.pods().list().getItems();
			for (Pod pod : items) {
				logger.info(pod.getMetadata().getName());
			}
		}
	}

	/*
	SERVER_OUTPUT:
	
	req.URL.Path=/api/v1/namespaces/$che/pods
	req.URL.RawQuery=
	query_params 0
	headers 7
	key=Impersonate-Extra-Space, value=[997f146d-b0f4-4a97-ab20-6414878d9508]
	key=Impersonate-Extra-W, value=[true]
	key=Connection, value=[Keep-Alive]
	key=Accept-Encoding, value=[gzip]
	key=User-Agent, value=[okhttp/3.9.1]
	key=Authorization, value=[Bearer token1111]
	key=Impersonate-User, value=[user1111]
	 */
	private static void Option2() {
		ConfigBuilder configBuilder = new ConfigBuilder() //
				.withMasterUrl("http://localhost:8080") //
				.withNamespace("$che") //
				.withTrustCerts(true) //
				.withOauthToken("token1111"); //

		Config config = configBuilder.build();
		config.getRequestConfig().setImpersonateUsername("user1111");
		Map<String, String> params = new HashMap<>();
		params.put("w", "true");
		params.put("space", "997f146d-b0f4-4a97-ab20-6414878d9508");
		config.getRequestConfig().setImpersonateExtras(params);

		try (KubernetesClient openShiftClient = new DefaultKubernetesClient(config)) {
			List<Pod> items = openShiftClient.pods().list().getItems();
			for (Pod pod : items) {
				logger.info(pod.getMetadata().getName());
			}
		}
	}

}
