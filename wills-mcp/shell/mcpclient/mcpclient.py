#!/usr/bin/python
import os
import sys
import time
import md5
import httplib, urllib
import json
import re
import mimetypes
import StringIO
import gzip

class McpClient:

    app_secret_key = '1qaz2wsx'
    api_params={
        'v' : '1.0',
        'app_id' : '1',
        'call_id' : str(long(time.time() * 1000))
    }
    root_path = '/api/'
    login_path = root_path + 'user/login'

    def __init__(self, url):
        self.url=url

    def gen_sig(self, norm_params, secret_key): 
        params = dict(self.api_params, **norm_params)
        params = sorted(params.items(), key=lambda d: d[0])
        str_p = ''
        for kv in params:
            kvt = kv[1] 
            if kv[1].__len__() > 50 :
                kvt = kv[1][0:50] 
            str_p += kv[0] + '=' + kvt
        print str_p
        str_p=md5.new(str_p + secret_key).hexdigest()
        return str_p

    def send_req(self, path, params):
        headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
        conn = httplib.HTTPConnection(self.url)
        print 'request url:%s' % (self.url + path)
        conn.request("POST", path, params, headers)
        response = conn.getresponse()
        content_enc = response.getheader('content-encoding')
        print content_enc
        data = response.read()
        if content_enc != None and content_enc.lower() == 'gzip':
            gzip_date = StringIO.StringIO(data)
            gzipper = gzip.GzipFile(fileobj=gzip_date)
            data = gzipper.read()
        conn.close()
        return data

    def post_multipart(self, path, fields, files):
        content_type, body = self.encode_multipart_formdata(fields, files)
        h = httplib.HTTP(self.url)
        h.putrequest('POST', path)
        h.putheader('content-type', content_type)
        h.putheader('content-length', str(len(body)))
        h.putheader('referer', 'http://127.0.0.1:8000/')
        h.endheaders()
        h.send(body)
        errcode, errmsg, headers = h.getreply()
        return h.file.read()

    def encode_multipart_formdata(self, fields, files):
        LIMIT = '----------lImIt_of_THE_fIle_eW_$'
        CRLF = '\r\n'
        L = []
        for (key, value) in fields:
            L.append('--' + LIMIT)
            L.append('Content-Disposition: form-data; name="%s"' % key)
            L.append('')
            L.append(value)
        for (key, filename, value) in files:
            L.append('--' + LIMIT)
            print filename
            L.append('Content-Disposition: form-data; name="%s"; filename="%s"' % (key, filename))
            L.append('Content-Type: %s' % self.get_content_type(filename))
            L.append('')
            L.append(value)
        L.append('--' + LIMIT + '--')
        L.append('')
        body = CRLF.join(L)
        BOUNDARY=LIMIT
        content_type = 'multipart/form-data; boundary=%s' % BOUNDARY
        return content_type, body

    def get_content_type(self, filename):
        return mimetypes.guess_type(filename)[0] or 'application/octet-stream'


    def login(self, norm_params):
        print "------mcp login start------"
        print "extToken:%s extType%s" % (norm_params['extToken'], norm_params['extType'])
        norm_params['sig'] = self.gen_sig(norm_params, self.app_secret_key)
        params = dict(self.api_params, **norm_params)
        str_param = urllib.urlencode(params)
        data = self.send_req(self.login_path, str_param)
        #login_js = json.loads(data)
        #print login_js
        return data
        print "------mcp login end------ "

    def execute_command(self, method_name, norm_params, t=None, secret_key=None, bin_params=None):
        print '------mcp process_method start------'
        data = None
        print 'method_name:%s t:%s secret_key:%s' % (method_name, t, secret_key)
        print 'norm_params:%s' % (norm_params)
        need_login = t != None and t != '' and secret_key != None and secret_key != ''
        print need_login
        method_name = method_name.replace('.', '/')
        for k,v in norm_params.items():
            norm_params[k] = urllib.quote(v)
        secret_key_tmp = self.app_secret_key
        if need_login:
            norm_params['t'] = t
            secret_key_tmp = secret_key
        norm_params['sig'] = self.gen_sig(norm_params, secret_key_tmp)
        params = dict(self.api_params, **norm_params)
        
        if bin_params:
            upfilename = None
            upfile = None
            upKey = None
            for k,v in bin_params.items():
                if k.startswith('MCP_BIN_N_'):
                    upfilename = v
                else:
                    upKey = k.replace('MCP_BIN_F_','')
                    upfile = v
            files = [(upKey, upfilename, upfile)]
            fields = []
            for k,v in params.items():
                fields.append((k,v))

            print fields
            data = self.post_multipart(self.root_path + method_name,fields,files)
        else:
            str_param = urllib.urlencode(params)
            print 'str_param:'+str_param
            data = self.send_req(self.root_path + method_name, str_param)
        print '------mcp process_method end------'
        return data
''' ------class end------ '''


def process(data):
    if not data.has_key('MCP_URL') or not data.has_key('MCP_COMMAND'):
        print 'ERROR: Missing MCP_URL or MCP_COMMAND!'
        quit()

    mcp_ticket = ''
    mcp_secret_key = ''
    
    mcp_url = data['MCP_URL']
    mcp_command = data['MCP_COMMAND'] 

    if data.has_key('t'):
        mcp_ticket = data['t']
    if data.has_key('MCP_TICKET'):
        mcp_ticket = data['MCP_TICKET']
        del data['MCP_TICKET']
    if data.has_key('secret_key'):
        mcp_secret_key = data['secret_key']
        del data['secret_key']
    if data.has_key('MCP_SECRET_KEY'):
        mcp_secret_key = data['MCP_SECRET_KEY']
        del data['MCP_SECRET_KEY']

    mcpClient = McpClient(mcp_url)

    #login
    if mcp_command == 'user.login':
        if not data.has_key('extToken'):
            print 'ERROR: Missing extToken'
            quit()

        if not data.has_key('extType'):
            print 'ERROR: Missing extType'
            quit()

        rt = mcpClient.login(data)
        print rt
        quit() 

    #normal command
    method_name = ''
    norm_params = {}
    bin_params = {}
    #print data
    for k,v in data.items():
        if k == 'MCP_URL':
            mcpClient.url = v
        elif k == 'MCP_COMMAND':
            method_name = v
        elif k.startswith('MCP_BIN_F_'):
            pf = open(v, 'rb').read()
            bin_params[k] = pf
        elif k.startswith('MCP_BIN_N_'):
            bin_params[k] = v
        else:
            norm_params[k] = v

    if len(bin_params) == 0:
        bin_params=None

    rt = mcpClient.execute_command(method_name, norm_params, mcp_ticket, mcp_secret_key, bin_params=bin_params)
    return rt

def read_conf(file_name):
    data = {}
    f = file(file_name)
    while True:
        line = f.readline()
        if len(line) == 0: # Zero length indicates EOF
            break
        line = line.strip('\n')
        if line.startswith('#'):
            continue
        pattern = re.compile(r'^(\w+=)')
        match = pattern.match(line)
        if match:
            pos = match.start()
            group = match.group()
            data[line[pos:len(group)-1]] = line[len(group):len(line)]
    print 'conf_file_data:%s' % (data)
    f.close() # close the file
    return data

if __name__ == '__main__':
    
    if len(sys.argv) < 2:
        print 'ERROR: Missing conf_file'
        quit()

    data = read_conf(sys.argv[1])

    rt = process(data)
    print rt

