package io.fabric8.openshift.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ParamTest4x {
	private static final Logger logger = LoggerFactory.getLogger(ParamTest4x.class);

	public static void main(String[] args) {
		Option1();
		Option2();
		Option3();
		ExtraOption1();
		// CallProxy();
		// CallOpenShift();
	}

	/*
	SERVER_OUTPUT:

	req.URL.Path=/api/v1/namespaces/ns;type=che;space=997f146d-b0f4-4a97-ab20-6414878d9508;w=true/pods
	req.URL.RawQuery=
	query_params 0
	headers 4
	key=Accept-Encoding, value=[gzip]
	key=User-Agent, value=[okhttp/3.9.1]
	key=Authorization, value=[Bearer token1111]
	key=Connection, value=[Keep-Alive]
	 */
	private static void Option1() {
		ConfigBuilder configBuilder = new ConfigBuilder() //
				.withMasterUrl("http://localhost:8080") //
				.withNamespace("ns;type=che;space=997f146d-b0f4-4a97-ab20-6414878d9508;w=true") //
				.withTrustCerts(true) //
				.withOauthToken("token1111");

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
	req.URL.RawQuery=space=997f146d-b0f4-4a97-ab20-6414878d9508&w=true/
	query_params 2
	key=space, value=[997f146d-b0f4-4a97-ab20-6414878d9508]
	key=w, value=[true/]
	headers 4
	key=Authorization, value=[Bearer token1111]
	key=Connection, value=[Keep-Alive]
	key=Accept-Encoding, value=[gzip]
	key=User-Agent, value=[okhttp/3.9.1]
	 */
	private static void Option2() {
		ConfigBuilder configBuilder = new ConfigBuilder() //
				.withMasterUrl("http://localhost:8080?space=997f146d-b0f4-4a97-ab20-6414878d9508&w=true") //
				.withNamespace("$che") //
				.withTrustCerts(true) //
				.withOauthToken("token1111");

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
	
	req.URL.Path=/api/v1/namespaces/ns;type=che;space=997f146d-b0f4-4a97-ab20-6414878d9508/pods
	req.URL.RawQuery=w=true/
	query_params 1
	key=w, value=[true/]
	headers 4
	key=Connection, value=[Keep-Alive]
	key=Accept-Encoding, value=[gzip]
	key=User-Agent, value=[okhttp/3.9.1]
	key=Authorization, value=[Bearer token1111]
	 */
	private static void Option3() {
		ConfigBuilder configBuilder = new ConfigBuilder() //
				.withMasterUrl("http://localhost:8080?w=true") //
				.withNamespace("ns;type=che;space=997f146d-b0f4-4a97-ab20-6414878d9508") //
				.withTrustCerts(true) //
				.withOauthToken("token1111");

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
	key=Authorization, value=[Bearer token1111]
	key=Impersonate-User, value=[must_set_with_some_dummy_value]
	key=Impersonate-Extra-Space, value=[997f146d-b0f4-4a97-ab20-6414878d9508]
	key=Impersonate-Extra-W, value=[true]
	key=Connection, value=[Keep-Alive]
	key=Accept-Encoding, value=[gzip]
	key=User-Agent, value=[okhttp/3.9.1]
	 */
	private static void ExtraOption1() {
		ConfigBuilder configBuilder = new ConfigBuilder() //
				.withMasterUrl("http://localhost:8080") //
				.withNamespace("$che") //
				.withTrustCerts(true) //
				.withOauthToken("token1111");

		Config config = configBuilder.build();

		Map<String, String> param = new HashMap<String, String>();
		param.put("space", "997f146d-b0f4-4a97-ab20-6414878d9508");
		param.put("w", "true");
		// this must be set then only "ImpersonateExtras" will be sent in request
		config.setImpersonateUsername("must_set_with_some_dummy_value");
		config.setImpersonateExtras(param);

		try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient(config)) {
			List<Pod> items = openShiftClient.pods().list().getItems();
			for (Pod pod : items) {
				logger.info(pod.getMetadata().getName());
			}
		}
	}

	private static void CallProxy() {
		ConfigBuilder configBuilder = new ConfigBuilder()
				.withMasterUrl("https://f8osoproxy-test-dsaas-preview.b6ff.rh-idev.openshiftapps.com")
				.withNamespace("nvirani-preview-stage").withTrustCerts(true).withOauthToken("set_your_auth_token_here");

		Config config = configBuilder.build();
		try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient(config)) {
			List<Pod> items = openShiftClient.pods().list().getItems();
			logger.info("items={}", items);
			for (Pod pod : items) {
				logger.info(pod.getMetadata().getName());
			}
		}
	}

	private static void CallOpenShift() {
		ConfigBuilder configBuilder = new ConfigBuilder()
				.withMasterUrl("https://console.starter-us-east-2.openshift.com").withUsername("nvirani-1").withNamespace("nvirani-1")
				.withPassword("set_your_password");

		Config config = configBuilder.build();
		try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient(config)) {
			List<Pod> items = openShiftClient.pods().list().getItems();
			logger.info("items={}", items);
			for (Pod pod : items) {
				logger.info(pod.getMetadata().getName());
			}
		}
	}

}