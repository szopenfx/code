using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using KISP.Nexus;

namespace KISP.Protocol
{
    /// <summary>
    /// KISP protocol handler
    /// </summary>
    public class Protocol
    {
        private int _ProtocolPort = Configuration.ProtocolPort();
        private Nexus.Nexus NexusPointer;


        /// <summary>
        /// Sets the protocol port, and starts a new Thread with the Listen() method
        /// </summary>
        /// <param name="ProtocolPort">Port to listen to</param>
        /// <param name="PassedNexusPointer">Pointer to Nexus object</param>
        public Protocol(int ProtocolPort, Nexus.Nexus PassedNexusPointer)
        {
            _ProtocolPort = ProtocolPort;
            NexusPointer = PassedNexusPointer;
            new Thread(new ThreadStart(Listen)).Start();
        }

        /// <summary>
        /// Listens at the _ProtocolPort for connections. If there is a new connection, a new ProtocolHandler
        /// object is made, and that objects HandleSocket() is invoked in a new thread.
        /// </summary>
        public void Listen()
        {
            TcpListener ProtocolTCPListener = new TcpListener(_ProtocolPort);
            Socket      ProtocolSocket;

            ProtocolTCPListener.Start();

            while (true)
            {               
                    ProtocolSocket = ProtocolTCPListener.AcceptSocket();

                    if (ProtocolSocket.Connected)
                    {
                        //TODO:start new thread
                        IPAddress RemoteIPAddress = ((IPEndPoint)ProtocolSocket.RemoteEndPoint).Address;
                        new Thread(new ThreadStart(new ProtocolHandler(ProtocolSocket, NexusPointer, RemoteIPAddress).HandleSocket)).Start();
                        //new ProtocolHandler(ProtocolSocket).HandleSocket();
                    }
            }
        }


    }

    
}
