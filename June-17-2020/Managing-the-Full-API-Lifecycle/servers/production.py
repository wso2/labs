#!/usr/bin/env python3
import json
from http import server, HTTPStatus
import socketserver
import ssl
import datetime
import uuid
import time

class EndpointHandler(server.BaseHTTPRequestHandler):
    def do_GET(self):
        self.common_handler()

    def do_POST(self):
        self.common_handler()

    def common_handler(self):
        time.sleep(1)
        
        response = {
            "count": 2,
            "list": [
                {
                    "id": str(uuid.uuid4()),
                    "name": "Handset",
                    "manufacturer": "Samsung Inc",
                    "model": "QQAR1266",
                    "price": "$10",
                    "status": "available"
                },
                {
                    "id": str(uuid.uuid4()),
                    "name": "Charger",
                    "manufacturer": "Samsung Inc",
                    "model": "QGGTW24",
                    "price": "$15",
                    "status": "available"
                }
            ]
        }
        if "accessories" in self.path:
            response = {
                    "count": 2,
                    "list": [
                        {
                            "id": str(uuid.uuid4()),
                            "name": "Charger Unit",
                            "manufacturer": "Power Gator",
                            "model": "PA-200mah",
                            "price": "$5",
                            "status": "available"
                        },
                        {
                            "id": str(uuid.uuid4()),
                            "name": "USB Cable",
                            "manufacturer": "Generic RPC",
                            "model": "USBCA124",
                            "price": "$1",
                            "status": "available"
                        }
                    ]
                }
        # response = {"uuid": str(uuid.uuid4()), "time": datetime.datetime.now(
        # ).strftime("%A, %d. %B %Y %I:%M:%S %p")}
        wire_data_byte = json.dumps(response).encode()

        self.send_response(HTTPStatus.OK)
        self.send_header("Content-type", "application/json")
        self.send_header("Content-length", len(wire_data_byte))

        self.end_headers()
        self.wfile.write(wire_data_byte)

    @staticmethod
    def run():
        port = EndpointHandler.port
        print('INFO: (Secured: {})Sample Server listening on localhost:{}...'.format(EndpointHandler.secured,
                                                                                     port))
        socketserver.TCPServer.allow_reuse_address = True
        httpd = socketserver.TCPServer(('', port), EndpointHandler)
        cert_path = 'yourpemfile.pem'
        print("DEBUG: cert_path = " + cert_path)
        if EndpointHandler.secured:
            httpd.socket = ssl.wrap_socket(
                httpd.socket, server_side=True, certfile=cert_path)
        httpd.serve_forever()

    port = 9000
    protocol_version = 'HTTP/1.1'
    secured = False


def main():
    """
        Run as a standalone server if needed
    """
    EndpointHandler.run()


if __name__ == '__main__':
    main()
