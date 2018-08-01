package io.fabric8.openshift.examples;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.client.OpenShiftClient;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.kubernetes.client.ConfigBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.net.URL;

public class ParamTest {

  static String option1URL = "http://localhost:8080/api/v1/namespaces/ns;type=che/pods/p;space=997f146d-b0f4-4a97-ab20-6414878d9508?w=true";
  static String option2URL = "http://localhost:8080/997f146d-b0f4-4a97-ab20-6414878d9508/api/v1/namespaces/$che/pods?w=true";
  static String option3URL = "http://localhost:8080/api/v1/namespaces/$che/pods?w=true&space=997f146d-b0f4-4a97-ab20-6414878d9508";

  public static void main(String[] args) throws Exception {

    ConfigBuilder configBuilder = new ConfigBuilder().withMasterUrl("http://localhost:8080");

    OpenShiftClient openShiftClient = new DefaultOpenShiftClient(configBuilder.build());
    OkHttpClient client = openShiftClient.adapt(OkHttpClient.class);

    // System.out.println("openShiftClient:" + openShiftClient);
    // System.out.println("client:" + client);

    URL requestUrl = new URL(option1URL);
    Request.Builder requestBuilder = new Request.Builder().get().url(requestUrl);
    Request request = requestBuilder.build();
    System.out.println("req.url=" + request.url());
    Response response = client.newCall(request).execute();
    System.out.println("option1 res.code=" + response.code());

    requestUrl = new URL(option2URL);
    requestBuilder = new Request.Builder().get().url(requestUrl);
    request = requestBuilder.build();
    System.out.println("req.url=" + request.url());
    response = client.newCall(request).execute();
    System.out.println("option2 res.code=" + response.code());

    requestUrl = new URL(option3URL);
    requestBuilder = new Request.Builder().get().url(requestUrl);
    request = requestBuilder.build();
    System.out.println("req.url=" + request.url());
    response = client.newCall(request).execute();
    System.out.println("option3 res.code=" + response.code());
  }

}
