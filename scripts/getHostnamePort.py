#!/usr/bin/python
import os
from xml.dom import minidom


znodeParentProp = "zookeeper.znode.parent"
zkClientPortProp = "hbase.zookeeper.property.clientPort"
zkQuorumProp = "hbase.zookeeper.quorum"

zkHostname = ""
zkPort = ""
zkParentZN = ""

config = os.popen("hbase org.apache.hadoop.hbase.HBaseConfiguration").read()

xmldoc = minidom.parseString(config)

properties = xmldoc.getElementsByTagName('property')


for propertyKV in properties:
	xmlKV = propertyKV.toxml()
	if znodeParentProp in xmlKV:
		xmlval = minidom.parseString(xmlKV)
		zkParentZN = xmlval.getElementsByTagName('value')[0].firstChild.nodeValue

	if zkClientPortProp in xmlKV:
		xmlval = minidom.parseString(xmlKV)
		zkPort = xmlval.getElementsByTagName('value')[0].firstChild.nodeValue

	if zkQuorumProp in xmlKV:
		xmlval = minidom.parseString(xmlKV)
		zkHostname = xmlval.getElementsByTagName('value')[0].firstChild.nodeValue

print zkHostname + ":" + zkPort + ":" + zkParentZN

