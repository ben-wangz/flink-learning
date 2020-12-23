package flink.learning.example.meteorological_data;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MeteorologicalSink<DataType extends KeyValueData> implements SinkFunction<DataType> {
    public static final Logger LOGGER = LoggerFactory.getLogger(MeteorologicalSink.class);
    private final String serviceHost;
    private final int servicePort;
    private final String namePrefix;

    public MeteorologicalSink(String serviceHost, int servicePort, String namePrefix) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.namePrefix = namePrefix;
    }

    @Override
    public void invoke(DataType data, Context context) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(String.format("http://%s:%s/updateData", serviceHost, servicePort));
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("name", String.format("%s_%s", namePrefix, data.key())));
        nameValuePairList.add(new BasicNameValuePair("json", data.value()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        }
    }
}
