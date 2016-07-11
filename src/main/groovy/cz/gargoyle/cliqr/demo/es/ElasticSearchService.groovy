package cz.gargoyle.cliqr.demo.es;

import org.apache.http.client.HttpClient

public class ElasticSearchService {

		ElasticSearchServer server;
		
		static NETWORK_HOST = "0.0.0.0"
		static NETWORK_PORT = 9200
		
	    def setup() {
			Map<String, String> config = new HashMap<String, String> ()
			config.put("network.host",NETWORK_HOST)
			config.put("path.data","/Users/mdivis/elasticsearch/data") 
			config.put("path.home","/Users/mdivis/elasticsearch")
			config.put("script.inline", "true")
			config.put("script.indexed", "true")
			config.put("script.update", "true")
			
			config.put("http.cors.enabled", "true")
			config.put("http.cors.allow-origin", "*")
			config.put("cluster.name", "elasticsearch")
			server = new ElasticSearchServer(config)
			
			server.start()
			
	    }

	    def cleanup() {
			server.stop()
			server = null
	    }
		
		def getClient() {
			return server.client;
		}

		def getHttpClient() {
			// make more intelligent by getting node and port from localhost:9200/_nodes/process?pretty
			
			HttpClient client = HttpClientBuilder.create().build();
			String serverUri = 'http://' + NETWORK_HOST + ":" + NETWORK_PORT
			ElasticSearchHttpClient httpClient = new ElasticSearchHttpClient(
				httpClient: client,
				serverUri: serverUri
			)
			
			return httpClient
		}
		
		public static void main(String[] args) {
			def server = new ElasticSearchService();
			server.setup()
			
			while(true) {
				sleep(1000)
			}
		}
	}
