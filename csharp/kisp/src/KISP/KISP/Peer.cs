using System;
using System.Collections.Generic;
using System.Text;
using System.Net;

namespace KISP.Nexus
{
    /// <summary>
    /// Class that represents a remote peer (his IP address)
    /// </summary>
    public class Peer
    {
        private string _UserName;
        private IPAddress _IPAddr;

        /// <summary>
        /// Construct Peer object
        /// </summary>
        /// <param name="IPAddr"></param>
        public Peer(IPAddress IPAddr)
        {
            _IPAddr = IPAddr;
        }

        /// <summary>
        /// The nick name of the user
        /// </summary>
        public string UserName
        {
            get
            {
                return _UserName;
            }
            set
            {
                _UserName = value;
            }
        }

        /// <summary>
        /// The IP address of the peer
        /// </summary>
        public IPAddress IPAddr
        {
            get
            {
                return _IPAddr;
            }
            set
            {
                _IPAddr = value;
            }
        }

        /// <summary>
        /// Set the IP address of the peer from a string representation
        /// </summary>
        /// <param name="IP"></param>
        public void SetIP(string IP)
        {
            _IPAddr = IPAddress.Parse(IP);
        }
    }
}
