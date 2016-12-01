#!/bin/bash

/usr/bin/java -jar BeerStore.jar 80 http://$ES_PORT_9200_TCP_ADDR:$ES_PORT_9200_TCP_PORT/ store &

/bin/bash
