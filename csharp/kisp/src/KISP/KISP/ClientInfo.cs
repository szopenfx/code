using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Net.Sockets;

namespace KISP.Binary
{
    public class ClientInfo : IDisposable
    {
        Socket _socket;
        byte[] _buffer;

        public Socket Socket
        {
            get
            {
                return _socket;
            }
        }

        public byte[] Buffer
        {
            get
            {
                return _buffer;
            }
            set
            {
                _buffer = value;
            }
        }

        public ClientInfo(Socket socket, byte[] buffer)
        {
            _socket = socket;
            _buffer = buffer;
        }
        #region IDisposable Members

        public void Dispose()
        {
            _buffer = null;
            if (_socket.Connected)
            {
                _socket.Shutdown(SocketShutdown.Both);
                _socket.Close();
            }
            _socket = null;
        }

        #endregion
    }
}
