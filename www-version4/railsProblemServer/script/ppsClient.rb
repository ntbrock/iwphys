#!/usr/bin/ruby

require 'xmlrpc/client'
require 'pp'

url = 'http://localhost:3003/xmlrpc/api'
method = 'pps.authenticate'
args = {:a => 'b'}

#method = 'hello_world'
#args = 'Snakes Alive'

server = XMLRPC::Client.new2(url)
result = server.call( method, args)
pp result
