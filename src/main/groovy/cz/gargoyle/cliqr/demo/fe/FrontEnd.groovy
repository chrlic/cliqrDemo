package cz.gargoyle.cliqr.demo.fe

import static spark.Spark.*
import cz.gargoyle.cliqr.demo.es.ElasticSearchHttpClient
import cz.gargoyle.cliqr.demo.es.ElasticSearchHttpResult
import groovy.json.JsonSlurper

import java.nio.file.Paths

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.settings.Settings

import java.net.InetAddress;

import org.apache.http.impl.client.HttpClientBuilder

import static groovyx.gpars.actor.Actors.*
import groovyx.gpars.*
import groovyx.gpars.actor.Actor

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import org.eclipse.jetty.websocket.api.*
import org.eclipse.jetty.websocket.api.annotations.*

@WebSocket
class FrontEnd {
	static ElasticSearchHttpClient client
	static String updateString = """
{
   "script" : "ctx._source.counter+=1",
   "upsert": {
       "counter": 1
   }
}
"""
	
	static boolean queuedRequest(String message) {
		def response = true
		try{
			def reply1 = waitingTask.sendAndWait("1", 2, TimeUnit.SECONDS)
			if (!(reply1.equals("1"))) {
				response = false
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
		response
	}

	static def waitingTask = actor {
		loop {
			react {
				sleep(50)
//				CpuCruncher.crunch(50)
				reply "1"
			}
		}
	}
	
	public static void main(String[] args) {
		String esServerUri = "http://localhost:9200/"
		int localPort = 4567
		String nodeId = "local"
		
		if (args.length > 0) {
			localPort = args[0].toInteger()
		}
		
		if (args.length > 1) {
			esServerUri = args[1]
		}
		
		if (args.length > 2) {
			nodeId = args[2]
		}
		
		
		println "DB In Use: ${esServerUri}"
		
		client = new ElasticSearchHttpClient()
		client.serverUri = esServerUri
		client.httpClient = HttpClientBuilder.create().build()
		
		def orderHandler = {req, res ->
			//CpuCruncher.crunch(300)
			def result = queuedRequest("")
			if (!result) {
				res.status(401)
				return ""
			}
						
			ElasticSearchHttpResult response = client.update("order", "counter", "2", 5, updateString)
			def jsonSlurper = new JsonSlurper()
			def esDoc
			if (response.body == null) { //first order creates counter, results in response code 201 which is understood as error 
				esDoc = jsonSlurper.parseText(response.errorBody)
			} else {
				esDoc = jsonSlurper.parseText(response.body)
			}
			'{ "order" : "' + esDoc._version + '", "nodeId" : "' + nodeId + '" }'
		}

		port(localPort)
		
		webSocket("/wsorder", FrontEnd.class);
		
		staticFiles.location("/public")

		get("/order", orderHandler	)
		post("/order", orderHandler	)	
	}
	
	def httpGetOrder(String serverUri) {
		HttpClient httpClient = HttpClientBuilder.create().build()
		HttpGet httpGet = new HttpGet(serverUri + "/order")
		def result = httpClient.execute(httpGet)
		return result
	}
	

		
	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {
		// println("Got: " + message)   				// Print message
		def startTime = new Date().getTime()
		def originHost = null
		def proxyHost = null
		
		if (session.upgradeRequest.headers['X-Forwarded-For'] != null) { //through proxy
			originHost = session.upgradeRequest.headers['X-Forwarded-For']
			proxyHost = session.upgradeRequest.headers['Host'][0]
		} else { //direct connection
			originHost = session.upgradeRequest.headers['Host'][0]
			proxyHost = null
		}
		
		println ("Origin host: ${originHost} | Proxy host: ${proxyHost}")

		Actor asyncRequest = actor {
			react { msg ->
				def inStartTime = new Date().getTime()
				def result = queuedRequest(msg)
				// println ("Queued Request finished in " + (new Date().getTime() - inStartTime) + " ms ")
				reply result
			}
		}

		Actor asyncHttpRequest = actor {
			react { msg ->
				String host = (proxyHost == null) ? originHost : proxyHost
				def httpResult = httpGetOrder("http://" + host)
				def status = (httpResult.getStatusLine().getStatusCode() == 200) ? "true" : "false"
				reply status
			}
		}
		asyncHttpRequest.sendAndContinue(message, {result ->
			ElasticSearchHttpResult esResponse = client.update("order", "counter", "2", 5, updateString)
			def response = '{ "status":"' + result + '", "id":"' + message + '" }'
			session.getRemote().sendString(response) 	// and send it back
		} )

		/*
		asyncRequest.sendAndContinue(message, {result ->
			ElasticSearchHttpResult esResponse = client.update("order", "counter", "2", 5, updateString)
			def response = '{ "status":"' + result + '", "id":"' + message + '" }'
			session.getRemote().sendString(response) 	// and send it back
		} )
		*/
		//println ("Sync message finished in " + (new Date().getTime() - startTime) + " ms ")  
	}

}
