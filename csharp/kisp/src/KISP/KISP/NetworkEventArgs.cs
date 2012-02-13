using System;
using System.Net;
using System.Net.Sockets;

namespace KISP.Binary
{
    /// <summary>
    /// Summary description for NetworkEventArgs.
    /// </summary>
    public class NetworkEventArgs : EventArgs
    {
        private ClientInfo _info;

        public ClientInfo Info
        {
            get
            {
                return _info;
            }
        }

        public NetworkEventArgs(ClientInfo info)
        {
            _info = info;
        }
    }
}
