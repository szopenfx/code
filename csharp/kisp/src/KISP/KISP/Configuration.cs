using System;
using System.Collections.Generic;
using System.Text;

namespace KISP.Nexus
{
    /// <summary>
    /// Singleton class that retrieves configuration from an XML file through static methods
    /// </summary>
    class Configuration
    {
        private static Configuration _Configuration = null;
        private XMLProxy _Proxy;

        /// <summary>
        /// Create new Configuration object
        /// </summary>
        /// <param name="ConfigFilename"></param>
        private Configuration(string ConfigFilename)
        {
            _Proxy = new XMLProxy(ConfigFilename);
        }

        /// <summary>
        /// Get an instance of Configuration; create it if necessary.
        /// </summary>
        /// <returns>The singleton Configuration instance</returns>
        private static XMLProxy Instance
        {
            get
            {
                if (_Configuration == null)
                    _Configuration = new Configuration("settings.xml");
                return _Configuration._Proxy;
            }
        }

        /// <summary>
        /// Get user name
        /// </summary>
        /// <returns>User name</returns>
        public static string Username()
        {
            return Instance.GetStr("user", "name");
        }

        /// <summary>
        /// Get protocol port
        /// </summary>
        /// <returns>Protocol port</returns>
        public static int ProtocolPort()
        {
            return Instance.GetInt("protocol", "port");
        }

        /// <summary>
        /// Get download directory
        /// </summary>
        /// <returns>Download directory</returns>
        public static string DownloadDir()
        {
            return Instance.GetStr("protocol", "downloaddir");
        }

        /// <summary>
        /// Get number of shares
        /// </summary>
        /// <returns>Number of shares</returns>
        public static int ShareCount()
        {
            return Instance.ListCount("shares", "share");
        }

        /// <summary>
        /// Get share type
        /// </summary>
        /// <param name="Index">Index of share</param>
        /// <returns>Share type</returns>
        public static string ShareType(int Index)
        {
            return Instance.GetListStr("shares", "share", Index, "type");
        }

        /// <summary>
        /// Get local share directory
        /// </summary>
        /// <param name="Index">Index of share</param>
        /// <returns>Local share directory</returns>
        public static string LocalShareDir(int Index)
        {
            return Instance.GetListStr("shares", "share", Index, "dir");
        }

        /// <summary>
        /// Get FTP share user name
        /// </summary>
        /// <param name="Index">Index of share</param>
        /// <returns>FTP share user name</returns>
        public static string FTPShareUsername(int Index)
        {
            return Instance.GetListStr("shares", "share", Index, "username");
        }

        /// <summary>
        /// Get FTP share password - be careful, kids! no encryption
        /// </summary>
        /// <param name="Index">Index of share</param>
        /// <returns>FTP share password</returns>
        public static string FTPSharePassword(int Index)
        {
            return Instance.GetListStr("shares", "share", Index, "username");
        }

        /// <summary>
        /// Get FTP share address
        /// </summary>
        /// <param name="Index">Index of share</param>
        /// <returns>FTP share address</returns>
        public static string FTPShareAddress(int Index)
        {
            return Instance.GetListStr("shares", "share", Index, "address");
        }

        /// <summary>
        /// Get multicass address
        /// </summary>
        /// <returns>Multicast address</returns>
        public static string MulticastAddress()
        {
            return Instance.GetStr("multicast", "address");
        }

        /// <summary>
        /// Get multicast port
        /// </summary>
        /// <returns>Multicast port</returns>
        public static int MulticastPort()
        {
            return Instance.GetInt("multicast", "port");
        }

        /// <summary>
        /// Get multicast interval
        /// </summary>
        /// <returns>Multicast interval</returns>
        public static int MulticastInterval()
        {
            return Instance.GetInt("multicast", "interval");
        }

        /// <summary>
        /// Get chess protocol port
        /// </summary>
        /// <returns>Chess protocol port</returns>
        public static int ChessPort()
        {
            return Instance.GetInt("chess", "port");
        }
    }
}
