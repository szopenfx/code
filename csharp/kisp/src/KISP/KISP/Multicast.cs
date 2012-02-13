using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;

using KISP.Nexus;

namespace KISP.Multicast
{
    /// <summary>
    /// Class for sending multicast messages
    /// </summary>
    class Multicast
    {
        Socket _WriteSocket;
        Socket _ReadSocket;
        Nexus.Nexus _Nexus;

        /// <summary>
        /// Create Multicast object
        /// </summary>
        /// <param name="nexus">Nexus object so that multicasts from peers can be handled</param>
        /// <param name="read">Create in read mode</param>
        /// <param name="write">Create in write mode</param>
        public Multicast(Nexus.Nexus nexus, bool read, bool write)
        {
            _Nexus = nexus;
            if(write)
                CreateWriteMulticast();
            if(read)
                CreateReadMulticast();
        }
        
        /// <summary>
        /// Create multicast writer socket
        /// </summary>
        private void CreateWriteMulticast()
        {
            IPAddress multicastip = IPAddress.Parse(Configuration.MulticastAddress());
            //IPAddress multicastip = IPAddress.Parse("224.5.6.7");
            
            IPEndPoint ipendpoint = new IPEndPoint(multicastip, Configuration.MulticastPort());
            
            // create normal unicast UDP socket
            _WriteSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, 
                ProtocolType.Udp);

            // join multicast group
            try
            {
                //_WriteSocket.SetSocketOption(SocketOptionLevel.IP, SocketOptionName.AddMembership,
                //    new MulticastOption(multicastip));

                _WriteSocket.SetSocketOption(SocketOptionLevel.IP, SocketOptionName.AddMembership, new MulticastOption(multicastip));

            }
            catch (SocketException e)
            {
                Console.WriteLine(e.ErrorCode);
            }

            // set TTL
            _WriteSocket.SetSocketOption(SocketOptionLevel.IP, 
                SocketOptionName.MulticastTimeToLive, 2);

            // connect the socket
            _WriteSocket.Connect(ipendpoint);

            // start writer thread
            new Thread(new ThreadStart(DoWriteMulticast)).Start();
        }

        /// <summary>
        /// Create multicast reader socket
        /// </summary>
        private void CreateReadMulticast()
        {
            string ip = Configuration.MulticastAddress();
            int port = Configuration.MulticastPort();

            IPAddress multicastip = IPAddress.Parse(ip);
            //IPAddress multicastip = IPAddress.Parse("224.5.6.7");
            
            IPEndPoint ipendpoint = new IPEndPoint(IPAddress.Any, port);
            
            // set up like unicast UDP socket
            _ReadSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);

            // bind endpoint to socket
            _ReadSocket.Bind(ipendpoint);
            
            // add to multicast group
            _ReadSocket.SetSocketOption(SocketOptionLevel.IP, SocketOptionName.AddMembership,
                new MulticastOption(multicastip, IPAddress.Any));
            new Thread(new ThreadStart(DoReadMulticast)).Start();
        }

        /// <summary>
        /// Thread: send IP address to multicast address every so often
        /// </summary>
        private void DoWriteMulticast()
        {
            IPHostEntry ipentry = Dns.GetHostEntry(Dns.GetHostName());
            IPAddress ipaddr = ipentry.AddressList[0];
            int interval = Configuration.MulticastInterval();
            for(int i = 0; i < 10; i++)
            {
                byte[] bytes = ASCIIEncoding.UTF8.GetBytes(ipaddr.ToString());
                _WriteSocket.Send(bytes);
                Thread.Sleep(interval);
                Console.WriteLine("Multicast.Send(" + ipaddr.ToString() + ")");
            }
        }

        /// <summary>
        /// Thread: do something when somebody sends IP address
        /// </summary>
        private void DoReadMulticast()
        {
            byte[] b = new byte[1024];
            string ip;
            while (true)
            {
                _ReadSocket.Receive(b);
                ip = Encoding.ASCII.GetString(b, 0, b.Length);
                ip = ip.Trim('\0');
                Console.WriteLine("Multicast.Read(" + ip + ")");
                if (!_Nexus.Peers.HasPeer(ip))
                    _Nexus.Invoke(new KISP.Nexus.Nexus.AddPeerDelegate(_Nexus.AddPeer), ip);
            }
        }
    }
}